package GUI;

import GUI.utilidades.Utilidades;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logica.DAOs.AcademicoDAO;
import logica.DAOs.CuentaDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.AcademicoDTO;
import logica.DTOs.CuentaDTO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import logica.DTOs.UsuarioDTO;
import logica.VerificacionUsuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ControladorGestorAcademicosGUI {

    private static final Logger logger = LogManager.getLogger(ControladorGestorAcademicosGUI.class);

    @FXML private TextField campoNumeroDePersonal;
    @FXML private TableView<AcademicoDTO> tablaAcademicos;
    @FXML private TableColumn<AcademicoDTO, String> columnaNumeroDePersonal;
    @FXML private TableColumn<AcademicoDTO, String> columnaNombres;
    @FXML private TableColumn<AcademicoDTO, String> columnaApellidos;
    @FXML private Label campoNombreEncontrado;
    @FXML private Label campoApellidoEncontrado;
    @FXML private Label campoNumeroDePersonalEncontrado;
    @FXML private Label campoCorreoEncontrado;
    @FXML private TextField campoNombreEditable;
    @FXML private TextField campoApellidoEditable;
    @FXML private TextField campoNumeroDePersonalEditable;
    @FXML private TextField campoCorreoEditable;
    @FXML private Button botonEditar;
    @FXML private Button botonGuardar;
    @FXML private Button botonCancelar;

    Utilidades utilidades = new Utilidades();
    private int idAcademico = 0;

    @FXML
    public void initialize() {

        columnaNumeroDePersonal.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumeroDePersonal()).asObject().asString());
        columnaNombres.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        columnaApellidos.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));

        cargarAcademicos();

        tablaAcademicos.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            mostrarDetallesDesdeTabla(newSel);
        });

        botonEditar.setVisible(false);
    }

    private void cargarAcademicos() {

        try {

            AcademicoDAO academicoDAO = new AcademicoDAO();
            ObservableList<AcademicoDTO> academicos = FXCollections.observableArrayList(academicoDAO.listarAcademicos());
            tablaAcademicos.setItems(academicos);

        } catch (Exception e) {

            logger.warn("Error al cargar la lista de estudiantes: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Ocurrio un error al cargar la lista de academicos.", "");
        }
    }

    @FXML
    private void buscarAcademico() {

        String numeroDePersonal = campoNumeroDePersonal.getText().trim();

        if (numeroDePersonal.isEmpty()) {

            utilidades.mostrarAlerta("Error", "Por favor llene el campo de busqueda.", "Introduzca en la barra de busqueda un numero de personal de 5 digitos");
            return;
        }

        try {

            AcademicoDAO academicoDAO = new AcademicoDAO();
            AcademicoDTO academicoDTO = academicoDAO.buscarAcademicoPorNumeroDePersonal(Integer.parseInt(numeroDePersonal));
            CuentaDAO cuentaDAO = new CuentaDAO();

            if (academicoDTO.getNumeroDePersonal() != -1) {

                campoNombreEncontrado.setText(academicoDTO.getNombre());
                campoApellidoEncontrado.setText(academicoDTO.getApellido());
                campoNumeroDePersonalEncontrado.setText(String.valueOf(academicoDTO.getNumeroDePersonal()));

                idAcademico = academicoDTO.getIdUsuario();
                CuentaDTO cuentaDTO = cuentaDAO.buscarCuentaPorID(idAcademico);
                campoCorreoEncontrado.setText(cuentaDTO.getCorreoElectronico());

            } else {

                utilidades.mostrarAlerta("Usuario no encontrado", "El academico no fue encontrado.", "");
                campoNombreEncontrado.setText("");
                campoApellidoEncontrado.setText("");
                campoNumeroDePersonalEncontrado.setText("");
                campoCorreoEncontrado.setText("");
            }

        } catch (SQLException | IOException e) {

            logger.warn("Error al buscar el académico: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Ocurrio un error en la busqueda", "");
        }
    }

    private void mostrarDetallesDesdeTabla(AcademicoDTO academicoSeleccionado) {

        if (academicoSeleccionado == null) {

            botonEditar.setVisible(false);
            return;
        }

        try {

            AcademicoDAO academicoDAO = new AcademicoDAO();
            AcademicoDTO academicoDTO = academicoDAO.buscarAcademicoPorNumeroDePersonal(academicoSeleccionado.getNumeroDePersonal());
            CuentaDAO cuentaDAO = new CuentaDAO();

            if (academicoDTO == null || academicoDTO.getIdUsuario() == -1) {

                utilidades.mostrarAlerta("Usuario no encontrado", "El academico no fue encontrado.", "");
                return;
            }

            campoNombreEncontrado.setText(academicoDTO.getNombre());
            campoApellidoEncontrado.setText(academicoDTO.getApellido());
            campoNumeroDePersonalEncontrado.setText(String.valueOf(academicoDTO.getNumeroDePersonal()));

            idAcademico = academicoDTO.getIdUsuario();
            CuentaDTO cuenta = cuentaDAO.buscarCuentaPorID(idAcademico);
            campoCorreoEncontrado.setText(cuenta.getCorreoElectronico());
            botonEditar.setVisible(true);

        } catch (SQLException | IOException e) {

            logger.warn("Error al mostrar detalles del académico: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Ocurrio un error al cargar los datos del academico.", "");
        }
    }

    @FXML
    private void abrirVentanaRegistrarAcademico() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistroAcademicoGUI.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setOnHiding(event -> cargarAcademicos());
            stage.show();

        } catch (IOException e) {

            logger.warn("Error al abrir la ventana de registro: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Ocurrio un error al cargar la ventana.", "En estos momentos no es posible registrar un academico.");
        }
    }

    @FXML
    private void editarAcademico() {

        String nombreOriginal = campoNombreEncontrado.getText();
        String apellidosOriginales = campoApellidoEncontrado.getText();
        String numeroDePersonalOriginal = campoNumeroDePersonalEncontrado.getText();
        String correoOriginal = campoCorreoEncontrado.getText();

        campoNombreEditable.setText(nombreOriginal);
        campoApellidoEditable.setText(apellidosOriginales);
        campoNumeroDePersonalEditable.setText(numeroDePersonalOriginal);
        campoCorreoEditable.setText(correoOriginal);

        cambiarModoEdicion(true);
        tablaAcademicos.setDisable(true);
    }

    @FXML
    private void guardarCambios() {

        AcademicoDTO academicoSeleccionado = tablaAcademicos.getSelectionModel().getSelectedItem();

        if (academicoSeleccionado == null) {

            logger.warn("No se ha seleccionado un académico.");
            utilidades.mostrarAlerta("Academico no seleccionado", "Por favor seleccione un academico.", "");
            return;
        }

        String nuevoNombre = campoNombreEditable.getText().trim();
        String nuevosApellidos = campoApellidoEditable.getText().trim();
        String nuevoNumeroDePersonal = campoNumeroDePersonalEditable.getText().trim();
        String nuevoCorreo = campoCorreoEditable.getText().trim();

        try {

            CuentaDAO cuentaDAO = new CuentaDAO();
            AcademicoDAO academicoDAO = new AcademicoDAO();
            String contrasena = cuentaDAO.buscarCuentaPorID(academicoSeleccionado.getIdUsuario()).getContrasena();

            if (VerificacionUsuario.camposVacios(nuevoNombre, nuevosApellidos, nuevoNumeroDePersonal, nuevoCorreo, contrasena)) {

                utilidades.mostrarAlerta("Campos Vacios.", "Por favor llene todos los campos.", "Faltan algunos campos por ser llenados.");
                return;
            }

            List<String> errores = VerificacionUsuario.validarCampos(nuevoNombre, nuevosApellidos, nuevoNumeroDePersonal, nuevoCorreo, contrasena);

            if (!errores.isEmpty()) {

                String mensajeError = String.join("\n", errores);
                utilidades.mostrarAlerta("Datos invalidos.", "Por favor introduzca datos validos.", mensajeError);
                return;
            }

            int numeroPersonal = Integer.parseInt(nuevoNumeroDePersonal);
            int estado = academicoSeleccionado.getEstado();
            int idAcademico = academicoSeleccionado.getIdUsuario();

            AcademicoDTO academicoDTO = new AcademicoDTO(numeroPersonal, idAcademico, nuevoNombre, nuevosApellidos, estado);
            CuentaDTO cuentaDTO = new CuentaDTO(nuevoCorreo, contrasena , idAcademico);

            if (numeroPersonal != academicoSeleccionado.getNumeroDePersonal()) {

                if (academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroPersonal).getNumeroDePersonal() != -1) {

                    utilidades.mostrarAlerta("Error", "Numero de personal existente.", "El numero de personal ya se encuentra registrado dentro del sistema.");
                    return;
                }
            }

            if (!nuevoCorreo.equals(campoCorreoEncontrado.getText())) {

                if (!cuentaDAO.buscarCuentaPorCorreo(nuevoCorreo).equals("N/A")) {

                    utilidades.mostrarAlerta("Error", "Correo existente.", "El correo ya se encuentra registrado dentro del sistema.");
                    return;
                }
            }

            UsuarioDAO usurioDAO = new UsuarioDAO();
            boolean usuarioModificado = usurioDAO.modificarUsuario(new UsuarioDTO(academicoSeleccionado.getIdUsuario(), nuevoNombre, nuevosApellidos, estado));
            boolean academicoModificado = academicoDAO.modificarAcademico(academicoDTO);
            boolean correoModificado = cuentaDAO.modificarCuenta(cuentaDTO);



            if (usuarioModificado && academicoModificado && correoModificado) {

                logger.info("El académico ha sido modificado correctamente.");
                utilidades.mostrarAlerta("Exito", "Academico modificado con exito.", "");
                cargarAcademicos();
                cambiarModoEdicion(false);

                botonEditar.setVisible(true);

            } else {

                logger.warn("No se pudo modificar el académico.");
                utilidades.mostrarAlerta("Error", "Algo salio mal.", "Ocurrio un error, por favor intentelo más tarde.");
            }

            tablaAcademicos.setDisable(false);
            botonEditar.setVisible(false);

        } catch (SQLException e) {

            logger.error("Error de base de datos durante el registro del académico.", e);
            utilidades.mostrarAlerta("Error", "Ocurrio un error, por favor intentelo de nuevo más tarde.", "");

        } catch (NumberFormatException e) {

            logger.error("Error de formato numérico en el número de personal.", e);
            utilidades.mostrarAlerta("Error", "Ocurrio un error en el formato.",
                    "Ocurrio un error en el formato del numero de personal, por favor revise que el numero de personal contenga 5 digitos.");

        } catch (IOException e) {

            logger.error("Error de entrada/salida durante el registro del académico.", e);
            utilidades.mostrarAlerta("Error", "Ocurrio un error, por favor intentelo de nuevo más tarde.", "");

        } catch (Exception e) {

            logger.error("Error inesperado durante el registro del académico.", e);
            utilidades.mostrarAlerta("Error", "Ocurrio un error, por favor intentelo de nuevo más tarde.", "");
        }
    }

    @FXML
    private void cancelarEdicion() {

        cambiarModoEdicion(false);
        tablaAcademicos.setDisable(false);
        AcademicoDTO academicoSeleccionado = tablaAcademicos.getSelectionModel().getSelectedItem();

        if (academicoSeleccionado != null) {

            mostrarDetallesDesdeTabla(academicoSeleccionado);

        } else {

            campoNombreEncontrado.setText("");
            campoApellidoEncontrado.setText("");
            campoNumeroDePersonalEncontrado.setText("");
            campoCorreoEncontrado.setText("");
        }

        botonEditar.setVisible(false);
    }

    private void cambiarModoEdicion(boolean enEdicion) {

        campoNombreEditable.setVisible(enEdicion);
        campoApellidoEditable.setVisible(enEdicion);
        campoNumeroDePersonalEditable.setVisible(enEdicion);
        campoCorreoEditable.setVisible(enEdicion);

        campoNombreEncontrado.setVisible(!enEdicion);
        campoApellidoEncontrado.setVisible(!enEdicion);
        campoNumeroDePersonalEncontrado.setVisible(!enEdicion);
        campoCorreoEncontrado.setVisible(!enEdicion);

        botonEditar.setVisible(!enEdicion);
        botonGuardar.setVisible(enEdicion);
        botonCancelar.setVisible(enEdicion);
    }
}