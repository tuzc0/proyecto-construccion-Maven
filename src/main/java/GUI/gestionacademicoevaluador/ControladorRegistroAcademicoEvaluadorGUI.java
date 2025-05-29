package GUI.gestionacademicoevaluador;

import GUI.utilidades.Utilidades;
import GUI.utilidades.UtilidadesContraseña;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logica.VerificacionUsuario;
import logica.DAOs.CuentaDAO;
import logica.DAOs.UsuarioDAO;
import logica.DAOs.AcademicoEvaluadorDAO;
import logica.DTOs.CuentaDTO;
import logica.DTOs.UsuarioDTO;
import logica.DTOs.AcademicoEvaluadorDTO;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorRegistroAcademicoEvaluadorGUI {

    private static final Logger LOGGER =
            LogManager.getLogger(
                    ControladorRegistroAcademicoEvaluadorGUI.class
            );
    private final UtilidadesContraseña utilContraseña =
            new UtilidadesContraseña();
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
    @FXML private Label contadorNombre;
    @FXML private Label contadorApellidos;
    @FXML private Label contadorNumeroPersonal;
    @FXML private Label contadorCorreo;
    @FXML private Label contadorContraseña;
    @FXML private Label contadorConfirmarContraseña;
    @FXML private Button botonRegistrar;
    @FXML private Button botonCancelar;
    @FXML private Button botonOjo;

    @FXML
    private void initialize() {

        VerificicacionGeneral verifGen = new VerificicacionGeneral();

        verifGen.contadorCaracteresTextField(
                campoNombre, contadorNombre, 50);
        verifGen.contadorCaracteresTextField(
                campoApellidos, contadorApellidos, 50);
        verifGen.contadorCaracteresTextField(
                campoNumeroPersonal, contadorNumeroPersonal, 5);
        verifGen.contadorCaracteresTextField(
                campoCorreo, contadorCorreo, 100);
        verifGen.contadorCaracteresTextField(
                campoContraseña, contadorContraseña, 64);
        verifGen.contadorCaracteresTextField(
                campoConfirmarContraseña,
                contadorConfirmarContraseña, 64);

        botonRegistrar.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
        botonOjo.setCursor(Cursor.HAND);

        campoContraseñaVisible
                .textProperty()
                .bindBidirectional(
                        campoContraseña.textProperty()
                );
        campoConfirmarContraseñaVisible
                .textProperty()
                .bindBidirectional(
                        campoConfirmarContraseña.textProperty()
                );

        utilContraseña.inicializarIcono(iconoOjo);

        campoContraseñaVisible.setVisible(false);
        campoContraseñaVisible.setManaged(false);
        campoConfirmarContraseñaVisible.setVisible(false);
        campoConfirmarContraseñaVisible.setManaged(false);
    }

    @FXML
    private void alternarVisibilidadContrasena() {

        utilContraseña.alternarVisibilidadContrasena(
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
        String numPersTxt = campoNumeroPersonal.getText().trim();
        String correo = campoCorreo.getText().trim();
        String contrasena = campoContraseña.getText().trim();

        List<String> vacios =
                VerificacionUsuario.camposVacios(
                        nombre, apellidos, numPersTxt, correo, contrasena
                );

        if (!vacios.isEmpty()) {

            String msg = String.join("\n", vacios);
            UTILIDADES.mostrarAlerta(
                    "Campos vacíos",
                    "Complete todos los campos requeridos.",
                    msg
            );
            return;
        }

        List<String> errores =
                VerificacionUsuario.validarCampos(
                        nombre, apellidos, numPersTxt, correo, contrasena
                );
        if (!errores.isEmpty()) {

            String msg = String.join("\n", errores);
            UTILIDADES.mostrarAlerta(
                    "Datos inválidos",
                    "Algunos campos no son válidos.",
                    msg
            );
            return;
        }

        if (!UtilidadesContraseña.esContraseñaIgual(
                campoContraseña,
                campoConfirmarContraseña
        )) {

            UTILIDADES.mostrarAlerta(
                    "Contraseñas no coinciden",
                    "Las contraseñas ingresadas no son iguales.",
                    "Revise la contraseña y su confirmación."
            );
            return;
        }

        try {

            int numeroPersonal = Integer.parseInt(numPersTxt);
            int estadoActivo = 1;
            int idUsuario = 0;

            CuentaDAO cuentaDAO = new CuentaDAO();
            AcademicoEvaluadorDAO evalDAO = new AcademicoEvaluadorDAO();

            AcademicoEvaluadorDTO existEval =
                    evalDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(
                            numeroPersonal
                    );

            if (existEval.getNumeroDePersonal() != -1) {

                UTILIDADES.mostrarAlerta(
                        "Número duplicado",
                        "Ya existe un académico con ese número.",
                        "Verifique que no esté registrado."
                );
                return;
            }

            if (!"N/A".equals(
                    new CuentaDAO().buscarCuentaPorCorreo(correo)
                            .getCorreoElectronico()
            )) {

                UTILIDADES.mostrarAlerta(
                        "Correo duplicado",
                        "Ya existe una cuenta con este correo.",
                        "Use un correo diferente."
                );
                return;
            }

            UsuarioDTO usuarioDTO = new UsuarioDTO(
                    idUsuario, nombre, apellidos, estadoActivo
            );
            idUsuario = new UsuarioDAO().insertarUsuario(usuarioDTO);

            CuentaDTO cuentaDTO = new CuentaDTO(
                    correo, contrasena, idUsuario
            );
            AcademicoEvaluadorDTO dtoEval = new AcademicoEvaluadorDTO(
                    numeroPersonal, idUsuario, nombre, apellidos, estadoActivo
            );

            boolean okCuenta = cuentaDAO.crearNuevaCuenta(cuentaDTO);
            boolean okInsert = evalDAO.insertarAcademicoEvaluador(dtoEval);

            if (okCuenta && okInsert) {

                LOGGER.info("Registro exitoso.");
                UTILIDADES.mostrarAlerta(
                        "Registro exitoso",
                        "El académico fue registrado correctamente.",
                        ""
                );
            } else {

                LOGGER.warn(
                        "Fallo al guardar datos del académico evaluador."
                );
                UTILIDADES.mostrarAlerta(
                        "Registro incompleto",
                        "No se guardaron todos los datos.",
                        "Revise la información e intente de nuevo."
                );
            }

        } catch (NumberFormatException e) {

            LOGGER.error(
                    "Formato numérico inválido: " + e.getMessage()
            );
            UTILIDADES.mostrarAlerta(
                    "Error de formato",
                    "El número debe ser numérico de 5 dígitos.",
                    "Revise el campo e intente de nuevo."
            );

        } catch (SQLException e) {
            LOGGER.error("Error BD: " + e.getMessage());
            UTILIDADES.mostrarAlerta(
                    "Error del sistema",
                    "Ocurrió un error al registrar.",
                    "Intente más tarde o contacte soporte."
            );

        } catch (IOException e) {

            LOGGER.error("Error I/O: " + e.getMessage());
            UTILIDADES.mostrarAlerta(
                    "Error interno",
                    "No se pudo completar la operación.",
                    "Intente de nuevo más tarde."
            );

        } catch (Exception e) {

            LOGGER.error("Error inesperado: " + e.getMessage());
            UTILIDADES.mostrarAlerta(
                    "Error desconocido",
                    "Ocurrió un error inesperado.",
                    "Contacte al administrador."
            );
        }
    }

    @FXML
    private void cancelarRegistroAcademico() {

        String nombreOriginal = campoNombre.getText();
        String apellidosOriginal = campoApellidos.getText();
        String numeroPersonalOriginal = campoNumeroPersonal.getText();
        String correoOriginal = campoCorreo.getText();

        UTILIDADES.mostrarAlertaConfirmacion(
                "Confirmar cancelación",
                "¿Está seguro que desea cancelar?",
                "Los cambios no guardados se perderán",
                () -> {

                    Stage ventanaActual = (Stage) botonCancelar.getScene().getWindow();
                    ventanaActual.close();
                },
                () -> {

                    campoNombre.setText(nombreOriginal);
                    campoApellidos.setText(apellidosOriginal);
                    campoNumeroPersonal.setText(numeroPersonalOriginal);
                    campoCorreo.setText(correoOriginal);

                    UTILIDADES.mostrarAlerta(
                            "Operación cancelada",
                            "Los cambios no han sido descartados",
                            "Puede continuar editando el registro"
                    );
                }
        );
    }
}
