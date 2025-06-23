package GUI;

import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import logica.DAOs.EstudianteDAO;
import logica.DAOs.EvaluacionContieneDAO;
import logica.DAOs.EvaluacionDAO;
import logica.DTOs.EstudianteDTO;
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

import static GUI.ControladorRegistrarEvaluacionGUI.idEvaluacionGenerada;

public class ControladorConsultarEstudiantesAEvaluarGUI {

    Logger logger =
            org.apache.logging.log4j.LogManager.getLogger(ControladorConsultarEstudiantesAEvaluarGUI.class);

    @FXML
    TableView<EstudianteDTO> tablaEstudiantes;

    @FXML
    TableColumn<EstudianteDTO, String> columnaNombres;

    @FXML
    TableColumn<EstudianteDTO, String> columnaApellidos;

    @FXML
    TableColumn<EstudianteDTO, String> columnaMatricula;

    @FXML
    TableColumn<EstudianteDTO, Void> columnaEvaluar;

    @FXML
    TextField campoMatricula;

    public static String matriculaEstudianteSeleccionado;

    private static int numeroPersonal = ControladorInicioDeSesionGUI.numeroDePersonal;

    private EstudianteDTO estudianteSeleccionado = new EstudianteDTO();

    private Utilidades gestorVentanas = new Utilidades();

    private IGestorAlertas utilidades = new Utilidades();

    private ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);

    @FXML
    public void initialize() {

        columnaMatricula.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMatricula()));
        columnaNombres.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        columnaApellidos.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));

        cargarEstudiantes();
        añadirBotonEvaluarATable();

        tablaEstudiantes.getSelectionModel().selectedItemProperty().addListener((estudianteObservado,
                                                                                 antiguoEstudiante,
                                                                                 nuevoEstudiante) -> {
            estudianteSeleccionado = nuevoEstudiante;
            tablaEstudiantes.refresh();
        });
    }


    private void cargarEstudiantes() {

        EstudianteDAO estudianteDAO = new EstudianteDAO();

        try {

            ObservableList<EstudianteDTO> estudiantes = FXCollections.observableArrayList(
                    estudianteDAO.listarEstudiantesNoEvaluados(numeroPersonal)
            );
            tablaEstudiantes.setItems(estudiantes);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error al cargar los estudiantes.",
                    "Por favor, intentelo de nuevo más tarde."
            );
        }
    }

    private void añadirBotonEvaluarATable() {

        Callback<TableColumn<EstudianteDTO, Void>, TableCell<EstudianteDTO, Void>> cellFactory =
                param -> new TableCell<>() {

            private final Button botonEvaluar = new Button("Evaluar");
            {
                botonEvaluar.setOnAction(event -> {

                    EstudianteDTO estudianteSeleccionado = getTableView().getItems().get(getIndex());
                    matriculaEstudianteSeleccionado = estudianteSeleccionado.getMatricula();
                    abrirVentanaRegistrarEvaluacion();


                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {

                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()) != estudianteSeleccionado) {

                    setGraphic(null);
                } else {

                    setGraphic(botonEvaluar);
                }
            }
        };

        columnaEvaluar.setCellFactory(cellFactory);
    }

    public void abrirVentanaRegistrarEvaluacion() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistrarEvaluacionGUI.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registrar Evaluación");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setOnCloseRequest(evento -> {
                eliminarEvaluacion();
            });

            stage.showAndWait();

            cargarEstudiantes();

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al abrir la ventana de registrar evaluación: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error al abrir la ventana de registrar evaluación.",
                    "Por favor, inténtelo de nuevo más tarde o contacte al administrador."
            );
        }

    }

    public void eliminarEvaluacion() {

        EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
        EvaluacionContieneDAO evaluacionContieneDAO = new EvaluacionContieneDAO();

        try {

            evaluacionContieneDAO.eliminarCriteriosPorIdEvaluacion(idEvaluacionGenerada);
            evaluacionDAO.eliminarEvaluacionDefinitivamente(idEvaluacionGenerada);

        } catch (SQLException e) {

           manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

           manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error al eliminar la evaluación.",
                    "Por favor, intentelo de nuevo más tarde."
            );
        }
    }


    @FXML
    public void buscarEstudiante() {

        String matriculaBuscada = campoMatricula.getText().trim();

        if (matriculaBuscada.isEmpty()) {
            cargarEstudiantes();
            return;
        }

        EstudianteDAO estudianteDAO = new EstudianteDAO();

        try {

            ObservableList<EstudianteDTO> estudiantes = FXCollections.observableArrayList(
                    estudianteDAO.listarEstudiantesNoEvaluados(numeroPersonal)
            );

            ObservableList<EstudianteDTO> estudiantesFiltrados = estudiantes.filtered(
                    estudiante -> estudiante.getMatricula().toLowerCase().contains(matriculaBuscada.toLowerCase())
            );

            if (estudiantesFiltrados.isEmpty()) {

                Utilidades utilidades = new Utilidades();
                utilidades.mostrarAlerta("No encontrado",
                        "Estudiante no encontrado",
                        "No se encontró un estudiante con la matrícula: "
                                + matriculaBuscada);
            } else {

                tablaEstudiantes.setItems(estudiantesFiltrados);

            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al buscar estudiante: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error al buscar el estudiante.",
                    "Por favor, inténtelo de nuevo más tarde."
            );
        }
    }
}
