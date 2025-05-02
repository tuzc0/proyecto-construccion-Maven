package GUI;


import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.util.logging.Logger;

public class ConsultarEstudianteGUI {

    Logger logger = Logger.getLogger(ConsultarEstudianteGUI.class.getName());

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


    private int idEstudiante = 0;



    @FXML
    public void initialize() {
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

        try {

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            ObservableList<EstudianteDTO> estudiantes = FXCollections.observableArrayList(estudianteDAO.listarEstudiantes());
            tablaEstudiantes.setItems(estudiantes);

        } catch (Exception e) {

            logger.severe("Error al cargar la lista de estudiantes: " + e.getMessage());
            Utilidades utilidades = new Utilidades();
            utilidades.mostrarVentana("/ErrorGUI.fxml");
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

            utilidades.mostrarVentana("/ErrorGUI.fxml");
        }
    }

    private void mostrarDetallesDesdeTabla(EstudianteDTO estudianteSeleccionado) {
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
            new Utilidades().mostrarVentana("/ErrorGUI.fxml");
        }
    }

    @FXML
    private void abrirVentanaRegistrarEstudiante() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistrarEstudiante.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.setOnHiding(event -> cargarEstudiantes());

            stage.show();

        } catch (IOException e) {
            logger.severe("Error al abrir la ventana de registro: " + e.getMessage());
            new Utilidades().mostrarVentana("/ErrorGUI.fxml");
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

                utilidades.mostrarVentanaError("/ErrorGUI.fxml", "El estudiante ha sido eliminado correctamente.");
                cargarEstudiantes();

            } else {

                utilidades.mostrarVentana("/ErrorGUI.fxml");
            }

        } catch (SQLException | IOException e) {

            logger.severe("Error al eliminar el estudiante: " + e.getMessage());
            utilidades.mostrarVentana("/ErrorGUI.fxml");
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
            utilidades.mostrarVentana("/ErrorGUI.fxml");
        } else {
            utilidades.mostrarVentanaError("/ErrorGUI.fxml", "Estudiantes eliminados exitosamente.");
        }

        cargarEstudiantes();
        botonEliminarSeleccionado.setDisable(true);
        botonEliminarSeleccionado.setManaged(false);
        botonEliminarSeleccionado.setVisible(false);
        botonCancelarSeleccion.setVisible(false);
        botonEditar.setDisable(false);
        botonEliminarEstudiante.setDisable(false);

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

            if (verificacionUsuario.camposVacios(nombre, apellidos, matricula, correo, " ")) {
                utilidades.mostrarVentanaError("/ErrorGUI.fxml", "Por favor, complete todos los campos.");
                return;
            }

            if (!verificacionUsuario.nombreValido(nombre)) {
                utilidades.mostrarVentanaError("/ErrorGUI.fxml", "Nombre inválido.");
                return;
            }

            if (!verificacionUsuario.apellidosValidos(apellidos)) {
                utilidades.mostrarVentanaError("/ErrorGUI.fxml", "Apellidos inválidos.");
                return;
            }

            if (!verificacionUsuario.matriculaValida(matricula)) {
                utilidades.mostrarVentanaError("/ErrorGUI.fxml", "Matrícula inválida.");
                return;
            }

            if (!verificacionUsuario.correoValido(correo)) {
                utilidades.mostrarVentanaError("/ErrorGUI.fxml", "Correo inválido.");
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

                    utilidades.mostrarVentanaError("/ErrorGUI.fxml", "La matrícula ya existe.");
                    return;

                }
            }

            if (!correoEncontrado.equals(correo)) {

                if (cuentaDAO.buscarCuentaPorID(idEstudiante).getCorreoElectronico() != "N/A") {

                    utilidades.mostrarVentanaError("/ErrorGUI.fxml", "El correo ya está registrado.");
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

            utilidades.mostrarVentana("/ErrorRegistroEstudiante.fxml");
        }

        botonSeleccionarEstudiantes.setDisable(false);
    }









}
