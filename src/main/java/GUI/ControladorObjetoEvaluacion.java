package GUI;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ControladorObjetoEvaluacion {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorObjetoEvaluacion.class);

    @FXML
    private Label etiquetaNombreEvaluador;
    @FXML
    private Label etiquetaCalificacion;

    private Utilidades gestorVentana = new Utilidades();
    private IGestorAlertas utilidades = new Utilidades();
    private ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);

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

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            utilidades.mostrarAlerta("Error al mostrar detalles de la evaluación",
                    "No se pudieron cargar los detalles de la evaluación. Por favor, inténtelo de nuevo más tarde.", "Error inesperado");
            logger.error("Error al mostrar detalles de la evaluación: ", e);

        }
    }
}