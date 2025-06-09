package GUI;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logica.DAOs.AutoevaluacionContieneDAO;
import logica.DAOs.AutoevaluacionDAO;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

import static GUI.ControladorRegistrarAutoevaluacionGUI.idAutoevaluacion;

public class ControladorMenuEstudianteGUI {


    Utilidades utilidades = new Utilidades();
    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorRegistrarAutoevaluacionGUI.class);

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
    public void abrirConsultarEvaluacionesEstudiante() {

        utilidades.mostrarVentana("/ConsultarEvaluacionesEstudianteGUI.fxml");
    }


}
