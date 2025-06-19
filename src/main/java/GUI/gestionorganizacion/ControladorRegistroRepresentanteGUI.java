package GUI.gestionorganizacion;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logica.verificacion.VerificicacionGeneral;

public class ControladorRegistroRepresentanteGUI {


    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoApellidos;

    @FXML
    private TextField campoCorreo;

    @FXML
    private TextField campoContacto;

    @FXML
    private Label etiquetaContadorNombreRepresentante;

    @FXML
    private Label etiquetaContadorApellidoRepresentante;

    @FXML
    private Label etiquetaContadorCorreoRepresentante;

    @FXML
    private Label etiquetaContadorContactoRepresentante;

    VerificicacionGeneral verificicacionGeneral = new VerificicacionGeneral();

    final int MAX_CARACTERES_NOMBRE = 50;

    final int MAX_CARACTERES_APELLIDOS = 50;

    final int MAX_CARACTERES_CORREO = 100;

    final int MAX_CARACTERES_CONTACTO = 10;

    @FXML
    private void initialize() {

       verificicacionGeneral.contadorCaracteresTextField(campoNombre, etiquetaContadorNombreRepresentante, MAX_CARACTERES_NOMBRE);
       verificicacionGeneral.contadorCaracteresTextField(campoApellidos, etiquetaContadorApellidoRepresentante, MAX_CARACTERES_APELLIDOS);
       verificicacionGeneral.contadorCaracteresTextField(campoCorreo, etiquetaContadorCorreoRepresentante, MAX_CARACTERES_CORREO);
       verificicacionGeneral.contadorCaracteresTextField(campoContacto, etiquetaContadorContactoRepresentante, MAX_CARACTERES_CONTACTO);

    }

    @FXML
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
