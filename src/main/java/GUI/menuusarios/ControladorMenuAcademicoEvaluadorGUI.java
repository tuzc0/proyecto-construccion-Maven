package GUI.menuusarios;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;

public class ControladorMenuAcademicoEvaluadorGUI {

    Utilidades utilidades = new Utilidades();

    @FXML
    private void abrirConsultarEstudiantesAEvaluar() {

        utilidades.mostrarVentana("/ConsultarEstudiantesAEvaluarGUI.fxml");
    }
}
