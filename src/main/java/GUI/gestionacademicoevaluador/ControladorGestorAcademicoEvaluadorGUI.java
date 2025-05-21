package GUI.gestionacademicoevaluador;

import GUI.gestionacademico.ControladorRegistroAcademicoGUI;
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

    private static final Logger LOGGER = LogManager.getLogger(ControladorRegistroAcademicoGUI.class);
    private Utilidades UTILIDADES = new Utilidades();

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
                new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getNumeroDePersonal())));

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

            ObservableList<AcademicoEvaluadorDTO> academicos = FXCollections.observableArrayList(academicoDAO.listarAcademicos());
            tablaAcademicos.setItems(academicos);

        } catch (Exception e) {

            LOGGER.error("Error al cargar la lista de academicos: " + e.getMessage());
            UTILIDADES.mostrarAlerta(
                    "Error al cargar académicos",
                    "No se pudo obtener la lista de académicos.",
                    "Por favor, contacte al administrador."
            );
        }
    }

    @FXML
    private void buscarAcademico() {

        String numeroDePersonal = campoNumeroDePersonal.getText().trim();

        if (numeroDePersonal.isEmpty()) {

            UTILIDADES.mostrarAlerta(
                    "Búsqueda incompleta",
                    "No se ingresó un número de personal.",
                    "Por favor, escribe el número de personal en el campo de búsqueda e intenta nuevamente."
            );
            return;
        }

        AcademicoEvaluadorDAO academicoDAO = new AcademicoEvaluadorDAO();
        CuentaDAO cuentaDAO = new CuentaDAO();

        try {

            AcademicoEvaluadorDTO academicoDTO = academicoDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(Integer.parseInt(numeroDePersonal));

            if (academicoDTO.getIdUsuario() != -1) {

                etiquetaNombreEncontrado.setText(academicoDTO.getNombre());
                etiquetaApellidoEncontrado.setText(academicoDTO.getApellido());
                etiquetaNumeroDePersonalEncontrado.setText(numeroDePersonal);

                idAcademico = academicoDTO.getIdUsuario();
                CuentaDTO cuenta = cuentaDAO.buscarCuentaPorID(idAcademico);
                etiquetaCorreoEncontrado.setText(cuenta.getCorreoElectronico());

            } else {

                etiquetaNombreEncontrado.setText("");
                etiquetaApellidoEncontrado.setText("");
                etiquetaNumeroDePersonalEncontrado.setText("");
                etiquetaCorreoEncontrado.setText("");

                UTILIDADES.mostrarAlerta(
                        "Académico no encontrado",
                        "No hay registros que coincidan con el número ingresado.",
                        "Verifica que el número de personal sea correcto o intenta con otro número."
                );
            }

        }  catch (SQLException sqlEx) {

            LOGGER.error("Error SQL al buscar al académico: " + sqlEx.getMessage());
            UTILIDADES.mostrarAlerta(
                "Error del sistema.",
                "Ocurrió un problema al acceder a los datos del académico.",
                "Intenta más tarde o contacte al administrador."
            );

        } catch (IOException ioEx) {

            LOGGER.error("Error de entrada/salida al buscar al académico: " + ioEx.getMessage());
            UTILIDADES.mostrarAlerta(
                "Error de conexión",
                "No se pudo establecer conexión para completar la búsqueda.",
                "Verifique su conexión a Internet o inténtelo nuevamente más tarde."
            );
        }
    }

    private void mostrarDetallesDesdeTabla(AcademicoEvaluadorDTO academicoSeleccionado) {

        if (academicoSeleccionado == null) {
            return;
        }

        AcademicoEvaluadorDAO academicoDAO = new AcademicoEvaluadorDAO();
        CuentaDAO cuentaDAO = new CuentaDAO();

        try {

            AcademicoEvaluadorDTO academicoDTO =
                    academicoDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(academicoSeleccionado.getNumeroDePersonal());

            if (academicoDTO.getIdUsuario() != -1) {

                etiquetaNombreEncontrado.setText(academicoDTO.getNombre());
                etiquetaApellidoEncontrado.setText(academicoDTO.getApellido());
                etiquetaNumeroDePersonalEncontrado.setText(String.valueOf(academicoDTO.getNumeroDePersonal()));

                idAcademico = academicoDTO.getIdUsuario();
                CuentaDTO cuenta = cuentaDAO.buscarCuentaPorID(idAcademico);
                etiquetaCorreoEncontrado.setText(cuenta.getCorreoElectronico());
            }

        } catch (SQLException sqlEx) {

            LOGGER.error("Error SQL al mostrar detalles del académico: " + sqlEx.getMessage());
            UTILIDADES.mostrarAlerta(
                    "Error del sistema",
                    "No se pudieron cargar los datos del académico.",
                    "Es posible que haya un problema con el sistema. Intenta nuevamente más tarde o contacta al administrador."
            );

        } catch (IOException ioEx) {

            LOGGER.error("Error de entrada/salida al mostrar detalles del académico: " + ioEx.getMessage());
            UTILIDADES.mostrarAlerta(
                    "Error de conexión",
                    "No fue posible recuperar los detalles del académico.",
                    "Verifique su conexión a Internet o vuelva a intentarlo en unos minutos."
            );
        }
    }

    @FXML
    private void abrirVentanaRegistrarAcademico() {

    }

    @FXML
    private void eliminarAcademico() {

        String numeroDePersonal = etiquetaNumeroDePersonalEncontrado.getText().trim();

        if (numeroDePersonal.isEmpty()) {

            UTILIDADES.mostrarAlerta(
                    "Advertencia",
                    "No se ha seleccionado un académico.",
                    "Por favor, selecciona un académico desde la tabla antes de intentar eliminarlo."
            );
            return;
        }

        AcademicoEvaluadorDAO academicoDAO = new AcademicoEvaluadorDAO();

        try {

            boolean academicoEliminado = academicoDAO.eliminarAcademicoEvaluadorPorNumeroDePersonal(Integer.parseInt(numeroDePersonal));

            if (academicoEliminado) {

                UTILIDADES.mostrarConfirmacion(
                        "Éxito",
                        "Académico eliminado correctamente.",
                        "La información ha sido actualizada correctamente."
                );
                cargarAcademicos();

            } else {

                UTILIDADES.mostrarAlerta(
                        "Operación fallida",
                        "No fue posible eliminar al académico.",
                        "Es posible que el número de personal no exista o haya ocurrido un error inesperado."
                );
            }

        } catch (SQLException sqlEx) {

        LOGGER.error("Error SQL al eliminar al académico: " + sqlEx.getMessage());
        UTILIDADES.mostrarAlerta(
                "Error del sistema.",
                "No se pudo eliminar al académico.",
                "Contacte al administrador."
        );

        } catch (IOException ioEx) {

            LOGGER.error("Error de entrada/salida al eliminar al académico: " + ioEx.getMessage());
            UTILIDADES.mostrarAlerta(
                "Error de conexión",
                "No se pudo completar la eliminación.",
                "Revise su conexión a internet o inténtelo nuevamente más tarde."
            );
        }
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

        tablaAcademicos.getSelectionModel().getSelectedItems().addListener((ListChangeListener<AcademicoEvaluadorDTO>) cambioSeleccion -> {

            int cantidadSeleccionados = tablaAcademicos.getSelectionModel().getSelectedItems().size();

            if (cantidadSeleccionados > 0) {

                etiquetaNumeroAcademicosSeleccionados.setText("Academicos seleccionados: " + cantidadSeleccionados);

            } else {

                etiquetaNumeroAcademicosSeleccionados.setText(" ");
            }
        });
    }

    @FXML
    private void eliminarAcademicoSeleccionado() {

        ObservableList<AcademicoEvaluadorDTO> academicosSeleccionados = tablaAcademicos.getSelectionModel().getSelectedItems();

        if (academicosSeleccionados == null || academicosSeleccionados.isEmpty()) {

            UTILIDADES.mostrarAlerta(
                    "Error",
                    "No se han seleccionado académicos para eliminar.",
                    "Por favor, seleccione uno o más académicos en la tabla antes de intentar eliminar."
            );
            return;
        }

        boolean errorAlEliminar = false;
        AcademicoEvaluadorDAO academicoDAO = new AcademicoEvaluadorDAO();

        try {

            for (AcademicoEvaluadorDTO academicoSeleccionado : academicosSeleccionados) {

                boolean eliminado = academicoDAO.eliminarAcademicoEvaluadorPorNumeroDePersonal(academicoSeleccionado.getNumeroDePersonal());

                if (!eliminado) {

                    errorAlEliminar = true;
                }
            }

        } catch (SQLException e) {

            LOGGER.error("Error de base de datos al eliminar académico: " + e.getMessage());
            UTILIDADES.mostrarAlerta(
                "Error del sistema",
                "Ocurrió un problema al eliminar los académicos seleccionados.",
                "Por favor, inténtelo nuevamente más tarde o contacte al administrador."
            );
            errorAlEliminar = true;

        } catch (IOException e) {

            LOGGER.error("Error de entrada/salida al eliminar académico: " + e.getMessage());
            UTILIDADES.mostrarAlerta(
                "Error de Sistema",
                "No se pudo completar la operación debido a un error interno.",
                "Por favor, inténtelo nuevamente más tarde."
            );
            errorAlEliminar = true;
        }

        if (errorAlEliminar) {

            UTILIDADES.mostrarAlerta(
                    "Error",
                    "No fue posible eliminar algunos de los académicos seleccionados.",
                    "Revise que los académicos no estén siendo usados en otros registros y vuelva a intentarlo."
            );

        } else {

            UTILIDADES.mostrarAlerta(
                    "Éxito",
                    "Los académicos seleccionados han sido eliminados correctamente.",
                    ""
            );
        }

        cargarAcademicos();
        eliminarAcademicosSeleccionadoConcluido();
    }

    private void eliminarAcademicosSeleccionadoConcluido() {

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

        String numeroDePersonalEncontrado = etiquetaNumeroDePersonalEncontrado.getText().trim();
        String correoEncontrado = etiquetaCorreoEncontrado.getText().trim();
        String nombre = campoNombreEditable.getText().trim();
        String apellidos = campoApellidoEditable.getText().trim();
        String numeroDePersonal = campoNumeroDePersonalEditable.getText().trim();
        String correo = campoCorreoEditable.getText().trim();

        VerificacionUsuario verificacionUsuario = new VerificacionUsuario();
        AcademicoEvaluadorDAO academicoDAO = new AcademicoEvaluadorDAO();
        CuentaDAO cuentaDAO = new CuentaDAO();

        try {

            String contraseña = cuentaDAO.buscarCuentaPorCorreo(correoEncontrado).getContrasena();
            int idUsuario = cuentaDAO.buscarCuentaPorCorreo(correoEncontrado).getIdUsuario();

            List<String> listaDeCamposVacios = VerificacionUsuario.camposVacios(nombre, apellidos, numeroDePersonalEncontrado, correo, contraseña);

            if (!listaDeCamposVacios.isEmpty()) {

                String camposVacios = String.join("\n", listaDeCamposVacios);
                UTILIDADES.mostrarAlerta(
                        "Campos vacíos",
                        "Por favor, complete todos los campos requeridos.",
                        camposVacios
                );
                return;
            }


            List<String> errores = VerificacionUsuario.validarCampos(nombre, apellidos, numeroDePersonal, correo, contraseña);

            if (!errores.isEmpty()) {

                String mensajeError = String.join("\n", errores);
                UTILIDADES.mostrarAlerta(
                        "Datos inválidos",
                        "Algunos campos contienen datos no válidos.",
                        mensajeError
                );
                return;
            }

            int numeroPersonal = Integer.parseInt(numeroDePersonal);

            if (!numeroDePersonalEncontrado.equals(numeroDePersonal)) {

                if (academicoDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(numeroPersonal).getIdUsuario() != -1) {

                    UTILIDADES.mostrarAlerta(
                            "Número de personal duplicado",
                            "El número de personal ya está registrado en el sistema.",
                            "Por favor, ingrese un número de personal diferente."
                    );
                    return;
                }
            }

            if (!correo.equals(correoEncontrado)) {

                if (!"N/A".equals(cuentaDAO.buscarCuentaPorCorreo(correo).getCorreoElectronico())) {

                    UTILIDADES.mostrarAlerta(
                            "Correo electrónico duplicado",
                            "El correo electrónico ya está registrado en el sistema.",
                            "Por favor, utilice un correo electrónico diferente."
                    );
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

                LOGGER.info("El académico ha sido modificado correctamente.");
                UTILIDADES.mostrarConfirmacion(
                        "Éxito",
                        "Los cambios se han guardado correctamente.",
                        ""
                );

            } else {

                LOGGER.warn("No se pudieron guardar completamente los cambios del académico.");
                UTILIDADES.mostrarAlerta(
                        "Advertencia",
                        "No se pudieron guardar todos los cambios.",
                        "Algunos datos no se modificaron correctamente. Por favor, inténtelo nuevamente."
                );
            }

            cancelarEdicion();
            cargarAcademicos();
            actualizarDatosGuardados();

        } catch (NumberFormatException e) {

            LOGGER.error("Error en formato numérico: " + e.getMessage());
            UTILIDADES.mostrarAlerta(
                    "Error de formato",
                    "El número de personal debe ser un valor numérico válido.",
                    "Por favor, revise el número de personal e intente de nuevo."
            );

        } catch (SQLException e) {

            LOGGER.error("Error de base de datos al guardar cambios: " + e.getMessage());
            UTILIDADES.mostrarAlerta(
                    "Error del sistema",
                    "Ocurrió un problema al guardar los cambios en el sistema.",
                    "Por favor, inténtelo nuevamente más tarde o contacte al soporte técnico."
            );

        } catch (IOException e) {

            LOGGER.error("Error de entrada/salida al guardar cambios: " + e.getMessage());
            UTILIDADES.mostrarAlerta(
                    "Error de Sistema",
                    "No se pudo completar la operación debido a un error interno.",
                    "Por favor, inténtelo nuevamente más tarde."
            );
        }
    }

    private void actualizarDatosGuardados() {

        botonSeleccionarAcademicos.setDisable(false);
        botonEliminarAcademico.setVisible(true);
        botonEditar.setVisible(true);
        tablaAcademicos.setDisable(false);
        botonRegistrarAcademico.setDisable(false);
    }
}
