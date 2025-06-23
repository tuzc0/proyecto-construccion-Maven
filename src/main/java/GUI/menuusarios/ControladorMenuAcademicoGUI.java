package GUI.menuusarios;

import GUI.ControladorInicioDeSesionGUI;
import GUI.gestionestudiante.AuxiliarGestionEstudiante;
import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;

public class ControladorMenuAcademicoGUI {

    Utilidades utilidades = new Utilidades();

    AuxiliarGestionEstudiante auxiliarGestionEstudiante = new AuxiliarGestionEstudiante();

    int NRC = auxiliarGestionEstudiante.obtenerNRC();

    @FXML
    public void abrirRegistroEstudiante() {

        String ventana = "/RegistroEstudianteGUI.fxml";

        mostrarVentana(ventana);

    }

    @FXML
    public void abrirConsultarEstudiante() {

        String ventana = "/GestorEstudiantesGUI.fxml";
        mostrarVentana(ventana);
    }

    @FXML
    public void abrirConsultarEvaluaciones() {

        String ventana = "/ConsultarEstudiantesEvaluadosGUI.fxml";
        mostrarVentana(ventana);
    }

    public void mostrarVentana(String ventana) {

        if (NRC == -1) {

            utilidades.mostrarAlerta(
                    "Grupo no encontrado",
                    "No se encontró un grupo activo",
                    "Por favor, asegúrese de que el número de personal sea correcto y que exista un grupo activo."
            );

        } else {
            utilidades.mostrarVentana(ventana);
        }
    }

    @FXML
    public void abrirVentanaCalificacionFinal() {

        String ventana = "/RegistrarCalificacionFinalGUI.fxml";
        mostrarVentana(ventana);
    }

    @FXML
    public void abrirConsultarReporteMensual() {

        String ventana = "/ListarEstudiantesConReporteMensual.fxml";
        mostrarVentana(ventana);
    }

    @FXML
    public void abrirConsultarAutoevaluacion() {

        String ventana = "/ListarEstudiantesConAutoevaluacionesGUI.fxml";
        mostrarVentana(ventana);
    }


}
