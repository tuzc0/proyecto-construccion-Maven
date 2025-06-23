package GUI;

import GUI.gestionestudiante.AuxiliarGestionEstudiante;
import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import logica.DAOs.EstudianteDAO;
import logica.DTOs.EstudianteDTO;
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.Logger;



import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorListarEstudiantesConReporteMensualGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorListarEstudiantesConReporteMensualGUI.class);

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
    Utilidades gestorVentanas = new Utilidades();
    IGestorAlertas utilidades = new Utilidades();
    ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);

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

            columnaMatricula.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMatricula()));
            columnaNombre.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
            columnaApellidos.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));


            tablaEstudiantes.getItems().setAll(estudiantesConReporte);
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


    private void configurarColumnaVerReporte() {

        columnaVerReporte.setCellFactory(param ->
                new javafx.scene.control.TableCell<>() {
            private final javafx.scene.control.Button botonVerReporte =
                    new javafx.scene.control.Button("Ver Reporte");

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

        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListaReportesEstudianteGUI.fxml"));
            Parent root = loader.load();

            ControladorListarReportesPorEstudianteGUI controlador = loader.getController();
            controlador.setMatriculaEstudiante(matriculaEstudiante);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e){
            manejadorExcepciones.manejarIOException(e);
        }  catch (Exception e) {
            logger.error("Error inesperado al abrir la ventana de consulta de reportes mensuales: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error inesperado al abrir la ventana de consulta de reportes mensuales.",
                    "Por favor, contacta al administrador si el problema persiste."
            );
        }
    }


}
