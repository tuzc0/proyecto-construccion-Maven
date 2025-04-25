package GUI;

import GUI.utilidades.Utilidades;
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

public class RegistroAcademicoGUI {

    private static final Logger logger = LogManager.getLogger(RegistroAcademicoGUI.class);

    @FXML private TextField campoNombre;
    @FXML private TextField campoApellidos;
    @FXML private TextField campoNumeroPersonal;
    @FXML private TextField campoCorreo;
    @FXML private TextField campoContrasena;

    Utilidades utilidades = new Utilidades();

    @FXML
    private void guardarAcademico() {

        try {

            String nombre = campoNombre.getText().trim();
            String apellidos = campoApellidos.getText().trim();
            String numeroPersonalTexto = campoNumeroPersonal.getText().trim();
            String correo = campoCorreo.getText().trim();
            String contrasena = campoContrasena.getText().trim();

            if (VerificacionUsuario.camposVacios(nombre, apellidos, numeroPersonalTexto, correo, contrasena)) {

                VerificacionUsuario.mostrarError("Campos vacíos detectados en el formulario.");
                return;
            }

            if (!VerificacionUsuario.validarCampos(nombre, apellidos, numeroPersonalTexto, correo, contrasena)) {

                VerificacionUsuario.mostrarError("Datos inválidos en el formulario.");
                return;
            }

            int numeroPersonal = Integer.parseInt(numeroPersonalTexto);
            int estadoActivo = 1;
            int idUsuario = 0;

            UsuarioDTO usuario = new UsuarioDTO(idUsuario, nombre, apellidos, estadoActivo);
            idUsuario = new UsuarioDAO().insertarUsuario(usuario);

            CuentaDTO cuenta = new CuentaDTO(correo, contrasena, idUsuario);
            AcademicoDTO academico = new AcademicoDTO(numeroPersonal, idUsuario, nombre, apellidos, estadoActivo);

            if (!crearCuentaYAcademico(cuenta, academico)) {

                VerificacionUsuario.mostrarError("Error al registrar el académico.");
                return;
            }

            logger.info("Registro de académico exitoso.");
            utilidades.mostrarVentana("/RegistroAcademicoExitosoGUI.fxml");

        } catch (SQLException | IOException | NumberFormatException e) {

            logger.error("Error durante el registro del académico.", e);
            utilidades.mostrarVentana("/ErrorRegistroAcademicoGUI.fxml");
        }
    }

    private boolean crearCuentaYAcademico(CuentaDTO cuenta, AcademicoDTO academico) throws SQLException, IOException {

        return new CuentaDAO().crearNuevaCuenta(cuenta) || new AcademicoDAO().insertarAcademico(academico);
    }

    @FXML
    private void cancelarRegistroAcademico() {

        logger.info("Cancelando el registro del académico.");
        campoNombre.clear();
        campoApellidos.clear();
        campoNumeroPersonal.clear();
        campoCorreo.clear();
        campoContrasena.clear();
    }
}
