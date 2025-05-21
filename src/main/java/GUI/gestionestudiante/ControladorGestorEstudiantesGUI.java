package GUI.gestionestudiante;

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
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ControladorGestorEstudiantesGUI {

    Logger logger = Logger.getLogger(ControladorGestorEstudiantesGUI.class.getName());

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
    private Label campoNombreEncontrado;

    @FXML
    private Label campoApellidoEncontrado;

    @FXML
    private Label campoMatriculaEncontrada;

    @FXML
    private Label campoCorreoEncontrado;

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
    private Label campoNumeroEstudiantesSeleccionados;

    @FXML
    private Button botonEliminarEstudiante;

    @FXML
    private Button botonSeleccionarEstudiantes;

    @FXML
    private Button botonRegistrarEstudiante;

    private int idEstudiante = 0;



    @FXML
    public void initialize() {

        botonSeleccionarEstudiantes.setCursor(Cursor.HAND);

        columnaMatricula.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMatricula()));
        columnaNombres.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        columnaApellidos.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));

        cargarEstudiantes();
        tablaEstudiantes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tablaEstudiantes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {

            mostrarDetallesDesdeTabla(newSel);
            botonEliminarSeleccionado.setDisable(newSel == null);
        });

    }

    private void cargarEstudiantes() {

        Utilidades utilidades = new Utilidades();

        try {

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            ObservableList<EstudianteDTO> estudiantes = FXCollections.observableArrayList(estudianteDAO.listarEstudiantes());
            tablaEstudiantes.setItems(estudiantes);

        } catch (Exception e) {

            logger.severe("Error al cargar la lista de estudiantes: " + e.getMessage());
            utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Error al cargar la lista de estudiantes.");
        }
    }

    @FXML
    private void buscarEstudiante() {

        String matricula = campoMatricula.getText().trim();
        Utilidades utilidades = new Utilidades();

        if (matricula.isEmpty()) {

            utilidades.mostrarVentana("/CamposVaciosGUI.fxml");
        }

        try {

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            EstudianteDTO estudiante = estudianteDAO.buscarEstudiantePorMatricula(matricula);

            CuentaDAO cuentaDAO = new CuentaDAO();
            if (estudiante.getIdUsuario() != -1) {

                campoNombreEncontrado.setText(estudiante.getNombre());
                campoApellidoEncontrado.setText(estudiante.getApellido());
                campoMatriculaEncontrada.setText(estudiante.getMatricula());

                idEstudiante = estudiante.getIdUsuario();
                CuentaDTO cuenta = cuentaDAO.buscarCuentaPorID(idEstudiante);
                campoCorreoEncontrado.setText(cuenta.getCorreoElectronico());
            } else {

                campoNombreEncontrado.setText("No hay estudiante con esa matricula");
                campoApellidoEncontrado.setText("");
                campoMatriculaEncontrada.setText("");
                campoCorreoEncontrado.setText("");
            }

        } catch (SQLException | IOException e) {

            utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Error al buscar el estudiante.");
            logger.severe("Error al buscar el estudiante: " + e.getMessage());
        }
    }

    private void mostrarDetallesDesdeTabla(EstudianteDTO estudianteSeleccionado) {

        Utilidades utilidades = new Utilidades();
        if (estudianteSeleccionado == null) return;

        try {

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            EstudianteDTO estudiante = estudianteDAO.buscarEstudiantePorMatricula(estudianteSeleccionado.getMatricula());
            CuentaDAO cuentaDAO = new CuentaDAO();

            if (estudiante.getIdUsuario() != -1) {

                campoNombreEncontrado.setText(estudiante.getNombre());
                campoApellidoEncontrado.setText(estudiante.getApellido());
                campoMatriculaEncontrada.setText(estudiante.getMatricula());

                idEstudiante = estudiante.getIdUsuario();
                CuentaDTO cuenta = cuentaDAO.buscarCuentaPorID(idEstudiante);
                campoCorreoEncontrado.setText(cuenta.getCorreoElectronico());
            }

        } catch (SQLException | IOException e) {

            logger.severe("Error al mostrar detalles del estudiante: " + e.getMessage());
            utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Error al mostrar detalles del estudiante.");
        }
    }

    @FXML
    private void abrirVentanaRegistrarEstudiante() {

        Utilidades utilidades = new Utilidades();
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistroEstudianteGUI.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.setOnHiding(event -> cargarEstudiantes());

            stage.show();

        } catch (IOException e) {

            logger.severe("Error al abrir la ventana de registro: " + e.getMessage());
            utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Error al abrir la ventana de registro.");
        }
    }

    @FXML
    private void eliminarEstudiante() {

        Utilidades utilidades = new Utilidades();
        String matricula = campoMatriculaEncontrada.getText().trim();

        if (matricula.isEmpty()) {

            utilidades.mostrarVentana("/CamposVaciosGUI.fxml");
            return;
        }

        try {

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            boolean eliminado = estudianteDAO.eliminarEstudiantePorMatricula(0, matricula);

            if (eliminado) {

                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "El estudiante ha sido eliminado correctamente.");
                cargarEstudiantes();

            } else {

                utilidades.mostrarVentana("/AvisoGUI.fxml");
            }

        } catch (SQLException | IOException e) {

            logger.severe("Error al eliminar el estudiante: " + e.getMessage());
            utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Error al eliminar el estudiante.");
        }
    }

    @FXML
    private void activarModoSeleccion() {

        botonEliminarSeleccionado.setManaged(true);
        botonEliminarSeleccionado.setVisible(true);
        botonCancelarSeleccion.setVisible(true);
        campoNumeroEstudiantesSeleccionados.setVisible(true);
        botonEditar.setDisable(true);
        botonEliminarEstudiante.setDisable(true);
        botonRegistrarEstudiante.setDisable(true);

        tablaEstudiantes.getSelectionModel().getSelectedItems().addListener((ListChangeListener<EstudianteDTO>) change -> {

            int cantidadSeleccionados = tablaEstudiantes.getSelectionModel().getSelectedItems().size();
            if (cantidadSeleccionados > 0) {

                campoNumeroEstudiantesSeleccionados.setText("Estudiantes seleccionados: " + cantidadSeleccionados);
            } else {

                campoNumeroEstudiantesSeleccionados.setText(" ");
            }
        });


    }

    @FXML
    private void eliminarEstudianteSeleccionado() {

        Utilidades utilidades = new Utilidades();
        ObservableList<EstudianteDTO> seleccionados = tablaEstudiantes.getSelectionModel().getSelectedItems();

        if (seleccionados == null || seleccionados.isEmpty()) {

            utilidades.mostrarVentana("/CamposVaciosGUI.fxml");
            return;
        }
        boolean error = false;

        try{

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            for (EstudianteDTO estudiante : seleccionados) {

                boolean eliminado = estudianteDAO.eliminarEstudiantePorMatricula(0, estudiante.getMatricula());
                if (!eliminado) {

                    error = true;
                }

            }
        } catch (SQLException | IOException e) {

            logger.severe("Error al eliminar el estudiante: " + e.getMessage());
            error = true;
        }

        if (error) {

            utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Error al eliminar algunos estudiantes.");
        } else {

            utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Estudiantes eliminados exitosamente.");
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
        campoNumeroEstudiantesSeleccionados.setVisible(false);
        botonEliminarEstudiante.setDisable(false);
        botonEditar.setDisable(false);
        botonRegistrarEstudiante.setDisable(false);

        tablaEstudiantes.getSelectionModel().clearSelection();
        campoNumeroEstudiantesSeleccionados.setText(" ");

    }

    @FXML
    private void editarEstudiante() {

        campoNombreEditable.setText(campoNombreEncontrado.getText());
        campoApellidoEditable.setText(campoApellidoEncontrado.getText());
        campoMatriculaEditable.setText(campoMatriculaEncontrada.getText());
        campoCorreoEditable.setText(campoCorreoEncontrado.getText());

        campoNombreEditable.setVisible(true);
        campoApellidoEditable.setVisible(true);
        campoMatriculaEditable.setVisible(true);
        campoCorreoEditable.setVisible(true);

        campoNombreEncontrado.setVisible(false);
        campoApellidoEncontrado.setVisible(false);
        campoMatriculaEncontrada.setVisible(false);
        campoCorreoEncontrado.setVisible(false);


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

        campoNombreEncontrado.setVisible(true);
        campoApellidoEncontrado.setVisible(true);
        campoMatriculaEncontrada.setVisible(true);
        campoCorreoEncontrado.setVisible(true);

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

        String matriculaEcontrada = campoMatriculaEncontrada.getText().trim();
        String correoEncontrado = campoCorreoEncontrado.getText().trim();

        String nombre = campoNombreEditable.getText().trim();
        String apellidos = campoApellidoEditable.getText().trim();
        String matricula = campoMatriculaEditable.getText().trim();
        String correo = campoCorreoEditable.getText().trim();

        Utilidades utilidades = new Utilidades();
        VerificacionUsuario verificacionUsuario = new VerificacionUsuario();

        try {

            List<String> listaDeCamposVacios = VerificacionUsuario.camposVacios(nombre, apellidos, correo, correo, correoEncontrado);

            if (!listaDeCamposVacios.isEmpty()) {

                String camposVacios = String.join("\n", listaDeCamposVacios);
                utilidades.mostrarAlerta(
                        "Campos vacíos",
                        "Por favor, complete todos los campos requeridos.",
                        camposVacios
                );
                return;
            }

            if (!verificacionUsuario.nombreValido(nombre)) {
                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Nombre inválido.");
                return;
            }

            if (!verificacionUsuario.apellidosValidos(apellidos)) {
                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Apellidos inválidos.");
                return;
            }

            if (!verificacionUsuario.matriculaValida(matricula)) {
                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Matrícula inválida.");
                return;
            }

            if (!verificacionUsuario.correoValido(correo)) {
                utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Correo inválido.");
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

                    utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "La matrícula ya existe.");
                    return;

                }
            }

            if (!correoEncontrado.equals(correo)) {

                if (cuentaDAO.buscarCuentaPorID(idEstudiante).getCorreoElectronico() != "N/A") {

                    utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "El correo ya está registrado.");
                    return;

                }
            }

            usuarioDAO.modificarUsuario(new UsuarioDTO(idEstudiante, nombre, apellidos, 1));
            cuentaDAO.modificarCuenta(new CuentaDTO(correo, contrasena, idEstudiante));
            estudianteDAO.modificarEstudiante(new EstudianteDTO(idEstudiante, nombre, apellidos, matricula, 1));

            campoNombreEncontrado.setText(nombre);
            campoApellidoEncontrado.setText(apellidos);
            campoMatriculaEncontrada.setText(matricula);
            campoCorreoEncontrado.setText(correo);

            cancelarEdicion();
            cargarEstudiantes();

        } catch (SQLException | IOException e) {

            utilidades.mostrarVentanaAviso("/AvisoGUI.fxml", "Error al guardar los cambios.");
            logger.severe("Error al guardar los cambios: " + e.getMessage());
        }

        botonSeleccionarEstudiantes.setDisable(false);
        botonEliminarEstudiante.setVisible(true);
        botonEditar.setVisible(true);
        tablaEstudiantes.setDisable(false);
        botonRegistrarEstudiante.setDisable(false);

    }

}
