package GUI.gestionorganizacion;

import GUI.ControladorConsultarEstudiantesAEvaluarGUI;
import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logica.DAOs.OrganizacionVinculadaDAO;
import logica.DTOs.OrganizacionVinculadaDTO;
import logica.ManejadorExcepciones;
import logica.VerificacionUsuario;
import logica.interfaces.IGestorAlertas;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControladorRegistroOrganizacionVinculadaGUI {

    Logger logger =
            org.apache.logging.log4j.LogManager.getLogger(ControladorRegistroOrganizacionVinculadaGUI.class);

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

    @FXML
    private Label etiquetaContadorNombre;

    @FXML
    private Label etiquetaContadorCorreo;

    @FXML
    private Label etiquetaContadorContacto;

    @FXML
    private Label etiquetaContadorDireccion;

    @FXML
    private Label etiquetaContadorNombreRepresentante;

    @FXML
    private Label etiquetaContadorApellidoRepresentante;

    @FXML
    private Label etiquetaContadorCorreoRepresentante;

    @FXML
    private Label etiquetaContadorContactoRepresentante;

    public static int idOrganizacion = 0;

    private Utilidades gestorVentanas = new Utilidades();

    private IGestorAlertas utilidades = new Utilidades();

    private ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);

    VerificacionUsuario verificacionUsuario = new VerificacionUsuario();

    VerificicacionGeneral verificicacionGeneral = new VerificicacionGeneral();
    final int MAX_CARACTERES_NOMBRE = 50;
    final int MAX_CARACTERES_CORREO = 100;
    final int MAX_CARACTERES_CONTACTO = 10;
    final int MAX_CARACTERES_DIRECCION = 255;
    final int MAX_CARACTERES_APELLIDOS = 50;

    @FXML
    private void initialize() {


        verificicacionGeneral.contadorCaracteresTextField(
                campoNombreOrganizacion, etiquetaContadorNombre, MAX_CARACTERES_NOMBRE);

        verificicacionGeneral.contadorCaracteresTextField(
                campoCorreoOrganizacion, etiquetaContadorCorreo, MAX_CARACTERES_CORREO);

        verificicacionGeneral.contadorCaracteresTextField(
                campoContactoOrganizacion, etiquetaContadorContacto, MAX_CARACTERES_CONTACTO);

        verificicacionGeneral.contadorCaracteresTextField(
                campoDireccionOrganizacion, etiquetaContadorDireccion, MAX_CARACTERES_DIRECCION);

        verificicacionGeneral.contadorCaracteresTextField(
                campoNombreRepresentante, etiquetaContadorNombreRepresentante, MAX_CARACTERES_NOMBRE);

        verificicacionGeneral.contadorCaracteresTextField(
                campoApellidosRepresentante, etiquetaContadorApellidoRepresentante, MAX_CARACTERES_APELLIDOS);

        verificicacionGeneral.contadorCaracteresTextField(
                campoCorreoRepresentante, etiquetaContadorCorreoRepresentante, MAX_CARACTERES_CORREO);

        verificicacionGeneral.contadorCaracteresTextField(
                campoContactoRepresentante, etiquetaContadorContactoRepresentante, MAX_CARACTERES_CONTACTO);


    }

    private void registrarOrganizacion() {


        String nombreOrganizacion = campoNombreOrganizacion.getText();
        String correoOrganizacion = campoCorreoOrganizacion.getText();
        String contactoOrganizacion = campoContactoOrganizacion.getText();
        String direccionOrganizacion = campoDireccionOrganizacion.getText();

        String nombreRepresentante = campoNombreRepresentante.getText();
        String apellidosRepresentante = campoApellidosRepresentante.getText();
        String correoRepresentante = campoCorreoRepresentante.getText();
        String contactoRepresentante = campoContactoRepresentante.getText();

        int estadoActivo = 1;

        List<String> erroresOrganizacion = verificacionUsuario.validarOrganizacionVinculada(
                nombreOrganizacion, correoOrganizacion, contactoOrganizacion, direccionOrganizacion);

        List<String> erroresRepresentante = verificacionUsuario.validarRepresentante(
                nombreRepresentante, apellidosRepresentante, contactoRepresentante, correoRepresentante);

        List<String> errores = new ArrayList<>();
        errores.addAll(erroresOrganizacion);
        errores.addAll(erroresRepresentante);

        if (!errores.isEmpty()) {
            String mensajeError = String.join("\n", errores);
            utilidades.mostrarAlerta("Error de registro", "Datos inválidos",
                    "Por favor, corrige los siguientes errores:\n" + mensajeError);
            return;
        }

        OrganizacionVinculadaDAO organizacionDAO = new OrganizacionVinculadaDAO();
        OrganizacionVinculadaDTO organizacionDTO =
                new OrganizacionVinculadaDTO(idOrganizacion, nombreOrganizacion, direccionOrganizacion, correoOrganizacion,
                        contactoOrganizacion, estadoActivo);

        try {
            OrganizacionVinculadaDTO organizacionExistente = organizacionDAO.buscarOrganizacionPorCorreo(correoOrganizacion);

            if (!organizacionExistente.getCorreo().equals("N/A")) {
                utilidades.mostrarAlerta("Error de registro", "Correo ya registrado",
                        "El correo electrónico ya está asociado a otra organización.");
                return;
            }

            organizacionExistente = organizacionDAO.buscarOrganizacionPorTelefono(contactoOrganizacion);

            if (!organizacionExistente.getNumeroDeContacto().equals("N/A")) {
                utilidades.mostrarAlerta("Error de registro", "Número de contacto ya registrado",
                        "El número de contacto ya está asociado a otra organización.");
                return;
            }

            idOrganizacion = organizacionDAO.crearNuevaOrganizacion(organizacionDTO);

            registrarRepresentante();

            utilidades.mostrarAlerta("Registro exitoso", "Organización registrada",
                    "La organización y el representante han sido registrados exitosamente.");
            botonRegistrarRepresentante.setDisable(false);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            utilidades.mostrarAlerta("Error de registro", "Error al registrar la organización",
                    "No se pudo registrar la organización. Por favor, inténtelo de nuevo más tarde.");
            logger.error("Error al registrar la organización: " + e);
        }
    }

    private void registrarRepresentante() {

        AuxiliarRegistroRepresentante auxiliarRegistroRepresentante = new AuxiliarRegistroRepresentante();
        String nombreRepresentante = campoNombreRepresentante.getText();
        String apellidosRepresentante = campoApellidosRepresentante.getText();
        String correoRepresentante = campoCorreoRepresentante.getText();
        String contactoRepresentante = campoContactoRepresentante.getText();

        auxiliarRegistroRepresentante.registrarRepresentante(nombreRepresentante, apellidosRepresentante,
                correoRepresentante, contactoRepresentante, idOrganizacion);

    }

    @FXML
    private void guardarOrganizacionYRepresentante() {

        registrarOrganizacion();
    }

    @FXML
    private void cancelarRegistro() {

        gestorVentanas.mostrarAlertaConfirmacion(

                "Confirmar eliminación",
                "¿Está seguro que desea cancelar el registro?",
                "Se cancelara el registro. Esta acción no se puede deshacer.",
                () -> {
                    cancelar();
                },
                () -> {
                    utilidades.mostrarAlerta("Cancelado",
                            "Registro cancelado",
                            "No se ha cancelado el registro.");
                }
        );
    }

    private void cancelar() {

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

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistroRepresentanteGUI.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e) {

            logger.error("Error al abrir la ventana de registro de representante: " + e);
            utilidades.mostrarAlerta("Error", "Error al abrir la ventana de registro",
                    "No se pudo abrir la ventana de registro. Por favor, inténtelo de nuevo más tarde.");

        } catch (Exception e) {

            logger.error("Error inesperado al abrir la ventana de registro de representante: " + e);
            utilidades.mostrarAlerta("Error inesperado", "Error al abrir la ventana de registro",
                    "Ocurrió un error inesperado. Por favor, inténtelo de nuevo más tarde.");
        }
    }
}
