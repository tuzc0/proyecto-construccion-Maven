package GUI;

import GUI.utilidades.Utilidades;
import GUI.utilidades.UtilidadesContraseña;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import logica.DAOs.CuentaDAO;
import logica.DAOs.EstudianteDAO;
import logica.DTOs.CuentaDTO;
import logica.DTOs.EstudianteDTO;
import logica.ManejadorExcepciones;
import logica.VerificacionUsuario;
import logica.interfaces.IGestorAlertas;
import logica.utilidadesproyecto.EncriptadorContraseñas;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;


public class ControladorEditarPerfilEstudianteGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorEditarPerfilEstudianteGUI.class);

    @FXML
    Label etiquetaNombres;

    @FXML
    Label etiquetaApellidos;

    @FXML
    Label etiquetaMatricula;

    @FXML
    Label etiquetaCorreo;

    @FXML
    PasswordField campoContraseña;

    @FXML
    TextField campoContraseñaDescifrada;

    @FXML
    ImageView iconoOjo;

    @FXML
    Button botonEditar;

    @FXML
    Button botonGuardar;

    @FXML
    Button botonCancelar;

    @FXML
    Label etiquetaContadorContraseña;

    VerificicacionGeneral verificacionGeneral = new VerificicacionGeneral();

    UtilidadesContraseña utilidadesContraseña = new UtilidadesContraseña();

    EncriptadorContraseñas encriptadorContraseñas = new EncriptadorContraseñas();

    Utilidades gestorVentana = new Utilidades();

    IGestorAlertas utilidades = new Utilidades();

    ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);

    String matricula = ControladorInicioDeSesionGUI.matricula;

    int idUsuario = 0;

    final int MAX_CARACTERES_CONTRASEÑA = 64;

    @FXML
    public void initialize() {

        verificacionGeneral.contadorCaracteresTextField(
                campoContraseña,
                etiquetaContadorContraseña,
                MAX_CARACTERES_CONTRASEÑA
        );

        verificacionGeneral.contadorCaracteresTextField(
                campoContraseñaDescifrada,
                etiquetaContadorContraseña,
                MAX_CARACTERES_CONTRASEÑA
        );

        cargarDatosPerfil();
        utilidadesContraseña.inicializarIcono(iconoOjo);
    }

    @FXML
    public void alternarVisibilidadContrasena() {

        utilidadesContraseña.visibilidadUnicaContraseña(campoContraseña, campoContraseñaDescifrada, iconoOjo);

    }


    public void cargarDatosPerfil() {

        EstudianteDAO estudianteDAO = new EstudianteDAO();
        CuentaDAO cuentaDAO = new CuentaDAO();

        try {

            EstudianteDTO estudianteDTO = estudianteDAO.buscarEstudiantePorMatricula(matricula);
            idUsuario = estudianteDTO.getIdUsuario();

            CuentaDTO cuentaDTO = cuentaDAO.buscarCuentaPorID(idUsuario);

            etiquetaNombres.setText(estudianteDTO.getNombre());
            etiquetaApellidos.setText(estudianteDTO.getApellido());
            etiquetaMatricula.setText(estudianteDTO.getMatricula());
            etiquetaCorreo.setText(cuentaDTO.getCorreoElectronico());
            campoContraseña.setText(encriptadorContraseñas.desencriptar(cuentaDTO.getContrasena()));
            campoContraseñaDescifrada.setText(encriptadorContraseñas.desencriptar(cuentaDTO.getContrasena()));


        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al cargar los datos del perfil del estudiante: " + e);
            gestorVentana.mostrarAlerta("Error inesperado",
                    "No se pudieron cargar los datos del perfil.",
                    "Por favor, intente nuevamente más tarde.");
        }
    }

    @FXML
    public void editarPerfil() {

        botonGuardar.setVisible(true);
        botonCancelar.setVisible(true);
        botonEditar.setVisible(false);
        etiquetaContadorContraseña.setVisible(true);

        campoContraseña.setDisable(false);
        campoContraseñaDescifrada.setDisable(false);
    }

    @FXML
    public void guardarCambiosPerfil() {

        VerificacionUsuario verificacionUsuario = new VerificacionUsuario();
        CuentaDAO cuentaDAO = new CuentaDAO();
        CuentaDTO cuentaDTO = new CuentaDTO();

        cuentaDTO.setIdUsuario(idUsuario);
        String nuevaContrasena = campoContraseña.getText();
        String nuevaContrasenaDescifrada = campoContraseñaDescifrada.getText();

        try {

            if (nuevaContrasena.isEmpty() || nuevaContrasenaDescifrada.isEmpty()) {

                gestorVentana.mostrarAlerta("Campos vacíos",
                        "Por favor, complete todos los campos.",
                        "No se puede guardar el perfil sin completar la contraseña.");
                return;
            }

            if (!verificacionUsuario.contrasenaValida(nuevaContrasena) ||
                    !verificacionUsuario.contrasenaValida(nuevaContrasenaDescifrada)) {

                gestorVentana.mostrarAlerta("Contraseña inválida",
                        "La contraseña debe tener al menos 8 caracteres, una letra mayúscula, " +
                                "una minúscula, un número y un carácter especial.",
                        "Por favor, ingrese una contraseña válida.");
                return;
            }

            String contraseñaEncriptada = encriptadorContraseñas.encriptar(nuevaContrasena);
            cuentaDTO.setContrasena(contraseñaEncriptada);
            cuentaDTO.setCorreoElectronico(etiquetaCorreo.getText());
            cuentaDAO.modificarCuenta(cuentaDTO);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al actualizar el perfil del estudiante: " + e);
            gestorVentana.mostrarAlerta(
                    "Error",
                    "Ocurrió un error al guardar los cambios.",
                    "Por favor, inténtelo de nuevo más tarde o contacte al administrador."
            );
        }

        cancelarEdicion();
    }

    @FXML
    public void cancelarEdicion() {

        botonGuardar.setVisible(false);
        botonCancelar.setVisible(false);
        botonEditar.setVisible(true);
        etiquetaContadorContraseña.setVisible(false);

        campoContraseña.setDisable(true);
        campoContraseñaDescifrada.setDisable(true);

        cargarDatosPerfil();
    }
}
