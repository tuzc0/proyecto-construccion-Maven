package GUI;

import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import logica.DAOs.AcademicoEvaluadorDAO;
import logica.DAOs.CuentaDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.AcademicoEvaluadorDTO;
import logica.DTOs.CuentaDTO;
import logica.DTOs.UsuarioDTO;
import logica.VerificacionUsuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorGestorAcademicoEvaluadorGUI {

    private static final Logger logger = LogManager.getLogger(ControladorRegistroAcademicoGUI.class);

    @FXML private TextField campoNumeroDePersonal;
    @FXML private Button botonBuscar;
    @FXML private Button botonEliminarSeleccionado;
    @FXML private TableView<AcademicoEvaluadorDTO> tablaAcademicos;
    @FXML private TableColumn<AcademicoEvaluadorDTO, String> columnaNumeroDePersonal;
    @FXML private TableColumn<AcademicoEvaluadorDTO, String> columnaNombres;
    @FXML private TableColumn<AcademicoEvaluadorDTO, String> columnaApellidos;
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
    @FXML private Button botonCancelarSeleccion;
    @FXML private Label campoNumeroAcademicosSeleccionados;
    @FXML private Button botonEliminarAcademico;
    @FXML private Button botonSeleccionarAcademicos;
    @FXML private Button botonRegistrarAcademico;

    private int idAcademico = 0;
    private Utilidades utilidades = new Utilidades();

    @FXML
    public void initialize() {

        botonBuscar.setCursor(Cursor.HAND);
        botonEliminarSeleccionado.setCursor(Cursor.HAND);
        botonEditar.setCursor(Cursor.HAND);
        botonGuardar.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
        botonCancelarSeleccion.setCursor(Cursor.HAND);
        botonEliminarAcademico.setCursor(Cursor.HAND);
        botonSeleccionarAcademicos.setCursor(Cursor.HAND);
        botonRegistrarAcademico.setCursor(Cursor.HAND);

        columnaNumeroDePersonal.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getNumeroDePersonal())));
        columnaNombres.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        columnaApellidos.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));

        cargarAcademicos();
        tablaAcademicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tablaAcademicos.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {

            mostrarDetallesDesdeTabla(newSel);
            botonEliminarSeleccionado.setDisable(newSel == null);
        });
    }

    private void cargarAcademicos() {

        try {

            AcademicoEvaluadorDAO academicoDAO = new AcademicoEvaluadorDAO();
            ObservableList<AcademicoEvaluadorDTO> academicos = FXCollections.observableArrayList(academicoDAO.listarAcademicos());
            tablaAcademicos.setItems(academicos);

        } catch (Exception e) {

            logger.error("Error al cargar la lista de academicos: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Error al cargar la lista de academicos.", "");
        }
    }

    @FXML
    private void buscarAcademico() {

        String numeroDePersonal = campoNumeroDePersonal.getText().trim();

        if (numeroDePersonal.isEmpty()) {

            utilidades.mostrarAlerta("Error", "Debe introducir un numero de personal para buscarlo.", "Introduzca un número de personal en el campo de busqueda.");
        }

        try {

            AcademicoEvaluadorDAO academicoDAO = new AcademicoEvaluadorDAO();
            AcademicoEvaluadorDTO academicoDTO = academicoDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(Integer.parseInt(numeroDePersonal));
            CuentaDAO cuentaDAO = new CuentaDAO();

            if (academicoDTO.getIdUsuario() != -1) {

                campoNombreEncontrado.setText(academicoDTO.getNombre());
                campoApellidoEncontrado.setText(academicoDTO.getApellido());
                campoNumeroDePersonalEncontrado.setText(numeroDePersonal);

                idAcademico = academicoDTO.getIdUsuario();
                CuentaDTO cuenta = cuentaDAO.buscarCuentaPorID(idAcademico);
                campoCorreoEncontrado.setText(cuenta.getCorreoElectronico());

            } else {

                campoNombreEncontrado.setText("No hay academico con ese numero de personal.");
                campoApellidoEncontrado.setText("");
                campoNumeroDePersonalEncontrado.setText("");
                campoCorreoEncontrado.setText("");
            }

        } catch (SQLException | IOException e) {

            logger.error("Error al buscar al academico: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Ocurrio un error al buscar.", "");
        }
    }

    private void mostrarDetallesDesdeTabla(AcademicoEvaluadorDTO academicoSeleccionado) {

        if (academicoSeleccionado == null) {

            return;
        }

        try {

            AcademicoEvaluadorDAO academicoDAO = new AcademicoEvaluadorDAO();
            AcademicoEvaluadorDTO academicoDTO = academicoDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(academicoSeleccionado.getNumeroDePersonal());
            CuentaDAO cuentaDAO = new CuentaDAO();

            if (academicoDTO.getIdUsuario() != -1) {

                campoNombreEncontrado.setText(academicoDTO.getNombre());
                campoApellidoEncontrado.setText(academicoDTO.getApellido());
                campoNumeroDePersonalEncontrado.setText(String.valueOf(academicoDTO.getNumeroDePersonal()));

                idAcademico = academicoDTO.getIdUsuario();
                CuentaDTO cuenta = cuentaDAO.buscarCuentaPorID(idAcademico);
                campoCorreoEncontrado.setText(cuenta.getCorreoElectronico());
            }

        } catch (SQLException | IOException e) {

            logger.error("Error al mostrar detalles del academico: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Ocurrio un error al cargar los detalles del academico.", "");
        }
    }

    @FXML
    private void abrirVentanaRegistrarAcademico() {

    }

    @FXML
    private void eliminarAcademico() {

        String numeroDePersonal = campoNumeroDePersonalEncontrado.getText().trim();

        if (numeroDePersonal.isEmpty()) {

            utilidades.mostrarAlerta("Error", "Seleccionar un academico para eliminar.", "");
            return;
        }

        try {

            AcademicoEvaluadorDAO academicoDAO = new AcademicoEvaluadorDAO();
            boolean academicoEliminado = academicoDAO.eliminarAcademicoEvaluadorPorNumeroDePersonal(Integer.parseInt(numeroDePersonal));

            if (academicoEliminado) {

                utilidades.mostrarAlerta("Exito", "El academico ha sido eliminado correctamente.", "");
                cargarAcademicos();

            } else {

                utilidades.mostrarAlerta("Error", "No fue posible eliminar al academico.", "");
            }

        } catch (SQLException | IOException e) {

            logger.error("Error al eliminar al academico: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Ocurrio un error.", "Por favor, intentelo más tarde.");
        }
    }

    @FXML
    private void activarModoSeleccion() {

        botonEliminarSeleccionado.setManaged(true);
        botonEliminarSeleccionado.setVisible(true);
        botonCancelarSeleccion.setVisible(true);
        campoNumeroAcademicosSeleccionados.setVisible(true);
        botonEditar.setDisable(true);
        botonEliminarAcademico.setDisable(true);
        botonRegistrarAcademico.setDisable(true);

        tablaAcademicos.getSelectionModel().getSelectedItems().addListener((ListChangeListener<AcademicoEvaluadorDTO>) change -> {

            int cantidadSeleccionados = tablaAcademicos.getSelectionModel().getSelectedItems().size();

            if (cantidadSeleccionados > 0) {

                campoNumeroAcademicosSeleccionados.setText("Academicos seleccionados: " + cantidadSeleccionados);

            } else {

                campoNumeroAcademicosSeleccionados.setText(" ");
            }
        });
    }

    @FXML
    private void eliminarAcademicoSeleccionado() {

        ObservableList<AcademicoEvaluadorDTO> academicosSeleccionados = tablaAcademicos.getSelectionModel().getSelectedItems();

        if (academicosSeleccionados == null || academicosSeleccionados.isEmpty()) {

            utilidades.mostrarAlerta("Error", "No se han seleccionado academicos para eliminar.", "Por favor, seleccione los academicos que desea eliminar.");
            return;
        }

        boolean errorAlEliminar = false;

        try {

            AcademicoEvaluadorDAO academicoDAO = new AcademicoEvaluadorDAO();

            for (AcademicoEvaluadorDTO academicoSeleccionado : academicosSeleccionados) {

                boolean eliminado = academicoDAO.eliminarAcademicoEvaluadorPorNumeroDePersonal(academicoSeleccionado.getNumeroDePersonal());

                if (!eliminado) {

                    errorAlEliminar = true;
                }
            }

        } catch (SQLException | IOException e) {

            logger.error("Error al eliminar el academico: " + e.getMessage());
            errorAlEliminar = true;
        }

        if (errorAlEliminar) {

            utilidades.mostrarAlerta("Error", "No se pudo realizar la eliminacion de los academicos.", "Por favor, intentelo más tarde.");

        } else {

            utilidades.mostrarAlerta("Exito", "Academicos eliminados exitosamente.", "");
        }

        cargarAcademicos();
        botonEliminarSeleccionado.setDisable(true);
        botonEliminarSeleccionado.setManaged(false);
        botonEliminarSeleccionado.setVisible(false);
        botonCancelarSeleccion.setVisible(false);
        botonEditar.setDisable(false);
        botonEliminarAcademico.setDisable(false);
        botonRegistrarAcademico.setDisable(false);

        tablaAcademicos.getSelectionModel().clearSelection();
    }

    @FXML
    private void cancelarSeleccionAcademico() {

        botonEliminarSeleccionado.setManaged(false);
        botonEliminarSeleccionado.setVisible(false);
        botonCancelarSeleccion.setVisible(false);
        campoNumeroAcademicosSeleccionados.setVisible(false);
        botonEliminarAcademico.setDisable(false);
        botonEditar.setDisable(false);
        botonRegistrarAcademico.setDisable(false);

        tablaAcademicos.getSelectionModel().clearSelection();
        campoNumeroAcademicosSeleccionados.setText(" ");

    }

    @FXML
    private void editarAcademico() {

        campoNombreEditable.setText(campoNombreEncontrado.getText());
        campoApellidoEditable.setText(campoApellidoEncontrado.getText());
        campoNumeroDePersonalEditable.setText(campoNumeroDePersonalEncontrado.getText());
        campoCorreoEditable.setText(campoCorreoEncontrado.getText());

        campoNombreEditable.setVisible(true);
        campoApellidoEditable.setVisible(true);
        campoNumeroDePersonalEditable.setVisible(true);
        campoCorreoEditable.setVisible(true);

        campoNombreEncontrado.setVisible(false);
        campoApellidoEncontrado.setVisible(false);
        campoNumeroDePersonalEncontrado.setVisible(false);
        campoCorreoEncontrado.setVisible(false);

        botonGuardar.setVisible(true);
        botonCancelar.setVisible(true);
        botonSeleccionarAcademicos.setDisable(true);

        botonEditar.setVisible(false);
        botonEliminarAcademico.setVisible(false);
        tablaAcademicos.setDisable(true);
        botonRegistrarAcademico.setDisable(true);
    }

    @FXML
    private void cancelarEdicion() {

        campoNombreEditable.setVisible(false);
        campoApellidoEditable.setVisible(false);
        campoNumeroDePersonalEditable.setVisible(false);
        campoCorreoEditable.setVisible(false);

        campoNombreEncontrado.setVisible(true);
        campoApellidoEncontrado.setVisible(true);
        campoNumeroDePersonalEncontrado.setVisible(true);
        campoCorreoEncontrado.setVisible(true);

        botonGuardar.setVisible(false);
        botonCancelar.setVisible(false);
        botonSeleccionarAcademicos.setDisable(false);

        botonEditar.setVisible(true);
        botonEliminarAcademico.setVisible(true);
        tablaAcademicos.setDisable(false);
        botonRegistrarAcademico.setDisable(false);
    }

    @FXML
    private void guardarCambios() {

        String numeroDePersonalEncontrado = campoNumeroDePersonalEncontrado.getText().trim();
        String correoEncontrado = campoCorreoEncontrado.getText().trim();
        String nombre = campoNombreEditable.getText().trim();
        String apellidos = campoApellidoEditable.getText().trim();
        String numeroDePersonal = campoNumeroDePersonalEditable.getText().trim();
        String correo = campoCorreoEditable.getText().trim();

        VerificacionUsuario verificacionUsuario = new VerificacionUsuario();

        try {

            AcademicoEvaluadorDAO academicoDAO = new AcademicoEvaluadorDAO();
            CuentaDAO cuentaDAO = new CuentaDAO();
            String contraseña = cuentaDAO.buscarCuentaPorCorreo(correoEncontrado).getContrasena();
            int idUsuario = cuentaDAO.buscarCuentaPorCorreo(correoEncontrado).getIdUsuario();

            if (VerificacionUsuario.camposVacios(nombre, apellidos, numeroDePersonal, correo, contraseña)) {

                utilidades.mostrarAlerta("Campos Vacios.", "Por favor llene todos los campos.", "Faltan algunos campos por ser llenados.");
                return;
            }

            List<String> errores = VerificacionUsuario.validarCampos(nombre, apellidos, numeroDePersonal, correo, contraseña);

            if (!errores.isEmpty()) {

                String mensajeError = String.join("\n", errores);
                utilidades.mostrarAlerta("Datos inválidos.", "Por favor, introduzca datos válidos.", mensajeError);
                return;
            }

            int numeroPersonal = Integer.parseInt(numeroDePersonal);

            if (!numeroDePersonalEncontrado.equals(numeroDePersonal)) {

                if (academicoDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(numeroPersonal).getIdUsuario() != -1) {

                    utilidades.mostrarAlerta("Error", "Numero de personal existente.", "El numero de personal ya se encuentra registrado dentro del sistema.");
                    return;
                }
            }

            if(!correo.equals(correoEncontrado)) {

                if (!"N/A".equals(cuentaDAO.buscarCuentaPorCorreo(correo).getCorreoElectronico())) {

                    utilidades.mostrarAlerta("Error", "Correo existente.", "El correo ya se encuentra registrado dentro del sistema.");
                    return;
                }
            }

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            UsuarioDTO usuarioDTO = new UsuarioDTO(idUsuario, nombre, apellidos, 1);
            AcademicoEvaluadorDTO academicoDTO = new AcademicoEvaluadorDTO(numeroPersonal, idUsuario, nombre, apellidos, 1);
            CuentaDTO cuentaDTO = new CuentaDTO(correo, contraseña, idUsuario);

            boolean usuarioModificado = usuarioDAO.modificarUsuario(usuarioDTO);
            boolean academicoModificado = academicoDAO.modificarAcademicoEvaluador(academicoDTO);
            boolean cuentaModificada = cuentaDAO.modificarCuenta(cuentaDTO);

            if (usuarioModificado && academicoModificado && cuentaModificada) {

                logger.info("El académico ha sido modificado correctamente.");
                utilidades.mostrarAlerta("Exito", "Academico modificado con exito.", "");

            } else {

                logger.warn("No se pudo modificar al academico.");
                utilidades.mostrarAlerta("Exito", "Academico modificado con exito.", "");
            }

            cancelarEdicion();
            cargarAcademicos();
            actualizarDatos();

        } catch (SQLException | IOException | NumberFormatException e) {

            logger.error("Error al guardar los cambios: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Ocurrio un error, por favor intentelo de nuevo más tarde.", "");
        }
    }

    private void actualizarDatos() {

        botonSeleccionarAcademicos.setDisable(false);
        botonEliminarAcademico.setVisible(true);
        botonEditar.setVisible(true);
        tablaAcademicos.setDisable(false);
        botonRegistrarAcademico.setDisable(false);
    }
}
