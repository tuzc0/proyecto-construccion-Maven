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
import logica.DAOs.GrupoDAO;
import logica.DTOs.EstudianteDTO;
import logica.DTOs.GrupoDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    @FXML
    TextField campoMatricula;

    public static String matriculaEstudianteSeleccionado;

    private EstudianteDTO estudianteSeleccionado = new EstudianteDTO();

    private static int numeroPersonal = ControladorInicioDeSesionGUI.numeroDePersonal;

    Utilidades utilidades = new Utilidades();


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
        Utilidades utilidades = new Utilidades();

        try {
            EstudianteDAO estudianteDAO = new EstudianteDAO();
            ObservableList<EstudianteDTO> estudiantes = FXCollections.observableArrayList(
                    estudianteDAO.listarEstudiantesNoEvaluados(numeroPersonal)
            );
            tablaEstudiantes.setItems(estudiantes);

        } catch (Exception e) {
            logger.severe("Error al cargar la lista de estudiantes: " + e);
            utilidades.mostrarAlerta("Error", "Ocurrio un error inesperado", "No se pudo cargar la lista de estudiantes.");
            e.printStackTrace();
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

            stage.setOnCloseRequest(evento -> {
                eliminarEvaluacion();
            });

            stage.showAndWait();

            cargarEstudiantes();

        } catch (IOException e) {

            logger.severe("Error al abrir la ventana RegistrarEvaluacionGUI: " + e);
            e.printStackTrace();
        }

    }

    public void eliminarEvaluacion() {

        EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
        EvaluacionContieneDAO evaluacionContieneDAO = new EvaluacionContieneDAO();

        try {

            evaluacionContieneDAO.eliminarCriteriosPorIdEvaluacion(idEvaluacionGenerada);
            evaluacionDAO.eliminarEvaluacionDefinitivamente(idEvaluacionGenerada);

        } catch (SQLException e) {

            logger.severe("Error de SQL: " + e);
        } catch (IOException e) {

            logger.severe("Error de IO: " + e);
        } catch (Exception e) {

            logger.severe("Error inesperado: " + e);
        }
    }


    @FXML
    public void buscarEstudiante() {

        String matriculaBuscada = campoMatricula.getText().trim();

        if (matriculaBuscada.isEmpty()) {
            cargarEstudiantes();
            return;
        }

        try {
            EstudianteDAO estudianteDAO = new EstudianteDAO();
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
        } catch (Exception e) {
            logger.severe("Error al buscar el estudiante: " + e);
        }

    }

}
