package GUI;

import GUI.utilidades.Utilidades;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logica.ContenedorActividadesReporte;
import logica.DAOs.ActividadDAO;
import logica.DAOs.EvidenciaReporteDAO;
import logica.DAOs.ReporteContieneDAO;
import logica.DAOs.ReporteDAO;
import logica.DTOs.ActividadDTO;
import logica.DTOs.EvidenciaReporteDTO;
import logica.DTOs.ReporteContieneDTO;
import logica.DTOs.ReporteDTO;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorConsultarReporteMensualGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorConsultarReporteMensualGUI.class);

    @FXML
    Label etiquetaMatricula;

    @FXML
    Label etiquetaFecha;

    @FXML
    TextArea textoMetodologia;

    @FXML
    TextArea textoObservaciones;

    @FXML
    Label etiquetaHoras;

    @FXML
    TableView<ContenedorActividadesReporte> tablaActividades;

    @FXML
    TableColumn<ContenedorActividadesReporte, String> columnaActividad;

    @FXML
    TableColumn <ContenedorActividadesReporte, String> columnaFechaInicio;

    @FXML
    TableColumn <ContenedorActividadesReporte, String> columnaFechaFin;

    @FXML
    ListView<String> listaArchivos;


    Utilidades utilidades = new Utilidades();

    ManejadorExepciones manejadorExepciones = new ManejadorExepciones();

    int idReporteSeleccionado = ControladorListarReportesPorEstudianteGUI.idReporteSeleccionado;

    @FXML
    public void initialize() {

        cargarDatosReporte();

        cargarActividades();
        columnaActividad.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getActividad().getNombre())
        );
        columnaFechaInicio.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReporteContiene().getFechaInicioReal().toString())
        );
        columnaFechaFin.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReporteContiene().getFechaFinReal().toString())
        );

    }

    @FXML
    public void cargarDatosReporte() {
        try {

            ReporteDAO reporteDAO = new ReporteDAO();
            ReporteDTO reporte = reporteDAO.buscarReportePorID(idReporteSeleccionado);

            if (reporte != null) {
                etiquetaMatricula.setText(reporte.getMatricula());
                etiquetaFecha.setText(reporte.getFecha().toString());
                textoMetodologia.setText(reporte.getMetodologia());
                textoObservaciones.setText(reporte.getObservaciones());
                etiquetaHoras.setText(String.valueOf(reporte.getNumeroHoras()));

                EvidenciaReporteDAO evidenciaReporteDAO = new EvidenciaReporteDAO();
                List<EvidenciaReporteDTO> evidenciasReporte = evidenciaReporteDAO.mostrarEvidenciasPorIdReporte(idReporteSeleccionado);
                if (evidenciasReporte != null && !evidenciasReporte.isEmpty()) {
                    for (EvidenciaReporteDTO evidencia : evidenciasReporte) {
                        listaArchivos.getItems().add(evidencia.getURL());
                    }
                } else {
                    utilidades.mostrarAlerta("No se encontraron evidencias", "Información", "El reporte no tiene evidencias asociadas.");
                }

            } else {
                utilidades.mostrarAlerta("No se encontró el reporte", "Información", "El reporte seleccionado no existe.");
            }


        } catch (SQLException e) {

            manejadorExepciones.manejarSQLException(e, logger, utilidades);

        } catch (IOException e) {

            manejadorExepciones.manejarIOException(e, logger, utilidades);

        } catch (Exception e) {
            logger.error("Error inesperado al cargar los datos del reporte: " + e);
            utilidades.mostrarAlerta("Error", "No se pudieron cargar los datos del reporte.", "Por favor, intente nuevamente más tarde.");
        }
    }

    @FXML
    public void verArchivo() {
        String urlSeleccionada = listaArchivos.getSelectionModel().getSelectedItem();
        if (urlSeleccionada != null && !urlSeleccionada.isEmpty()) {

            try {

                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                desktop.browse(new java.net.URI(urlSeleccionada));

            } catch (Exception e) {

                utilidades.mostrarAlerta("Error", "No se pudo abrir el archivo.", "Por favor, verifique la URL.");
                logger.error("Error al abrir el archivo: " + e);

            }
        } else {

            utilidades.mostrarAlerta("Advertencia", "No se seleccionó ningún archivo.", "Seleccione un archivo de la lista para abrirlo.");
        }
    }


    public void cargarActividades () {

        ActividadDAO actividadDAO = new ActividadDAO();
        ReporteContieneDAO reporteContieneDAO = new ReporteContieneDAO();

        try {

            List<ReporteContieneDTO> listaActividadesReporte = reporteContieneDAO.listarReporteContienePorIDReporte(idReporteSeleccionado);

            for (ReporteContieneDTO reporteContiene : listaActividadesReporte) {

                ActividadDTO actividadDTO = actividadDAO.buscarActividadPorID(reporteContiene.getIdActividad());

                ContenedorActividadesReporte contenedorActividadesReporte = new ContenedorActividadesReporte(
                        actividadDTO,
                        reporteContiene
                );


                tablaActividades.getItems().add(contenedorActividadesReporte);
            }


        } catch (SQLException e) {

            manejadorExepciones.manejarSQLException(e, logger, utilidades);

        } catch (IOException e) {

            manejadorExepciones.manejarIOException(e, logger, utilidades);

        } catch (Exception e) {

            logger.error("Error inesperado al cargar las actividades: " + e);
            utilidades.mostrarAlerta("Error",
                    "No se pudieron cargar las actividades.",
                    "Por favor, intente nuevamente más tarde.");
        }
    }

    @FXML
    public void cerrarVentana() {

        Stage stage = (Stage) etiquetaMatricula.getScene().getWindow();
        if (stage != null) {
            stage.close();
        } else {
            utilidades.mostrarAlerta("Error", "No se pudo cerrar la ventana.", "Por favor, intente nuevamente.");
        }

    }
}
