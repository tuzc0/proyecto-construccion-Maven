package GUI;


import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import logica.ContenedorCriteriosAutoevaluacion;
import logica.DAOs.AutoevaluacionContieneDAO;
import logica.DAOs.AutoevaluacionDAO;
import logica.DAOs.CriterioAutoevaluacionDAO;
import logica.DAOs.EvidenciaAutoevaluacionDAO;
import logica.DTOs.*;
import org.apache.logging.log4j.Logger;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class ControladorConsultarAutoevaluacionGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorConsultarAutoevaluacionGUI.class);

    @FXML
    private Label etiquetaFecha;

    @FXML
    private Label etiquetaPromedio;

    @FXML
    private TableView<ContenedorCriteriosAutoevaluacion> tablaAutoevaluacion;

    @FXML
    private TableColumn<ContenedorCriteriosAutoevaluacion, String> columnaNumeroCriterio;

    @FXML
    private TableColumn<ContenedorCriteriosAutoevaluacion, String> columnaCriterio;

    @FXML
    private TableColumn<ContenedorCriteriosAutoevaluacion, String> columnaCalificacion;

    @FXML
    private ListView<String> listaArchivos;

    private int idAutoevaluacion;

    private String matricula = ControladorInicioDeSesionGUI.matricula;

    Utilidades utilidades = new Utilidades();

    ManejadorExepciones manejadorExepciones = new ManejadorExepciones();

    @FXML
    private void initialize() {

        columnaNumeroCriterio.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getCriterioAutoevaluacion().getNumeroCriterio())));
        columnaCriterio.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getCriterioAutoevaluacion().getDescripcion()));
        columnaCalificacion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getAutoEvaluacionContiene().getCalificacion())));
        listaArchivos.setOnMouseClicked(this::abrirURLDrive);

        cargarAutoevaluacion();

    }

    public void cargarCriterios(){
        try{

            if(matricula == null || matricula.isEmpty() || matricula.equals(" ")) {

                matricula = ControladorListarEstudiantesConAutoevaluacionGUI.matriculaEstudiante;
            }

            AutoevaluacionDAO autoevaluacionDAO = new AutoevaluacionDAO();
            AutoevaluacionContieneDAO contieneDAO = new AutoevaluacionContieneDAO();
            CriterioAutoevaluacionDAO criterioDAO = new CriterioAutoevaluacionDAO();

            AutoevaluacionDTO auto = autoevaluacionDAO.buscarAutoevaluacionPorMatricula(matricula);
            idAutoevaluacion = auto.getIDAutoevaluacion();
            etiquetaFecha.setText(auto.getFecha().toString());
            etiquetaPromedio.setText(String.valueOf(auto.getCalificacionFinal()));

            Timestamp fecha = auto.getFecha();

            List<AutoEvaluacionContieneDTO> listaContiene = contieneDAO.listarAutoevaluacionesPorIdAutoevaluacion(idAutoevaluacion);
            List<CriterioAutoevaluacionDTO> listaCriterios = criterioDAO.listarCriteriosAutoevaluacionActivos();

            for (CriterioAutoevaluacionDTO criterio : listaCriterios) {
                for (AutoEvaluacionContieneDTO contiene : listaContiene) {
                    if (criterio.getIDCriterio() == contiene.getIdCriterio()) {
                        tablaAutoevaluacion.getItems().add(new ContenedorCriteriosAutoevaluacion(criterio, contiene));
                    }
                }
            }
        } catch (SQLException e){

            manejadorExepciones.manejarSQLException(e, logger, utilidades); ;

        } catch (IOException e) {

            manejadorExepciones.manejarIOException(e, logger, utilidades);

        } catch (Exception e) {

            logger.error("Error inesperado al cargar los criterios de autoevaluación: " + e.getMessage(), e);
            utilidades.mostrarAlerta("Error", "Ocurrió un error al cargar los criterios de autoevaluación.",
                    "Por favor, inténtelo de nuevo más tarde.");

        }
    }

    private void cargarAutoevaluacion() {

        try {

            cargarCriterios();

            EvidenciaAutoevaluacionDAO evidenciaDAO = new EvidenciaAutoevaluacionDAO();

            EvidenciaAutoevaluacionDTO evidencias = evidenciaDAO.mostrarEvidenciaAutoevaluacionPorID(idAutoevaluacion);

            if (evidencias != null && evidencias.getURL() != null && !evidencias.getURL().isEmpty()) {
                listaArchivos.getItems().add(evidencias.getURL());
            } else {
                listaArchivos.getItems().add("No hay evidencias disponibles.");
            }

        } catch (SQLException e) {

            manejadorExepciones.manejarSQLException(e, logger, utilidades);

        } catch (IOException e) {

            manejadorExepciones.manejarIOException(e, logger, utilidades);

        } catch (Exception e) {

            logger.error("Error inesperado al cargar la autoevaluación: " + e.getMessage(), e);
            utilidades.mostrarAlerta("Error", "Ocurrió un error al cargar la autoevaluación.",
                    "Por favor, inténtelo de nuevo más tarde.");
        }
    }

    private void abrirURLDrive(MouseEvent event) {

        if (event.getClickCount() == 2) {

            String url = listaArchivos.getSelectionModel().getSelectedItem();

            if (url != null && Desktop.isDesktopSupported()) {

                try {

                    Desktop.getDesktop().browse(URI.create(url));

                } catch (IOException e) {

                    manejadorExepciones.manejarIOException(e, logger, utilidades);

                }
            }
        }
    }

    @FXML
    private void cerrarVentana() {

        Stage stage = (Stage) etiquetaFecha.getScene().getWindow();
        stage.close();

    }
}

