package GUI.gestionorganizacion;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logica.DAOs.OrganizacionVinculadaDAO;
import logica.DTOs.OrganizacionVinculadaDTO;
import logica.VerificacionUsuario;
import logica.verificacion.VerificicacionGeneral;

import javax.imageio.IIOException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;


public class ControladorGestorOrganizacionGUI {

    Logger logger = Logger.getLogger(ControladorGestorOrganizacionGUI.class.getName());

    @FXML
    private Label campoNombreEncontrado;

    @FXML
    private Label campoCorreoEncontrado;

    @FXML
    private Label campoContactoEncontrado;

    @FXML
    private Label campoDireccionEncontrada;

    @FXML
    private TextField campoNombreEditable;

    @FXML
    private TextField campoCorreoEditable;

    @FXML
    private TextField campoContactoEditable;

    @FXML
    private TextField campoDireccionEditable;

    @FXML
    private Button botonCancelar;

    @FXML
    private Button botonGuardar;

    @FXML
    private Button botonEditar;

    @FXML
    private Label etiquetaContadorNombre;

    @FXML
    private Label etiquetaContadorCorreo;

    @FXML
    private Label etiquetaContadorContacto;

    @FXML
    private Label etiquetaContadorDireccion;

    private int idOrganizacionSeleccionada = ControladorConsultarOrganizacionGUI.idOrganizacionSeleccionada;

    VerificicacionGeneral verificicacionGeneral = new VerificicacionGeneral();
    final int MAX_CARACTERES_NOMBRE = 50;
    final int MAX_CARACTERES_CORREO = 100;
    final int MAX_CARACTERES_CONTACTO = 10;
    final int MAX_CARACTERES_DIRECCION = 255;

    @FXML
    public void initialize() {

        verificicacionGeneral.contadorCaracteresTextField(campoNombreEditable, etiquetaContadorNombre, MAX_CARACTERES_NOMBRE);
        verificicacionGeneral.contadorCaracteresTextField(campoCorreoEditable, etiquetaContadorCorreo, MAX_CARACTERES_CORREO);
        verificicacionGeneral.contadorCaracteresTextField(campoContactoEditable, etiquetaContadorContacto, MAX_CARACTERES_CONTACTO);
        verificicacionGeneral.contadorCaracteresTextField(campoDireccionEditable, etiquetaContadorDireccion, MAX_CARACTERES_DIRECCION);

        cargarDatosOrganizacion();
    }

    @FXML
    private void cargarDatosOrganizacion( ) {

        Utilidades utilidades = new Utilidades();
        OrganizacionVinculadaDAO organizacionDAO = new OrganizacionVinculadaDAO();


        try {

            OrganizacionVinculadaDTO organizacionDTO = organizacionDAO.buscarOrganizacionPorID(idOrganizacionSeleccionada);
            String nombre = organizacionDTO.getNombre();
            String correo = organizacionDTO.getCorreo();
            String contacto = organizacionDTO.getNumeroDeContacto();
            String direccion = organizacionDTO.getDireccion();
            campoNombreEncontrado.setText(nombre);
            campoCorreoEncontrado.setText(correo);
            campoContactoEncontrado.setText(contacto);
            campoDireccionEncontrada.setText(direccion);

        } catch (IOException e){

            logger.warning("Error de IO: " + e);
            utilidades.mostrarAlerta("Error", "No se pudo cargar los datos",
                    "error al cargar los datos de la organizacion seleccionada");

        } catch (SQLException e) {

            logger.warning("Error de SQL: " + e);
            utilidades.mostrarAlerta("Error", "No se pudo cargar los datos",
                    "error al cargar los datos de la organizacion seleccionada");

        } catch (Exception e) {

            logger.warning("Error: " + e);
            utilidades.mostrarAlerta("Error", "No se pudo cargar los datos",
                    "error al cargar los datos de la organizacion seleccionada");

        }
    }

