package GUI.menuusarios;

import GUI.ControladorInicioDeSesionGUI;
import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;

public class ControladorMenuCoordinador {

    Utilidades utilidades = new Utilidades();

    int numeroDePersonal = ControladorInicioDeSesionGUI.numeroDePersonal;


    @FXML
    public void abrirRegistroAcademico() {
        utilidades.mostrarVentana("/RegistroAcademicoGUI.fxml");
    }

    @FXML
    public void abrirConsultarAcademico() {
        utilidades.mostrarVentana("/GestorAcademicosGUI.fxml");
    }

    @FXML
    public void abrirRegistroAcademicoEvaludoar() {utilidades.mostrarVentana("/RegistroAcademicoEvaluadorGUI.fxml");}

    @FXML
    public void abrirConsultarAcademicoEvaluador() {utilidades.mostrarVentana(("/GestorAcademicosEvaluadoresGUI.fxml"));}

    @FXML
    public void abrirRegistroProyecto() {utilidades.mostrarVentana(("/RegistroProyectoGUI.fxml"));}

    @FXML
    public void abrirConsultarProyecto() {utilidades.mostrarVentana(("/ConsultarProyectosGUI.fxml"));}

    @FXML
    public void abrirAsignarEstudianteAProyecto() {utilidades.mostrarVentana(("/AsignarEstudianteAProyectoGUI.fxml"));}

    @FXML
    public void abrirRegistrarOrganizacion() {utilidades.mostrarVentana("/RegistroOrganizacionVinculadaGUI.fxml");}

    @FXML
    public void abrirConsultarOrganizacionVinculada() {utilidades.mostrarVentana(("/ConsultarOrganizacionesGUI.fxml"));}

    @FXML
    public void abrirHabilitarEvaluaciones() {utilidades.mostrarVentana(("/HabilitarEvaluacionesGUI.fxml"));}

    @FXML
    public void abrirHabilitarAutoevaluaciones() {
        utilidades.mostrarVentana("/HabilitarAutoevaluacionesGUI.fxml");
    }

    @FXML
    public void abrirGestionPeriodo() {
        utilidades.mostrarVentana("/CrearGruposYPeriodoGUI.fxml");
    }
}
