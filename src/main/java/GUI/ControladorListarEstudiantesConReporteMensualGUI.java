package GUI;

import GUI.gestionestudiante.AuxiliarGestionEstudiante;
import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import logica.DAOs.EstudianteDAO;
import logica.DTOs.EstudianteDTO;
import org.apache.logging.log4j.Logger;



import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorListarEstudiantesConReporteMensualGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorListarEstudiantesConReporteMensualGUI.class);

    Utilidades utilidades = new Utilidades();

    @FXML
    TableView<EstudianteDTO> tablaEstudiantes;

    @FXML
    TableColumn<EstudianteDTO, String> columnaMatricula;

    @FXML
    TableColumn<EstudianteDTO, String> columnaNombre;

    @FXML
    TableColumn<EstudianteDTO, String> columnaApellidos;

    @FXML
    TableColumn<EstudianteDTO, String> columnaVerReporte;

    AuxiliarGestionEstudiante auxiliarGestionEstudiantes = new AuxiliarGestionEstudiante();

    public static String matriculaEstudiante = " ";

    private int NRC = 0;


    @FXML
    public void initialize() {

        cargarDatosEstudiantes();
        configurarColumnaVerReporte();

    }

    public void cargarDatosEstudiantes() {
        try {
            NRC = auxiliarGestionEstudiantes.obtenerNRC();


            EstudianteDAO estudianteDAO = new EstudianteDAO();
            List<EstudianteDTO> estudiantesConReporte = estudianteDAO.listarEstudiantesConReporteMensualPorGrupo(NRC);


            columnaMatricula.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMatricula()));
            columnaNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
            columnaApellidos.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));


            tablaEstudiantes.getItems().setAll(estudiantesConReporte);
        } catch (SQLException e) {

            logger.error("Error al cargar los datos de los estudiantes de la base de datos: " + e);
            utilidades.mostrarAlerta("Error de base de datos", "No se pudieron cargar los datos de los estudiantes. Por favor, intente más tarde.", "Error de base de datos");

        } catch (IOException e) {

            logger.error("Error al cargar los datos de los estudiantes: " + e);
            utilidades.mostrarAlerta("Error de entrada/salida", "No se pudieron cargar los datos de los estudiantes. Por favor, intente más tarde.", "Error de entrada/salida");


        } catch (Exception e) {

            logger.error("Error inesperado al cargar los datos de los estudiantes: " + e);
            utilidades.mostrarAlerta("Error inesperado", "No se pudieron cargar los datos de los estudiantes. Por favor, intente más tarde.", "Error inesperado");

        }
    }


    private void configurarColumnaVerReporte() {
        columnaVerReporte.setCellFactory(param -> new javafx.scene.control.TableCell<>() {
            private final javafx.scene.control.Button botonVerReporte = new javafx.scene.control.Button("Ver Reporte");

            {
                botonVerReporte.setOnAction(event -> {
                    EstudianteDTO estudiante = getTableView().getItems().get(getIndex());
                    matriculaEstudiante = estudiante.getMatricula();
                    verListaReporteMensual();
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(botonVerReporte);
                }
            }
        });
    }

    private void verListaReporteMensual( ) {

            utilidades.mostrarVentana("/ListaReportesEstudianteGUI.fxml");

    }


}
