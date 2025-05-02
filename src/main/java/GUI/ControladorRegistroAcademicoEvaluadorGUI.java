package GUI;

import GUI.utilidades.Utilidades;
import javafx.scene.control.PasswordField;
import logica.DAOs.AcademicoEvaluadorDAO;
import logica.DTOs.AcademicoEvaluadorDTO;
import logica.VerificacionUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import logica.DAOs.CuentaDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.CuentaDTO;
import logica.DTOs.UsuarioDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javafx.scene.image.ImageView;
import GUI.utilidades.UtilidadesContraseña;

public class ControladorRegistroAcademicoEvaluadorGUI {

    private static final Logger logger = LogManager.getLogger(ControladorRegistroAcademicoEvaluadorGUI.class);

    @FXML private TextField campoNombre;
    @FXML private TextField campoApellidos;
    @FXML private TextField campoNumeroPersonal;
    @FXML private TextField campoCorreo;
    @FXML private ImageView iconoOjo;
    @FXML private PasswordField campoContraseña;
    @FXML private PasswordField campoConfirmarContraseña;
    @FXML private TextField campoContraseñaVisible;
    @FXML private TextField campoConfirmarContraseñaVisible;

    private boolean contraseñaVisible = false;
    private final UtilidadesContraseña utilidadesContraseña = new UtilidadesContraseña();
    private final Utilidades utilidades = new Utilidades();

    @FXML
    private void initialize() {

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

                utilidades.mostrarVentanaAviso("ErrorGUI", "Campos vacíos detectados en el formulario.");
                return;
            }

            if (!UtilidadesContraseña.esContraseñaIgual(campoContraseña, campoConfirmarContraseña)) {

                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Las contraseñas no coinciden.");
                return;
            }

            List<String> errores = VerificacionUsuario.validarCampos(nombre, apellidos, numeroPersonalTexto, correo, contrasena);

            if (!errores.isEmpty()) {
                String mensajeError = String.join("\n", errores);
                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", mensajeError);
                return;
            }

            int numeroPersonal = Integer.parseInt(numeroPersonalTexto);
            int estadoActivo = 1;
            int idUsuario = 0;

            CuentaDAO cuentaDAO = new CuentaDAO();
            AcademicoEvaluadorDAO academicoevaluadorDAO = new AcademicoEvaluadorDAO();

            AcademicoEvaluadorDTO academicoExistente = academicoevaluadorDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(numeroPersonal);
            CuentaDTO cuentaEncontrada = cuentaDAO.buscarCuentaPorCorreo(correo);

            if (academicoExistente.getNumeroDePersonal() != -1){

                utilidades.mostrarVentana("/ErrorRegistroAcademico.fxml");
                return;
            }

            if (cuentaEncontrada.getCorreoElectronico() != "N/A") {

                utilidades.mostrarVentana("/ErrorRegistroAcademico.fxml");
                return;
            }

            UsuarioDTO usuario = new UsuarioDTO(idUsuario, nombre, apellidos, estadoActivo);
            idUsuario = new UsuarioDAO().insertarUsuario(usuario);

            CuentaDTO cuentaDTO = new CuentaDTO(correo, contrasena, idUsuario);
            AcademicoEvaluadorDTO academicoDTO = new AcademicoEvaluadorDTO(numeroPersonal, idUsuario, nombre, apellidos, estadoActivo);

            cuentaDAO.crearNuevaCuenta(cuentaDTO);
            academicoevaluadorDAO.insertarAcademicoEvaluador(academicoDTO);

            logger.info("Registro de académico exitoso.");
            utilidades.mostrarVentana("/RegistroAcademicoExitosoGUI.fxml");

        } catch (SQLException | IOException | NumberFormatException e) {

            logger.error("Error durante el registro del académico.", e);
            utilidades.mostrarVentana("/ErrorRegistroAcademicoGUI.fxml");
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
