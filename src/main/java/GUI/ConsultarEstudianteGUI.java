package GUI;


import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logica.DAOs.CuentaDAO;
import logica.DAOs.EstudianteDAO;
import logica.DTOs.CuentaDTO;
import logica.DTOs.EstudianteDTO;

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

    private int idEstudiante = 0;




    @FXML
    public void initialize() {
        columnaMatricula.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMatricula()));
        columnaNombres.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        columnaApellidos.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));

        cargarEstudiantes();

        tablaEstudiantes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            mostrarDetallesDesdeTabla(newSel);
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


}
