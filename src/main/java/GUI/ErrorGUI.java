package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ErrorGUI {

    @FXML
    private Label campoMensaje;

    public void setMensaje(String mensaje) {
        campoMensaje.setText(mensaje);
    }
}
