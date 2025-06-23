package GUI.gestionorganizacion;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logica.DAOs.RepresentanteDAO;
import logica.DTOs.RepresentanteDTO;
import logica.ManejadorExcepciones;
import logica.VerificacionUsuario;
import logica.interfaces.IGestorAlertas;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Text;

import javax.imageio.IIOException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorConsultarRepresentante {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorConsultarRepresentante.class);

    @FXML
    TextField campoNombre;

    @FXML
    TextField campoApellidos;

    @FXML
    TextField campoCorreo;

    @FXML
    TextField campoTelefono;

    @FXML
    Label etiquetaContadorNombre;

    @FXML
    Label etiquetaContadorApellidos;

    @FXML
    Label etiquetaContadorCorreo;

    @FXML
    Label etiquetaContadorTelefono;

    @FXML
    Button botonEditar;

    @FXML
    Button botonGuardar;

    @FXML
    Button botonCancelar;

    int idRepresentante = 0;

    int idOrganizacionVinculada = 0;

    IGestorAlertas mensajeDeAlerta = new Utilidades();

    Utilidades utilidades = new Utilidades();

    ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(mensajeDeAlerta, logger);

    VerificicacionGeneral verificicacionGeneral = new VerificicacionGeneral();

    VerificacionUsuario verificacionUsuario = new VerificacionUsuario();

    RepresentanteDTO representante = new RepresentanteDTO();

    final int MAX_CARACTERES_NOMBRE_Y_APELLIDOS = 50;
    final int MAX_CARACTERES_CORREO = 100;
    final int MAX_CARACTERES_CONTACTO = 10;

    public void setIdRepresentante(int id) {

        this.idRepresentante = id;
        cargarDatosRepresentante();
    }


    @FXML
    private void initialize() {

        verificicacionGeneral.contadorCaracteresTextField(campoNombre, etiquetaContadorNombre, MAX_CARACTERES_NOMBRE_Y_APELLIDOS);
        verificicacionGeneral.contadorCaracteresTextField(campoApellidos, etiquetaContadorApellidos, MAX_CARACTERES_NOMBRE_Y_APELLIDOS);
        verificicacionGeneral.contadorCaracteresTextField(campoCorreo, etiquetaContadorCorreo, MAX_CARACTERES_CORREO);
        verificicacionGeneral.contadorCaracteresTextField(campoTelefono, etiquetaContadorTelefono, MAX_CARACTERES_CONTACTO);


    }

    public void cargarDatosRepresentante( ) {

        RepresentanteDAO representanteDAO = new RepresentanteDAO();

        try {

            representante = representanteDAO.buscarRepresentantePorID(idRepresentante);
            System.out.println("ID del representante: " + idRepresentante);

            if (representante != null) {

                campoNombre.setText(representante.getNombre());
                campoApellidos.setText(representante.getApellidos());
                campoCorreo.setText(representante.getCorreo());
                campoTelefono.setText(representante.getTelefono());
                idOrganizacionVinculada = representante.getIdOrganizacion();

                campoNombre.setDisable(true);
                campoApellidos.setDisable(true);
                campoCorreo.setDisable(true);
                campoTelefono.setDisable(true);

            } else {

                utilidades.mostrarAlerta(
                    "Datos no encontrados",
                    "No se encontraron datos del representante.",
                    "Por favor, verifique el ID del representante."
                );
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e){

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error al cargar los datos del representante: " + e.getMessage());
            utilidades.mostrarAlerta(
                "Error al cargar datos",
                "No se pudieron cargar los datos del representante.",
                "Por favor, intente nuevamente más tarde."
            );

        }

    }

    @FXML
    public void editarRepresentante() {

        campoNombre.setDisable(false);
        campoApellidos.setDisable(false);
        campoCorreo.setDisable(false);
        campoTelefono.setDisable(false);

        botonEditar.setDisable(true);
        botonGuardar.setVisible(true);
        botonCancelar.setVisible(true);

        etiquetaContadorApellidos.setVisible(true);
        etiquetaContadorNombre.setVisible(true);
        etiquetaContadorCorreo.setVisible(true);
        etiquetaContadorTelefono.setVisible(true);

    }

    @FXML
    public void guardarCambiosRepresentante() {

        RepresentanteDAO representanteDAO = new RepresentanteDAO();

        String nombre = campoNombre.getText();
        String apellidos = campoApellidos.getText();
        String correo = campoCorreo.getText();
        String telefono = campoTelefono.getText();

        try {

            List<String> errores = verificacionUsuario.validarRepresentante(nombre, apellidos, telefono, correo);

            if (!errores.isEmpty()) {
                utilidades.mostrarAlerta(
                        "Errores de validación",
                        "Por favor, corrija los siguientes errores:",
                        String.join("\n", errores)
                );
                return;
            }

            RepresentanteDTO representanteExistente = representanteDAO.buscarRepresentantePorCorreo(correo);

            if (!campoCorreo.getText().equals(representante.getCorreo()) && representanteExistente.getCorreo() != ("N/A")) {
                utilidades.mostrarAlerta("Error",
                        "Correo ya registrado",
                        "El correo electrónico ya está asociado a otro representante.");
                return;
            }

            representanteExistente = representanteDAO.buscarRepresentantePorTelefono(telefono);

            if (!campoTelefono.getText().equals(representante.getTelefono()) && representanteExistente.getTelefono() != ("N/A")) {
                utilidades.mostrarAlerta("Error",
                        "Número de contacto ya registrado",
                        "El número de contacto ya está asociado a otro representante.");
                return;
            }


            RepresentanteDTO representante = new RepresentanteDTO(
                idRepresentante, correo, telefono, nombre, apellidos, idOrganizacionVinculada, 1);

            representanteDAO.modificarRepresentante(representante);

            utilidades.mostrarAlerta(
                "Actualización exitosa",
                "Los datos del representante se han actualizado correctamente.",
                "Los cambios se guardaron exitosamente."
            );
            cargarDatosRepresentante();

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {
            logger.error("Error al guardar los cambios del representante: " + e.getMessage());
            utilidades.mostrarAlerta(
                "Error al guardar cambios",
                "No se pudieron guardar los cambios del representante.",
                "Por favor, intente nuevamente más tarde."
            );
        }

        campoNombre.setDisable(true);
        campoApellidos.setDisable(true);
        campoCorreo.setDisable(true);
        campoTelefono.setDisable(true);

        botonEditar.setDisable(false);
        botonGuardar.setVisible(false);
        botonCancelar.setVisible(false);

        etiquetaContadorApellidos.setVisible(false);
        etiquetaContadorNombre.setVisible(false);
        etiquetaContadorCorreo.setVisible(false);
        etiquetaContadorTelefono.setVisible(false);

    }

    @FXML
    public void cancelarEdicion() {

        campoNombre.setDisable(true);
        campoApellidos.setDisable(true);
        campoCorreo.setDisable(true);
        campoTelefono.setDisable(true);
        botonEditar.setDisable(false);
        botonGuardar.setVisible(false);
        botonCancelar.setVisible(false);
        etiquetaContadorApellidos.setVisible(false);
        etiquetaContadorNombre.setVisible(false);
        etiquetaContadorCorreo.setVisible(false);
        etiquetaContadorTelefono.setVisible(false);


        cargarDatosRepresentante();
    }


}
