package GUI;

import GUI.utilidades.Utilidades;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import logica.DAOs.ReporteDAO;
import logica.DTOs.ReporteDTO;
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorListarReportesPorEstudianteGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorListarReportesPorEstudianteGUI.class);

    @FXML
    TableView<ReporteDTO> tablaReportes;

    @FXML
    TableColumn<ReporteDTO, String> columnaMetodologia;

    @FXML
    TableColumn<ReporteDTO, String> columnaFecha;

    @FXML
    TableColumn<ReporteDTO, String> columnaVerReporte;

    Utilidades gestorVentana = new Utilidades();
    IGestorAlertas utilidades = new Utilidades();
    ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);

    public static int idReporteSeleccionado = 0;

    public String matriculaEstudiante = ControladorListarEstudiantesConReporteMensualGUI.matriculaEstudiante;


    public void setMatriculaEstudiante(String matricula) {

        this.matriculaEstudiante = matricula;
        cargarDatosReportes();

    }

    @FXML
    public void initialize() {


        columnaFecha.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFecha().toString()));
        columnaMetodologia.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMetodologia()));
        columnaVerReporte.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getObservaciones()));

        configurarColumnaVerReporte();
    }

    public void cargarDatosReportes() {

        ReporteDAO reporteDAO = new ReporteDAO();

        try {
            List<ReporteDTO> listaReportes = reporteDAO.buscarReportesPorMatricula(matriculaEstudiante);

            if (listaReportes != null && !listaReportes.isEmpty()) {
                tablaReportes.getItems().setAll(listaReportes);

            } else {
                gestorVentana.mostrarAlerta("No se encontraron reportes para el estudiante seleccionado.",
                        "Información",
                        "El estudiante no tiene reportes mensuales registrados.");
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al cargar los reportes: ", e);
            utilidades.mostrarAlerta("Error al cargar los reportes",
                    "No se pudieron cargar los reportes del estudiante. Por favor, inténtelo de nuevo más tarde.",
                    "Error inesperado");
        }
    }

    private void configurarColumnaVerReporte() {

        columnaVerReporte.setCellFactory(parametro -> new TableCell<>() {
            private final Button BOTON_VER_DETALLES = new Button("Ver Detalles");

            {
                BOTON_VER_DETALLES.setOnAction(evento -> {
                    ReporteDTO reporte = getTableView().getItems().get(getIndex());
                    idReporteSeleccionado = reporte.getIDReporte();
                    verDetallesReporte(reporte);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(BOTON_VER_DETALLES);
                }
            }
        });
    }

    private void verDetallesReporte(ReporteDTO reporte) {

        gestorVentana.mostrarVentana("/ConsultarReporteMensualGUI.fxml");
    }
}
