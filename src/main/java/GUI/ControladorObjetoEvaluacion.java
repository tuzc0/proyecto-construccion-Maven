package GUI;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;

public class ControladorObjetoEvaluacion {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorObjetoEvaluacion.class);

    Utilidades utilidades = new Utilidades();
    @FXML private HBox root;
    @FXML private Label etiquetaNombreEvaluador;
    @FXML private Label etiquetaCalificacion;

    private int idEvaluacion;

    public void setDatosEvaluacion(String nombreEvaluador, String calificacion, int idEvaluacion) {
        etiquetaNombreEvaluador.setText(nombreEvaluador);
        etiquetaCalificacion.setText(calificacion);
        this.idEvaluacion = idEvaluacion;
    }

    @FXML
    private void mostrarDetallesEvaluacion() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetallesEvaluacionGUI.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            ControladorDetallesEvaluacionGUI controller = loader.getController();
            controller.cargarDetallesEvaluacion(this.idEvaluacion);

            stage.setTitle("Detalles de Evaluación");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (Exception e) {

            logger.error("Error al cargar la ventana de detalles de evaluación: " +  e);
            utilidades.mostrarAlerta("Error", "No se pudo cargar la evaluación", "La evaluación solicitada no existe o no se pudo cargar.");
        }
    }
}