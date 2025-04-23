package GUI;

import GUI.utilidades.Utilidades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

        Utilidades utilidades = new Utilidades();


        try {

            if (nombre.isBlank() || apellidos.isBlank() || matricula.isBlank() || correo.isBlank() || contraseña.isBlank()) {
                utilidades.mostrarVentana("/ErrorRegistroEstudiante.fxml");
                return;
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

    }
}