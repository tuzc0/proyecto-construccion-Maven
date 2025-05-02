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
            utilidades.mostrarVentanaError("/ErrorGUI.fxml", "Error al cargar la lista de académicos");
        }
    }

    @FXML
    private void buscarAcademico() {

        String numeroDePersonal = campoNumeroDePersonal.getText().trim();

        if (numeroDePersonal.isEmpty()) {

            utilidades.mostrarVentanaError("/ErrorGUI.fxml", "Por favor ingrese un número de personal");
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

                utilidades.mostrarVentanaError("/ErrorGUI.fxml", "El académico no fue encontrado");
                campoNombreEncontrado.setText("");
                campoApellidoEncontrado.setText("");
                campoNumeroDePersonalEncontrado.setText("");
                campoCorreoEncontrado.setText("");
            }

        } catch (SQLException | IOException e) {

            logger.warn("Error al buscar el académico: " + e.getMessage());
            utilidades.mostrarVentanaError("/ErrorGUI.fxml", "Ocurrio un error al buscar al académico");
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

                utilidades.mostrarVentanaError("/ErrorGUI.fxml", "El académico no fue encontrado");
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
            utilidades.mostrarVentanaError("/ErrorGUI.fxml", "Error al mostrar detalles del académico");
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
            utilidades.mostrarVentanaError("/ErrorGUI.fxml", "En estos momentos no se puede registrar un nuevo academico");
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
            utilidades.mostrarVentanaError("/ErrorGUI.fxml", "Por favor seleccione un académico");
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

                utilidades.mostrarVentanaError("/CamposVaciosGUI.fxml", "Por favor complete todos los campos");
                return;
            }

            List<String> errores = VerificacionUsuario.validarCampos(nuevoNombre, nuevosApellidos, nuevoNumeroDePersonal, nuevoCorreo, contrasena);

            if (!errores.isEmpty()) {

                String mensajeError = String.join("\n", errores);
                utilidades.mostrarVentanaError("/ErrorGUI.fxml", mensajeError);
                return;
            }

            int numeroPersonal = Integer.parseInt(nuevoNumeroDePersonal);
            int estado = academicoSeleccionado.getEstado();
            int idAcademico = academicoSeleccionado.getIdUsuario();

            AcademicoDTO academicoDTO = new AcademicoDTO(numeroPersonal, idAcademico, nuevoNombre, nuevosApellidos, estado);
            CuentaDTO cuentaDTO = new CuentaDTO(nuevoCorreo, contrasena , idAcademico);

            if (numeroPersonal != academicoSeleccionado.getNumeroDePersonal()) {

                if (academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroPersonal).getNumeroDePersonal() != -1) {

                    utilidades.mostrarVentanaError("/ErrorGUI.fxml", "El número de personal ya existe");
                    return;
                }
            }

            if (!nuevoCorreo.equals(campoCorreoEncontrado.getText())) {

                if (!cuentaDAO.buscarCuentaPorCorreo(nuevoCorreo).equals("N/A")) {

                    utilidades.mostrarVentanaError("/ErrorGUI.fxml", "El correo electrónico ya existe");
                    return;
                }
            }

            UsuarioDAO usurioDAO = new UsuarioDAO();
            boolean usuarioModificado = usurioDAO.modificarUsuario(new UsuarioDTO(academicoSeleccionado.getIdUsuario(), nuevoNombre, nuevosApellidos, estado));
            boolean academicoModificado = academicoDAO.modificarAcademico(academicoDTO);
            boolean correoModificado = cuentaDAO.modificarCuenta(cuentaDTO);



            if (usuarioModificado && academicoModificado && correoModificado) {

                logger.info("El académico ha sido modificado correctamente.");
                utilidades.mostrarVentanaError("/ErrorGUI.fxml", "El académico ha sido modificado correctamente");
                cargarAcademicos();
                cambiarModoEdicion(false);

                botonEditar.setVisible(true);

            } else {

                logger.warn("No se pudo modificar el académico.");
                utilidades.mostrarVentanaError("/ErrorGUI.fxml", "No se pudo modificar el académico.");
            }

            tablaAcademicos.setDisable(false);
            botonEditar.setVisible(false);

        } catch (SQLException e) {

            logger.error("Error de base de datos durante el registro del académico.", e);
            utilidades.mostrarVentanaError("/ErrorGUI.fxml", "Ocurrio un error, por favor intente más tarde.");

        } catch (NumberFormatException e) {

            logger.error("Error de formato numérico en el número de personal.", e);
            utilidades.mostrarVentanaError("/ErrorGUI.fxml", "El número de personal debe ser un número entero.");
        } catch (IOException e) {

            logger.error("Error de entrada/salida durante el registro del académico.", e);
            utilidades.mostrarVentanaError("/ErrorGUI.fxml", "Ocurrio un error, por favor intente más tarde.");

        } catch (Exception e) {

            logger.error("Error inesperado durante el registro del académico.", e);
            utilidades.mostrarVentanaError("/ErrorRegistroAcademicoGUI.fxml", "Ocurrio un error, por facvor intente más tarde.");
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