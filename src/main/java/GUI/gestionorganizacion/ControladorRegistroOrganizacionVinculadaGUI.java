package GUI.gestionorganizacion;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logica.DAOs.OrganizacionVinculadaDAO;
import logica.DTOs.OrganizacionVinculadaDTO;
import logica.VerificacionUsuario;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ControladorRegistroOrganizacionVinculadaGUI {

    Logger logger = Logger.getLogger(ControladorRegistroOrganizacionVinculadaGUI.class.getName());

    @FXML
    private TextField campoNombreOrganizacion;

    @FXML
    private TextField campoCorreoOrganizacion;

    @FXML
    private TextField campoContactoOrganizacion;

    @FXML
    private TextField campoDireccionOrganizacion;

    @FXML
    private TextField campoNombreRepresentante;

    @FXML
    private TextField campoApellidosRepresentante;

    @FXML
    private TextField campoContactoRepresentante;

    @FXML
    private TextField campoCorreoRepresentante;

    @FXML
    private Button botonRegistrarRepresentante;

    public static int idOrganizacion = 0;



    private void registrarOrganizacion() {

        Utilidades utilidades = new Utilidades();
        VerificacionUsuario verificacionUsuario = new VerificacionUsuario();

        String nombreOrganizacion = campoNombreOrganizacion.getText();
        String correoOrganizacion = campoCorreoOrganizacion.getText();
        String contactoOrganizacion = campoContactoOrganizacion.getText();
        String direccionOrganizacion = campoDireccionOrganizacion.getText();

        int estadoActivo = 1;

        if (nombreOrganizacion.isEmpty() || correoOrganizacion.isEmpty() || contactoOrganizacion.isEmpty() || direccionOrganizacion.isEmpty()) {
            utilidades.mostrarAlerta("Error de registro", "Campos vacíos", "Por favor, complete todos los campos.");
            return;
        }

        if (!verificacionUsuario.nombreValido(nombreOrganizacion)) {
            utilidades.mostrarAlerta("Error de registro", "Nombre inválido", "El nombre de la organización no es válido.");
            return;
        }

        if (!verificacionUsuario.nombreValido(nombreOrganizacion)) {
            utilidades.mostrarAlerta("Error de registro", "Nombre inválido", "El nombre de la organización no es válido.");
            return;
        }

        if (!verificacionUsuario.correoValido(correoOrganizacion)) {
            utilidades.mostrarAlerta("Error de registro", "Correo inválido", "El correo de la organización no es válido.");
            return;
        }

        OrganizacionVinculadaDAO organizacionDAO= new OrganizacionVinculadaDAO();
        OrganizacionVinculadaDTO organizacionDTO = new OrganizacionVinculadaDTO(idOrganizacion, nombreOrganizacion, correoOrganizacion, contactoOrganizacion, direccionOrganizacion, estadoActivo);


        try {

            OrganizacionVinculadaDTO organizacionExistente = organizacionDAO.buscarOrganizacionPorCorreo(correoOrganizacion);

            if (organizacionExistente.getCorreo() != ("N/A")) {
                utilidades.mostrarAlerta("Error de registro", "Correo ya registrado", "El correo electrónico ya está asociado a otra organización.");
                return;
            }

            organizacionExistente = organizacionDAO.buscarOrganizacionPorTelefono(contactoOrganizacion);

            if (organizacionExistente.getNumeroDeContacto() != ("N/A")) {
                utilidades.mostrarAlerta("Error de registro", "Número de contacto ya registrado", "El número de contacto ya está asociado a otra organización.");
                return;
            }

            idOrganizacion = organizacionDAO.crearNuevaOrganizacion(organizacionDTO);
            utilidades.mostrarAlerta("Registro exitoso", "Organización registrada", "La organización ha sido registrada exitosamente.");
            botonRegistrarRepresentante.setDisable(false);

        } catch (SQLException e) {

            utilidades.mostrarAlerta("Error de registro", "Error al registrar la organización", "No se pudo registrar la organización. Por favor, inténtelo de nuevo más tarde.");
            logger.severe("Error al registrar la organización: " + e.getMessage());

        } catch (IOException e){

            utilidades.mostrarAlerta("Error de registro", "Error al registrar la organización", "No se pudo registrar la organización. Por favor, inténtelo de nuevo más tarde.");
            logger.severe("Error al registrar la organización: " + e.getMessage());

        } catch (Exception e) {

            utilidades.mostrarAlerta("Error de registro", "Error al registrar la organización", "No se pudo registrar la organización. Por favor, inténtelo de nuevo más tarde.");
            logger.severe("Error al registrar la organización: " + e.getMessage());

        }
    }

    private void registrarRepresentante() {

        AuxiliarRegistroRepresentante auxiliarRegistroRepresentante = new AuxiliarRegistroRepresentante();
        String nombreRepresentante = campoNombreRepresentante.getText();
        String apellidosRepresentante = campoApellidosRepresentante.getText();
        String correoRepresentante = campoCorreoRepresentante.getText();
        String contactoRepresentante = campoContactoRepresentante.getText();

        auxiliarRegistroRepresentante.registrarRepresentante(nombreRepresentante, apellidosRepresentante, correoRepresentante, contactoRepresentante, idOrganizacion);


    }

    @FXML
    private void guardarOrganizacionYRepresentante() {

        registrarOrganizacion();
        registrarRepresentante();


    }

    @FXML
    private void cancelarRegistro() {

        campoNombreOrganizacion.clear();
        campoCorreoOrganizacion.clear();
        campoContactoOrganizacion.clear();
        campoDireccionOrganizacion.clear();
        campoNombreRepresentante.clear();
        campoApellidosRepresentante.clear();
        campoContactoRepresentante.clear();
        campoCorreoRepresentante.clear();
    }

    @FXML
    private void registrarNuevoRepresentante() {

        Utilidades utilidades = new Utilidades();
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistroRepresentanteGUI.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e) {

            logger.severe("Error al abrir la ventana de registro representante: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Error al abrir la ventana de registro", "No se pudo abrir la ventana de registro. Por favor, inténtelo de nuevo más tarde.");

        }
    }
}
