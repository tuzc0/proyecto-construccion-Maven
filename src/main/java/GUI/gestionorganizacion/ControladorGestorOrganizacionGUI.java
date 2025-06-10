package GUI.gestionorganizacion;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logica.DAOs.OrganizacionVinculadaDAO;
import logica.DTOs.OrganizacionVinculadaDTO;

import javax.imageio.IIOException;
import java.sql.SQLException;
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

    private int idOrganizacionSeleccionada = ControladorConsultarOrganizacionGUI.idOrganizacionSeleccionada;


    @FXML
    public void initialize() {

        cargarDatosOrganizacion();
    }

    @FXML
    private void cargarDatosOrganizacion( ) {

        Utilidades utilidades = new Utilidades();
        OrganizacionVinculadaDAO organizacionDAO = new OrganizacionVinculadaDAO();
        OrganizacionVinculadaDTO organizacionDTO = new OrganizacionVinculadaDTO();

        try {

            organizacionDTO = organizacionDAO.buscarOrganizacionPorID(idOrganizacionSeleccionada);
            campoNombreEncontrado.setText(organizacionDTO.getNombre());
            campoCorreoEncontrado.setText(organizacionDTO.getCorreo());
            campoContactoEncontrado.setText(organizacionDTO.getNumeroDeContacto());
            campoDireccionEncontrada.setText(organizacionDTO.getDireccion());

        } catch (IIOException e){

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

        botonCancelar.setVisible(false);
        botonGuardar.setVisible(false);
        botonEditar.setVisible(true);
    }

    @FXML
    public void listarRepresentantes (){

    }
}
