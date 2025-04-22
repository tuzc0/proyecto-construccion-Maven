package GUI.utilidades;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;



public class Utilidades {

    private static final Logger logger = LogManager.getLogger(Utilidades.class);

    private void mostrarVentana(String fxml) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {

            logger.error("Error al cargar la ventana: " + fxml, e);

        }
    }
}
