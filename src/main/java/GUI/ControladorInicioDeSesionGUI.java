package GUI;

import GUI.utilidades.Utilidades;
import GUI.utilidades.UtilidadesContraseña;
import javafx.fxml.FXML;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import logica.DAOs.*;
import logica.DTOs.*;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;


public class ControladorInicioDeSesionGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorInicioDeSesionGUI.class);

    @FXML
    TextField campoCorreo;

    @FXML
    TextField  campoContraseña;


    @FXML
    PasswordField contraseñaCifrada;

    @FXML
    ImageView iconoOjo;

    Utilidades utilidades = new Utilidades();

    int idUsuario = 0;

    static String matricula = " ";

    static int numeroDePersonal = 0;

    UtilidadesContraseña utilidadesContraseña = new UtilidadesContraseña();



    @FXML
    public void initialize() {

        UtilidadesContraseña utilidadesContraseña = new UtilidadesContraseña();
        utilidadesContraseña.inicializarIcono(iconoOjo);

        campoContraseña.setVisible(false);
    }


    @FXML
    public void alternarVisibilidadContrasena() {


        utilidadesContraseña.visibilidadUnicaContraseña(contraseñaCifrada,campoContraseña, iconoOjo);
    }


    @FXML
    public void iniciarSesion() {

        String correo = campoCorreo.getText();
        String contraseñaOculta = contraseñaCifrada.getText();
        String contraseña = contraseñaOculta;

        if (correo.isEmpty() || contraseña.isEmpty()) {
           utilidades.mostrarAlerta("Campos Vacíos", "Por favor, complete todos los campos.", "Todos los campos son obligatorios para iniciar sesión.");
        }

        CuentaDAO cuentaDAO = new CuentaDAO();
        String contraseñaEncontrada = " ";
        String correoEncontrado = " ";

        try{

            CuentaDTO cuenta = cuentaDAO.buscarCuentaPorCorreo(correo);
            correoEncontrado = cuenta.getCorreoElectronico();
            contraseñaEncontrada = cuenta.getContrasena();
            idUsuario = cuenta.getIdUsuario();

            if (correoEncontrado.equals("N/A")) {
                utilidades.mostrarAlerta("Correo no encontrado", "El correo electrónico ingresado no está registrado.", "Por favor, verifique su correo o regístrese.");
                return;
            }

            if (!contraseñaEncontrada.equals(contraseña)) {
                utilidades.mostrarAlerta("Contraseña incorrecta", "La contraseña ingresada es incorrecta.", "Por favor, intente nuevamente.");
                return;
            }

            utilidades.mostrarAlerta("Inicio de sesión exitoso", "Bienvenido", "Has iniciado sesión correctamente.");
            validarTipoUsuario(idUsuario);

        } catch (SQLException e) {

            logger.error("Error al iniciar sesión: " + e.getMessage());
            utilidades.mostrarAlerta("Error de inicio de sesión", "No se pudo iniciar sesión", "Por favor, intente nuevamente más tarde.");

        } catch (IOException e) {

            logger.error("Error de entrada/salida: " + e.getMessage());
            utilidades.mostrarAlerta("Error de entrada/salida", "No se pudo iniciar sesión", "Por favor, intente nuevamente más tarde.");

        } catch (Exception e) {

            logger.error("Error inesperado: " + e.getMessage());
            utilidades.mostrarAlerta("Error inesperado", "No se pudo iniciar sesión", "Por favor, intente nuevamente más tarde.");
        }
    }


    public void validarTipoUsuario (int idUsuario) {

        EstudianteDAO estudianteDAO = new EstudianteDAO();
        AcademicoDAO academicoDAO = new AcademicoDAO();
        AcademicoEvaluadorDAO academicoEvaluadorDAO = new AcademicoEvaluadorDAO();
        CoordinadorDAO coordinadorDAO = new CoordinadorDAO();

        try {

            EstudianteDTO estudiante = estudianteDAO.buscarEstudiantePorID(idUsuario);
            if (estudiante.getIdUsuario() != -1) {

                utilidades.mostrarVentana("/MenuEstudianteGUI.fxml");
                matricula = estudiante.getMatricula();
                return;
            }

            AcademicoDTO academico = academicoDAO.buscarAcademicoPorID(idUsuario);
            if (academico.getIdUsuario() != -1) {

                utilidades.mostrarVentana("/MenuAcademicoGUI.fxml");
                numeroDePersonal = academico.getNumeroDePersonal();
                return;
            }

            AcademicoEvaluadorDTO academicoEvaluador = academicoEvaluadorDAO.buscarAcademicoEvaluadorPorID(idUsuario);
            if (academicoEvaluador.getIdUsuario() != -1) {

                utilidades.mostrarVentana("/MenuAcademicoEvaluadorGUI.fxml");
                numeroDePersonal = academicoEvaluador.getNumeroDePersonal();
                return;
            }

            CoordinadorDTO coordinador = coordinadorDAO.buscarCoordinadorPorID(idUsuario);
            if (coordinador.getIdUsuario() != -1) {

                utilidades.mostrarVentana("/MenuCoordinadorGUI.fxml");
                numeroDePersonal = coordinador.getNumeroDePersonal();
                return;
            }

        } catch (SQLException | IOException e) {
            logger.error("Error al validar tipo de usuario: " + e.getMessage());
        }




    }

}
