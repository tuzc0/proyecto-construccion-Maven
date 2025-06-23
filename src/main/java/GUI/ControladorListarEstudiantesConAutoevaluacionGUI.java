package GUI;

import GUI.gestionestudiante.AuxiliarGestionEstudiante;
import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import logica.DAOs.EstudianteDAO;
import logica.DTOs.EstudianteDTO;
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorListarEstudiantesConAutoevaluacionGUI {

    Logger logger =
            org.apache.logging.log4j.LogManager.getLogger(ControladorListarEstudiantesConReporteMensualGUI.class);

    @FXML
    TableView<EstudianteDTO> tablaEstudiantes;

    @FXML
    TableColumn<EstudianteDTO, String> columnaMatricula;

    @FXML
    TableColumn<EstudianteDTO, String> columnaNombre;

    @FXML
    TableColumn<EstudianteDTO, String> columnaApellidos;

    @FXML
    TableColumn<EstudianteDTO, String> columnaVerAutoevaluacion;

    AuxiliarGestionEstudiante auxiliarGestionEstudiantes = new AuxiliarGestionEstudiante();
    Utilidades gestorVentanas = new Utilidades();
    IGestorAlertas utilidades = new Utilidades();
    ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);

    public static String matriculaEstudiante = " ";
    private int NRC = 0;


    @FXML
    public void initialize() {

        cargarDatosEstudiantes();
        configurarColumnaVerAutoevaluacion();
    }

    public void cargarDatosEstudiantes() {

        try {

            NRC = auxiliarGestionEstudiantes.obtenerNRC();

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            List<EstudianteDTO> estudiantesConAutoevaluacion =
                    estudianteDAO.listarEstudiantesConAutoevaluacion(NRC);


            columnaMatricula.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMatricula()));
            columnaNombre.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
            columnaApellidos.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));

            tablaEstudiantes.getItems().setAll(estudiantesConAutoevaluacion);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al cargar los datos de los estudiantes: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error inesperado",
                    "No se pudieron cargar los datos de los estudiantes.",
                    "Por favor, intente más tarde."
            );

        }
    }

    private void configurarColumnaVerAutoevaluacion() {

        columnaVerAutoevaluacion.setCellFactory(param ->
                new javafx.scene.control.TableCell<>() {

                    private final javafx.scene.control.Button botonVerEvaluacion =
                            new javafx.scene.control.Button("Ver Autoevaluacion");

                    {
                        botonVerEvaluacion.setOnAction(event -> {
                            EstudianteDTO estudiante = getTableView().getItems().get(getIndex());
                            matriculaEstudiante = estudiante.getMatricula();
                            verAutoevaluacion();
                        });
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {

                        super.updateItem(item, empty);
                        if (empty) {

                            setGraphic(null);

                        } else {

                            setGraphic(botonVerEvaluacion);

                        }
                    }
                });
    }

    private void verAutoevaluacion() {

        gestorVentanas.mostrarVentana("/ConsultarAutoevaluacionGUI.fxml");
    }
}
