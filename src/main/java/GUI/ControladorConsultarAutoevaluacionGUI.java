package GUI;


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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

public class ControladorConsultarAutoevaluacionGUI {

    @FXML private Label etiquetaFecha;
    @FXML private Label etiquetaPromedio;
    @FXML private TableView<ContenedorCriteriosAutoevaluacion> tablaAutoevaluacion;
    @FXML private TableColumn<ContenedorCriteriosAutoevaluacion, String> columnaNumeroCriterio;
    @FXML private TableColumn<ContenedorCriteriosAutoevaluacion, String> columnaCriterio;
    @FXML private TableColumn<ContenedorCriteriosAutoevaluacion, String> columnaCalificacion;
    @FXML private ListView<String> listaArchivos;

    private int idAutoevaluacion;

    private String matricula = ControladorInicioDeSesionGUI.matricula;



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
            AutoevaluacionDAO autoevaluacionDAO = new AutoevaluacionDAO();
            AutoevaluacionContieneDAO contieneDAO = new AutoevaluacionContieneDAO();
            CriterioAutoevaluacionDAO criterioDAO = new CriterioAutoevaluacionDAO();


            AutoevaluacionDTO auto = autoevaluacionDAO.buscarAutoevaluacionPorMatricula(matricula);
            idAutoevaluacion = auto.getIDAutoevaluacion();
            etiquetaFecha.setText(auto.getFecha().toString());
            etiquetaPromedio.setText(String.valueOf(auto.getCalificacionFinal()));

            List<AutoEvaluacionContieneDTO> listaContiene = contieneDAO.listarAutoevaluacionesPorIdAutoevaluacion(idAutoevaluacion);
            List<CriterioAutoevaluacionDTO> listaCriterios = criterioDAO.listarCriteriosAutoevaluacionActivos();

            for (CriterioAutoevaluacionDTO criterio : listaCriterios) {
                for (AutoEvaluacionContieneDTO contiene : listaContiene) {
                    if (criterio.getIDCriterio() == contiene.getIdCriterio()) {
                        tablaAutoevaluacion.getItems().add(new ContenedorCriteriosAutoevaluacion(criterio, contiene));
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarAutoevaluacion() {
        try {

            cargarCriterios();

            EvidenciaAutoevaluacionDAO evidenciaDAO = new EvidenciaAutoevaluacionDAO();

            System.out.println("Cargando autoevaluación para la matrícula: " + idAutoevaluacion);

            EvidenciaAutoevaluacionDTO evidencias = evidenciaDAO.mostrarEvidenciaAutoevaluacionPorID(idAutoevaluacion);

            System.out.println("Evidencia encontrada: " + evidencias);
            if (evidencias != null && evidencias.getURL() != null && !evidencias.getURL().isEmpty()) {
                System.out.println("URL de evidencia: " + evidencias.getURL());
                listaArchivos.getItems().add(evidencias.getURL());
            } else {
                listaArchivos.getItems().add("No hay evidencias disponibles.");
            }


        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    private void abrirURLDrive(MouseEvent event) {

        if (event.getClickCount() == 2) {
            String url = listaArchivos.getSelectionModel().getSelectedItem();
            if (url != null && Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(URI.create(url));
                } catch (IOException e) {
                    e.printStackTrace();
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

