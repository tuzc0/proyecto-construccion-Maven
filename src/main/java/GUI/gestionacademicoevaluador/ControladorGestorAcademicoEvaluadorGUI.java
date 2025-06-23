package GUI.gestionacademicoevaluador;

import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logica.DAOs.AcademicoDAO;
import logica.DAOs.AcademicoEvaluadorDAO;
import logica.DAOs.CuentaDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.AcademicoDTO;
import logica.DTOs.AcademicoEvaluadorDTO;
import logica.DTOs.CuentaDTO;
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

public class ControladorGestorAcademicoEvaluadorGUI {

    private static final Logger LOGGER =
            LogManager.getLogger(ControladorGestorAcademicoEvaluadorGUI.class);

    @FXML private TextField campoNumeroDePersonal;
    @FXML private Button botonBuscar;
    @FXML private Button botonEliminarSeleccionado;
    @FXML private TableView<AcademicoEvaluadorDTO> tablaAcademicos;
    @FXML private TableColumn<AcademicoEvaluadorDTO, String> columnaNumeroDePersonal;
    @FXML private TableColumn<AcademicoEvaluadorDTO, String> columnaNombres;
    @FXML private TableColumn<AcademicoEvaluadorDTO, String> columnaApellidos;
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


    private int idAcademico = 0;

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
                campoNumeroDePersonalEditable, etiquetaContadorNumeroPersonal,
                MAX_CARACTERES_NUMERO_PERSONAL);
        verficacionGeneral.contadorCaracteresTextField(
                campoCorreoEditable, etiquetaContadorCorreo, MAX_CARACTERES_CORREO);

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
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));

        columnaApellidos.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));

        cargarAcademicos();
        tablaAcademicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tablaAcademicos.getSelectionModel().selectedItemProperty().addListener(
                (observadorAcademico, academicoAnterior,
                 academicoSeleccionado) -> {

                    mostrarDetallesDesdeTabla(academicoSeleccionado);
                    botonEliminarSeleccionado.setDisable(academicoSeleccionado == null);
                }
        );
    }

    private void cargarAcademicos() {

        AcademicoEvaluadorDAO academicoDAO = new AcademicoEvaluadorDAO();

        try {

            ObservableList<AcademicoEvaluadorDTO> academicos =
                    FXCollections.observableArrayList(academicoDAO.listarAcademicos());
            tablaAcademicos.setItems(academicos);

        } catch (SQLException e) {

           manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

           manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error inesperado al cargar académicos evaluadores: " + e);
            utilidades.mostrarAlerta(
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

            utilidades.mostrarAlerta(
                    "Búsqueda incompleta",
                    "No se ingresó un número de personal.",
                    "Por favor, escribe el número de personal en el campo de " +
                            "búsqueda e intenta nuevamente."
            );
            return;
        }

        AcademicoEvaluadorDAO academicoDAO = new AcademicoEvaluadorDAO();
        CuentaDAO cuentaDAO = new CuentaDAO();

        try {

            AcademicoEvaluadorDTO academicoEvaluadorDTO = academicoDAO.
                    buscarAcademicoEvaluadorPorNumeroDePersonal(Integer.parseInt(numeroDePersonal));
            int academicoEncontrado = -1;
            int academicoInactivo = 0;

            if (academicoEvaluadorDTO.getIdUsuario() != academicoEncontrado) {

                if (academicoEvaluadorDTO.getEstado() != academicoInactivo) {

                    etiquetaNombreEncontrado.setText(academicoEvaluadorDTO.getNombre());
                    etiquetaApellidoEncontrado.setText(academicoEvaluadorDTO.getApellido());
                    etiquetaNumeroDePersonalEncontrado.setText(numeroDePersonal);

                    idAcademico = academicoEvaluadorDTO.getIdUsuario();
                    CuentaDTO cuenta = cuentaDAO.buscarCuentaPorID(idAcademico);
                    etiquetaCorreoEncontrado.setText(cuenta.getCorreoElectronico());
                }

            } else {

                etiquetaNombreEncontrado.setText("");
                etiquetaApellidoEncontrado.setText("");
                etiquetaNumeroDePersonalEncontrado.setText("");
                etiquetaCorreoEncontrado.setText("");

                utilidades.mostrarAlerta(
                        "Académico no encontrado",
                        "No hay registros que coincidan con el número ingresado.",
                        "Verifica que el número de personal sea correcto o intenta con otro número."
                );
            }

        }  catch (SQLException e) {

           manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error inesperado al buscar académico evaluador: " + e);
            utilidades.mostrarAlerta(
                    "Error interno del sistema",
                    "Ocurrió un error al buscar el académico.",
                    "Ocurrió un error dentro del sistema, por favor inténtelo de nuevo más tarde " +
                            "o contacte al administrador."
            );
        }
    }

    private void mostrarDetallesDesdeTabla(AcademicoEvaluadorDTO academicoSeleccionado) {

        if (academicoSeleccionado == null) {
            return;
        }

        AcademicoEvaluadorDAO academicoEvaluadorDAO = new AcademicoEvaluadorDAO();
        CuentaDAO cuentaDAO = new CuentaDAO();

        try {

            AcademicoEvaluadorDTO academicoEvaluadorDTO =
                    academicoEvaluadorDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(
                            academicoSeleccionado.getNumeroDePersonal());
            int academicoNoEncontrado = -1;

            if (academicoEvaluadorDTO.getIdUsuario() != academicoNoEncontrado) {

                etiquetaNombreEncontrado.setText(academicoEvaluadorDTO.getNombre());
                etiquetaApellidoEncontrado.setText(academicoEvaluadorDTO.getApellido());
                etiquetaNumeroDePersonalEncontrado
                        .setText(String.valueOf(academicoEvaluadorDTO.getNumeroDePersonal()));

                idAcademico = academicoEvaluadorDTO.getIdUsuario();
                CuentaDTO cuentaDTO = cuentaDAO.buscarCuentaPorID(idAcademico);
                etiquetaCorreoEncontrado.setText(cuentaDTO.getCorreoElectronico());
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error inesperado al mostrar detalles del académico: " + e);
            utilidades.mostrarAlerta(
                    "Error interno del sistema",
                    "Ocurrió un error al mostrar los detalles del académico.",
                    "Ocurrió un error dentro del sistema, por favor inténtelo de nuevo más tarde " +
                            "o contacte al administrador."
            );
        }
    }

    @FXML
    private void abrirVentanaRegistrarAcademicoEvaluador() {

        gestorVentanas.abrirVentana(
                "/RegistroAcademicoEvaluadorGUI.fxml",
                "Registro de Académico Evaluador",
                (Stage) botonRegistrarAcademico.getScene().getWindow()
        );

        cargarAcademicos();
    }

    private boolean eliminarAcademico(int numeroDePersonal) {
        AcademicoEvaluadorDAO academicoEvaluadorDAO = new AcademicoEvaluadorDAO();
        boolean estadoEliminacion = false;

        try {

            boolean academicoEliminado = academicoEvaluadorDAO.eliminarAcademicoEvaluadorPorNumeroDePersonal(numeroDePersonal);

            if (academicoEliminado) {

                estadoEliminacion = true;
                LOGGER.info("Académico con número de personal " + numeroDePersonal + " eliminado correctamente.");

            } else {

                LOGGER.warn("No se pudo eliminar al académico con número de personal: " + numeroDePersonal);
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {
            LOGGER.error("Error inesperado al eliminar académico: " + e);
            utilidades.mostrarAlerta(
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
                .addListener((ListChangeListener<AcademicoEvaluadorDTO>)
                cambioSeleccion -> {

            int cantidadSeleccionados = tablaAcademicos.getSelectionModel().getSelectedItems().size();
            int sinAcademicosSeleccionados = 0;

            if (cantidadSeleccionados > sinAcademicosSeleccionados) {

                etiquetaNumeroAcademicosSeleccionados.setText("Académicos seleccionados: " +
                        cantidadSeleccionados);

            } else {

                etiquetaNumeroAcademicosSeleccionados.setText(" ");
            }
        });
    }

    @FXML
    private void eliminarAcademicoSeleccionado() {

        ObservableList<AcademicoEvaluadorDTO> academicosSeleccionados =
                tablaAcademicos.getSelectionModel().getSelectedItems();

        if (academicosSeleccionados == null || academicosSeleccionados.isEmpty()) {

            utilidades.mostrarAlerta(
                    "Error",
                    "No se han seleccionado académicos para eliminar.",
                    "Por favor, seleccione uno o más académicos en la tabla antes de intentar eliminar.");
            return;
        }

        List<AcademicoEvaluadorDTO> copiaAcademicosDTO = new ArrayList<>(academicosSeleccionados);

        gestorVentanas.mostrarAlertaConfirmacion(
                "Confirmar eliminación",
                "¿Está seguro que desea eliminar los académicos seleccionados?",
                "Se eliminarán " + academicosSeleccionados.size() + " académicos. " +
                        "Esta acción no se puede deshacer.",
                () -> {

                    boolean errorAlEliminar = false;

                    for (AcademicoEvaluadorDTO academicoSeleccionado : academicosSeleccionados) {

                        if (!eliminarAcademico(academicoSeleccionado.getNumeroDePersonal())) {
                            errorAlEliminar = true;
                        }
                    }

                    if (errorAlEliminar) {

                        utilidades.mostrarAlerta(
                                "Error",
                                "No fue posible eliminar a algunos de los académicos seleccionados.",
                                "Inténtelo más tarde o contacte al administrador.");

                    } else {

                        utilidades.mostrarAlerta(
                                "Éxito",
                                "Los académicos seleccionados han sido eliminados correctamente.",
                                "");
                    }

                    cargarAcademicos();
                    eliminarAcademicosSeleccionadoConcluido();
                },
                () -> {

                    tablaAcademicos.getSelectionModel().clearSelection();

                    for (AcademicoEvaluadorDTO academicoDTO : copiaAcademicosDTO) {
                        tablaAcademicos.getSelectionModel().select(academicoDTO);
                    }

                    utilidades.mostrarAlerta(
                            "Operación cancelada",
                            "La eliminación fue cancelada",
                            "Los académicos no han sido eliminados.");
                }
        );
    }

    private void eliminarAcademicosSeleccionadoConcluido() {

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
        String nombre = campoNombreEditable.getText().trim();
        String apellidos = campoApellidoEditable.getText().trim();
        String numeroDePersonalTexto = campoNumeroDePersonalEditable.getText().trim();
        String correo = campoCorreoEditable.getText().trim();

        AcademicoEvaluadorDAO academicoDAO = new AcademicoEvaluadorDAO();
        CuentaDAO cuentaDAO = new CuentaDAO();

        try {

            String contraseña = cuentaDAO.buscarCuentaPorCorreo(correoEncontrado).getContrasena();
            int idUsuario = cuentaDAO.buscarCuentaPorCorreo(correoEncontrado).getIdUsuario();

            List<String> listaDeCamposVacios =
                    VerificacionUsuario.camposVacios(
                            nombre,
                            apellidos,
                            numeroDePersonalEncontrado,
                            correo,
                            contraseña
                    );

            if (!listaDeCamposVacios.isEmpty()) {

                String camposVacios = String.join("\n", listaDeCamposVacios);
                utilidades.mostrarAlerta(
                        "Campos vacíos",
                        "Por favor, complete todos los campos requeridos.",
                        camposVacios
                );
                return;
            }

            List<String> listaDeErrores =
                    VerificacionUsuario.validarCampos(
                            nombre,
                            apellidos,
                            numeroDePersonalTexto,
                            correo,
                            contraseña
                    );

            if (!listaDeErrores.isEmpty()) {

                String mensajeError = String.join("\n", listaDeErrores);
                utilidades.mostrarAlerta(
                        "Datos inválidos",
                        "Algunos campos contienen datos no válidos.",
                        mensajeError
                );
                return;
            }

            int numeroDePersonal = Integer.parseInt(numeroDePersonalTexto);
            int academicoNoEncontrado = -1;
            String correoNoEncontrado = "N/A";
            AcademicoEvaluadorDTO academicoEncontrado =
                    academicoDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(numeroDePersonal);
            CuentaDTO cuentaEncontrada = cuentaDAO.buscarCuentaPorCorreo(correo);

            if (!numeroDePersonalEncontrado.equals(numeroDePersonalTexto)) {

                if (academicoEncontrado.getNumeroDePersonal() != academicoNoEncontrado) {

                    utilidades.mostrarAlerta(
                            "Número de personal duplicado",
                            "El número de personal ya está registrado en el sistema.",
                            "Por favor, ingrese un número de personal diferente."
                    );
                    return;
                }
            }

            if (!correo.equals(correoEncontrado)) {

                if (!correoNoEncontrado.equals(cuentaEncontrada.getCorreoElectronico())) {

                    utilidades.mostrarAlerta(
                            "Correo electrónico duplicado",
                            "El correo electrónico ya está registrado en el sistema.",
                            "Por favor, utilice un correo electrónico diferente."
                    );
                    return;
                }
            }

            int estadoActivo = 1;

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            UsuarioDTO usuarioDTO = new UsuarioDTO(
                    idUsuario,
                    nombre,
                    apellidos,
                    estadoActivo
            );
            AcademicoEvaluadorDTO academicoDTO = new AcademicoEvaluadorDTO(
                    numeroDePersonal,
                    idUsuario,
                    nombre,
                    apellidos,
                    estadoActivo
            );
            CuentaDTO cuentaDTO = new CuentaDTO(
                    correo,
                    contraseña,
                    idUsuario
            );

            boolean usuarioModificado = usuarioDAO.modificarUsuario(usuarioDTO);
            boolean academicoModificado = academicoDAO.modificarAcademicoEvaluador(academicoDTO);
            boolean cuentaModificada = cuentaDAO.modificarCuenta(cuentaDTO);

            if (usuarioModificado && academicoModificado && cuentaModificada) {

                LOGGER.info("El académico ha sido modificado correctamente.");
                gestorVentanas.mostrarAlertaConfirmacion(
                        "Éxito",
                        "Los cambios se han guardado correctamente.",
                        ""
                );

            } else {

                LOGGER.warn("No se pudieron guardar completamente los cambios del académico.");
                utilidades.mostrarAlerta(
                        "Error",
                        "No se pudieron guardar todos los cambios realizados al académico.",
                        "Algunos datos no se modificaron correctamente. " +
                                "Por favor, inténtelo nuevamente."
                );
            }

            cancelarEdicion();
            cargarAcademicos();
            actualizarDatosGuardados();

        } catch (NumberFormatException e) {

            LOGGER.error("Error en formato numérico: " + e);
            utilidades.mostrarAlerta(
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
            utilidades.mostrarAlerta(
                    "Error interno del sistema",
                    "Ocurrió un error al guardar los datos.",
                    "Ocurrió un error dentro del sistema al editar al académico, por favor inténtelo " +
                            "de nuevo más tarde o contacte al administrador."
            );
        }
    }

    private void actualizarDatosGuardados() {

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
