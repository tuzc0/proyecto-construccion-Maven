package GUI;

import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import logica.DAOs.EstudianteDAO;
import logica.DAOs.EvaluacionContieneDAO;
import logica.DAOs.EvaluacionDAO;
import logica.DTOs.EstudianteDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import static GUI.ControladorRegistrarEvaluacionGUI.idEvaluacionGenerada;

public class ControladorConsultarEstudiantesAEvaluarGUI {

    Logger logger = Logger.getLogger(ControladorConsultarEstudiantesAEvaluarGUI.class.getName());

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

    public static String matriculaEstudianteSeleccionado;

    private EstudianteDTO estudianteSeleccionado = new EstudianteDTO();


    @FXML
    public void initialize() {

        columnaMatricula.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMatricula()));
        columnaNombres.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        columnaApellidos.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));

        cargarEstudiantes();
        añadirBotonEvaluarATable();

        tablaEstudiantes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            estudianteSeleccionado = newValue;
            tablaEstudiantes.refresh();
        });
    }

    private void cargarEstudiantes() {

        Utilidades utilidades = new Utilidades();

        try {

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            ObservableList<EstudianteDTO> estudiantes = FXCollections.observableArrayList(estudianteDAO.listarEstudiantes());
            tablaEstudiantes.setItems(estudiantes);

        } catch (Exception e) {

            logger.severe("Error al cargar la lista de estudiantes: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Error de entrada/salida", "No se pudo cargar la lista de estudiantes.");
        }
    }

    private void añadirBotonEvaluarATable() {



        Callback<TableColumn<EstudianteDTO, Void>, TableCell<EstudianteDTO, Void>> cellFactory = param -> new TableCell<>() {

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

            stage.setOnCloseRequest(event -> {
                eliminarEvaluacion();
            });

            stage.showAndWait();

        } catch (IOException e) {
            logger.severe("Error al abrir la ventana RegistrarEvaluacionGUI: " + e.getMessage());
        }

    }

    public void eliminarEvaluacion() {

        EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
        EvaluacionContieneDAO evaluacionContieneDAO = new EvaluacionContieneDAO();

        try {

            evaluacionContieneDAO.eliminarCriteriosPorIdEvaluacion(idEvaluacionGenerada);
            evaluacionDAO.eliminarEvaluacionDefinitivamente(idEvaluacionGenerada);

        } catch (SQLException e) {

            logger.severe("Error de SQL: " + e.getMessage());
        } catch (IOException e) {

            logger.severe("Error de IO: " + e.getMessage());
        } catch (Exception e) {

            logger.severe("Error inesperado: " + e.getMessage());
        }
    }


    @FXML
    public void buscarEstudiante(){

    }
}
