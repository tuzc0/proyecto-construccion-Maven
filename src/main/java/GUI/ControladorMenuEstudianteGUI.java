package GUI;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logica.DAOs.AutoevaluacionContieneDAO;
import logica.DAOs.AutoevaluacionDAO;
import logica.DAOs.ReporteDAO;
import logica.DTOs.AutoevaluacionDTO;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

import static GUI.ControladorRegistrarAutoevaluacionGUI.idAutoevaluacion;

public class ControladorMenuEstudianteGUI {


    Utilidades utilidades = new Utilidades();
    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorRegistrarAutoevaluacionGUI.class);

    @FXML
    private Button botonRegistrarAutoevaluacion;

    @FXML
    private Button botonConsultarAutoevaluacion;

    private String matricula = ControladorInicioDeSesionGUI.matricula;


    @FXML
    public void initialize() {

        verificarAutoevaluacionRegistrada();
    }

    private void verificarAutoevaluacionRegistrada() {
        AutoevaluacionDAO autoevaluacionDAO = new AutoevaluacionDAO();

        try {

            AutoevaluacionDTO autoevaluacion = autoevaluacionDAO.buscarAutoevaluacionPorMatricula(matricula);

            if (autoevaluacion.getIDAutoevaluacion() == -1) {

                botonRegistrarAutoevaluacion.setDisable(false);
                botonConsultarAutoevaluacion.setDisable(true);
                logger.info("No se encontró una autoevaluación vinculada a la matrícula: " + matricula);
            } else {

                botonConsultarAutoevaluacion.setDisable(false);
                botonRegistrarAutoevaluacion.setDisable(true);
                logger.info("Se encontró una autoevaluación vinculada a la matrícula: " + matricula);
            }
        } catch (Exception e) {
            logger.error("Error al verificar autoevaluación: " + e.getMessage());
        }
    }

    @FXML
    public void abrirEditarPerfilEstudiante() {
        utilidades.mostrarVentana("/EditarPerfilEstudianteGUI.fxml");
    }

    @FXML
    public void abrirRegistrarAutoevaluacion() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistrarAutoevaluacionGUI.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registrar Autoevaluación");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setOnHidden(event -> {

                verificarAutoevaluacionRegistrada();
            });

            stage.setOnCloseRequest(event -> {
                eliminarEvaluacion();
            });

            stage.showAndWait();

        } catch (IOException e) {

            logger.error("Error al abrir la ventana RegistrarAutoevaluacionGUI: " + e.getMessage());
        }
    }

    public void eliminarEvaluacion() {

        AutoevaluacionDAO autoevaluacionDAO = new AutoevaluacionDAO();
        AutoevaluacionContieneDAO autoevaluacionContieneDAO = new AutoevaluacionContieneDAO();

        try {

            autoevaluacionContieneDAO.eliminarCriteriosDefinitivamentePorIdAutoevaluacion(idAutoevaluacion);
            autoevaluacionDAO.eliminarAutoevaluacionDefinitivamentePorID(idAutoevaluacion);


        } catch (SQLException e) {

            logger.error("Error de SQL al eliminar la evaluación: " + e.getMessage());

        } catch (IOException e) {

            logger.error("Error de IO al eliminar la evaluación: " + e.getMessage());

        } catch (Exception e) {

            logger.error("Error inesperado al eliminar la evaluación: " + e.getMessage());

        }
    }

    @FXML
    public void abrirConsultarAutoevaluacion() {

        utilidades.mostrarVentana("/ConsultarAutoevaluacionGUI.fxml");


    }

    @FXML
    public void abrirConsultarEvaluacionesEstudiante() {

        utilidades.mostrarVentana("/ConsultarEvaluacionesEstudianteGUI.fxml");

    }

    @FXML
    public void abrirRegistrarReporteMensual() {
        try {
            ReporteDAO reporteDAO = new ReporteDAO();

            int mesActual = java.time.LocalDate.now().getMonthValue();
            int añoActual = java.time.LocalDate.now().getYear();

            if (reporteDAO.existeReporteEnMes(matricula,mesActual, añoActual)) {
                utilidades.mostrarAlerta("Reporte Mensual", "Ya has registrado un reporte mensual este mes.", "No puedes registrar más de un reporte mensual por mes.");
                return;
            }

            utilidades.mostrarVentana("/RegistrarReporteMensualGUI.fxml");
        } catch (SQLException | IOException e) {
            logger.error("Error al verificar reporte mensual: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "No se pudo verificar el reporte mensual.", "Por favor, intenta nuevamente más tarde.");
        }
    }

    @FXML
    public void abrirRegistrarCronogramaActividades() {

        utilidades.mostrarVentana("/RegistroCronogramaActividadesGUI.fxml");
    }

}
