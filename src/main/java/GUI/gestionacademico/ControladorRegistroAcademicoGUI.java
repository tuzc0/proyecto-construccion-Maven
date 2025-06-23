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
import logica.interfaces.IGestorAlertas;
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

    EncriptadorContraseñas encriptadorContraseñas = new EncriptadorContraseñas();
    private UtilidadesContraseña utilidadesConstraña = new UtilidadesContraseña();
    private IGestorAlertas utilidades = new Utilidades();
    private Utilidades utilidadesVentana = new Utilidades();
    private boolean contraseñaVisible = false;

    @FXML
    private void initialize() {

        VerificicacionGeneral verificacionGeneral = new VerificicacionGeneral();

        final int MAX_CARACTERES_NOMBRE = 50;
        final int MAX_CARACTERES_NUMERO_PERSONAL = 5;
        final int MAX_CARACTERES_CORREO = 100;
        final int MAX_CARACTERES_CONTRASEÑA = 64;

        verificacionGeneral.contadorCaracteresTextField(
                campoNombre, etiquetaContadorNombre, MAX_CARACTERES_NOMBRE);
        verificacionGeneral.contadorCaracteresTextField(
                campoApellidos, etiquetaContadorApellidos, MAX_CARACTERES_NOMBRE);
        verificacionGeneral.contadorCaracteresTextField(
                campoNumeroPersonal, etiquetaContadorNumeroPersonal, MAX_CARACTERES_NUMERO_PERSONAL);
        verificacionGeneral.contadorCaracteresTextField(
                campoCorreo, etiquetaContadorCorreo, MAX_CARACTERES_CORREO);
        verificacionGeneral.contadorCaracteresTextField(
                contraseñaIngresada, etiquetaContadorContraseña, MAX_CARACTERES_CONTRASEÑA);
        verificacionGeneral.contadorCaracteresTextField(
                contraseñaConfirmada,
                etiquetaContadorConfirmarContraseña, MAX_CARACTERES_CONTRASEÑA);

        campoContraseñaVisible.textProperty()
                .bindBidirectional(contraseñaIngresada.textProperty());
        campoConfirmarContraseñaVisible.textProperty()
                .bindBidirectional(contraseñaConfirmada.textProperty());

        utilidadesConstraña.inicializarIcono(imagenOjo);
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

        utilidadesConstraña.alternarVisibilidadContrasena(
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

            String mensajeError = String.join("\n", camposVacios);
            utilidades.mostrarAlerta(
                    "Campos vacíos",
                    "Por favor, complete todos los campos requeridos.",
                    mensajeError
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

            String mensajeError = String.join("\n", errores);
            utilidades.mostrarAlerta(
                    "Datos inválidos",
                    "Algunos campos contienen datos no válidos.",
                    mensajeError
            );
            return;
        }

        if (!UtilidadesContraseña.esContraseñaIgual(contraseñaIngresada, contraseñaConfirmada)) {

            utilidades.mostrarAlerta(
                    "Contraseñas distintas",
                    "Las contraseñas ingresadas no son iguales.",
                    "Asegúrese de que la contraseña y su confirmación coincidan."
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
            CuentaDTO cuentaExistente = cuentaDAO.buscarCuentaPorCorreo(correo);
            int academicoEncontrado = -1;
            String cuentaEncontrada = "N/A";

            if (academicoExistente.getNumeroDePersonal() != academicoEncontrado) {

                utilidades.mostrarAlerta(
                        "Número de personal duplicado",
                        "Ya existe un académico con este número de"
                                + " personal.",
                        "Verifique que el número no esté ya registrado."
                );
                return;
            }

            if (!cuentaEncontrada.equals(cuentaExistente.getCorreoElectronico())) {

                utilidades.mostrarAlerta(
                        "Correo electrónico duplicado",
                        "Ya existe una cuenta con ese correo.",
                        "Por favor, utilice un correo diferente."
                );
                return;
            }

            UsuarioDTO usuarioDTO = new UsuarioDTO(idUsuario, nombre, apellidos, estadoActivo);
            idUsuario = new UsuarioDAO().insertarUsuario(usuarioDTO);
            CuentaDTO cuentaDTO = new CuentaDTO(correo, contrasena, idUsuario);

            contrasena = encriptadorContraseñas.encriptar(contrasena);

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
                utilidades.mostrarAlerta(
                        "Registro exitoso",
                        "El académico ha sido registrado correctamente.",
                        ""
                );

            } else {

                LOGGER.warn("No se pudieron guardar todos los datos del académico.");
                utilidades.mostrarAlerta(
                        "Registro incompleto",
                        "No se pudieron guardar todos los datos.",
                        "Por favor, verifique la información e intente"
                                + " nuevamente."
                );
            }

        } catch (NumberFormatException e) {

            LOGGER.error("Error de formato numérico al registrar académico: " + e);
            utilidades.mostrarAlerta(
                    "Error de formato",
                    "El número de personal debe ser un valor numérico"
                            + " de 5 dígitos.",
                    "Revise el campo correspondiente e intente de nuevo."
            );

        } catch (SQLException e) {

            String estadoSQL = e.getSQLState();

            switch (estadoSQL) {

                case "08S01":

                    LOGGER.error("Error de conexión con la base datos: " + e);
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

            LOGGER.error("Error de IOException al registrar académico: " + e);
            utilidades.mostrarAlerta(
                    "Error interno del sistema",
                    "No se pudo completar la operación.",
                    "Ocurrió un error dentro del sistema, por favor inténtelo de nuevo más tarde " +
                            "o contacte al administrador."
            );

        } catch (Exception e) {

            LOGGER.error("Error inesperado al registrar académico: " + e);
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

        utilidadesVentana.mostrarAlertaConfirmacion(
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
