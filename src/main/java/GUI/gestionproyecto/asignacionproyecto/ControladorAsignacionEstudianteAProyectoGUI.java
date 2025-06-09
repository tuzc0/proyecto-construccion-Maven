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

    private final Utilidades UTILIDADES = new Utilidades();
    private final ManejadorDeAccionAsignacion MANEJADOR_NAVEGACION = new ManejadorDeAccionAsignacion();

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
                MANEJADOR_NAVEGACION.mostrarSeleccionProyecto(estudianteDTO, this, proyectos);

            } else {

                ProyectoDTO proyectoActual = proyectoDAO.buscarProyectoPorID(estudianteDTO.getIdProyecto());
                MANEJADOR_NAVEGACION.mostrarDetallesAsignacion(proyectoActual, estudianteDTO, this);
            }
        } catch (SQLException | IOException e) {

            UTILIDADES.mostrarAlerta(
                    "Error",
                    "No se pudo completar la acción",
                    "Por favor, intentelo más tarde.");
            LOGGER.severe("Error en acción de botón: " + e);
        }
    }

    private void cargarEstudiantes() {

        EstudianteDAO estudianteDAO = new EstudianteDAO();

        try {

            tablaAsignacion.getItems().setAll(estudianteDAO.listarEstudiantes());

        } catch (SQLException | IOException e) {

            UTILIDADES.mostrarAlerta("Error", "No se pudieron cargar los estudiantes", "");
            LOGGER.severe("Error al cargar estudiantes: " + e);
        }
    }

    private void configurarInteracciones() {

        botonBuscarEstudiante.setCursor(Cursor.HAND);
        botonBuscarEstudiante.setOnAction(event -> buscarEstudiante());

        botonRegresar.setCursor(Cursor.HAND);
        botonRegresar.setOnAction(event -> regresar());
    }

    @FXML
    private void buscarEstudiante() {

        String textoBusqueda = campoBusqueda.getText().trim();
        EstudianteDAO estudianteDAO = new EstudianteDAO();

        if (!textoBusqueda.isEmpty()) {

            try {

                List<EstudianteDTO> listaEstudiantes = estudianteDAO.listarEstudiantes();
                List<EstudianteDTO> estudiantesFiltrados = listaEstudiantes.stream()
                        .filter(estudiante -> estudiante.getMatricula().contains(textoBusqueda))
                        .toList();

                tablaAsignacion.getItems().setAll(estudiantesFiltrados);

            } catch (SQLException | IOException e) {

                LOGGER.severe("No se pudo realizar la búsqueda: " + e);
                UTILIDADES.mostrarAlerta("Error",
                        "No se pudo realizar la búsqueda.",
                        "Por favor, intente nuevamente.");
            }

        } else {

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

                UTILIDADES.mostrarAlerta("Éxito",
                        "Proyecto reasignado correctamente",
                        "");
                cargarEstudiantes();

            } else {
                UTILIDADES.mostrarAlerta(
                        "Error",
                        "No se pudo reasignar el proyecto",
                        "");
            }
        } catch (SQLException | IOException e) {
            LOGGER.severe("No se pudo reasignar al estudiante: " + e);

            UTILIDADES.mostrarAlerta("Error",
                    "No se pudo realizar la asignación.",
                    "Por favor, intente nuevamente.");
        }
    }

    @FXML
    private void regresar() {

        ((Stage) botonRegresar.getScene().getWindow()).close();
    }
}