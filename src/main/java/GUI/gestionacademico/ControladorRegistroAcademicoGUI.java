package GUI.gestionacademico;

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
import logica.DAOs.AcademicoDAO;
import logica.DAOs.CuentaDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.AcademicoDTO;
import logica.DTOs.CuentaDTO;
import logica.DTOs.UsuarioDTO;
import logica.utilidadesproyecto.EncriptadorContraseñas;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorRegistroAcademicoGUI {

    private static final Logger LOGGER =
            LogManager.getLogger(ControladorRegistroAcademicoGUI.class);
    private final UtilidadesContraseña UTILIDADES_CONTRASEÑA =
            new UtilidadesContraseña();
    private final Utilidades UTILIDADES = new Utilidades();

    @FXML private TextField campoNombre;
    @FXML private TextField campoApellidos;
    @FXML private TextField campoNumeroPersonal;
    @FXML private TextField campoCorreo;
    @FXML private ImageView imagenOjo;
    @FXML private PasswordField contraseñaIngresada;
    @FXML private PasswordField contraseñaConfirmada;
    @FXML private TextField campoContraseñaVisible;
    @FXML private TextField campoConfirmarContraseñaVisible;
    @FXML private Label etiquetaContadorNombre;
    @FXML private Label etiquetaContadorApellidos;
    @FXML private Label etiquetaContadorNumeroPersonal;
    @FXML private Label etiquetaContadorCorreo;
    @FXML private Label etiquetaContadorContraseña;
    @FXML private Label etiquetaContadorConfirmarContraseña;
    @FXML private Button botonRegistrar;
    @FXML private Button botonCancelar;
    @FXML private Button botonOjo;

    private boolean contraseñaVisible = false;

    EncriptadorContraseñas encriptadorContraseñas = new EncriptadorContraseñas();

    @FXML
    private void initialize() {

        VerificicacionGeneral verifGen = new VerificicacionGeneral();
        final int MAX_CARACTERES_NOMBRE = 50;
        final int MAX_CARACTERES_NUMERO_PERSONAL = 5;
        final int MAX_CARACTERES_CORREO = 100;
        final int MAX_CARACTERES_CONTRASEÑA = 64;

        verifGen.contadorCaracteresTextField(
                campoNombre, etiquetaContadorNombre, MAX_CARACTERES_NOMBRE);
        verifGen.contadorCaracteresTextField(
                campoApellidos, etiquetaContadorApellidos, MAX_CARACTERES_NOMBRE);
        verifGen.contadorCaracteresTextField(
                campoNumeroPersonal, etiquetaContadorNumeroPersonal, MAX_CARACTERES_NUMERO_PERSONAL);
        verifGen.contadorCaracteresTextField(
                campoCorreo, etiquetaContadorCorreo, MAX_CARACTERES_CORREO);
        verifGen.contadorCaracteresTextField(
                contraseñaIngresada, etiquetaContadorContraseña, MAX_CARACTERES_CONTRASEÑA);
        verifGen.contadorCaracteresTextField(
                contraseñaConfirmada,
                etiquetaContadorConfirmarContraseña, MAX_CARACTERES_CONTRASEÑA);

        campoContraseñaVisible
                .textProperty()
                .bindBidirectional(contraseñaIngresada.textProperty());
        campoConfirmarContraseñaVisible
                .textProperty()
                .bindBidirectional(
                        contraseñaConfirmada.textProperty());

        UTILIDADES_CONTRASEÑA.inicializarIcono(imagenOjo);
        botonRegistrar.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
        botonOjo.setCursor(Cursor.HAND);

        campoContraseñaVisible.setVisible(false);
        campoContraseñaVisible.setManaged(false);
        campoConfirmarContraseñaVisible.setVisible(false);
        campoConfirmarContraseñaVisible.setManaged(false);
    }

    @FXML
    private void alternarVisibilidadContrasena() {

        UTILIDADES_CONTRASEÑA.alternarVisibilidadContrasena(
                contraseñaIngresada,
                campoContraseñaVisible,
                contraseñaConfirmada,
                campoConfirmarContraseñaVisible,
                imagenOjo
        );
    }

    @FXML
    private void guardarAcademico() {

        String nombre = campoNombre.getText().trim();
        String apellidos = campoApellidos.getText().trim();
        String numeroDePersonalTexto = campoNumeroPersonal.getText().trim();
        String correo = campoCorreo.getText().trim();
        String contrasena = contraseñaIngresada.getText().trim();

        List<String> camposVacios =
                VerificacionUsuario.camposVacios(
                        nombre,
                        apellidos,
                        numeroDePersonalTexto,
                        correo,
                        contrasena
                );

        if (!camposVacios.isEmpty()) {

            String msg = String.join("\n", camposVacios);
            UTILIDADES.mostrarAlerta(
                    "Campos vacíos",
                    "Por favor, complete todos los campos requeridos.",
                    msg
            );
            return;
        }

        List<String> errores = VerificacionUsuario.validarCampos(
                nombre,
                apellidos,
                numeroDePersonalTexto,
                correo,
                contrasena
        );

        if (!errores.isEmpty()) {

            String msg = String.join("\n", errores);
            UTILIDADES.mostrarAlerta(
                    "Datos inválidos",
                    "Algunos campos contienen datos no válidos.",
                    msg
            );
            return;
        }

        if (!UtilidadesContraseña.esContraseñaIgual(
                contraseñaIngresada,
                contraseñaConfirmada
        )) {

            UTILIDADES.mostrarAlerta(
                    "Contraseñas no coinciden",
                    "Las contraseñas ingresadas no son iguales.",
                    "Asegúrese de que la contraseña y su confirmación"
                            + " coincidan exactamente."
            );
            return;
        }

        int estadoActivo = 1;
        int idUsuario = 0;

        CuentaDAO cuentaDAO = new CuentaDAO();
        AcademicoDAO academicoDAO = new AcademicoDAO();

        try {

            int numeroPersonal = Integer.parseInt(numeroDePersonalTexto);

            AcademicoDTO academicoExistente = academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroPersonal);
            CuentaDTO cuentaEncontrada = cuentaDAO.buscarCuentaPorCorreo(correo);
            int academicoEncontrado = -1;

            if (academicoExistente.getNumeroDePersonal() != academicoEncontrado) {

                UTILIDADES.mostrarAlerta(
                        "Número de personal duplicado",
                        "Ya existe un académico con este número de"
                                + " personal.",
                        "Verifique que el número no esté ya registrado."
                );
                return;
            }

            if (!"N/A".equals(cuentaEncontrada.getCorreoElectronico())) {

                UTILIDADES.mostrarAlerta(
                        "Correo electrónico duplicado",
                        "Ya existe una cuenta con este correo.",
                        "Por favor, utilice un correo diferente."
                );
                return;
            }

            UsuarioDTO usuarioDTO = new UsuarioDTO(idUsuario, nombre, apellidos, estadoActivo);
            idUsuario = new UsuarioDAO().insertarUsuario(usuarioDTO);

            contrasena = encriptadorContraseñas.encriptar(contrasena);

            CuentaDTO cuentaDTO = new CuentaDTO(correo, contrasena, idUsuario);
            AcademicoDTO academicoDTO = new AcademicoDTO(
                    numeroPersonal,
                    idUsuario,
                    nombre,
                    apellidos,
                    estadoActivo
            );

            boolean creacionCuenta = cuentaDAO.crearNuevaCuenta(cuentaDTO);
            boolean registroExito = academicoDAO.insertarAcademico(academicoDTO);

            if (creacionCuenta && registroExito) {

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
                        "Por favor, verifique la información e intente"
                                + " nuevamente."
                );
            }

        } catch (NumberFormatException e) {

            LOGGER.error("Error de formato numérico al registrar académico: " + e);
            UTILIDADES.mostrarAlerta(
                    "Error de formato",
                    "El número de personal debe ser un valor numérico"
                            + " de 5 dígitos.",
                    "Revise el campo correspondiente e intente de nuevo."
            );

        } catch (SQLException e) {

            if (e.getMessage().contains("Unknown database")){

                UTILIDADES.mostrarAlerta("Error de conexion con la base de datos",
                        "Base de datos desconocida",
                        "Por favor, intente nuevamente más tarde.");
                return;

            }


            LOGGER.error("Error de base de datos al registrar académico: " + e);
            UTILIDADES.mostrarAlerta(
                    "Error del sistema",
                    "Ocurrió un error al registrar al académico.",
                    "Por favor, inténtelo nuevamente más tarde"
                            + " o contacte al soporte técnico."
            );

        } catch (IOException e) {

            LOGGER.error("Error de I/O al registrar académico: " + e);
            UTILIDADES.mostrarAlerta(
                    "Error de sistema",
                    "No se pudo completar la operación debido"
                            + " a un error interno.",
                    "Por favor, inténtelo nuevamente más tarde."
            );

        } catch (Exception e) {

            LOGGER.error("Error inesperado al registrar académico: " + e);
            UTILIDADES.mostrarAlerta(
                    "Error desconocido",
                    "Ocurrió un error inesperado al registrar"
                            + " al académico.",
                    "Por favor, inténtelo de nuevo o contacte"
                            + " al administrador del sistema."
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
