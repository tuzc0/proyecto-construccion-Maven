package GUI;

import GUI.utilidades.Utilidades;
import GUI.utilidades.UtilidadesContraseña;
import javafx.fxml.FXML;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import logica.DAOs.*;
import logica.DTOs.*;
import logica.utilidadesproyecto.EncriptadorContraseñas;
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

    public static String matricula = " ";

    public static int numeroDePersonal = 0;

    UtilidadesContraseña utilidadesContraseña = new UtilidadesContraseña();

    EncriptadorContraseñas encriptadorContraseñas = new EncriptadorContraseñas();

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
           utilidades.mostrarAlerta("Campos Vacíos",
                   "Por favor, complete todos los campos.",
                   "Todos los campos son obligatorios para iniciar sesión.");
        }

        CuentaDAO cuentaDAO = new CuentaDAO();
        String contraseñaEncontrada = " ";
        String correoEncontrado = " ";

        try{

            CuentaDTO cuenta = cuentaDAO.buscarCuentaPorCorreo(correo);
            correoEncontrado = cuenta.getCorreoElectronico();
            contraseñaEncontrada = cuenta.getContrasena();
            contraseñaEncontrada = encriptadorContraseñas.desencriptar(contraseñaEncontrada);

            idUsuario = cuenta.getIdUsuario();

            if (correoEncontrado.equals("N/A")) {
                utilidades.mostrarAlerta("Correo no encontrado",
                        "El correo electrónico ingresado no está registrado.",
                        "Por favor, verifique su correo o regístrese.");
                return;
            }

            if (!contraseñaEncontrada.equals(contraseña)) {
                utilidades.mostrarAlerta("Contraseña incorrecta",
                        "La contraseña ingresada es incorrecta.",
                        "Por favor, intente nuevamente.");
                return;
            }

            utilidades.mostrarAlerta("Inicio de sesión exitoso",
                    "Bienvenido",
                    "Has iniciado sesión correctamente.");
            validarTipoUsuario(idUsuario);

        } catch (SQLException e) {

            logger.error("Error al iniciar sesión: " + e);

            if (e.getMessage().contains("The driver has not received any packets from the server.")){

                utilidades.mostrarAlerta("Error de conexion con la base de datos",
                        "No se pudo iniciar sesión, porque no hay conexion con la base de datos",
                        "Por favor, intente nuevamente más tarde.");
                return;

            }

            utilidades.mostrarAlerta("Error con la base de datos",
                    "No se pudo iniciar sesión",
                    "Por favor, verifique su correo y contraseña e intente nuevamente.");


        } catch (IOException e) {

            logger.error("Error de entrada/salida: " + e);
            utilidades.mostrarAlerta("Error de entrada/salida",
                    "No se pudo iniciar sesión",
                    "Por favor, intente nuevamente más tarde.");

        } catch (Exception e) {

            logger.error("Error inesperado: " + e);
            utilidades.mostrarAlerta("Error inesperado",
                    "No se pudo iniciar sesión",
                    "Por favor, intente nuevamente más tarde.");
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

                matricula = estudiante.getMatricula();
                utilidades.mostrarVentana("/MenuEstudianteGUI.fxml");
                return;
            }

            AcademicoDTO academico = academicoDAO.buscarAcademicoPorID(idUsuario);

            if (academico.getIdUsuario() != -1) {

                numeroDePersonal = academico.getNumeroDePersonal();
                utilidades.mostrarVentana("/MenuAcademicoGUI.fxml");
                return;
            }

            AcademicoEvaluadorDTO academicoEvaluador = academicoEvaluadorDAO.buscarAcademicoEvaluadorPorID(idUsuario);

            if (academicoEvaluador.getIdUsuario() != -1) {

                numeroDePersonal = academicoEvaluador.getNumeroDePersonal();
                utilidades.mostrarVentana("/MenuAcademicoEvaluadorGUI.fxml");
                return;
            }

            CoordinadorDTO coordinador = coordinadorDAO.buscarCoordinadorPorID(idUsuario);

            if (coordinador.getIdUsuario() != -1) {

                numeroDePersonal = coordinador.getNumeroDePersonal();
                utilidades.mostrarVentana("/MenuCoordinadorGUI.fxml");
                return;
            }

        } catch (SQLException | IOException e) {

            logger.error("Error al validar tipo de usuario: " + e);
        }
    }

}
