package GUI.gestionproyecto.asignacionproyecto;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import logica.DAOs.EstudianteDAO;
import logica.DAOs.ProyectoDAO;
import logica.DTOs.EstudianteDTO;
import logica.DTOs.ProyectoDTO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ControladorAsignacionEstudianteAProyectoGUI {
    private static final Logger LOGGER = Logger.getLogger(ControladorAsignacionEstudianteAProyectoGUI.class.getName());

    @FXML private TableView<EstudianteDTO> tablaAsignacion;
    @FXML private TableColumn<EstudianteDTO, String> columnaNombreEstudiante;
    @FXML private TableColumn<EstudianteDTO, String> columnaMatricula;
    @FXML private TableColumn<EstudianteDTO, Void> columnaProyecto;
    @FXML private TextField campoBusqueda;
    @FXML private Button botonBuscarEstudiante;
    @FXML private Button botonRegresar;

    private final ManejadorDeAccionAsignacion MANEJADOR_NAVEGACION = new ManejadorDeAccionAsignacion();
    private Utilidades utilidades = new Utilidades();

    @FXML
    public void initialize() {

        configurarTabla();
        cargarEstudiantes();
        configurarInteracciones();
    }

    private void configurarTabla() {

        columnaNombreEstudiante.setCellValueFactory(celda ->
                new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getNombre() + " " + celda.getValue().getApellido()));

        columnaMatricula.setCellValueFactory(celda ->
                new javafx.beans.property.SimpleStringProperty(celda.getValue().getMatricula()));

        columnaProyecto.setCellFactory(crearFabricaBotonAccion());

        columnaNombreEstudiante.setReorderable(false);
        columnaMatricula.setReorderable(false);
        columnaProyecto.setReorderable(false);
    }

    private Callback<TableColumn<EstudianteDTO, Void>, TableCell<EstudianteDTO, Void>> crearFabricaBotonAccion() {

        return param -> new TableCell<>() {

            private final Button botonDeAcccion = new Button();

            {
                botonDeAcccion.setCursor(Cursor.HAND);
                botonDeAcccion.setOnAction(evento -> manejarAccionBoton(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void elemento, boolean vacio) {

                super.updateItem(elemento, vacio);

                if (vacio) {

                    setGraphic(null);

                } else {

                    EstudianteDTO estudianteDTO = getTableView().getItems().get(getIndex());
                    int proyectoNoAsignado = 0;

                    if (estudianteDTO.getIdProyecto() == proyectoNoAsignado) {

                        botonDeAcccion.setText("Asignar Proyecto");

                    } else {

                        botonDeAcccion.setText("Ver Asignación");
                    }

                    setGraphic(botonDeAcccion);
                }
            }
        };
    }

    private void manejarAccionBoton(EstudianteDTO estudianteDTO) {

        ProyectoDAO proyectoDAO = new ProyectoDAO();

        try {

            if (estudianteDTO.getIdProyecto() == 0) {

                List<ProyectoDTO> proyectos = proyectoDAO.listarProyectosConCupo();
                MANEJADOR_NAVEGACION.abrirVentanaSeleccionProyecto(estudianteDTO, this, proyectos);

            } else {

                ProyectoDTO proyectoActual = proyectoDAO.buscarProyectoPorID(estudianteDTO.getIdProyecto());
                MANEJADOR_NAVEGACION.abrirVentanaDetallesAsignacionProyecto(proyectoActual, estudianteDTO, this);
            }
        } catch (SQLException e) {

            LOGGER.severe("Error en la base de datos durante el metodo manejarAccionBoton: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "Acción no completada",
                    "Ocurrió un problema al intentar realizar la acción. " +
                            "Por favor, inténtelo nuevamente más tarde."
            );

        } catch (IOException e) {

            LOGGER.severe("Error al manejarAccionBoton: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "Ocurrió un problema inesperado",
                    "No se pudo completar la acción debido a un error interno. " +
                            "Por favor, inténtelo nuevamente más tarde o contacte al administrador."
            );
        }
    }

    private void cargarEstudiantes() {

        EstudianteDAO estudianteDAO = new EstudianteDAO();

        try {

            tablaAsignacion.getItems().setAll(estudianteDAO.listarEstudiantes());

        } catch (SQLException e) {

            LOGGER.severe("Error en la base de datos al cargar estudiantes: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "No se pudieron cargar los estudiantes",
                    "Ocurrió un problema al intentar cargar la lista de estudiantes. " +
                            "Por favor, inténtelo nuevamente más tarde o contacte al administrador."
            );

        } catch (IOException e) {

            LOGGER.severe("Error al cargar estudiantes: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "No se pudieron cargar los estudiantes",
                    "Ocurrió un problema al intentar cargar la lista de estudiantes. " +
                            "Por favor, inténtelo nuevamente más tarde o contacte al administrador."
            );
        }
    }

    private void configurarInteracciones() {

        botonBuscarEstudiante.setCursor(Cursor.HAND);
        botonBuscarEstudiante.setOnAction(event -> buscarEstudiante());

        botonRegresar.setCursor(Cursor.HAND);
        botonRegresar.setOnAction(evento -> regresar());
    }

    @FXML
    private void buscarEstudiante() {

        String textoBusqueda = campoBusqueda.getText().trim();
        EstudianteDAO estudianteDAO = new EstudianteDAO();

        if (!textoBusqueda.isEmpty()) {

            try {

                List<EstudianteDTO> listaEstudiantes = estudianteDAO.listarEstudiantes();
                List<EstudianteDTO> estudiantesFiltrados = listaEstudiantes.stream()
                        .filter(estudianteDTO -> estudianteDTO.getMatricula().contains(textoBusqueda))
                        .toList();

                tablaAsignacion.getItems().setAll(estudiantesFiltrados);

            } catch (SQLException e) {

                LOGGER.severe("Error dentro de la base de datos, no se pudo realizar la búsqueda: " + e);
                utilidades.mostrarAlerta(
                        "Error",
                        "Búsqueda no completada",
                        "Ocurrió un problema al intentar realizar la búsqueda. " +
                                "Por favor, inténtelo nuevamente más tarde."
                );

            } catch (IOException e) {

                LOGGER.severe("No se pudo realizar la accion de busqueda: " + e);
                utilidades.mostrarAlerta(
                        "Error",
                        "No se pudo realizar la búsqueda.",
                        "Ocurrió un problema al intentar buscar estudiantes. " +
                                "Por favor, inténtelo nuevamente más tarde o contacte al administrador."
                );
            }

        } else {

            utilidades.mostrarAlerta(
                    "Campo de busqueda vacio. ",
                    "El campo de busqueda se encuentra vacio.",
                    "Por favor, introduzca la matricula de un estudiante."
            );
            cargarEstudiantes();
        }
    }


    public void actualizarAsignacion(EstudianteDTO estudianteDTO, int idProyecto) {

        EstudianteDAO estudianteDAO = new EstudianteDAO();
        int proyectoNoAsignado = 0;

        try {

            if (estudianteDTO.getIdProyecto() > proyectoNoAsignado) {

                estudianteDAO.reasignarProyecto(estudianteDTO);
            }

            if (estudianteDAO.asignarProyecto(idProyecto, estudianteDTO.getMatricula())) {

                utilidades.mostrarAlerta("Éxito",
                        "Proyecto reasignado correctamente",
                        "");
                cargarEstudiantes();

            } else {
                utilidades.mostrarAlerta(
                        "Error",
                        "No se pudo reasignar el proyecto",
                        "");
            }
        } catch (SQLException | IOException e) {
            LOGGER.severe("No se pudo reasignar al estudiante: " + e);

            utilidades.mostrarAlerta("Error",
                    "No se pudo realizar la asignación.",
                    "Por favor, intente nuevamente.");
        }
    }

    @FXML
    private void regresar() {

        utilidades.mostrarAlertaConfirmacion(
                "Confirmar acción",
                "¿Está seguro que desea regresar?",
                "Los cambios no guardados se perderán.",
                () -> ((Stage) botonRegresar.getScene().getWindow()).close(),
                () -> {

                }
        );
    }
}