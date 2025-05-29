package GUI;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;

public class ControladorMenuEstudianteGUI {


    Utilidades utilidades = new Utilidades();

    @FXML
    public void abrirEditarPerfilEstudiante() {
        utilidades.mostrarVentana("/EditarPerfilEstudianteGUI.fxml");
    }
}
