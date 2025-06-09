package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControladorObjetoEvaluacion {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetallesEvaluacionGUI.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            ControladorDetallesEvaluacionGUI controller = loader.getController();
            controller.cargarDetallesEvaluacion(this.idEvaluacion);

            stage.setTitle("Detalles de Evaluación");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}