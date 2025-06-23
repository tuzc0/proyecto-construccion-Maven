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
import logica.DAOs.EstudianteDAO;
import logica.DTOs.EstudianteDTO;
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class ControladorConsultarEstudiantesEvaluadosGUI {

    Logger logger =
            org.apache.logging.log4j.LogManager.getLogger(ControladorConsultarEstudiantesEvaluadosGUI.class);

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

    AuxiliarGestionEstudiante auxiliarGestionEstudiante = new AuxiliarGestionEstudiante();

    Utilidades gestorVentanas = new Utilidades();

    IGestorAlertas utilidades = new Utilidades();

    ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);

    private static int numeroPersonal = ControladorInicioDeSesionGUI.numeroDePersonal;

    int NRC = auxiliarGestionEstudiante.obtenerNRC();


    @FXML
    public void initialize() {

        columnaMatricula.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMatricula()));
        columnaNombres.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        columnaApellidos.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));

        cargarEstudiantes();
        añadirBotonVerEvaluacionesATabla();

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
                    estudianteDAO.listarEstudiantesConEvaluacionesPorGrupo(NRC));
            tablaEstudiantes.setItems(estudiantes);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error al cargar los estudiantes: ", e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error al cargar los estudiantes.",
                    "Por favor, inténtelo de nuevo más tarde."
            );

        }
    }

    private void añadirBotonVerEvaluacionesATabla() {

        Callback<TableColumn<EstudianteDTO, Void>, TableCell<EstudianteDTO, Void>> cellFactory =
                param -> new TableCell<>() {

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

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/ConsultarEvaluacionesEstudianteGUI.fxml"));
            Parent root = loader.load();

            ControladorConsultarEvaluacionesEstudianteGUI controlador = loader.getController();
            controlador.setMatricula(matriculaEstudianteSeleccionado);

            Stage stage = new Stage();
            stage.setTitle("Evaluaciones del Estudiante");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
            cargarEstudiantes();

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
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
                    estudianteDAO.listarEstudiantesConEvaluacionesPorGrupo(NRC)
            );

            ObservableList<EstudianteDTO> estudiantesFiltrados = estudiantes.filtered(
                    estudiante ->
                            estudiante.getMatricula().toLowerCase().contains(matriculaBuscada.toLowerCase())
            );

            if (estudiantesFiltrados.isEmpty()) {

                utilidades.mostrarAlerta(
                        "No encontrado",
                        "Estudiante no encontrado",
                        "No se encontró un estudiante con la matrícula: " + matriculaBuscada
                );

            } else {

                tablaEstudiantes.setItems(estudiantesFiltrados);
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
        }
    }
}
