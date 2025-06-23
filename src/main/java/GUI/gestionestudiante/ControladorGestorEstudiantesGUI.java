package GUI.gestionestudiante;

import GUI.ControladorObjetoEvaluacion;
import logica.ManejadorExcepciones;
import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logica.DAOs.CuentaDAO;
import logica.DAOs.EstudianteDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.CuentaDTO;
import logica.DTOs.EstudianteDTO;
import logica.DTOs.UsuarioDTO;
import logica.VerificacionUsuario;
import logica.interfaces.IGestorAlertas;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorGestorEstudiantesGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorObjetoEvaluacion.class);

    @FXML
    private TextField campoMatricula;

    @FXML
    private Button botonBuscar;

    @FXML
    private Button botonEliminarSeleccionado;

    @FXML
    private TableView<EstudianteDTO> tablaEstudiantes;

    @FXML
    private TableColumn<EstudianteDTO, String> columnaMatricula;

    @FXML
    private TableColumn<EstudianteDTO, String> columnaNombres;

    @FXML
    private TableColumn<EstudianteDTO, String> columnaApellidos;

    @FXML
    private Label etiquetaNombreEncontrado;

    @FXML
    private Label etiquetaApellidoEncontrado;

    @FXML
    private Label etiquetaMatriculaEncontrada;

    @FXML
    private Label etiquetaCorreoEncontrado;

    @FXML
    private TextField campoNombreEditable;

    @FXML
    private TextField campoApellidoEditable;

    @FXML
    private TextField campoMatriculaEditable;

    @FXML
    private TextField campoCorreoEditable;

    @FXML
    private Button botonEditar;

    @FXML
    private Button botonGuardar;

    @FXML
    private Button botonCancelar;

    @FXML
    private Button botonCancelarSeleccion;

    @FXML
    private Label etiquetaNumeroEstudiantesSeleccionados;

    @FXML
    private Button botonEliminarEstudiante;

    @FXML
    private Button botonSeleccionarEstudiantes;

    @FXML
    private Button botonRegistrarEstudiante;

    @FXML
    private Label etiquetaContadorNombre;

    @FXML
    private Label etiquetaContadorApellido;

    @FXML
    private Label etiquetaContadorMatricula;

    @FXML
    private Label etiquetaContadorCorreo;

    private int idEstudiante = 0;

    private int NRC;

    VerificicacionGeneral verificicacionGeneral = new VerificicacionGeneral();

    final int MAX_CARACTERES_NOMBRE_Y_APELLIDOS = 50;

    final int MAX_CARACTERES_CORREO = 100;

    final int MAX_CARACTERES_MATRICULA= 9;

    Utilidades utilidadesVentana = new Utilidades();
    IGestorAlertas mensajeDeAlerta = new Utilidades();

    ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(mensajeDeAlerta, logger);

    @FXML
    public void initialize() {

        verificicacionGeneral.contadorCaracteresTextField(campoNombreEditable, etiquetaContadorNombre, MAX_CARACTERES_NOMBRE_Y_APELLIDOS);
        verificicacionGeneral.contadorCaracteresTextField(campoApellidoEditable, etiquetaContadorApellido, MAX_CARACTERES_NOMBRE_Y_APELLIDOS);
        verificicacionGeneral.contadorCaracteresTextField(campoMatriculaEditable, etiquetaContadorMatricula, MAX_CARACTERES_MATRICULA);
        verificicacionGeneral.contadorCaracteresTextField(campoCorreoEditable, etiquetaContadorCorreo, MAX_CARACTERES_CORREO);

        botonSeleccionarEstudiantes.setCursor(Cursor.HAND);

        columnaMatricula.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMatricula()));
        columnaNombres.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        columnaApellidos.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));

        cargarEstudiantes();
        tablaEstudiantes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tablaEstudiantes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {

            mostrarDetallesDesdeTabla(newSel);
            botonEliminarSeleccionado.setDisable(newSel == null);
        });

    }

    private void cargarEstudiantes() {


        AuxiliarGestionEstudiante auxiliarGestionEstudiantes = new AuxiliarGestionEstudiante();
        NRC = auxiliarGestionEstudiantes.obtenerNRC();


        try {

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            ObservableList<EstudianteDTO> estudiantes = FXCollections.observableArrayList(estudianteDAO.obtenerEstudiantesActivosPorNRC(NRC));
            tablaEstudiantes.setItems(estudiantes);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al cargar los estudiantes: " + e);
            utilidadesVentana.mostrarAlerta(
                    "Error inesperado",
                    "No se pudieron cargar los estudiantes.",
                    "Por favor, intente nuevamente más tarde."
            );
        }
    }


    @FXML
    private void buscarEstudiante() {

        String matricula = campoMatricula.getText().trim();

        if (matricula.isEmpty()) {

            utilidadesVentana.mostrarAlerta(
                    "Campos vacíos",
                    "Por favor, ingrese una matrícula para buscar.",
                    "El campo de matrícula no puede estar vacío."
            );
        }

        try {

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            EstudianteDTO estudiante = estudianteDAO.buscarEstudiantePorMatricula(matricula);

            CuentaDAO cuentaDAO = new CuentaDAO();
            if (estudiante.getIdUsuario() != -1) {

                etiquetaNombreEncontrado.setText(estudiante.getNombre());
                etiquetaApellidoEncontrado.setText(estudiante.getApellido());
                etiquetaMatriculaEncontrada.setText(estudiante.getMatricula());

                idEstudiante = estudiante.getIdUsuario();
                CuentaDTO cuenta = cuentaDAO.buscarCuentaPorID(idEstudiante);
                etiquetaCorreoEncontrado.setText(cuenta.getCorreoElectronico());
            } else {

                etiquetaNombreEncontrado.setText("No hay estudiante con esa matricula");
                etiquetaApellidoEncontrado.setText("");
                etiquetaMatriculaEncontrada.setText("");
                etiquetaCorreoEncontrado.setText("");
            }

        } catch (SQLException e){

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al buscar el estudiante: " + e);
            utilidadesVentana.mostrarAlerta(
                    "Error inesperado",
                    "No se pudo buscar el estudiante.",
                    "Por favor, intente nuevamente más tarde."
            );
        }
    }

    private void mostrarDetallesDesdeTabla(EstudianteDTO estudianteSeleccionado) {

        if (estudianteSeleccionado == null) return;

        try {

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            EstudianteDTO estudiante = estudianteDAO.buscarEstudiantePorMatricula(estudianteSeleccionado.getMatricula());
            CuentaDAO cuentaDAO = new CuentaDAO();

            if (estudiante.getIdUsuario() != -1) {

                etiquetaNombreEncontrado.setText(estudiante.getNombre());
                etiquetaApellidoEncontrado.setText(estudiante.getApellido());
                etiquetaMatriculaEncontrada.setText(estudiante.getMatricula());

                idEstudiante = estudiante.getIdUsuario();
                CuentaDTO cuenta = cuentaDAO.buscarCuentaPorID(idEstudiante);
                etiquetaCorreoEncontrado.setText(cuenta.getCorreoElectronico());
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al mostrar los detalles del estudiante: " + e);
            utilidadesVentana.mostrarAlerta(
                    "Error inesperado",
                    "No se pudieron mostrar los detalles del estudiante.",
                    "Por favor, intente nuevamente más tarde."
            );
        }
    }

    @FXML
    private void abrirVentanaRegistrarEstudiante() {

        Utilidades utilidades = new Utilidades();

        try {

            if (NRC == -1){

                utilidades.mostrarAlerta(
                        "NRC no encontrado",
                        "No se ha encontrado un NRC asociado a este usuario.",
                        "Por favor, asegúrese de que el NRC esté configurado correctamente en la base de datos."
                );
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistroEstudianteGUI.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.setOnHiding(event -> cargarEstudiantes());

            stage.show();

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
        }
    }


    private void eliminarEstudiante() {

        Utilidades utilidades = new Utilidades();
        String matricula = etiquetaMatriculaEncontrada.getText().trim();


        if (matricula.isEmpty()) {

            utilidades.mostrarAlerta(
                    "Campos vacíos",
                    "Por favor, ingrese una matrícula para eliminar.",
                    "El campo de matrícula no puede estar vacío."
            );
            return;
        }

        try {

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            boolean eliminado = estudianteDAO.eliminarEstudiantePorMatricula(0, matricula);

            if (eliminado) {

                utilidades.mostrarAlerta(
                        "Estudiante eliminado",
                        "El estudiante ha sido eliminado correctamente.",
                        "Puede continuar con otras acciones."
                );
                cargarEstudiantes();

            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al eliminar el estudiante: " + e);
            utilidades.mostrarAlerta(
                    "Error inesperado",
                    "No se pudo eliminar el estudiante.",
                    "Por favor, intente nuevamente más tarde."
            );
        }
    }

    @FXML
    private void confirmarEliminacionEstudiante() {

        utilidadesVentana.mostrarAlertaConfirmacion(
                "Confirmar eliminación",
                "¿Está seguro que desea eliminar el estudiante seleccionado?",
                "Se eliminará el estudiante seleccionado. Esta acción no se puede deshacer.",
                () -> {
                    eliminarEstudiante();
                },
                () -> {
                    utilidadesVentana.mostrarAlerta("Cancelado",
                            "Eliminación cancelada",
                            "No se ha eliminado ningún estudiante.");
                }
        );
    }

    @FXML
    private void activarModoSeleccion() {

        botonEliminarSeleccionado.setManaged(true);
        botonEliminarSeleccionado.setVisible(true);
        botonCancelarSeleccion.setVisible(true);
        etiquetaNumeroEstudiantesSeleccionados.setVisible(true);
        botonEditar.setDisable(true);
        botonEliminarEstudiante.setDisable(true);
        botonRegistrarEstudiante.setDisable(true);

        tablaEstudiantes.getSelectionModel().getSelectedItems().addListener((ListChangeListener<EstudianteDTO>)
                cambioEstudiante -> {

            int cantidadSeleccionados = tablaEstudiantes.getSelectionModel().getSelectedItems().size();
            if (cantidadSeleccionados > 0) {

                etiquetaNumeroEstudiantesSeleccionados.setText("Estudiantes seleccionados: " + cantidadSeleccionados);
            } else {

                etiquetaNumeroEstudiantesSeleccionados.setText(" ");
            }
        });
    }

    @FXML
    private void confirmacionEliminarEstudianteSeleccionado() {

        utilidadesVentana.mostrarAlertaConfirmacion(
                "Confirmar eliminación",
                "¿Está seguro que desea eliminar los estudiantes seleccionados?",
                "Se eliminarán los estudiantes seleccionados. Esta acción no se puede deshacer.",
                () -> {
                    eliminarEstudianteSeleccionado();
                },
                () -> {
                    utilidadesVentana.mostrarAlerta("Cancelado",
                            "Eliminación cancelada",
                            "No se ha eliminado ningún estudiante.");
                }
        );
    }


    private void eliminarEstudianteSeleccionado() {

        Utilidades utilidades = new Utilidades();
        ObservableList<EstudianteDTO> seleccionados = tablaEstudiantes.getSelectionModel().getSelectedItems();

        if (seleccionados == null || seleccionados.isEmpty()) {

            utilidades.mostrarAlerta(
                    "No hay estudiantes seleccionados",
                    "Por favor, seleccione al menos un estudiante para eliminar.",
                    "Seleccione uno o más estudiantes de la tabla."
            );
            return;
        }
        boolean error = false;

        try{

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            for (EstudianteDTO estudiante : seleccionados) {

                boolean eliminado =
                        estudianteDAO.eliminarEstudiantePorMatricula(0, estudiante.getMatricula());

                if (!eliminado) {

                    error = true;
                }

            }
        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            error = true;

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            error = true;

        } catch (Exception e) {

            logger.error("Error inesperado al eliminar estudiantes: " + e);
            utilidades.mostrarAlerta(
                    "Error inesperado",
                    "No se pudieron eliminar los estudiantes seleccionados.",
                    "Por favor, intente nuevamente más tarde."
            );
            error = true;
        }

        if (error) {

            utilidades.mostrarAlerta(
                    "Error al eliminar estudiantes",
                    "No se pudieron eliminar todos los estudiantes seleccionados.",
                    "Por favor, intente nuevamente."
            );
        } else {

            utilidades.mostrarAlerta(
                    "Estudiantes eliminados",
                    "Los estudiantes seleccionados han sido eliminados correctamente.",
                    "Puede continuar con otras acciones."
            );
        }

        cargarEstudiantes();
        botonEliminarSeleccionado.setDisable(true);
        botonEliminarSeleccionado.setManaged(false);
        botonEliminarSeleccionado.setVisible(false);
        botonCancelarSeleccion.setVisible(false);
        botonEditar.setDisable(false);
        botonEliminarEstudiante.setDisable(false);
        botonRegistrarEstudiante.setDisable(false);

        tablaEstudiantes.getSelectionModel().clearSelection();
    }

    @FXML
    private void cancelarSeleccionEstudiante() {

        botonEliminarSeleccionado.setManaged(false);
        botonEliminarSeleccionado.setVisible(false);
        botonCancelarSeleccion.setVisible(false);
        etiquetaNumeroEstudiantesSeleccionados.setVisible(false);
        botonEliminarEstudiante.setDisable(false);
        botonEditar.setDisable(false);
        botonRegistrarEstudiante.setDisable(false);

        tablaEstudiantes.getSelectionModel().clearSelection();
        etiquetaNumeroEstudiantesSeleccionados.setText(" ");

    }

    @FXML
    private void editarEstudiante() {

        if (etiquetaMatriculaEncontrada.getText().trim().isEmpty()) {
            utilidadesVentana.mostrarAlerta("No se ha seleccionado un estudiante",
                    "Por favor, busque o seleccione un estudiante antes de editar.",
                    "El campo de matrícula no puede estar vacío.");
            return;
        }

        campoNombreEditable.setText(etiquetaNombreEncontrado.getText());
        campoApellidoEditable.setText(etiquetaApellidoEncontrado.getText());
        campoMatriculaEditable.setText(etiquetaMatriculaEncontrada.getText());
        campoCorreoEditable.setText(etiquetaCorreoEncontrado.getText());

        etiquetaContadorApellido.setVisible(true);
        etiquetaContadorNombre.setVisible(true);
        etiquetaContadorMatricula.setVisible(true);
        etiquetaContadorCorreo.setVisible(true);

        campoNombreEditable.setVisible(true);
        campoApellidoEditable.setVisible(true);
        campoMatriculaEditable.setVisible(true);
        campoCorreoEditable.setVisible(true);

        etiquetaNombreEncontrado.setVisible(false);
        etiquetaApellidoEncontrado.setVisible(false);
        etiquetaMatriculaEncontrada.setVisible(false);
        etiquetaCorreoEncontrado.setVisible(false);


        botonGuardar.setVisible(true);
        botonCancelar.setVisible(true);
        botonSeleccionarEstudiantes.setDisable(true);

        botonEditar.setVisible(false);
        botonEliminarEstudiante.setVisible(false);
        tablaEstudiantes.setDisable(true);
        botonRegistrarEstudiante.setDisable(true);

    }

    @FXML
    private void cancelarEdicion() {

        campoNombreEditable.setVisible(false);
        campoApellidoEditable.setVisible(false);
        campoMatriculaEditable.setVisible(false);
        campoCorreoEditable.setVisible(false);

        etiquetaNombreEncontrado.setVisible(true);
        etiquetaApellidoEncontrado.setVisible(true);
        etiquetaMatriculaEncontrada.setVisible(true);
        etiquetaCorreoEncontrado.setVisible(true);

        etiquetaContadorApellido.setVisible(false);
        etiquetaContadorNombre.setVisible(false);
        etiquetaContadorMatricula.setVisible(false);
        etiquetaContadorCorreo.setVisible(false);

        botonGuardar.setVisible(false);
        botonCancelar.setVisible(false);
        botonSeleccionarEstudiantes.setDisable(false);

        botonEditar.setVisible(true);
        botonEliminarEstudiante.setVisible(true);
        tablaEstudiantes.setDisable(false);
        botonRegistrarEstudiante.setDisable(false);
    }

    @FXML
    private void guardarCambios() {

        String matriculaEcontrada = etiquetaMatriculaEncontrada.getText().trim();
        String correoEncontrado = etiquetaCorreoEncontrado.getText().trim();

        String nombre = campoNombreEditable.getText().trim();
        String apellidos = campoApellidoEditable.getText().trim();
        String matricula = campoMatriculaEditable.getText().trim();
        String correo = campoCorreoEditable.getText().trim();

        Utilidades utilidades = new Utilidades();
        VerificacionUsuario verificacionUsuario = new VerificacionUsuario();

        try {

            List<String> errores = verificacionUsuario.validarCamposRegistroEstudiante(
                    nombre, apellidos, matricula, correo, "", "");

            if (!errores.isEmpty()) {
                utilidades.mostrarAlerta("Campos incompletos",
                        "Por favor, complete todos los campos requeridos.",
                        String.join("\n", errores));
                return;
            }

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            CuentaDAO cuentaDAO = new CuentaDAO();
            UsuarioDAO usuarioDAO = new UsuarioDAO();

            EstudianteDTO otroEstudiante = estudianteDAO.buscarEstudiantePorMatricula(matricula);
            idEstudiante = otroEstudiante.getIdUsuario();

            cuentaDAO.buscarCuentaPorID(idEstudiante);
            String contrasena = cuentaDAO.buscarCuentaPorID(idEstudiante).getContrasena();

            if (!matriculaEcontrada.equals(matricula)) {

                if (otroEstudiante.getMatricula() != "N/A"){

                    utilidades.mostrarAlerta(
                            "Matricula ya registrada",
                            "La matrícula ingresada ya está registrada.",
                            "Por favor, ingrese una matrícula diferente."
                    );

                }
            }

            if (!correoEncontrado.equals(correo)) {

                if (cuentaDAO.buscarCuentaPorID(idEstudiante).getCorreoElectronico() == "N/A") {

                    utilidades.mostrarAlerta(
                            "Correo no encontrado",
                            "El correo electrónico ingresado no está asociado a ninguna cuenta.",
                            "Por favor, ingrese un correo electrónico válido."
                    );
                    return;

                }
            }

            usuarioDAO.modificarUsuario(new UsuarioDTO(idEstudiante, nombre, apellidos, 1));
            cuentaDAO.modificarCuenta(new CuentaDTO(correo, contrasena, idEstudiante));
            estudianteDAO.modificarEstudiante(new EstudianteDTO(idEstudiante, nombre, apellidos, matricula, 1,0));

            etiquetaNombreEncontrado.setText(nombre);
            etiquetaApellidoEncontrado.setText(apellidos);
            etiquetaMatriculaEncontrada.setText(matricula);
            etiquetaCorreoEncontrado.setText(correo);

            cancelarEdicion();
            cargarEstudiantes();

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al guardar los cambios del estudiante: " + e);
            utilidades.mostrarAlerta(
                    "Error inesperado",
                    "No se pudieron guardar los cambios del estudiante.",
                    "Por favor, intente nuevamente más tarde."
            );
        }

        botonSeleccionarEstudiantes.setDisable(false);
        botonEliminarEstudiante.setVisible(true);
        botonEditar.setVisible(true);
        tablaEstudiantes.setDisable(false);
        botonRegistrarEstudiante.setDisable(false);

    }

}
