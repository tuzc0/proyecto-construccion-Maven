package GUI.utilidades;

import GUI.ErrorGUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;



public class Utilidades {

    private static final Logger logger = LogManager.getLogger(Utilidades.class);

    public void mostrarVentana(String fxml) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {

            logger.error("Error al cargar la ventana: " + fxml, e);

        }
    }

    public void mostrarVentanaError(String fxml, String mensaje) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ErrorGUI) {
                ((ErrorGUI) controller).setMensaje(mensaje);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            logger.error("Error al cargar la ventana: " + fxml, e);
        }
    }

}
