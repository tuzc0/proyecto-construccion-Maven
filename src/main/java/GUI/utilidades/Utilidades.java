package GUI.utilidades;

import GUI.ControladorVentanaAvisoGUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.Optional;


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

    public static void abrirVentana(String rutaFXML, String tituloVentana, Stage ventanaPadre) {

        try {

            FXMLLoader cargadorFXML = new FXMLLoader(Utilidades.class.getResource(rutaFXML));
            Parent raizVentana = cargadorFXML.load();

            Stage nuevaVentana = new Stage();
            nuevaVentana.setScene(new Scene(raizVentana));
            nuevaVentana.setTitle(tituloVentana);

            nuevaVentana.initModality(Modality.APPLICATION_MODAL);
            nuevaVentana.initOwner(ventanaPadre);
            nuevaVentana.showAndWait();

        } catch (IOException excepcionEntradaSalida) {

            LogManager.getLogger(Utilidades.class).error(
                    "Error al abrir la ventana: " + excepcionEntradaSalida.getMessage());

            Utilidades utilidades = new Utilidades();

            utilidades.mostrarAlerta(
                    "Error del sistema",
                    "No se pudo abrir la ventana",
                    "Contacte al administrador");
        }
    }

    public void mostrarVentanaAviso(String fxml, String mensaje) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ControladorVentanaAvisoGUI) {
                ((ControladorVentanaAvisoGUI) controller).setMensaje(mensaje);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            logger.error("Error al cargar la ventana: " + fxml, e);
        }
    }

    public void mostrarAlerta(String titulo, String cabecera, String contenido) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(cabecera);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    public void mostrarAlertaConfirmacion(String titulo, String cabecera, String contenido) {

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(cabecera);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    public void mostrarAlertaConfirmacion(String titulo, String cabecera, String contenido,
                                          Runnable accionConfirmar, Runnable accionCancelar) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(cabecera);
        alerta.setContentText(contenido);

        ButtonType botonConfirmar = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alerta.getButtonTypes().setAll(botonConfirmar, botonCancelar);

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == botonConfirmar) {
            accionConfirmar.run();
        } else {
            accionCancelar.run();
        }
    }
}