    public void editarOrganizacion() {

        String nombre = campoNombreEncontrado.getText();
        String correo = campoCorreoEncontrado.getText();
        String contacto = campoContactoEncontrado.getText();
        String direccion = campoDireccionEncontrada.getText();

        campoContactoEditable.setText(contacto);
        campoNombreEditable.setText(nombre);
        campoCorreoEditable.setText(correo);
        campoDireccionEditable.setText(direccion);

        campoNombreEncontrado.setVisible(false);
        campoCorreoEncontrado.setVisible(false);
        campoContactoEncontrado.setVisible(false);
        campoDireccionEncontrada.setVisible(false);

        campoNombreEditable.setVisible(true);
        campoCorreoEditable.setVisible(true);
        campoContactoEditable.setVisible(true);
        campoDireccionEditable.setVisible(true);

        etiquetaContadorNombre.setVisible(true);
        etiquetaContadorCorreo.setVisible(true);
        etiquetaContadorContacto.setVisible(true);
        etiquetaContadorDireccion.setVisible(true);

        botonCancelar.setVisible(true);
        botonGuardar.setVisible(true);
        botonEditar.setVisible(false);

    }

    @FXML
    public void cancelarEdicion (){

        campoNombreEncontrado.setVisible(true);
        campoCorreoEncontrado.setVisible(true);
        campoContactoEncontrado.setVisible(true);
        campoDireccionEncontrada.setVisible(true);

        campoNombreEditable.setVisible(false);
        campoCorreoEditable.setVisible(false);
        campoContactoEditable.setVisible(false);
        campoDireccionEditable.setVisible(false);

        etiquetaContadorNombre.setVisible(false);
        etiquetaContadorCorreo.setVisible(false);
        etiquetaContadorContacto.setVisible(false);
        etiquetaContadorDireccion.setVisible(false);

        botonCancelar.setVisible(false);
        botonGuardar.setVisible(false);
        botonEditar.setVisible(true);
    }

    @FXML
    public void guardarEdicion () {

        Utilidades utilidades = new Utilidades();
        VerificacionUsuario verificacionUsuario = new VerificacionUsuario();
        OrganizacionVinculadaDAO organizacionDAO = new OrganizacionVinculadaDAO();
        OrganizacionVinculadaDTO organizacionDTO = new OrganizacionVinculadaDTO();

        String nombre = campoNombreEditable.getText();
        String correo = campoCorreoEditable.getText();
        String contacto = campoContactoEditable.getText();
        String direccion = campoDireccionEditable.getText();

        try {

            List<String> errores = verificacionUsuario.validarOrganizacionVinculada(nombre, correo, contacto, direccion);

            if (!errores.isEmpty()) {
                String mensajeError = String.join("\n", errores);
                utilidades.mostrarAlerta("Error de validación",
                        "Datos inválidos",
                        mensajeError);
                return;
            }

            OrganizacionVinculadaDTO organizacionExistente = organizacionDAO.buscarOrganizacionPorCorreo(correo);

            if (!organizacionExistente.getCorreo().equals("N/A")
                    && !correo.equals(campoCorreoEncontrado.getText())) {
                utilidades.mostrarAlerta("Error de registro", "Correo ya registrado",
                        "El correo electrónico ya está asociado a otra organización.");
                return;
            }

            organizacionExistente = organizacionDAO.buscarOrganizacionPorTelefono(contacto);

            if (!organizacionExistente.getNumeroDeContacto().equals("N/A")
                    && !contacto.equals(campoContactoEncontrado.getText())) {
                utilidades.mostrarAlerta("Error de registro", "Número de contacto ya registrado",
                        "El número de contacto ya está asociado a otra organización.");
                return;
            }

            organizacionDTO.setIdOrganizacion(idOrganizacionSeleccionada);
            organizacionDTO.setNombre(nombre);
            organizacionDTO.setCorreo(correo);
            organizacionDTO.setNumeroDeContacto(contacto);
            organizacionDTO.setDireccion(direccion);

            organizacionDAO.modificarOrganizacion(organizacionDTO);

            utilidades.mostrarAlerta("Éxito", "Organización actualizada",
                    "Los datos de la organización se han actualizado correctamente.");
            cargarDatosOrganizacion();
            cancelarEdicion();

        } catch (IOException e) {
            logger.warning("Error de IO: " + e);
            utilidades.mostrarAlerta("Error", "No se pudo actualizar la organización",
                    "error al actualizar los datos de la organizacion seleccionada");
        } catch (SQLException e) {
            logger.warning("Error de SQL: " + e);
            utilidades.mostrarAlerta("Error", "No se pudo actualizar la organización",
                    "error al actualizar los datos de la organizacion seleccionada");
        } catch (Exception e) {
            logger.warning("Error: " + e);
            utilidades.mostrarAlerta("Error", "No se pudo actualizar la organización",
                    "error al actualizar los datos de la organizacion seleccionada");
        }
    }

    @FXML
    public void listarRepresentantes (){

    }
}
