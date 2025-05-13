package GUI.gestionestudiante;

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

import java.io.IOException;
import java.sql.SQLException;

public class ControladorRegistroEstudianteGUI {

    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoApellidos;

    @FXML
    private TextField campoMatricula;

    @FXML
    private TextField campoCorreo;

    @FXML
    private PasswordField campoContraseña;

    @FXML
    private PasswordField campoConfirmarContraseña;

    @FXML
    private TextField campoContraseñaVisible;

    @FXML
    private TextField campoConfirmarContraseñaVisible;

    @FXML
    private Button botonVerContraseña;

    @FXML
    private ImageView iconoOjo;

    private boolean contraseñaVisible = false;

    private final UtilidadesContraseña utilidadesContraseña = new UtilidadesContraseña();

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
    private void guardarEstudiante(ActionEvent event) {

        String nombre = campoNombre.getText();
        String apellidos = campoApellidos.getText();
        String matricula = campoMatricula.getText();
        String correo = campoCorreo.getText();
        String contraseña = campoContraseña.getText();
        int estadoActivo = 1;
        int idUsuario = 0;

        Utilidades utilidades = new Utilidades();
        VerificacionUsuario verificacionUsuario = new VerificacionUsuario();


        try {

            if (verificacionUsuario.camposVacios(nombre, apellidos, matricula, correo, contraseña)) {

                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Por favor, complete todos los campos.");
                return;
            }

            if (!verificacionUsuario.correoValido(correo)) {

                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Correo electronico ingresado inválido.");
                return;
            }

            if (!verificacionUsuario.matriculaValida(matricula)) {

                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Matrícula ingresada inválida.");
                return;
            }

            if (!verificacionUsuario.contrasenaValida(contraseña)) {

                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Contraseña ingresada inválida,.");
                return;
            }

            if (!verificacionUsuario.nombreValido(nombre)) {

                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Nombre ingresado inválido.");
                return;
            }

            if (!verificacionUsuario.apellidosValidos(apellidos)) {

                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Apellidos ingresados inválidos.");
                return;
            }

            if (!UtilidadesContraseña.esContraseñaIgual(campoContraseña, campoConfirmarContraseña)) {

                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Las contraseñas ingresadas no coinciden.");
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

                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "El correo ya está registrado.");
                return;

            }

            UsuarioDTO usuarioDTO = new UsuarioDTO(idUsuario, nombre, apellidos, estadoActivo);
            idUsuario = usuarioDAO.insertarUsuario(usuarioDTO);

            CuentaDTO cuentaDTO = new CuentaDTO(correo, contraseña, idUsuario);
            cuentaDAO.crearNuevaCuenta(cuentaDTO);

            EstudianteDTO estudianteDTO = new EstudianteDTO(idUsuario, nombre, apellidos, matricula, estadoActivo);
            estudianteDAO.insertarEstudiante(estudianteDTO);

            utilidades.mostrarVentana("/RegistroEstudianteExitosoGUI.fxml");


        } catch (SQLException e) {

            utilidades.mostrarVentana("/ErrorRegistroEstudiante.fxml");

        } catch (IOException i) {

            utilidades.mostrarVentana("/ErrorRegistroEstudiante.fxml");

        } catch (Exception e) {

            utilidades.mostrarVentana("/ErrorRegistroEstudiante.fxml");

        }

    }


    @FXML
    private void cancelarRegistro(ActionEvent event) {


        campoNombre.clear();
        campoApellidos.clear();
        campoMatricula.clear();
        campoCorreo.clear();
        campoContraseña.clear();
        campoConfirmarContraseña.clear();

    }

}