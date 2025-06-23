package GUI.gestionorganizacion;

import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logica.DAOs.OrganizacionVinculadaDAO;
import logica.DAOs.RepresentanteDAO;
import logica.DTOs.OrganizacionVinculadaDTO;
import logica.DTOs.RepresentanteDTO;
import logica.ManejadorExcepciones;
import logica.VerificacionUsuario;
import logica.interfaces.IGestorAlertas;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class ControladorGestorOrganizacionGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorGestorOrganizacionGUI.class);

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

    @FXML
    private TableView<RepresentanteDTO> tablaRepresentantes;

    @FXML
    private TableColumn<RepresentanteDTO, String> columnaNombre;

    @FXML
    private TableColumn<RepresentanteDTO, String> columnaCorreo;

    @FXML
    private TableColumn<RepresentanteDTO, String> columnaApellidos;

    public int idOrganizacionSeleccionada = ControladorConsultarOrganizacionGUI.idOrganizacionSeleccionada;

    int idRepresentanteSeleccionado = 0;

    IGestorAlertas mensajeDeAlerta = new Utilidades();

    Utilidades utilidades = new Utilidades();

    ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(mensajeDeAlerta, logger);

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
        listarRepresentantes();

    }

    public void listarRepresentantes() {

        RepresentanteDAO representanteDAO = new RepresentanteDAO();

        try {

            List<RepresentanteDTO> representantes = representanteDAO.obtenerRepresentantesActivosPorIdOrganizacion(idOrganizacionSeleccionada);

            if (representantes.isEmpty()) {
                utilidades.mostrarAlerta("Información", "Sin representantes", "No hay representantes activos para esta organización.");
                return;
            }

            ObservableList<RepresentanteDTO> listaObservable = FXCollections.observableArrayList(representantes);
            tablaRepresentantes.setItems(listaObservable);

            columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            columnaCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
            columnaApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            utilidades.mostrarAlerta("Error", "No se pudo listar los representantes", "Error al cargar los representantes.");
            logger.error("Error al listar los representantes: " + e);
        }
    }

    @FXML
    private void cargarDatosOrganizacion( ) {

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

            manejadorExcepciones.manejarIOException(e);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (Exception e) {

            utilidades.mostrarAlerta("Error", "No se pudo cargar los datos",
                    "error al cargar los datos de la organizacion seleccionada");

            logger.error("Error al cargar los datos de la organización: " + e);

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

            manejadorExcepciones.manejarIOException(e);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (Exception e) {

            utilidades.mostrarAlerta("Error", "No se pudo actualizar la organización",
                    "error al actualizar los datos de la organizacion seleccionada");
            logger.error("Error al actualizar los datos de la organización: " + e);

        }
    }

    @FXML
    public void confirmarEliminacionRepresentante() {

        utilidades.mostrarAlertaConfirmacion(
                "Confirmar eliminación",
                "¿Está seguro que desea eliminar al representante seleccionado?",
                "Se eliminará al representante seleccionado. Esta acción no se puede deshacer.",
                () -> {
                    eliminarRepresentante();
                },
                () -> {
                    utilidades.mostrarAlerta("Cancelado",
                            "Eliminación cancelada",
                            "No se ha eliminado ningún representante.");
                }
        );

    }

    public void eliminarRepresentante() {

        tablaRepresentantes.getSelectionModel().getSelectedItem();
        RepresentanteDTO representanteSeleccionado = tablaRepresentantes.getSelectionModel().getSelectedItem();

        if (representanteSeleccionado == null) {
            utilidades.mostrarAlerta("Error", "No se ha seleccionado un representante",
                    "Por favor, seleccione un representante de la lista.");
            return;
        }

        RepresentanteDAO representanteDAO = new RepresentanteDAO();

        try {

            idRepresentanteSeleccionado = representanteSeleccionado.getIDRepresentante();

            if (representanteDAO.estaVinculadoAProyectoActivo(idRepresentanteSeleccionado)) {

                utilidades.mostrarAlerta("Error", "No se puede eliminar el representante",
                        "El representante está vinculado a un proyecto activo y no se puede eliminar.");
                return;

            }

            boolean eliminado = representanteDAO.eliminarRepresentantePorID(idRepresentanteSeleccionado);

            if (eliminado) {
                utilidades.mostrarAlerta("Éxito", "Representante eliminado",
                        "El representante ha sido eliminado correctamente.");
                listarRepresentantes();
            } else {
                utilidades.mostrarAlerta("Error", "No se pudo eliminar el representante",
                        "Ocurrió un error al intentar eliminar el representante seleccionado.");
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            utilidades.mostrarAlerta("Error", "Error inesperado",
                    "Ocurrió un error inesperado al eliminar el representante.");
            logger.error("Error al eliminar el representante: " + e);
        }

    }

    @FXML
    public void registrarRepresentante() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistroRepresentanteGUI.fxml"));
            Parent root = loader.load();

            ControladorRegistroRepresentanteGUI controladorRegistroRepresentanteGUI = loader.getController();
            controladorRegistroRepresentanteGUI.setIdOrganizacion(idOrganizacionSeleccionada);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            listarRepresentantes();

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {
            utilidades.mostrarAlerta("Error", "Error inesperado",
                    "Ocurrió un error inesperado al registrar el representante.");
            logger.error("Error al registrar el representante: " + e);
        }
    }

    @FXML
    public void verDetallesRepresentante() {

        try {

            RepresentanteDTO representanteSeleccionado = tablaRepresentantes.getSelectionModel().getSelectedItem();

            if (representanteSeleccionado == null) {
                utilidades.mostrarAlerta("Error", "No se ha seleccionado un representante",
                        "Por favor, seleccione un representante de la lista.");
                return;
            }
            idRepresentanteSeleccionado = representanteSeleccionado.getIDRepresentante();


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConsultarRepresentanteGUI.fxml"));
            Parent root = loader.load();

            ControladorConsultarRepresentante controlador = loader.getController();
            controlador.setIdRepresentante(idRepresentanteSeleccionado);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            listarRepresentantes();

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        }  catch (Exception e) {

            utilidades.mostrarAlerta("Error", "Error inesperado",
                    "Ocurrió un error inesperado al cargar los detalles del representante.");
            logger.error("Error al cargar los detalles del representante: " + e);

        }

    }

}
