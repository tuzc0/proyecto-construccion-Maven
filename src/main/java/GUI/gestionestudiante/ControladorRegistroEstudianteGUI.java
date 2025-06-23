package GUI.gestionestudiante;

import GUI.ControladorObjetoEvaluacion;
import GUI.utilidades.Utilidades;
import GUI.utilidades.UtilidadesContraseña;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import logica.DAOs.CuentaDAO;
import logica.DAOs.EstudianteDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.CuentaDTO;
import logica.DTOs.EstudianteDTO;
import logica.DTOs.UsuarioDTO;
import javafx.scene.image.ImageView;
import logica.ManejadorExcepciones;
import logica.VerificacionUsuario;
import logica.interfaces.IGestorAlertas;
import logica.utilidadesproyecto.EncriptadorContraseñas;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorRegistroEstudianteGUI {

    Logger logger =
            org.apache.logging.log4j.LogManager.getLogger(ControladorRegistroEstudianteGUI.class);

    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoApellidos;

    @FXML
    private TextField campoMatricula;

    @FXML
    private TextField campoCorreo;

    @FXML
    private PasswordField contraseñaIngresada;

    @FXML
    private PasswordField contraseñaConfirmada;

    @FXML
    private TextField campoContraseñaVisible;

    @FXML
    private TextField campoConfirmarContraseñaVisible;


    @FXML
    private ImageView imagenOjo;

    @FXML
    private Label etiquetaContadorNombre;

    @FXML
    private Label etiquetaContadorApellido;

    @FXML
    private Label etiquetaContadorMatricula;

    @FXML
    private Label etiquetaContadorCorreo;

    @FXML
    private Label etiquetaContadorContraseña;

    @FXML
    private Label etiquetaContadorConfirmarContraseña;


    private final UtilidadesContraseña utilidadesContraseña = new UtilidadesContraseña();

    AuxiliarGestionEstudiante auxiliarGestionEstudiante = new AuxiliarGestionEstudiante();

    int NRC = auxiliarGestionEstudiante.obtenerNRC();


    VerificacionUsuario verificacionUsuario = new VerificacionUsuario();

    VerificicacionGeneral verificicacionGeneral = new VerificicacionGeneral();

    private Utilidades gestorVentanas = new Utilidades();

    private IGestorAlertas utilidades = new Utilidades();

    private ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);

    final int MAX_CARACTERES_NOMBRE_Y_APELLIDOS = 50;

    final int MAX_CARACTERES_CORREO = 100;

    final int MAX_CARACTERES_MATRICULA= 9;

    final int MAX_CARACTERES_CONTRASEÑA = 64;

    @FXML
    private void initialize() {

        verificicacionGeneral.contadorCaracteresTextField(campoNombre, etiquetaContadorNombre, MAX_CARACTERES_NOMBRE_Y_APELLIDOS);
        verificicacionGeneral.contadorCaracteresTextField(campoApellidos, etiquetaContadorApellido, MAX_CARACTERES_NOMBRE_Y_APELLIDOS);
        verificicacionGeneral.contadorCaracteresTextField(campoMatricula, etiquetaContadorMatricula, MAX_CARACTERES_MATRICULA);
        verificicacionGeneral.contadorCaracteresTextField(campoCorreo, etiquetaContadorCorreo, MAX_CARACTERES_CORREO);
        verificicacionGeneral.contadorCaracteresTextField(contraseñaIngresada, etiquetaContadorContraseña, MAX_CARACTERES_CONTRASEÑA);
        verificicacionGeneral.contadorCaracteresTextField(contraseñaConfirmada, etiquetaContadorConfirmarContraseña, MAX_CARACTERES_CONTRASEÑA);

        campoContraseñaVisible.textProperty().bindBidirectional(contraseñaIngresada.textProperty());
        campoConfirmarContraseñaVisible.textProperty().bindBidirectional(contraseñaConfirmada.textProperty());

        utilidadesContraseña.inicializarIcono(imagenOjo);

        campoContraseñaVisible.setVisible(false);
        campoContraseñaVisible.setManaged(false);

        campoConfirmarContraseñaVisible.setVisible(false);
        campoConfirmarContraseñaVisible.setManaged(false);
    }

    @FXML
    private void alternarVisibilidadContrasena() {

        utilidadesContraseña.alternarVisibilidadContrasena(
                contraseñaIngresada,
                campoContraseñaVisible,
                contraseñaConfirmada,
                campoConfirmarContraseñaVisible,
                imagenOjo
        );
    }


    @FXML
    private void guardarEstudiante(ActionEvent evento) {

        EncriptadorContraseñas encriptadorContraseñas = new EncriptadorContraseñas();

        String nombre = campoNombre.getText().trim();
        String apellidos = campoApellidos.getText().trim();
        String matricula = campoMatricula.getText().trim();
        String correo = campoCorreo.getText().trim();
        String contraseña = contraseñaIngresada.getText().trim();
        String confirmarContraseña = contraseñaConfirmada.getText().trim();

        int estadoActivo = 1;
        int idUsuario = 0;

        try {

            List<String> errores = verificacionUsuario.validarCamposRegistroEstudiante(nombre, apellidos, matricula, correo, contraseña, confirmarContraseña);

            if (!errores.isEmpty()) {
                utilidades.mostrarAlerta("Campos incompletos",
                        "Por favor, complete todos los campos requeridos.",
                        String.join("\n", errores));
                return;
            }

            if (!UtilidadesContraseña.esContraseñaIgual(contraseñaIngresada, contraseñaConfirmada)) {

                utilidades.mostrarAlerta("Contraseñas no coinciden",
                        "Las contraseñas ingresadas no coinciden.",
                        "Por favor, asegúrese de que ambas contraseñas sean idénticas.");
                return;
            }

            CuentaDAO cuentaDAO = new CuentaDAO();
            EstudianteDAO estudianteDAO = new EstudianteDAO();

            EstudianteDTO estudianteExistente = estudianteDAO.buscarEstudiantePorMatricula(matricula);
            CuentaDTO cuentaEncontrada = cuentaDAO.buscarCuentaPorCorreo(correo);

            if (estudianteExistente.getMatricula() != "N/A"){

                utilidades.mostrarAlerta("Matrícula ya registrada",
                        "La matrícula ingresada ya está asociada a una cuenta.",
                        "Por favor, utilice otra matrícula o inicie sesión si ya tiene una cuenta.");
                return;

            }

            if (cuentaEncontrada.getCorreoElectronico() != "N/A") {

                utilidades.mostrarAlerta("Correo electrónico ya registrado",
                        "El correo electrónico ingresado ya está asociado a una cuenta.",
                        "Por favor, utilice otro correo electrónico o inicie sesión si ya tiene una cuenta.");
                return;

            }

            String contrasena = encriptadorContraseñas.encriptar(contraseña);

            UsuarioDAO usuarioDAO = new UsuarioDAO();

            UsuarioDTO usuarioDTO = new UsuarioDTO(idUsuario, nombre, apellidos, estadoActivo);
            idUsuario = usuarioDAO.insertarUsuario(usuarioDTO);

            CuentaDTO cuentaDTO = new CuentaDTO(correo, contrasena, idUsuario);
            cuentaDAO.crearNuevaCuenta(cuentaDTO);

            EstudianteDTO estudianteDTO = new EstudianteDTO(idUsuario, nombre, apellidos, matricula, estadoActivo, 0, NRC, 0);
            estudianteDAO.insertarEstudiante(estudianteDTO);

            utilidades.mostrarAlerta("Registro exitoso",
                    "Estudiante registrado correctamente",
                    "El estudiante ha sido registrado exitosamente.");


        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException i) {

            manejadorExcepciones.manejarIOException(i);

        } catch (Exception e) {

            utilidades.mostrarAlerta("Error inesperado",
                    "No se pudo registrar el estudiante",
                    "Por favor, intente nuevamente más tarde.");
            logger.error("Error inesperado al registrar el estudiante: " + e);
        }
    }

    @FXML
    private void cancelarRegistro(ActionEvent event) {

        campoNombre.clear();
        campoApellidos.clear();
        campoMatricula.clear();
        campoCorreo.clear();
        contraseñaIngresada.clear();
        contraseñaConfirmada.clear();

    }
}