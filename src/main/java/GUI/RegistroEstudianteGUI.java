package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import logica.DAOs.CuentaDAO;
import logica.DAOs.EstudianteDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.CuentaDTO;
import logica.DTOs.EstudianteDTO;
import logica.DTOs.UsuarioDTO;

import java.io.IOException;
import java.sql.SQLException;

public class RegistroEstudianteGUI {

    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoApellidos;

    @FXML
    private TextField campoMatricula;

    @FXML
    private TextField campoCorreo;

    @FXML
    private TextField campoContraseña;


    @FXML
    private void guardarEstudiante(ActionEvent event) {

        String nombre = campoNombre.getText();
        String apellidos = campoApellidos.getText();
        String matricula = campoMatricula.getText();
        String correo = campoCorreo.getText();
        String contraseña = campoContraseña.getText();
        int estadoActivo = 1;
        int idUsuario = 0;


        try {

            if (nombre.isEmpty() || apellidos.isEmpty() || matricula.isEmpty() || correo.isEmpty() || contraseña.isEmpty()) {
                Parent root = FXMLLoader.load(getClass().getResource("CamposVaciosGUI.fxml"));
            }

            UsuarioDTO usuarioDTO = new UsuarioDTO(idUsuario, nombre, apellidos, estadoActivo);
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            idUsuario = usuarioDAO.insertarUsuario(usuarioDTO);

            CuentaDTO cuentaDTO = new CuentaDTO(correo, contraseña, idUsuario);
            CuentaDAO cuentaDAO = new CuentaDAO();
            cuentaDAO.crearNuevaCuenta(cuentaDTO);

            EstudianteDTO estudianteDTO = new EstudianteDTO(idUsuario, nombre, apellidos, matricula, estadoActivo);
            EstudianteDAO estudianteDAO = new EstudianteDAO();
            estudianteDAO.insertarEstudiante(estudianteDTO);

            Parent root = FXMLLoader.load(getClass().getResource("RegistroEstudianteExitosoGUI.fxml"));


        } catch (SQLException e) {



        } catch (IOException i) {



        } catch (Exception e) {

        }

    }


    @FXML
    private void cancelarRegistro(ActionEvent event) {

        campoNombre.clear();
        campoApellidos.clear();
        campoMatricula.clear();
        campoCorreo.clear();
        campoContraseña.clear();
    }
}