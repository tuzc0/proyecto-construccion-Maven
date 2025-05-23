import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logica.DAOs.CriterioEvaluacionDAO;
import logica.DAOs.EvaluacionContieneDAO;
import logica.DTOs.CriterioEvaluacionDTO;
import logica.DTOs.EvaluacionContieneDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TestRegistrarEvaluacionGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ConsultarEstudiantesAEvaluarGUI.fxml"));
        primaryStage.setTitle("Consultar Estudiantes");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }
}