package GUI.gestionorganizacion;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ControladorRegistroRepresentanteGUI {


    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoApellidos;

    @FXML
    private TextField campoCorreo;

    @FXML
    private TextField campoContacto;


    public void registrarRepresentante() {

        AuxiliarRegistroRepresentante auxiliarRegistroRepresentante = new AuxiliarRegistroRepresentante();
        String nombre = campoNombre.getText();
        String apellidos = campoApellidos.getText();
        String correo = campoCorreo.getText();
        String numeroContacto = campoContacto.getText();
        int idOrganizacion = 0;

        idOrganizacion = ControladorRegistroOrganizacionVinculadaGUI.idOrganizacion;

        auxiliarRegistroRepresentante.registrarRepresentante(nombre, apellidos, correo, numeroContacto, idOrganizacion);
    }

    @FXML
    public void cancelarRegistro() {

        campoNombre.clear();
        campoApellidos.clear();
        campoCorreo.clear();
        campoContacto.clear();
    }

    @FXML
    public void guardarRegistro() {

        registrarRepresentante();
    }


}
