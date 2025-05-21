package GUI.gestionacademico;

import GUI.utilidades.Utilidades;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import logica.VerificacionUsuario;
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
import java.util.List;
import javafx.scene.image.ImageView;
import GUI.utilidades.UtilidadesContraseña;

public class ControladorRegistroAcademicoGUI {

    private static final Logger LOGGER = LogManager.getLogger(ControladorRegistroAcademicoGUI.class);
    private final UtilidadesContraseña UTILIDADESCONTRASEÑA = new UtilidadesContraseña();
    private final Utilidades UTILIDADES = new Utilidades();

    @FXML private TextField campoNombre;
    @FXML private TextField campoApellidos;
    @FXML private TextField campoNumeroPersonal;
    @FXML private TextField campoCorreo;
    @FXML private ImageView iconoOjo;
    @FXML private PasswordField campoContraseña;
    @FXML private PasswordField campoConfirmarContraseña;
    @FXML private TextField campoContraseñaVisible;
    @FXML private TextField campoConfirmarContraseñaVisible;
    @FXML private Button botonRegistrar;
    @FXML private Button botonCancelar;
    @FXML private Button botonOjo;

    private boolean contraseñaVisible = false;

    @FXML
    private void initialize() {

        botonRegistrar.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
        botonOjo.setCursor(Cursor.HAND);

        campoContraseñaVisible.textProperty().bindBidirectional(campoContraseña.textProperty());
        campoConfirmarContraseñaVisible.textProperty().bindBidirectional(campoConfirmarContraseña.textProperty());

        UTILIDADESCONTRASEÑA.inicializarIcono(iconoOjo);

        campoContraseñaVisible.setVisible(false);
        campoContraseñaVisible.setManaged(false);

        campoConfirmarContraseñaVisible.setVisible(false);
        campoConfirmarContraseñaVisible.setManaged(false);
    }

    @FXML
    private void alternarVisibilidadContrasena() {

        UTILIDADESCONTRASEÑA.alternarVisibilidadContrasena(
                campoContraseña,
                campoContraseñaVisible,
                campoConfirmarContraseña,
                campoConfirmarContraseñaVisible,
                iconoOjo
        );
    }

    @FXML
    private void guardarAcademico() {

        String nombre = campoNombre.getText().trim();
        String apellidos = campoApellidos.getText().trim();
        String numeroPersonalTexto = campoNumeroPersonal.getText().trim();
        String correo = campoCorreo.getText().trim();
        String contrasena = campoContraseña.getText().trim();

        try {

            List<String> listaDeCamposVacios = VerificacionUsuario.camposVacios(nombre, apellidos, numeroPersonalTexto, correo, contrasena);

            if (!listaDeCamposVacios.isEmpty()) {

                String camposVacios = String.join("\n", listaDeCamposVacios);
                UTILIDADES.mostrarAlerta(
                        "Campos vacíos",
                        "Por favor, complete todos los campos requeridos.",
                        camposVacios
                );
                return;
            }

            List<String> errores = VerificacionUsuario.validarCampos(nombre, apellidos, numeroPersonalTexto, correo, contrasena);

            if (!errores.isEmpty()) {

                String mensajeError = String.join("\n", errores);
                UTILIDADES.mostrarAlerta(
                        "Datos inválidos",
                        "Algunos campos contienen datos no válidos.",
                        mensajeError
                );
                return;
            }

            if (!UtilidadesContraseña.esContraseñaIgual(campoContraseña, campoConfirmarContraseña)) {

                UTILIDADES.mostrarAlerta(
                        "Contraseñas no coinciden",
                        "Las contraseñas ingresadas no son iguales.",
                        "Asegúrese de que la contraseña y su confirmación coincidan exactamente."
                );
                return;
            }

            int numeroPersonal = Integer.parseInt(numeroPersonalTexto);
            int estadoActivo = 1;
            int idUsuario = 0;

            CuentaDAO cuentaDAO = new CuentaDAO();
            AcademicoDAO academicoDAO = new AcademicoDAO();

            AcademicoDTO academicoExistente = academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroPersonal);
            CuentaDTO cuentaEncontrada = cuentaDAO.buscarCuentaPorCorreo(correo);

            if (academicoExistente.getNumeroDePersonal() != -1){

                UTILIDADES.mostrarAlerta(
                        "Número de personal duplicado",
                        "Ya existe un académico con este número de personal.",
                        "Verifique que el número de personal no esté ya registrado en el sistema."
                );
                return;
            }

            if (!"N/A".equals(cuentaEncontrada.getCorreoElectronico())) {

                UTILIDADES.mostrarAlerta(
                        "Correo electrónico duplicado",
                        "Ya existe una cuenta registrada con este correo electrónico.",
                        "Por favor, utilice un correo electrónico diferente."
                );
                return;
            }

            UsuarioDTO usuarioDTO = new UsuarioDTO(idUsuario, nombre, apellidos, estadoActivo);
            idUsuario = new UsuarioDAO().insertarUsuario(usuarioDTO);

            CuentaDTO cuentaDTO = new CuentaDTO(correo, contrasena, idUsuario);
            AcademicoDTO academicoDTO = new AcademicoDTO(numeroPersonal, idUsuario, nombre, apellidos, estadoActivo);

            boolean cuentaCreadaConExito = cuentaDAO.crearNuevaCuenta(cuentaDTO);
            boolean registroRealizadoConExito = academicoDAO.insertarAcademico(academicoDTO);

            if (cuentaCreadaConExito && registroRealizadoConExito) {

                LOGGER.info("Registro de académico exitoso.");
                UTILIDADES.mostrarAlerta(
                        "Registro exitoso",
                        "El académico ha sido registrado correctamente.",
                        ""
                );

            } else {

                LOGGER.warn("No se pudieron guardar todos los datos del académico.");
                UTILIDADES.mostrarAlerta(
                        "Registro incompleto",
                        "No se pudieron guardar todos los datos.",
                        "Por favor, verifique la información e intente nuevamente."
                );
            }

        } catch (NumberFormatException e) {

            LOGGER.error("Error de formato numérico al registrar académico: " + e.getMessage());
            UTILIDADES.mostrarAlerta(
                    "Error de formato",
                    "El número de personal debe ser un valor numérico de 5 dígitos.",
                    "Revise el campo correspondiente e intente de nuevo."
            );

        } catch (SQLException e) {

            LOGGER.error("Error de base de datos al registrar académico: " + e.getMessage());
            UTILIDADES.mostrarAlerta(
                    "Error del sistema",
                    "Ocurrió un error al registrar al académico.",
                    "Por favor, inténtelo nuevamente más tarde o contacte al soporte técnico."
            );

        } catch (IOException e) {

            LOGGER.error("Error de entrada/salida al registrar académico: " + e.getMessage());
            UTILIDADES.mostrarAlerta(
                    "Error de sistema",
                    "No se pudo completar la operación debido a un error interno.",
                    "Por favor, inténtelo nuevamente más tarde."
            );

        } catch (Exception e) {

            LOGGER.error("Error inesperado al registrar académico: " + e.getMessage());
            UTILIDADES.mostrarAlerta(
                    "Error desconocido",
                    "Ocurrió un error inesperado al registrar al académico.",
                    "Por favor, inténtelo de nuevo o contacte al administrador del sistema."
            );
        }
    }

    @FXML
    private void cancelarRegistroAcademico() {

        LOGGER.info("Cancelando el registro del académico.");
        campoNombre.clear();
        campoApellidos.clear();
        campoNumeroPersonal.clear();
        campoCorreo.clear();
        campoContraseña.clear();
    }
}