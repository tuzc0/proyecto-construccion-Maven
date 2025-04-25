package GUI;

import GUI.utilidades.Utilidades;
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
import javafx.scene.image.ImageView;
import GUI.utilidades.UtilidadesContraseña;

public class RegistroAcademicoGUI {

    private static final Logger logger = LogManager.getLogger(RegistroAcademicoGUI.class);

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

                VerificacionUsuario.mostrarError("Campos vacíos detectados en el formulario.");
                return;
            }

            if (!UtilidadesContraseña.esContraseñaIgual(campoContraseña, campoConfirmarContraseña)) {

                utilidades.mostrarVentana("/ErrorRegistroEstudiante.fxml");
                return;
            }

            if (!VerificacionUsuario.validarCampos(nombre, apellidos, numeroPersonalTexto, correo, contrasena)) {

                VerificacionUsuario.mostrarError("Datos inválidos en el formulario.");
                return;
            }

            int numeroPersonal = Integer.parseInt(numeroPersonalTexto);
            int estadoActivo = 1;
            int idUsuario = 0;

            CuentaDTO cuentaExistente = new CuentaDAO().buscarCuentaPorCorreo(correo);
            AcademicoDTO academicoExistente = new AcademicoDAO().buscarAcademicoPorNumeroDePersonal(numeroPersonal);

            if (!verificarExistenciaCuentaYAcademico(cuentaExistente, academicoExistente)) {

                VerificacionUsuario.mostrarError("Error al registrar el académico.");
                return;
            }

            UsuarioDTO usuario = new UsuarioDTO(idUsuario, nombre, apellidos, estadoActivo);
            idUsuario = new UsuarioDAO().insertarUsuario(usuario);

            CuentaDTO cuentaDTO = new CuentaDTO(correo, contrasena, idUsuario);
            AcademicoDTO academicoDTO = new AcademicoDTO(numeroPersonal, idUsuario, nombre, apellidos, estadoActivo);

            CuentaDAO cuentaDAO = new CuentaDAO();
            cuentaDAO.crearNuevaCuenta(cuentaDTO);

            AcademicoDAO academicoDAO = new AcademicoDAO();
            academicoDAO.insertarAcademico(academicoDTO);

            logger.info("Registro de académico exitoso.");
            utilidades.mostrarVentana("/RegistroAcademicoExitosoGUI.fxml");

        } catch (SQLException | IOException | NumberFormatException e) {

            logger.error("Error durante el registro del académico.", e);
            utilidades.mostrarVentana("/ErrorRegistroAcademicoGUI.fxml");
        }
    }

    private boolean verificarExistenciaCuentaYAcademico(CuentaDTO cuenta, AcademicoDTO academico) {

        boolean cuentaExistente = false;

        if ("N/A".equals(cuenta.getCorreoElectronico()) || academico.getNumeroDePersonal() == -1) {

            cuentaExistente = true;
        }

        return cuentaExistente;
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