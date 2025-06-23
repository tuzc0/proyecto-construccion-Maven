package GUI;

import GUI.utilidades.Utilidades;
import GUI.utilidades.UtilidadesContraseña;
import javafx.fxml.FXML;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import logica.DAOs.*;
import logica.DTOs.*;
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
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

    private Utilidades gestorVetanas = new Utilidades();
    private IGestorAlertas utilidades = new Utilidades();
    private ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);
    private UtilidadesContraseña utilidadesContraseña = new UtilidadesContraseña();
    private EncriptadorContraseñas encriptadorContraseñas = new EncriptadorContraseñas();

    int idUsuario = 0;
    public static String matricula = " ";
    public static int numeroDePersonal = 0;

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
           gestorVetanas.mostrarAlerta("Campos Vacíos",
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
                gestorVetanas.mostrarAlerta("Correo no encontrado",
                        "El correo electrónico ingresado no está registrado.",
                        "Por favor, verifique su correo o regístrese.");
                return;
            }

            if (!contraseñaEncontrada.equals(contraseña)) {
                gestorVetanas.mostrarAlerta("Contraseña incorrecta",
                        "La contraseña ingresada es incorrecta.",
                        "Por favor, intente nuevamente.");
                return;
            }

            gestorVetanas.mostrarAlerta("Inicio de sesión exitoso",
                    "Bienvenido",
                    "Has iniciado sesión correctamente.");
            validarTipoUsuario(idUsuario);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado: " + e);
            gestorVetanas.mostrarAlerta("Error inesperado",
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
                gestorVetanas.mostrarVentana("/MenuEstudianteGUI.fxml");
                return;
            }

            AcademicoDTO academico = academicoDAO.buscarAcademicoPorID(idUsuario);

            if (academico.getIdUsuario() != -1) {

                numeroDePersonal = academico.getNumeroDePersonal();
                gestorVetanas.mostrarVentana("/MenuAcademicoGUI.fxml");
                return;
            }

            AcademicoEvaluadorDTO academicoEvaluador = academicoEvaluadorDAO.buscarAcademicoEvaluadorPorID(idUsuario);

            if (academicoEvaluador.getIdUsuario() != -1) {

                numeroDePersonal = academicoEvaluador.getNumeroDePersonal();
                gestorVetanas.mostrarVentana("/MenuAcademicoEvaluadorGUI.fxml");
                return;
            }

            CoordinadorDTO coordinador = coordinadorDAO.buscarCoordinadorPorID(idUsuario);

            if (coordinador.getIdUsuario() != -1) {

                numeroDePersonal = coordinador.getNumeroDePersonal();
                gestorVetanas.mostrarVentana("/MenuCoordinadorGUI.fxml");
                return;
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al validar el tipo de usuario: " + e);
            gestorVetanas.mostrarAlerta("Error inesperado",
                    "No se pudo validar el tipo de usuario",
                    "Por favor, intente nuevamente más tarde.");
        }
    }

}
