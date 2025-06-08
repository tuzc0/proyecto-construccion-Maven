package GUI.gestionacademico;

import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logica.DAOs.CuentaDAO;
import logica.DAOs.AcademicoDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.CuentaDTO;
import logica.DTOs.AcademicoDTO;
import logica.DTOs.UsuarioDTO;
import logica.VerificacionUsuario;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControladorGestorAcademicosGUI {

    private static final Logger LOGGER =
            LogManager.getLogger(ControladorRegistroAcademicoGUI.class);
    private Utilidades UTILIDADES = new Utilidades();

    @FXML private TextField campoNumeroDePersonal;
    @FXML private Button botonBuscar;
    @FXML private Button botonEliminarSeleccionado;
    @FXML private TableView<AcademicoDTO> tablaAcademicos;
    @FXML private TableColumn<AcademicoDTO, String> columnaNumeroDePersonal;
    @FXML private TableColumn<AcademicoDTO, String> columnaNombres;
    @FXML private TableColumn<AcademicoDTO, String> columnaApellidos;
    @FXML private Label etiquetaNombreEncontrado;
    @FXML private Label etiquetaApellidoEncontrado;
    @FXML private Label etiquetaNumeroDePersonalEncontrado;
    @FXML private Label etiquetaCorreoEncontrado;
    @FXML private TextField campoNombreEditable;
    @FXML private TextField campoApellidoEditable;
    @FXML private TextField campoNumeroDePersonalEditable;
    @FXML private TextField campoCorreoEditable;
    @FXML private Label contadorNombre;
    @FXML private Label contadorApellidos;
    @FXML private Label contadorNumeroPersonal;
    @FXML private Label contadorCorreo;
    @FXML private Button botonEditar;
    @FXML private Button botonGuardar;
    @FXML private Button botonCancelar;
    @FXML private Button botonCancelarSeleccion;
    @FXML private Label etiquetaNumeroAcademicosSeleccionados;
    @FXML private Button botonEliminarAcademico;
    @FXML private Button botonSeleccionarAcademicos;
    @FXML private Button botonRegistrarAcademico;

    private int idAcademico = 0;

    @FXML
    public void initialize() {

        VerificicacionGeneral verficacionGeneral = new VerificicacionGeneral();
        final int MAX_CARACTERES_NOMBRE = 50;
        final int MAX_CARACTERES_NUMERO_PERSONAL = 5;
        final int MAX_CARACTERES_CORREO = 100;

        verficacionGeneral.contadorCaracteresTextField(
                campoNombreEditable, contadorNombre, MAX_CARACTERES_NOMBRE);
        verficacionGeneral.contadorCaracteresTextField(
                campoApellidoEditable, contadorApellidos, MAX_CARACTERES_NOMBRE);
        verficacionGeneral.contadorCaracteresTextField(
                campoNumeroDePersonalEditable, contadorNumeroPersonal, MAX_CARACTERES_NUMERO_PERSONAL);
        verficacionGeneral.contadorCaracteresTextField(
                campoCorreoEditable, contadorCorreo, MAX_CARACTERES_CORREO);

        botonBuscar.setCursor(Cursor.HAND);
        botonEliminarSeleccionado.setCursor(Cursor.HAND);
        botonEditar.setCursor(Cursor.HAND);
        botonGuardar.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
        botonCancelarSeleccion.setCursor(Cursor.HAND);
        botonEliminarAcademico.setCursor(Cursor.HAND);
        botonSeleccionarAcademicos.setCursor(Cursor.HAND);
        botonRegistrarAcademico.setCursor(Cursor.HAND);

        columnaNumeroDePersonal.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(cellData.getValue().getNumeroDePersonal())));

        columnaNombres.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getNombre()));

        columnaApellidos.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getApellido()));

        cargarAcademicos();

        tablaAcademicos.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);

        tablaAcademicos.getSelectionModel().selectedItemProperty()
                .addListener((observadorAcademico, academicoAnterior,
                              academicoSeleccionado) -> {
                    mostrarDetallesDesdeTabla(academicoSeleccionado);
                    botonEliminarSeleccionado.setDisable(
                            academicoSeleccionado == null);
                });
    }

    private void cargarAcademicos() {

        AcademicoDAO academicoDAO = new AcademicoDAO();

        try {

            ObservableList<AcademicoDTO> academicos =
                    FXCollections.observableArrayList(
                            academicoDAO.listarAcademicos());
            tablaAcademicos.setItems(academicos);

        } catch (Exception e) {

            LOGGER.error("Error al cargar la lista de academicos: " + e);
            UTILIDADES.mostrarAlerta(
                    "Error al cargar académicos",
                    "No se pudo obtener la lista de académicos.",
                    "Por favor, contacte al administrador.");
        }
    }

    @FXML
    private void buscarAcademico() {

        String numeroDePersonal = campoNumeroDePersonal.getText().trim();

        if (numeroDePersonal.isEmpty()) {

            UTILIDADES.mostrarAlerta(
                    "Búsqueda incompleta",
                    "No se ingresó un número de personal.",
                    "Por favor, escribe el número de personal en el campo " +
                            "de búsqueda e intenta nuevamente.");
            return;
        }

        AcademicoDAO academicoDAO = new AcademicoDAO();
        CuentaDAO cuentaDAO = new CuentaDAO();

        try {

            int numeroDePersonalABuscar = Integer.parseInt(numeroDePersonal);

            AcademicoDTO academicoDTO =
                    academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroDePersonalABuscar);
            int academicoEncontrado = -1;
            int academicoInactivo = 0;

            if (academicoDTO.getIdUsuario() != academicoEncontrado) {

                if (academicoDTO.getEstado() != academicoInactivo) {

                    etiquetaNombreEncontrado.setText(academicoDTO.getNombre());
                    etiquetaApellidoEncontrado.setText(academicoDTO.getApellido());
                    etiquetaNumeroDePersonalEncontrado.setText(numeroDePersonal);

                    idAcademico = academicoDTO.getIdUsuario();
                    CuentaDTO cuenta = cuentaDAO.buscarCuentaPorID(idAcademico);
                    etiquetaCorreoEncontrado.setText(cuenta.getCorreoElectronico());

                } else {
                    
                    UTILIDADES.mostrarAlerta(
                            "Académico no activo",
                            "El académico no se encuentra activo en estos momentos.",
                            "Verifique el estado del académico dentro del sistema.");
                }

            } else {

                etiquetaNombreEncontrado.setText("");
                etiquetaApellidoEncontrado.setText("");
                etiquetaNumeroDePersonalEncontrado.setText("");
                etiquetaCorreoEncontrado.setText("");

                UTILIDADES.mostrarAlerta(
                        "Académico no encontrado",
                        "No hay registros que coincidan con el número ingresado.",
                        "Verifica que el número de personal sea correcto o " +
                                "intenta con otro número.");
            }

        } catch (NumberFormatException e) {
            UTILIDADES.mostrarAlerta("Error de busqueda",
            "El número de personal tiene que ser un número",
            "Verifique que el número introducido en el campo de busqueda sea un número Ej.12345."
            );
        }

        catch (SQLException sqlEx) {

            LOGGER.error("Error SQL al buscar al académico: " + sqlEx);
            UTILIDADES.mostrarAlerta(
                    "Error del sistema.",
                    "Ocurrió un problema al acceder a los datos del académico.",
                    "Intenta más tarde o contacte al administrador.");

        } catch (IOException ioEx) {

            LOGGER.error("Error de entrada/salida al buscar al académico: " + ioEx);
            UTILIDADES.mostrarAlerta(
                    "Error de conexión",
                    "No se pudo establecer conexión para completar la búsqueda.",
                    "Verifique su conexión a Internet o inténtelo nuevamente " +
                            "más tarde.");

        }
    }

    private void mostrarDetallesDesdeTabla(AcademicoDTO academicoSeleccionado) {

        if (academicoSeleccionado == null) {
            return;
        }

        AcademicoDAO academicoDAO = new AcademicoDAO();
        CuentaDAO cuentaDAO = new CuentaDAO();

        try {

            AcademicoDTO academicoDTO =
                    academicoDAO.buscarAcademicoPorNumeroDePersonal(
                            academicoSeleccionado.getNumeroDePersonal());
            int academicoNoEncontrado = -1;

            if (academicoDTO.getIdUsuario() != academicoNoEncontrado) {

                etiquetaNombreEncontrado.setText(academicoDTO.getNombre());
                etiquetaApellidoEncontrado.setText(academicoDTO.getApellido());
                etiquetaNumeroDePersonalEncontrado.setText(
                        String.valueOf(academicoDTO.getNumeroDePersonal()));

                idAcademico = academicoDTO.getIdUsuario();
                CuentaDTO cuenta = cuentaDAO.buscarCuentaPorID(idAcademico);
                etiquetaCorreoEncontrado.setText(
                        cuenta.getCorreoElectronico());
            }

        } catch (SQLException sqlEx) {

            LOGGER.error("Error SQL al mostrar detalles del académico: " + sqlEx);
            UTILIDADES.mostrarAlerta(
                    "Error del sistema",
                    "No se pudieron cargar los datos del académico.",
                    "Es posible que haya un problema con el sistema. Intenta " +
                            "nuevamente más tarde o contacta al administrador.");

        } catch (IOException ioEx) {

            LOGGER.error("Error de entrada/salida al mostrar detalles " +
                    "del académico: " + ioEx);
            UTILIDADES.mostrarAlerta(
                    "Error de conexión",
                    "No fue posible recuperar los detalles del académico.",
                    "Verifique su conexión a Internet o vuelva a intentarlo " +
                            "en unos minutos.");
        }
    }

    @FXML
    private void abrirVentanaRegistrarAcademico() {

        UTILIDADES.abrirVentana(
                "/RegistroAcademicoGUI.fxml",
                "Registro de Académico",
                (Stage) botonRegistrarAcademico.getScene().getWindow()
        );

        cargarAcademicos();
    }

    private boolean eliminarAcademico(int numeroDePersonal) {

        AcademicoDAO academicoDAO = new AcademicoDAO();
        boolean estadoEliminacion = false;

        try {

            boolean academicoEliminado = academicoDAO.eliminarAcademicoPorNumeroDePersonal(numeroDePersonal);

            if (academicoEliminado) {
                estadoEliminacion = true;
            } else {
                LOGGER.warn("No se pudo eliminar al académico con número de personal: " + numeroDePersonal);
            }

        } catch (SQLException sqlEx) {

            LOGGER.error("Error SQL al eliminar al académico: " + sqlEx);

        } catch (IOException ioEx) {

            LOGGER.error("Error de entrada/salida al eliminar al académico: " + ioEx);
        }

        return estadoEliminacion;
    }

    @FXML
    private void activarModoSeleccion() {

        botonEliminarSeleccionado.setManaged(true);
        botonEliminarSeleccionado.setVisible(true);
        botonCancelarSeleccion.setVisible(true);
        etiquetaNumeroAcademicosSeleccionados.setVisible(true);
        botonEditar.setDisable(true);
        botonEliminarAcademico.setDisable(true);
        botonRegistrarAcademico.setDisable(true);

        tablaAcademicos.getSelectionModel().getSelectedItems()
                .addListener((ListChangeListener<AcademicoDTO>)
                        cambioSeleccion -> {

                            int cantidadSeleccionados =
                                    tablaAcademicos.getSelectionModel()
                                            .getSelectedItems().size();
                            int sinAcademicosSeleccionados = 0;

                            if (cantidadSeleccionados > sinAcademicosSeleccionados) {
                                etiquetaNumeroAcademicosSeleccionados.setText(
                                        "Academicos seleccionados: " + cantidadSeleccionados);
                            } else {
                                etiquetaNumeroAcademicosSeleccionados.setText(" ");
                            }
                        });
    }

    @FXML
    private void eliminarAcademicoSeleccionado() {

        ObservableList<AcademicoDTO> academicosSeleccionados =
                tablaAcademicos.getSelectionModel().getSelectedItems();

        if (academicosSeleccionados == null || academicosSeleccionados.isEmpty()) {

            UTILIDADES.mostrarAlerta(
                    "Error",
                    "No se han seleccionado académicos para eliminar.",
                    "Por favor, seleccione uno o más académicos en la tabla antes de intentar eliminar.");
            return;
        }

        List<AcademicoDTO> copiaAcademicos = new ArrayList<>(academicosSeleccionados);

        UTILIDADES.mostrarAlertaConfirmacion(
                "Confirmar eliminación",
                "¿Está seguro que desea eliminar los académicos seleccionados?",
                "Se eliminarán " + academicosSeleccionados.size() + " académicos. Esta acción no se puede deshacer.",
                () -> {

                    boolean errorAlEliminar = false;

                    for (AcademicoDTO academicoSeleccionado : academicosSeleccionados) {

                        if (!eliminarAcademico(academicoSeleccionado.getNumeroDePersonal())) {
                            errorAlEliminar = true;
                        }
                    }

                    if (errorAlEliminar) {

                        UTILIDADES.mostrarAlerta(
                                "Error",
                                "No fue posible eliminar algunos de los académicos seleccionados.",
                                "Intentelo más tarde o contacte al administrador.");

                    } else {
                        UTILIDADES.mostrarAlerta(
                                "Éxito",
                                "Los académicos seleccionados han sido eliminados correctamente.",
                                "");
                    }

                    cargarAcademicos();
                    eliminarAcademicosSeleccionadosConcluidos();
                },
                () -> {

                    tablaAcademicos.getSelectionModel().clearSelection();

                    for (AcademicoDTO academico : copiaAcademicos) {
                        tablaAcademicos.getSelectionModel().select(academico);
                    }

                    UTILIDADES.mostrarAlerta(
                            "Operación cancelada",
                            "La eliminación fue cancelada",
                            "Los académicos no han sido eliminados.");
                }
        );
    }

    private void eliminarAcademicosSeleccionadosConcluidos() {

        botonEliminarSeleccionado.setDisable(true);
        botonEliminarSeleccionado.setManaged(false);
        botonEliminarSeleccionado.setVisible(false);
        botonCancelarSeleccion.setVisible(false);
        botonEditar.setDisable(false);
        botonEliminarAcademico.setDisable(false);
        botonRegistrarAcademico.setDisable(false);

        contadorNombre.setDisable(false);
        contadorApellidos.setDisable(false);
        contadorNumeroPersonal.setDisable(false);
        contadorCorreo.setDisable(false);

        tablaAcademicos.getSelectionModel().clearSelection();
    }

    @FXML
    private void cancelarSeleccionAcademico() {

        botonEliminarSeleccionado.setManaged(false);
        botonEliminarSeleccionado.setVisible(false);
        botonCancelarSeleccion.setVisible(false);
        etiquetaNumeroAcademicosSeleccionados.setVisible(false);
        botonEliminarAcademico.setDisable(false);
        botonEditar.setDisable(false);
        botonRegistrarAcademico.setDisable(false);

        tablaAcademicos.getSelectionModel().clearSelection();
        etiquetaNumeroAcademicosSeleccionados.setText(" ");
    }

    @FXML
    private void editarAcademico() {

        campoNombreEditable.setText(etiquetaNombreEncontrado.getText());
        campoApellidoEditable.setText(etiquetaApellidoEncontrado.getText());
        campoNumeroDePersonalEditable.setText(
                etiquetaNumeroDePersonalEncontrado.getText());
        campoCorreoEditable.setText(etiquetaCorreoEncontrado.getText());

        campoNombreEditable.setVisible(true);
        campoApellidoEditable.setVisible(true);
        campoNumeroDePersonalEditable.setVisible(true);
        campoCorreoEditable.setVisible(true);

        etiquetaNombreEncontrado.setVisible(false);
        etiquetaApellidoEncontrado.setVisible(false);
        etiquetaNumeroDePersonalEncontrado.setVisible(false);
        etiquetaCorreoEncontrado.setVisible(false);

        contadorNombre.setVisible(true);
        contadorApellidos.setVisible(true);
        contadorNumeroPersonal.setVisible(true);
        contadorCorreo.setVisible(true);

        botonGuardar.setVisible(true);
        botonCancelar.setVisible(true);
        botonSeleccionarAcademicos.setDisable(true);
        botonBuscar.setDisable(true);

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

        etiquetaNombreEncontrado.setVisible(true);
        etiquetaApellidoEncontrado.setVisible(true);
        etiquetaNumeroDePersonalEncontrado.setVisible(true);
        etiquetaCorreoEncontrado.setVisible(true);

        contadorNombre.setVisible(false);
        contadorApellidos.setVisible(false);
        contadorNumeroPersonal.setVisible(false);
        contadorCorreo.setVisible(false);

        botonGuardar.setVisible(false);
        botonCancelar.setVisible(false);
        botonSeleccionarAcademicos.setDisable(false);
        botonBuscar.setDisable(false);

        botonEditar.setVisible(true);
        botonEliminarAcademico.setVisible(true);
        tablaAcademicos.setDisable(false);
        botonRegistrarAcademico.setDisable(false);
    }

    @FXML
    private void guardarCambios() {

        String numeroDePersonalEncontrado =
                etiquetaNumeroDePersonalEncontrado.getText().trim();
        String correoEncontrado =
                etiquetaCorreoEncontrado.getText().trim();
        String nombreEditado = campoNombreEditable.getText().trim();
        String apellidosEditado = campoApellidoEditable.getText().trim();
        String numeroDePersonalEditado =
                campoNumeroDePersonalEditable.getText().trim();
        String correoEditado = campoCorreoEditable.getText().trim();

        AcademicoDAO academicoDAO = new AcademicoDAO();
        CuentaDAO cuentaDAO = new CuentaDAO();

        try {

            String contraseña =
                    cuentaDAO.buscarCuentaPorCorreo(correoEncontrado)
                            .getContrasena();
            int idUsuario =
                    cuentaDAO.buscarCuentaPorCorreo(correoEncontrado)
                            .getIdUsuario();

            List<String> listaDeCamposVacios =
                    VerificacionUsuario.camposVacios(
                            nombreEditado, apellidosEditado, numeroDePersonalEditado,
                            correoEditado, contraseña);

            if (!listaDeCamposVacios.isEmpty()) {

                String camposVacios = String.join("\n", listaDeCamposVacios);
                UTILIDADES.mostrarAlerta(
                        "Campos vacíos",
                        "Por favor, complete todos los campos requeridos.",
                        camposVacios);
                return;
            }

            List<String> errores =
                    VerificacionUsuario.validarCampos(
                            nombreEditado, apellidosEditado, 
                            numeroDePersonalEditado, correoEditado, contraseña);

            if (!errores.isEmpty()) {

                String mensajeError = String.join("\n", errores);
                UTILIDADES.mostrarAlerta(
                        "Datos inválidos",
                        "Algunos campos contienen datos no válidos.",
                        mensajeError);
                return;
            }

            int numeroPersonal = Integer.parseInt(numeroDePersonalEditado);
            int academicoNoEncontrado = -1;

            if (!numeroDePersonalEncontrado.equals(numeroDePersonalEditado)) {

                if (academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroPersonal).
                        getIdUsuario() != academicoNoEncontrado) {

                    UTILIDADES.mostrarAlerta(
                            "Número de personal duplicado",
                            "El número de personal ya está registrado en el sistema.",
                            "Por favor, ingrese un número de personal diferente.");
                    return;
                }
            }

            if(!correoEditado.equals(correoEncontrado)) {

                if (!"N/A".equals(cuentaDAO.buscarCuentaPorCorreo(correoEditado)
                        .getCorreoElectronico())) {

                    UTILIDADES.mostrarAlerta(
                            "Correo electrónico duplicado",
                            "El correo electrónico ya está registrado en el sistema.",
                            "Por favor, utilice un correo electrónico diferente.");
                    return;
                }
            }

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            UsuarioDTO usuarioDTO =
                    new UsuarioDTO(idUsuario, nombreEditado, apellidosEditado, 1);
            AcademicoDTO academicoDTO =
                    new AcademicoDTO(numeroPersonal, idUsuario, nombreEditado, apellidosEditado, 1);
            CuentaDTO cuentaDTO =
                    new CuentaDTO(correoEditado, contraseña, idUsuario);

            boolean usuarioModificado =
                    usuarioDAO.modificarUsuario(usuarioDTO);
            boolean academicoModificado =
                    academicoDAO.modificarAcademico(academicoDTO);
            boolean cuentaModificada =
                    cuentaDAO.modificarCuenta(cuentaDTO);

            if (usuarioModificado && academicoModificado && cuentaModificada) {

                LOGGER.info("El académico ha sido modificado correctamente.");
                UTILIDADES.mostrarMensajeConfirmacion(
                        "Éxito",
                        "Los cambios se han guardado correctamente.",
                        "");

            } else {

                LOGGER.warn("No se pudieron guardar completamente los cambios " +
                        "del académico.");
                UTILIDADES.mostrarAlerta(
                        "Advertencia",
                        "No se pudieron guardar todos los cambios.",
                        "Algunos datos no se modificaron correctamente. " +
                                "Por favor, inténtelo nuevamente.");
            }

            etiquetaNumeroDePersonalEncontrado.setText(numeroDePersonalEditado);
            etiquetaCorreoEncontrado.setText(correoEditado);
            etiquetaNombreEncontrado.setText(nombreEditado);
            etiquetaApellidoEncontrado.setText(apellidosEditado);

            cancelarEdicion();
            cargarAcademicos();
            actualizarDatos();

        } catch (NumberFormatException e) {

            LOGGER.error("Error en formato numérico: " + e);
            UTILIDADES.mostrarAlerta(
                    "Error de formato",
                    "El número de personal debe ser un valor numérico válido.",
                    "Por favor, revise el número de personal e intente de nuevo.");

        } catch (SQLException e) {

            LOGGER.error("Error de base de datos al guardar cambios: " + e);
            UTILIDADES.mostrarAlerta(
                    "Error del sistema",
                    "Ocurrió un problema al guardar los cambios en el sistema.",
                    "Por favor, inténtelo nuevamente más tarde o contacte " +
                            "al soporte técnico.");

        } catch (IOException e) {

            LOGGER.error("Error de entrada/salida al guardar cambios: " + e);
            UTILIDADES.mostrarAlerta(
                    "Error de Sistema",
                    "No se pudo completar la operación debido a un error interno.",
                    "Por favor, inténtelo nuevamente más tarde.");
        }
    }

    private void actualizarDatos() {

        botonSeleccionarAcademicos.setDisable(false);
        botonEliminarAcademico.setVisible(true);
        botonEditar.setVisible(true);
        tablaAcademicos.setDisable(false);
        botonRegistrarAcademico.setDisable(false);

        contadorNombre.setVisible(false);
        contadorApellidos.setVisible(false);
        contadorNumeroPersonal.setVisible(false);
        contadorCorreo.setVisible(false);
    }
}