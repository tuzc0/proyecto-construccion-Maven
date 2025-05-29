package GUI;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;

public class ControladorMenuAcademicoGUI {

    Utilidades utilidades = new Utilidades();

    int numeroDePersonal = ControladorInicioDeSesionGUI.numeroDePersonal;


    @FXML
    public void abrirRegistroEstudiante() {
        utilidades.mostrarVentana("/RegistroEstudianteGUI.fxml");
    }

    @FXML
    public void abrirConsultarEstudiante() {
        utilidades.mostrarVentana("/GestorEstudiantesGUI.fxml");
    }


}
