package GUI;

import GUI.utilidades.Utilidades;
import GUI.utilidades.VerificacionUsuario;
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

    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoApellidos;

    @FXML
    private TextField campoNumeroPersonal;

    @FXML
    private TextField campoCorreo;

    @FXML
    private TextField campoContrasena;

    @FXML
    private void initialize() {
    }

    Utilidades utilidades = new Utilidades();

    @FXML
    private void guardarAcademico() {

        String nombre = campoNombre.getText();
        String apellidos = campoApellidos.getText();
        String numeroPersonalTexto = campoNumeroPersonal.getText().trim();
        String correo = campoCorreo.getText();
        String contrasena = campoContrasena.getText();

        int idUsuario = 0;
        int estadoActivo = 1;

        if (nombre.isEmpty() || apellidos.isEmpty() || numeroPersonalTexto.isEmpty() ||
                correo.isEmpty() || contrasena.isEmpty()) {

            logger.warn("Campos vacíos detectados en el formulario.");
            utilidades.mostrarVentana("/ErrorRegistroAcademicoGUI.fxml");
            return;
        }

        int numeroPersonal;

        try {

            numeroPersonal = Integer.parseInt(numeroPersonalTexto);

        } catch (NumberFormatException e) {

            logger.error("Error al convertir el número de personal: " + numeroPersonalTexto, e);
            utilidades.mostrarVentana("/ErrorRegistroAcademicoGUI.fxml");
            return;

        }

        try {

            CuentaDAO cuentaDAO = new CuentaDAO();
            CuentaDTO existenciaCuenta = cuentaDAO.buscarCuentaPorCorreo(correo);

            AcademicoDAO academicoDAO = new AcademicoDAO();
            AcademicoDTO existenciaAcademico = academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroPersonal);

            if (existenciaAcademico.getNumeroDePersonal() != -1 || existenciaCuenta.getCorreoElectronico() != "N/A") {

                logger.info("El académico o la cuenta ya existen en la base de datos.");
                utilidades.mostrarVentana("/ErrorRegistroAcademicoGUI.fxml");
                return;

            }

            UsuarioDTO usuarioDTO = new UsuarioDTO(idUsuario, nombre, apellidos, estadoActivo);
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            idUsuario = usuarioDAO.insertarUsuario(usuarioDTO);

            CuentaDTO cuentaDTO = new CuentaDTO(correo, contrasena, idUsuario);
            boolean cuentaCreadaConExito = cuentaDAO.crearNuevaCuenta(cuentaDTO);

            AcademicoDTO academicoDTO = new AcademicoDTO(numeroPersonal, idUsuario, nombre, apellidos, estadoActivo);
            boolean academicoCreadoConExito = academicoDAO.insertarAcademico(academicoDTO);

            if (academicoCreadoConExito && cuentaCreadaConExito) {

                logger.info("Registro de académico exitoso.");
                utilidades.mostrarVentana("/RegistroAcademicoExitosoGUI.fxml");

            } else {

                logger.error("Error al registrar el académico.");
                utilidades.mostrarVentana("/ErrorRegistroAcademicoGUI.fxml");
            }

        } catch (SQLException e) {

            logger.error("Error de SQL durante el registro del académico.", e);
            utilidades.mostrarVentana("/ErrorRegistroAcademicoGUI.fxml");

        } catch (IOException e) {

            logger.error("Error de IO durante el registro del académico.", e);
            utilidades.mostrarVentana("/ErrorRegistroAcademicoGUI.fxml");

        } catch (Exception e) {

            logger.error("Error inesperado durante el registro del académico.", e);
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
        campoContrasena.clear();
    }
}