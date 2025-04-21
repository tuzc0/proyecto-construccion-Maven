import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("Iniciando la aplicación JavaFX...");

        // Carga el archivo FXML
        Parent root = FXMLLoader.load(getClass().getResource("/interfaz.fxml"));

        // Configura la ventana principal
        primaryStage.setTitle("Aplicación JavaFX con Scene Builder");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        logger.info("Aplicación JavaFX iniciada correctamente.");
    }

    public static void main(String[] args) {
        logger.info("Lanzando la aplicación...");
        launch(args);
    }
}
