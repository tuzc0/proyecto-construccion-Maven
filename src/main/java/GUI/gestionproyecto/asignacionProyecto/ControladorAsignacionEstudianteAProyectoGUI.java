package GUI.gestionproyecto.asignacionProyecto;

import GUI.gestionproyecto.ControladorConsultarProyectosGUI;
import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
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

    private static final Logger LOGGER = Logger
            .getLogger(ControladorConsultarProyectosGUI.class.getName());

    @FXML
    private TableView<EstudianteDTO> tablaAsignacion;
    @FXML
    private TableColumn<EstudianteDTO, String> columnaNombreEstudiante;
    @FXML
    private TableColumn<EstudianteDTO, String> columnaMatricula;
    @FXML
    private TableColumn<EstudianteDTO, Void> columnaProyecto;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private Button botonBuscarEstudiante;
    @FXML
    private Button botonRegresar;

    private final Utilidades utilidadesGUI = new Utilidades();
    private EstudianteDAO estudianteDAO;
    private ProyectoDAO proyectoDAO;

    @FXML
    public void initialize() {

        configurarColumnasTabla();
        cargarListaEstudiantes();
        agregarBotonAccionATabla();

        columnaNombreEstudiante.setReorderable(false);
        columnaMatricula.setReorderable(false);
        columnaProyecto.setReorderable(false);

        botonBuscarEstudiante.setCursor(Cursor.HAND);
        botonRegresar.setCursor(Cursor.HAND);

        proyectoDAO = new ProyectoDAO();
    }

    private void configurarColumnasTabla() {

        columnaNombreEstudiante.setCellValueFactory(celda ->
                new javafx.beans.property.SimpleStringProperty(
                        celda.getValue().getNombre() + " " + celda.getValue().getApellido()));
        columnaMatricula.setCellValueFactory(celda ->
                new javafx.beans.property.SimpleStringProperty(celda.getValue().getMatricula()));
    }

    private void cargarListaEstudiantes() {

        estudianteDAO = new EstudianteDAO();

        try {

            List<EstudianteDTO> listaEstudiantes = estudianteDAO.listarEstudiantes();
            tablaAsignacion.getItems().setAll(listaEstudiantes);

        } catch (SQLException | IOException excepcion) {

            utilidadesGUI.mostrarAlerta(
                    "Error",
                    "No se pudieron cargar los estudiantes.",
                    "Por favor, intente nuevamente.");
        }
    }

    private void agregarBotonAccionATabla() {

        Callback<TableColumn<EstudianteDTO, Void>, TableCell<EstudianteDTO, Void>> fabricaCeldas = new Callback<>() {

            @Override
            public TableCell<EstudianteDTO, Void> call(final TableColumn<EstudianteDTO, Void> parametro) {

                return new TableCell<>() {

                    private final Button botonAccion = new Button();

                    {

                        botonAccion.setOnAction((evento) -> {

                            EstudianteDTO estudianteSeleccionado = getTableView().getItems().get(getIndex());

                            if (estudianteSeleccionado.getIdProyecto() == 0) {

                                abrirVentanaSeleccionProyecto(estudianteSeleccionado);

                            } else {

                                mostrarAsignacionEstudiante(estudianteSeleccionado);
                            }
                        });

                    }

                    @Override
                    public void updateItem(Void item, boolean vacio) {

                        super.updateItem(item, vacio);

                        if (vacio) {

                            setGraphic(null);

                        } else {

                            EstudianteDTO estudianteSeleccionado = getTableView().getItems().get(getIndex());

                            if (estudianteSeleccionado.getIdProyecto() == 0) {

                                botonAccion.setText("Asignar Proyecto");

                            } else {

                                botonAccion.setText("Ver Asignación");
                            }

                            botonAccion.setCursor(Cursor.HAND);
                            setGraphic(botonAccion);
                        }
                    }
                };
            }
        };

        columnaProyecto.setCellFactory(fabricaCeldas);
    }

    private void abrirVentanaSeleccionProyecto(EstudianteDTO estudiante) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SeleccionarProyectoGUI.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Seleccionar Proyecto");
            stage.initModality(Modality.APPLICATION_MODAL);

            ControladorSeleccionProyectoGUI controlador = loader.getController();
            controlador.inicializarDatos(estudiante, this);

            List<ProyectoDTO> proyectos = proyectoDAO.listarProyectos();
            controlador.cargarProyectos(proyectos);

            stage.showAndWait();

        } catch (IOException | SQLException e) {

            LOGGER.severe("Error al abrir ventana de selección de proyectos: " + e);
            utilidadesGUI.mostrarAlerta(
                    "Error",
                    "No se pudo abrir la ventana de selección",
                    "Por favor, intente nuevamente."
            );
        }
    }

    public void actualizarAsignacionEstudiante(EstudianteDTO estudiante, int idProyecto) {

        try {

            boolean proyectoAsignado = estudianteDAO.asignarProyecto(idProyecto, estudiante.getMatricula());

            if (proyectoAsignado) {

                utilidadesGUI.mostrarAlerta("Éxito",
                        "Proyecto asignado correctamente.",
                        "El estudiante ha sido asignado al proyecto.");
                cargarListaEstudiantes();

            } else {

                utilidadesGUI.mostrarAlerta("Error",
                        "No se pudo asignar el proyecto.",
                        "Por favor, intente nuevamente.");
            }

        } catch (SQLException | IOException e) {

            LOGGER.severe("Error al asignar el proyecto: " + e);
            utilidadesGUI.mostrarAlerta("Error",
                    "Ocurrió un error al asignar el proyecto.",
                    "Por favor, intente nuevamente.");
        }
    }

    private void mostrarAsignacionEstudiante(EstudianteDTO estudiante) {

        try {

            ProyectoDTO proyecto = proyectoDAO.buscarProyectoPorID(estudiante.getIdProyecto());
            utilidadesGUI.mostrarAlerta("Asignación Actual",
                    "El estudiante está asignado a:",
                    "Proyecto: " + proyecto.getNombre() +
                            "\nID: " + proyecto.getIdProyecto());

        } catch (SQLException | IOException e) {

            LOGGER.severe("Error al obtener proyecto: " + e);
            utilidadesGUI.mostrarAlerta("Error",
                    "No se pudo obtener la información del proyecto",
                    "Por favor, intente nuevamente.");
        }
    }

    @FXML
    private void buscarEstudiante() {

        String textoBusqueda = campoBusqueda.getText().trim();
        estudianteDAO = new EstudianteDAO();

        if (!textoBusqueda.isEmpty()) {

            try {

                List<EstudianteDTO> listaEstudiantes = estudianteDAO.listarEstudiantes();
                List<EstudianteDTO> estudiantesFiltrados = listaEstudiantes.stream()
                        .filter(estudiante -> estudiante.getMatricula().contains(textoBusqueda) ||
                                (estudiante.getNombre() + " " + estudiante.getApellido()).toLowerCase()
                                        .contains(textoBusqueda.toLowerCase()))
                        .toList();
                tablaAsignacion.getItems().setAll(estudiantesFiltrados);

            } catch (SQLException | IOException e) {

                LOGGER.severe("No se pudo realizar la búsqueda: " + e);
                utilidadesGUI.mostrarAlerta("Error",
                        "No se pudo realizar la búsqueda.",
                        "Por favor, intente nuevamente.");
            }

        } else {

            cargarListaEstudiantes();
        }
    }

    @FXML
    private void regresar() {

        Stage stage = (Stage) botonRegresar.getScene().getWindow();
        stage.close();
    }
}