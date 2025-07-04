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
import logica.DAOs.GrupoDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.CuentaDTO;
import logica.DTOs.AcademicoDTO;
import logica.DTOs.GrupoDTO;
import logica.DTOs.UsuarioDTO;
import logica.ManejadorExcepciones;
import logica.VerificacionUsuario;
import logica.interfaces.IGestorAlertas;
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
    @FXML private Label etiquetaContadorNombre;
    @FXML private Label etiquetaContadorApellidos;
    @FXML private Label etiquetaContadorNumeroPersonal;
    @FXML private Label etiquetaContadorCorreo;
    @FXML private Button botonEditar;
    @FXML private Button botonGuardar;
    @FXML private Button botonCancelar;
    @FXML private Button botonCancelarSeleccion;
    @FXML private Label etiquetaNumeroAcademicosSeleccionados;
    @FXML private Button botonEliminarAcademico;
    @FXML private Button botonSeleccionarAcademicos;
    @FXML private Button botonRegistrarAcademico;

    private Utilidades gestorVentanas = new Utilidades();
    private IGestorAlertas utilidades = new Utilidades();
    private ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, LOGGER);

    private int idUsuario = 0;

    @FXML
    public void initialize() {

        VerificicacionGeneral verficacionGeneral = new VerificicacionGeneral();

        final int MAX_CARACTERES_NOMBRE = 50;
        final int MAX_CARACTERES_NUMERO_PERSONAL = 5;
        final int MAX_CARACTERES_CORREO = 100;

        verficacionGeneral.contadorCaracteresTextField(
                campoNombreEditable, etiquetaContadorNombre, MAX_CARACTERES_NOMBRE);
        verficacionGeneral.contadorCaracteresTextField(
                campoApellidoEditable, etiquetaContadorApellidos, MAX_CARACTERES_NOMBRE);
        verficacionGeneral.contadorCaracteresTextField(
                campoNumeroDePersonalEditable, etiquetaContadorNumeroPersonal, MAX_CARACTERES_NUMERO_PERSONAL);
        verficacionGeneral.contadorCaracteresTextField(
                campoCorreoEditable, etiquetaContadorCorreo, MAX_CARACTERES_CORREO);

        columnaNumeroDePersonal.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(cellData.getValue().getNumeroDePersonal())));

        columnaNombres.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getNombre()));

        columnaApellidos.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getApellido()));

        tablaAcademicos.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);

        cargarAcademicos();

        tablaAcademicos.getSelectionModel().selectedItemProperty()
                .addListener((academicoObservado, academicoAnterior,
                              academicoSeleccionado) -> {

                    mostrarDetallesDesdeTabla(academicoSeleccionado);
                    botonEliminarSeleccionado.setDisable(
                            academicoSeleccionado == null);
                });

        botonBuscar.setCursor(Cursor.HAND);
        botonEliminarSeleccionado.setCursor(Cursor.HAND);
        botonEditar.setCursor(Cursor.HAND);
        botonGuardar.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
        botonCancelarSeleccion.setCursor(Cursor.HAND);
        botonEliminarAcademico.setCursor(Cursor.HAND);
        botonSeleccionarAcademicos.setCursor(Cursor.HAND);
        botonRegistrarAcademico.setCursor(Cursor.HAND);
    }

    private void cargarAcademicos() {

        AcademicoDAO academicoDAO = new AcademicoDAO();

        try {

            ObservableList<AcademicoDTO> academicos = FXCollections.observableArrayList(
                    academicoDAO.listarAcademicos());
            tablaAcademicos.setItems(academicos);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error inesperado al cargar académico: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error interno del sistema",
                    "Ocurrió un error al cargar los datos.",
                    "Ocurrió un error dentro del sistema, por favor inténtelo de nuevo más tarde " +
                            "o contacte al administrador."
            );
        }
    }

    @FXML
    private void buscarAcademico() {

        String numeroDePersonal = campoNumeroDePersonal.getText().trim();

        if (numeroDePersonal.isEmpty()) {

            gestorVentanas.mostrarAlerta(
                    "Búsqueda invalida",
                    "No se ingresó un número de personal.",
                    "Por favor, escribe el número de personal en el campo " +
                            "de búsqueda e intenta nuevamente."
            );
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

                    idUsuario = academicoDTO.getIdUsuario();
                    CuentaDTO cuenta = cuentaDAO.buscarCuentaPorID(idUsuario);
                    etiquetaCorreoEncontrado.setText(cuenta.getCorreoElectronico());
                }

            } else {

                etiquetaNombreEncontrado.setText("");
                etiquetaApellidoEncontrado.setText("");
                etiquetaNumeroDePersonalEncontrado.setText("");
                etiquetaCorreoEncontrado.setText("");

                gestorVentanas.mostrarAlerta(
                        "Académico no encontrado",
                        "No hay registros que coincidan con el número ingresado.",
                        "Verifica que el número de personal sea correcto o " +
                                "intenta con otro número.");
            }

        } catch (NumberFormatException e) {

            gestorVentanas.mostrarAlerta(
                    "Error de busqueda",
                    "El número de personal tiene que ser un número",
                    "Verifique que el número introducido en el campo de busqueda sea un número Ej.12345."
            );

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error inesperado al buscar académico: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error interno del sistema",
                    "Ocurrió un error al buscar el académico.",
                    "Ocurrió un error dentro del sistema, por favor inténtelo de nuevo más tarde " +
                            "o contacte al administrador."
            );
        }
    }

    private void mostrarDetallesDesdeTabla(AcademicoDTO academicoSeleccionado) {

        if (academicoSeleccionado == null) {
            return;
        }

        AcademicoDAO academicoDAO = new AcademicoDAO();
        CuentaDAO cuentaDAO = new CuentaDAO();

        try {

            AcademicoDTO academicoDTO = academicoDAO.buscarAcademicoPorNumeroDePersonal(
                    academicoSeleccionado.getNumeroDePersonal());
            int academicoNoEncontrado = -1;

            if (academicoDTO.getIdUsuario() != academicoNoEncontrado) {

                etiquetaNombreEncontrado.setText(academicoDTO.getNombre());
                etiquetaApellidoEncontrado.setText(academicoDTO.getApellido());
                etiquetaNumeroDePersonalEncontrado.
                        setText(String.valueOf(academicoDTO.getNumeroDePersonal()));

                idUsuario = academicoDTO.getIdUsuario();
                CuentaDTO cuenta = cuentaDAO.buscarCuentaPorID(idUsuario);
                etiquetaCorreoEncontrado.setText(cuenta.getCorreoElectronico());
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error inesperado al mostrar detalles del académico: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error interno del sistema",
                    "Ocurrió un error al mostrar los detalles del académico.",
                    "Ocurrió un error dentro del sistema, por favor inténtelo de nuevo más tarde " +
                            "o contacte al administrador."
            );

        }
    }

    @FXML
    private void abrirVentanaRegistrarAcademico() {

        gestorVentanas.abrirVentana(
                "/RegistroAcademicoGUI.fxml",
                "Registro de Académico",
                (Stage) botonRegistrarAcademico.getScene().getWindow()
        );

        cargarAcademicos();
    }

    private boolean eliminarAcademico(int numeroDePersonal) {

        AcademicoDAO academicoDAO = new AcademicoDAO();
        GrupoDAO grupoDAO = new GrupoDAO();
        boolean estadoEliminacion = false;
        boolean academicoEliminado = false;

        try {

            GrupoDTO grupoDTO =
                    grupoDAO.buscarGrupoActivoPorNumeroDePersonal(numeroDePersonal);

            if (grupoDTO.getNRC() != (-1)) {

                gestorVentanas.mostrarAlerta(
                        "Error al eliminar académico",
                        "El académico no puede ser eliminado porque está asignado a un grupo activo.",
                        "Por favor, desasigne al académico del grupo antes de intentar eliminarlo."
                );

                estadoEliminacion = false;

            } else {

                academicoEliminado = academicoDAO.eliminarAcademicoPorNumeroDePersonal(numeroDePersonal);

            }

            if (academicoEliminado) {
                estadoEliminacion = true;

            } else {

                LOGGER.warn("No se pudo eliminar al académico con número de personal: " +
                        numeroDePersonal);
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error inesperado al eliminar académico: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error interno del sistema",
                    "Ocurrió un error al eliminar el académico.",
                    "Ocurrió un error dentro del sistema, por favor inténtelo de nuevo más tarde " +
                            "o contacte al administrador."
            );
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

                            int cantidadSeleccionados = tablaAcademicos.getSelectionModel()
                                    .getSelectedItems().size();
                            int sinAcademicosSeleccionados = 0;

                            if (cantidadSeleccionados > sinAcademicosSeleccionados) {
                                etiquetaNumeroAcademicosSeleccionados
                                        .setText("Academicos seleccionados: " + cantidadSeleccionados);
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

            gestorVentanas.mostrarAlerta(
                    "Error",
                    "No se han seleccionado académicos para eliminar.",
                    "Por favor, seleccione uno o más académicos en la tabla " +
                            "antes de intentar eliminar."
            );
            return;
        }

        List<AcademicoDTO> copiaAcademicos = new ArrayList<>(academicosSeleccionados);

        gestorVentanas.mostrarAlertaConfirmacion(
                "Confirmar eliminación",
                "¿Está seguro que desea eliminar los académicos seleccionados?",
                "Se eliminarán " + academicosSeleccionados.size() +
                        " académicos. Esta acción no se puede deshacer.",
                () -> {

                    boolean errorAlEliminar = false;

                    for (AcademicoDTO academicoSeleccionado : academicosSeleccionados) {

                        if (!eliminarAcademico(academicoSeleccionado.getNumeroDePersonal())) {
                            errorAlEliminar = true;
                        }
                    }

                    if (errorAlEliminar) {

                        gestorVentanas.mostrarAlerta(
                                "Error",
                                "No fue posible eliminar algunos de los académicos seleccionados.",
                                "Inténtelo más tarde o contacte al administrador."
                        );

                    } else {

                        gestorVentanas.mostrarAlerta(
                                "Éxito",
                                "Los académicos seleccionados han sido eliminados correctamente.",
                                ""
                        );
                    }

                    cargarAcademicos();
                    eliminarAcademicosSeleccionadosConcluido();
                },
                () -> {

                    tablaAcademicos.getSelectionModel().clearSelection();

                    for (AcademicoDTO academico : copiaAcademicos) {
                        tablaAcademicos.getSelectionModel().select(academico);
                    }

                    gestorVentanas.mostrarAlerta(
                            "Operación cancelada",
                            "La eliminación fue cancelada",
                            "Los académicos no han sido eliminados."
                    );
                }
        );
    }

    private void eliminarAcademicosSeleccionadosConcluido() {

        botonEliminarSeleccionado.setDisable(true);
        botonEliminarSeleccionado.setManaged(false);
        botonEliminarSeleccionado.setVisible(false);
        botonCancelarSeleccion.setVisible(false);
        botonEditar.setDisable(false);
        botonEliminarAcademico.setDisable(false);
        botonRegistrarAcademico.setDisable(false);

        etiquetaContadorNombre.setDisable(false);
        etiquetaContadorApellidos.setDisable(false);
        etiquetaContadorNumeroPersonal.setDisable(false);
        etiquetaContadorCorreo.setDisable(false);

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
        campoNumeroDePersonalEditable.setText(etiquetaNumeroDePersonalEncontrado.getText());
        campoCorreoEditable.setText(etiquetaCorreoEncontrado.getText());

        campoNombreEditable.setVisible(true);
        campoApellidoEditable.setVisible(true);
        campoNumeroDePersonalEditable.setVisible(true);
        campoCorreoEditable.setVisible(true);

        etiquetaNombreEncontrado.setVisible(false);
        etiquetaApellidoEncontrado.setVisible(false);
        etiquetaNumeroDePersonalEncontrado.setVisible(false);
        etiquetaCorreoEncontrado.setVisible(false);

        etiquetaContadorNombre.setVisible(true);
        etiquetaContadorApellidos.setVisible(true);
        etiquetaContadorNumeroPersonal.setVisible(true);
        etiquetaContadorCorreo.setVisible(true);

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

        etiquetaContadorNombre.setVisible(false);
        etiquetaContadorApellidos.setVisible(false);
        etiquetaContadorNumeroPersonal.setVisible(false);
        etiquetaContadorCorreo.setVisible(false);

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

        String numeroDePersonalEncontrado = etiquetaNumeroDePersonalEncontrado.getText().trim();
        String correoEncontrado = etiquetaCorreoEncontrado.getText().trim();
        String nombreEditado = campoNombreEditable.getText().trim();
        String apellidosEditado = campoApellidoEditable.getText().trim();
        String numeroDePersonalEditado = campoNumeroDePersonalEditable.getText().trim();
        String correoEditado = campoCorreoEditable.getText().trim();

        AcademicoDAO academicoDAO = new AcademicoDAO();
        CuentaDAO cuentaDAO = new CuentaDAO();

        try {

            String contraseña = cuentaDAO.buscarCuentaPorCorreo(correoEncontrado).getContrasena();
            int idUsuario = cuentaDAO.buscarCuentaPorCorreo(correoEncontrado).getIdUsuario();

            List<String> listaDeCamposVacios =
                    VerificacionUsuario.camposVacios(
                            nombreEditado,
                            apellidosEditado,
                            numeroDePersonalEditado,
                            correoEditado,
                            contraseña
                    );

            if (!listaDeCamposVacios.isEmpty()) {

                String mensajeDelCampoVacio = String.join("\n", listaDeCamposVacios);
                gestorVentanas.mostrarAlerta(
                        "Campos vacíos",
                        "Por favor, complete todos los campos requeridos.",
                        mensajeDelCampoVacio
                );
                return;
            }

            List<String> errores =
                    VerificacionUsuario.validarCampos(
                            nombreEditado,
                            apellidosEditado,
                            numeroDePersonalEditado,
                            correoEditado,
                            contraseña
                    );

            if (!errores.isEmpty()) {

                String mensajeError = String.join("\n", errores);
                gestorVentanas.mostrarAlerta(
                        "Datos inválidos",
                        "Algunos campos contienen datos no válidos.",
                        mensajeError);
                return;
            }

            int numeroPersonal = Integer.parseInt(numeroDePersonalEditado);
            int academicoNoEncontrado = -1;
            String correoNoEncontrado = "N/A";

            if (!numeroDePersonalEncontrado.equals(numeroDePersonalEditado)) {

                if (academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroPersonal).
                        getIdUsuario() != academicoNoEncontrado) {

                    gestorVentanas.mostrarAlerta(
                            "Número de personal invalido",
                            "El número de personal ya está registrado en el sistema.",
                            "Por favor, ingrese un número de personal diferente.");
                    return;
                }
            }

            if(!correoEditado.equals(correoEncontrado)) {

                if (!correoNoEncontrado.equals(cuentaDAO.buscarCuentaPorCorreo(correoEditado)
                        .getCorreoElectronico())) {

                    gestorVentanas.mostrarAlerta(
                            "Correo electrónico invalido",
                            "El correo electrónico ya se encuentra registrado en el sistema.",
                            "Por favor, utilice un correo electrónico diferente.");
                    return;
                }
            }

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            int estadoActivo = 1;

            UsuarioDTO usuarioDTO = new UsuarioDTO(
                    idUsuario,
                    nombreEditado,
                    apellidosEditado,
                    estadoActivo
            );
            CuentaDTO cuentaDTO = new CuentaDTO(
                    correoEditado,
                    contraseña,
                    idUsuario
            );
            AcademicoDTO academicoDTO = new AcademicoDTO(
                    numeroPersonal,
                    idUsuario,
                    nombreEditado,
                    apellidosEditado,
                    estadoActivo
            );

            boolean usuarioModificado = usuarioDAO.modificarUsuario(usuarioDTO);
            boolean academicoModificado = academicoDAO.modificarAcademico(academicoDTO);
            boolean cuentaModificada = cuentaDAO.modificarCuenta(cuentaDTO);

            if (usuarioModificado && academicoModificado && cuentaModificada) {

                LOGGER.info("El académico ha sido modificado correctamente.");
                gestorVentanas.mostrarAlertaConfirmacion(
                        "Éxito",
                        "Los cambios se han realizado correctamente.",
                        ""
                );

            } else {

                LOGGER.warn("No se pudieron guardar completamente los cambios del académico.");
                gestorVentanas.mostrarAlerta(
                        "Error",
                        "No se pudieron guardar todos los cambios.",
                        "Algunos datos no se modificaron correctamente. " +
                                "Por favor, inténtelo nuevamente."
                );
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
            gestorVentanas.mostrarAlerta(
                    "Error de formato",
                    "El número de personal debe ser un valor numérico válido.",
                    "Por favor, revise el número de personal e intente de nuevo."
            );

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error inesperado al registrar académico: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error interno del sistema",
                    "Ocurrió un error al guardar los datos.",
                    "Ocurrió un error dentro del sistema al editar al académico, por favor inténtelo " +
                            "de nuevo más tarde o contacte al administrador."
            );
        }
    }

    private void actualizarDatos() {

        botonSeleccionarAcademicos.setDisable(false);
        botonEliminarAcademico.setVisible(true);
        botonEditar.setVisible(true);
        tablaAcademicos.setDisable(false);
        botonRegistrarAcademico.setDisable(false);

        etiquetaContadorNombre.setVisible(false);
        etiquetaContadorApellidos.setVisible(false);
        etiquetaContadorNumeroPersonal.setVisible(false);
        etiquetaContadorCorreo.setVisible(false);
    }
}