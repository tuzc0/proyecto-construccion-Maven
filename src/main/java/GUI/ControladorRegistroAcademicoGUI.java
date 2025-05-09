package GUI;

import GUI.utilidades.Utilidades;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import logica.VerificacionUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import logica.DAOs.AcademicoDAO;
import logica.DAOs.CuentaDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.AcademicoDTO;
import logica.DTOs.CuentaDTO;
import logica.DTOs.UsuarioDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.image.ImageView;
import GUI.utilidades.UtilidadesContraseña;

public class ControladorRegistroAcademicoGUI {

    private static final Logger logger = LogManager.getLogger(ControladorRegistroAcademicoGUI.class);

    @FXML private TextField campoNombre;
    @FXML private TextField campoApellidos;
    @FXML private TextField campoNumeroPersonal;
    @FXML private TextField campoCorreo;
    @FXML private ImageView iconoOjo;
    @FXML private PasswordField campoContraseña;
    @FXML private PasswordField campoConfirmarContraseña;
    @FXML private TextField campoContraseñaVisible;
    @FXML private TextField campoConfirmarContraseñaVisible;
    @FXML private Button botonRegistrar;
    @FXML private Button botonCancelar;
    @FXML private Button botonOjo;

    private boolean contraseñaVisible = false;
    private final UtilidadesContraseña utilidadesContraseña = new UtilidadesContraseña();
    private final Utilidades utilidades = new Utilidades();

    @FXML
    private void initialize() {

        botonRegistrar.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
        botonOjo.setCursor(Cursor.HAND);

        campoContraseñaVisible.textProperty().bindBidirectional(campoContraseña.textProperty());
        campoConfirmarContraseñaVisible.textProperty().bindBidirectional(campoConfirmarContraseña.textProperty());

        utilidadesContraseña.inicializarIcono(iconoOjo);

        campoContraseñaVisible.setVisible(false);
        campoContraseñaVisible.setManaged(false);

        campoConfirmarContraseñaVisible.setVisible(false);
        campoConfirmarContraseñaVisible.setManaged(false);
    }

    @FXML
    private void alternarVisibilidadContrasena() {

        utilidadesContraseña.alternarVisibilidadContrasena(
                campoContraseña,
                campoContraseñaVisible,
                campoConfirmarContraseña,
                campoConfirmarContraseñaVisible,
                iconoOjo
        );
    }

    @FXML
    private void guardarAcademico() {

        try {

            String nombre = campoNombre.getText().trim();
            String apellidos = campoApellidos.getText().trim();
            String numeroPersonalTexto = campoNumeroPersonal.getText().trim();
            String correo = campoCorreo.getText().trim();
            String contrasena = campoContraseña.getText().trim();

            if (VerificacionUsuario.camposVacios(nombre, apellidos, numeroPersonalTexto, correo, contrasena)) {

                utilidades.mostrarAlerta("Campos vacíos.", "Por favor, llene todos los campos.", "Faltan algunos campos por llenar.");
                return;
            }

            List<String> errores = VerificacionUsuario.validarCampos(nombre, apellidos, numeroPersonalTexto, correo, contrasena);

            if (!errores.isEmpty()) {

                String mensajeError = String.join("\n", errores);
                utilidades.mostrarAlerta("Datos inválidos.", "Por favor, introduzca datos válidos.", mensajeError);
                return;
            }

            if (!UtilidadesContraseña.esContraseñaIgual(campoContraseña, campoConfirmarContraseña)) {

                utilidades.mostrarAlerta("Contraseñas no coinciden.", "Las contraseñas no coinciden.", "Por favor, asegúrese de que la contraseña y su confirmación sean iguales.");
                return;
            }

            int numeroPersonal = Integer.parseInt(numeroPersonalTexto);
            int estadoActivo = 1;
            int idUsuario = 0;

            CuentaDAO cuentaDAO = new CuentaDAO();
            AcademicoDAO academicoDAO = new AcademicoDAO();

            AcademicoDTO academicoExistente = academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroPersonal);
            CuentaDTO cuentaEncontrada = cuentaDAO.buscarCuentaPorCorreo(correo);

            if (academicoExistente.getNumeroDePersonal() != -1){

                utilidades.mostrarAlerta("Academico existente.", "El academico ya existe.", "Por favor, revise que el academico no se encuentre registrado.");
                return;
            }

            if (!"N/A".equals(cuentaEncontrada.getCorreoElectronico())) {

                utilidades.mostrarAlerta("Correo existente.", "El correo ya está registrado.", "Por favor, verifique que el correo no se encuentre ya registrado.");
                return;
            }

            UsuarioDTO usuario = new UsuarioDTO(idUsuario, nombre, apellidos, estadoActivo);
            idUsuario = new UsuarioDAO().insertarUsuario(usuario);

            CuentaDTO cuentaDTO = new CuentaDTO(correo, contrasena, idUsuario);
            AcademicoDTO academicoDTO = new AcademicoDTO(numeroPersonal, idUsuario, nombre, apellidos, estadoActivo);

            boolean cuentaCreadaConExito = cuentaDAO.crearNuevaCuenta(cuentaDTO);
            boolean registroRealizadoConExito = academicoDAO.insertarAcademico(academicoDTO);

            if (cuentaCreadaConExito && registroRealizadoConExito) {

                logger.info("Registro de académico exitoso.");
                utilidades.mostrarAlerta("Éxito.", "Registro exitoso.", "El académico fue registrado con éxito.");

            } else {

                logger.warn("No se pudo modificar el académico.");
                utilidades.mostrarAlerta("Error", "Algo salio mal.", "Ocurrio un error, por favor intentelo más tarde.");
            }

        } catch (SQLException e) {

            logger.error("Error durante el registro del académico.", e);
            utilidades.mostrarAlerta("Error.", "Ocurrió un error. Por favor, inténtelo de nuevo más tarde.", "");

        } catch (NumberFormatException e) {

            logger.error("Error durante el registro del académico.", e);
            utilidades.mostrarAlerta("Error.", "Ocurrió un error de formato.", "Verifique que el número de personal tenga exactamente 5 dígitos.");

        } catch (IOException e) {

            logger.error("Error durante el registro del académico.", e);
            utilidades.mostrarAlerta("Error.", "Ocurrió un error. Por favor, inténtelo de nuevo más tarde.", "");

        } catch (Exception e) {

            logger.error("Error durante el registro del académico.", e);
            utilidades.mostrarAlerta("Error.", "Ocurrió un error. Por favor, inténtelo de nuevo más tarde.", "");
        }
    }

    @FXML
    private void cancelarRegistroAcademico() {

        logger.info("Cancelando el registro del académico.");
        campoNombre.clear();
        campoApellidos.clear();
        campoNumeroPersonal.clear();
        campoCorreo.clear();
        campoContraseña.clear();
    }
}