package GUI.gestionestudiante;

import GUI.ControladorObjetoEvaluacion;
import GUI.utilidades.Utilidades;
import GUI.utilidades.UtilidadesContraseña;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import logica.VerificacionUsuario;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorRegistroEstudianteGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorObjetoEvaluacion.class);

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
    private Button botonVerContraseña;

    @FXML
    private ImageView imagenOjo;

    private boolean contraseñaVisible = false;

    private final UtilidadesContraseña utilidadesContraseña = new UtilidadesContraseña();

    AuxiliarGestionEstudiante auxiliarGestionEstudiante = new AuxiliarGestionEstudiante();
    int NRC = auxiliarGestionEstudiante.obtenerNRC();

    @FXML
    private void initialize() {

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
    private void guardarEstudiante(ActionEvent event) {

        String nombre = campoNombre.getText();
        String apellidos = campoApellidos.getText();
        String matricula = campoMatricula.getText();
        String correo = campoCorreo.getText();
        String contraseña = contraseñaIngresada.getText();
        int estadoActivo = 1;
        int idUsuario = 0;

        Utilidades utilidades = new Utilidades();
        VerificacionUsuario verificacionUsuario = new VerificacionUsuario();


        try {


            List<String> listaDeCamposVacios =
                    VerificacionUsuario.camposVacios(nombre, apellidos, correo, correo, contraseña);

            if (!listaDeCamposVacios.isEmpty()) {

                String camposVacios = String.join("\n", listaDeCamposVacios);
                utilidades.mostrarAlerta(
                        "Campos vacíos",
                        "Por favor, complete todos los campos requeridos.",
                        camposVacios
                );
                return;
            }

            if (!verificacionUsuario.correoValido(correo)) {

                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml",
                        "Correo electronico ingresado inválido.");
                return;
            }

            if (!verificacionUsuario.matriculaValida(matricula)) {

                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml",
                        "Matrícula ingresada inválida.");
                return;
            }

            if (!verificacionUsuario.contrasenaValida(contraseña)) {

                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml",
                        "Contraseña ingresada inválida,.");
                return;
            }

            if (!verificacionUsuario.nombreValido(nombre)) {

                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml",
                        "Nombre ingresado inválido.");
                return;
            }

            if (!verificacionUsuario.apellidosValidos(apellidos)) {

                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml",
                        "Apellidos ingresados inválidos.");
                return;
            }

            if (!UtilidadesContraseña.esContraseñaIgual(contraseñaIngresada, contraseñaConfirmada)) {

                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml",
                        "Las contraseñas ingresadas no coinciden.");
                return;
            }

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            CuentaDAO cuentaDAO = new CuentaDAO();
            EstudianteDAO estudianteDAO = new EstudianteDAO();

            EstudianteDTO estudianteExistente = estudianteDAO.buscarEstudiantePorMatricula(matricula);
            CuentaDTO cuentaEncontrada = cuentaDAO.buscarCuentaPorCorreo(correo);

            if (estudianteExistente.getMatricula() != "N/A"){

                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "La matrícula ya existe.");
                return;

            }

            if (cuentaEncontrada.getCorreoElectronico() != "N/A") {

                utilidades.mostrarAlerta("Correo electrónico ya registrado",
                        "El correo electrónico ingresado ya está asociado a una cuenta.",
                        "Por favor, utilice otro correo electrónico o inicie sesión si ya tiene una cuenta.");
                return;

            }

            UsuarioDTO usuarioDTO = new UsuarioDTO(idUsuario, nombre, apellidos, estadoActivo);
            idUsuario = usuarioDAO.insertarUsuario(usuarioDTO);

            CuentaDTO cuentaDTO = new CuentaDTO(correo, contraseña, idUsuario);
            cuentaDAO.crearNuevaCuenta(cuentaDTO);

            EstudianteDTO estudianteDTO = new EstudianteDTO(idUsuario, nombre, apellidos, matricula, estadoActivo, 0, NRC, 0);
            estudianteDAO.insertarEstudiante(estudianteDTO);

            utilidades.mostrarAlerta("Registro exitoso",
                    "Estudiante registrado correctamente",
                    "El estudiante ha sido registrado exitosamente.");


        } catch (SQLException e) {

            utilidades.mostrarAlerta("Error de base de datos",
                    "No se pudo registrar el estudiante",
                    "Por favor, intente nuevamente más tarde.");

            logger.error("Error al registrar el estudiante: " + e);



        } catch (IOException i) {

            utilidades.mostrarAlerta("Error de entrada/salida",
                    "No se pudo registrar el estudiante",
                    "Por favor, intente nuevamente más tarde.");
            logger.error("Error de entrada/salida al registrar el estudiante: " + i);

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