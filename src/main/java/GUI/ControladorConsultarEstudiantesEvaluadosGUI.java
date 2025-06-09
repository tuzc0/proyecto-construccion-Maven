package GUI;

import GUI.gestionestudiante.AuxiliarGestionEstudiante;
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
import logica.DAOs.CuentaDAO;
import logica.DAOs.EstudianteDAO;
import logica.DAOs.EvaluacionContieneDAO;
import logica.DAOs.EvaluacionDAO;
import logica.DTOs.CuentaDTO;
import logica.DTOs.EstudianteDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import static GUI.ControladorRegistrarEvaluacionGUI.idEvaluacionGenerada;

public class ControladorConsultarEstudiantesEvaluadosGUI {

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

    AuxiliarGestionEstudiante auxiliarGestionEstudiante = new AuxiliarGestionEstudiante();
    int NRC = auxiliarGestionEstudiante.obtenerNRC();


    @FXML
    public void initialize() {

        columnaMatricula.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMatricula()));
        columnaNombres.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        columnaApellidos.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));

        cargarEstudiantes();
        añadirBotonVerEvaluacionesATabla();

        tablaEstudiantes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            estudianteSeleccionado = newValue;
            tablaEstudiantes.refresh();
        });
    }

    private void cargarEstudiantes() {
        Utilidades utilidades = new Utilidades();

        try {
            EstudianteDAO estudianteDAO = new EstudianteDAO();
            ObservableList<EstudianteDTO> estudiantes = FXCollections.observableArrayList(
                    estudianteDAO.listarEstudiantesConEvaluacionesPorGrupo(NRC)
            );
            tablaEstudiantes.setItems(estudiantes);

        } catch (Exception e) {
            logger.severe("Error al cargar la lista de estudiantes: " + e);
            utilidades.mostrarAlerta("Error", "Error de entrada/salida", "No se pudo cargar la lista de estudiantes.");
        }
    }

    private void añadirBotonVerEvaluacionesATabla() {



        Callback<TableColumn<EstudianteDTO, Void>, TableCell<EstudianteDTO, Void>> cellFactory = param -> new TableCell<>() {

            private final Button botonEvaluar = new Button("Ver Evaluaciones");

            {
                botonEvaluar.setOnAction(event -> {

                    EstudianteDTO estudianteSeleccionado = getTableView().getItems().get(getIndex());
                    matriculaEstudianteSeleccionado = estudianteSeleccionado.getMatricula();
                    abrirVentanaConsultarEvaluacionesEstudiante();

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

    public void abrirVentanaConsultarEvaluacionesEstudiante() {

        try {


            System.out.println("Matricula del estudiante seleccionado: " + matriculaEstudianteSeleccionado);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConsultarEvaluacionesEstudianteGUI.fxml"));
            Parent root = loader.load();

            ConsultarEvaluacionesEstudiante controlador = loader.getController();
            controlador.setMatricula(matriculaEstudianteSeleccionado);

            Stage stage = new Stage();
            stage.setTitle("Evaluaciones del Estudiante");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);


            stage.showAndWait();

            cargarEstudiantes();

        } catch (IOException e) {
            logger.severe("Error al abrir la ventana RegistrarEvaluacionGUI: " + e.getMessage());
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
                    estudianteDAO.listarEstudiantesConEvaluacionesPorGrupo(NRC)
            );

            ObservableList<EstudianteDTO> estudiantesFiltrados = estudiantes.filtered(
                    estudiante -> estudiante.getMatricula().toLowerCase().contains(matriculaBuscada.toLowerCase())
            );

            if (estudiantesFiltrados.isEmpty()) {
                Utilidades utilidades = new Utilidades();
                utilidades.mostrarAlerta("No encontrado", "Estudiante no encontrado", "No se encontró un estudiante con la matrícula: " + matriculaBuscada);
            } else {
                tablaEstudiantes.setItems(estudiantesFiltrados);
            }
        } catch (Exception e) {
            logger.severe("Error al buscar el estudiante: " + e.getMessage());
        }
    }
}
