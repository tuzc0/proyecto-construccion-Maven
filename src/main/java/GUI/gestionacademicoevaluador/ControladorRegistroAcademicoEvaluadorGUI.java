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
import logica.utilidadesproyecto.EncriptadorContraseñas;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorRegistroAcademicoEvaluadorGUI {

    private static final Logger LOGGER = LogManager.
            getLogger(ControladorRegistroAcademicoEvaluadorGUI.class);

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

    private UtilidadesContraseña utilContraseña = new UtilidadesContraseña();
    private Utilidades utilidades = new Utilidades();
    private EncriptadorContraseñas encriptadorContraseñas = new EncriptadorContraseñas();

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

        botonRegistrar.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
        botonOjo.setCursor(Cursor.HAND);

        campoContraseñaVisible.textProperty().bindBidirectional(contraseñaIngresada.textProperty());
        campoConfirmarContraseñaVisible.textProperty().bindBidirectional(contraseñaConfirmada.textProperty());

        utilContraseña.inicializarIcono(imagenOjo);

        campoContraseñaVisible.setVisible(false);
        campoContraseñaVisible.setManaged(false);
        campoConfirmarContraseñaVisible.setVisible(false);
        campoConfirmarContraseñaVisible.setManaged(false);
    }

    @FXML
    private void alternarVisibilidadContrasena() {

        utilContraseña.alternarVisibilidadContrasena(
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

        List<String> vacios =
                VerificacionUsuario.camposVacios(
                        nombre,
                        apellidos,
                        numeroDePersonalTexto,
                        correo,
                        contrasena
                );

        if (!vacios.isEmpty()) {

            String mensajeCampoVacio = String.join("\n", vacios);
            utilidades.mostrarAlerta(
                    "Campos vacíos",
                    "Complete todos los campos requeridos.",
                    mensajeCampoVacio
            );
            return;
        }

        List<String> errores =
                VerificacionUsuario.validarCampos(
                        nombre,
                        apellidos,
                        numeroDePersonalTexto,
                        correo,
                        contrasena
                );

        if (!errores.isEmpty()) {

            String mensajeDeError = String.join("\n", errores);
            utilidades.mostrarAlerta(
                    "Datos inválidos",
                    "Algunos campos no son válidos.",
                    mensajeDeError
            );
            return;
        }

        if (!UtilidadesContraseña.esContraseñaIgual(contraseñaIngresada, contraseñaConfirmada)) {

            utilidades.mostrarAlerta(
                    "Contraseñas no coinciden",
                    "Las contraseñas ingresadas no son iguales.",
                    "Revise la contraseña y su confirmación."
            );
            return;
        }

        try {

            int numeroPersonal = Integer.parseInt(numeroDePersonalTexto);
            int estadoActivo = 1;
            int idUsuario = 0;

            CuentaDAO cuentaDAO = new CuentaDAO();
            AcademicoEvaluadorDAO academicoEvaluadorDAO = new AcademicoEvaluadorDAO();

            AcademicoEvaluadorDTO academicoEvaluadorExistente =
                    academicoEvaluadorDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(numeroPersonal);
            int numeroDePersonalEncontrado = -1;
            String correoNoEncontrado = "N/A";

            if (academicoEvaluadorExistente.getNumeroDePersonal() != numeroDePersonalEncontrado) {

                utilidades.mostrarAlerta(
                        "Número duplicado",
                        "Ya existe un académico con ese número.",
                        "Verifique que no esté registrado."
                );
                return;
            }

            if (!correoNoEncontrado.equals(new CuentaDAO().buscarCuentaPorCorreo(correo)
                            .getCorreoElectronico())) {

                utilidades.mostrarAlerta(
                        "Correo duplicado",
                        "Ya existe una cuenta con este correo.",
                        "Use un correo diferente."
                );
                return;
            }

            UsuarioDTO usuarioDTO = new UsuarioDTO(
                    idUsuario,
                    nombre,
                    apellidos,
                    estadoActivo
            );

            idUsuario = new UsuarioDAO().insertarUsuario(usuarioDTO);

            contrasena = encriptadorContraseñas.encriptar(contrasena);

            CuentaDTO cuentaDTO = new CuentaDTO(
                    correo,
                    contrasena,
                    idUsuario
            );
            AcademicoEvaluadorDTO academicoEvaluadorDTO = new AcademicoEvaluadorDTO(
                    numeroPersonal,
                    idUsuario,
                    nombre,
                    apellidos,
                    estadoActivo
            );

            boolean cuentaCreada = cuentaDAO.crearNuevaCuenta(cuentaDTO);
            boolean academicoInsertado =
                    academicoEvaluadorDAO.insertarAcademicoEvaluador(academicoEvaluadorDTO);

            if (cuentaCreada && academicoInsertado) {

                LOGGER.info("Registro exitoso.");
                utilidades.mostrarAlerta(
                        "Registro exitoso",
                        "El académico fue registrado correctamente.",
                        ""
                );

            } else {

                LOGGER.warn("Fallo al guardar datos del académico evaluador.");
                utilidades.mostrarAlerta(
                        "Registro incompleto",
                        "No se guardaron todos los datos.",
                        "Revise la información e intente de nuevo."
                );
            }

        } catch (NumberFormatException e) {

            LOGGER.error(
                    "Formato numérico inválido: " + e);
            utilidades.mostrarAlerta(
                    "Error de formato",
                    "El número debe ser numérico de 5 dígitos.",
                    "Revise el campo e intente de nuevo."
            );

        } catch (SQLException e) {

            String estadoSQL = e.getSQLState();

            switch (estadoSQL) {

                case "08S01":

                    LOGGER.error("El servicio de SQL se encuentra desactivado: " + e);
                    utilidades.mostrarAlerta(
                            "Error de conexión",
                            "No se pudo establecer una conexión con la base de datos.",
                            "La base de datos se encuentra desactivada."
                    );
                    break;

                case "42000":

                    LOGGER.error("La base de datos no existe: " + e);
                    utilidades.mostrarAlerta(
                            "Error de conexión",
                            "No se pudo establecer conexión con la base de datos.",
                            "La base de datos actualmente no existe."
                    );
                    break;

                case "28000":

                    LOGGER.error("Credenciales invalidas para el acceso: " + e);
                    utilidades.mostrarAlerta(
                            "Credenciales inválidas",
                            "Usuario o contraseña incorrectos.",
                            "Por favor, verifique los datos de acceso a la base" +
                                    "de datos"
                    );
                    break;

                default:

                    LOGGER.error("Error de SQL no manejado: " + estadoSQL + "-" + e);
                    utilidades.mostrarAlerta(
                            "Error del sistema.",
                            "Se produjo un error al acceder a la base de datos.",
                            "Por favor, contacte al soporte técnico."
                    );
                    break;
            }

        } catch (IOException e) {

            LOGGER.error("Error de IOException: " + e);
            utilidades.mostrarAlerta(
                    "Error interno del sistema",
                    "No se pudo completar la operación.",
                    "Ocurrió un error dentro del sistema, por favor inténtelo de nuevo más tarde " +
                            "o contacte al administrador."
            );

        } catch (Exception e) {

            LOGGER.error("Error inesperado: " + e);
            utilidades.mostrarAlerta(
                    "Error interno del sistema",
                    "Ocurrió un error al completar la operación.",
                    "Ocurrió un error dentro del sistema, por favor inténtelo de nuevo más tarde " +
                            "o contacte al administrador."
            );
        }
    }

    @FXML
    private void cancelarRegistroAcademico() {

        String nombreOriginal = campoNombre.getText();
        String apellidosOriginal = campoApellidos.getText();
        String numeroPersonalOriginal = campoNumeroPersonal.getText();
        String correoOriginal = campoCorreo.getText();

        utilidades.mostrarAlertaConfirmacion(
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

                    utilidades.mostrarAlerta(
                            "Operación cancelada",
                            "Los cambios no han sido descartados",
                            "Puede continuar editando el registro"
                    );
                }
        );
    }
}
