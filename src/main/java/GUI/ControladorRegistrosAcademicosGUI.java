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
import logica.DTOs.AcademicoDTO;
import logica.DTOs.CuentaDTO;
import java.io.IOException;
import java.sql.SQLException;
import logica.VerificacionUsuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ControladorRegistrosAcademicosGUI {

    private static final Logger logger = LogManager.getLogger(ControladorRegistrosAcademicosGUI.class);

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

    AcademicoDAO academicoDAO = new AcademicoDAO();
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
    }

    private void cargarAcademicos() {

        try {

            ObservableList<AcademicoDTO> academicos = FXCollections.observableArrayList(academicoDAO.listarAcademicos());
            tablaAcademicos.setItems(academicos);

        } catch (Exception e) {

            logger.warn("Error al cargar la lista de estudiantes: " + e.getMessage());
            utilidades.mostrarVentana("/ErrorGUI.fxml");
        }
    }

    @FXML
    private void buscarAcademico() {

        String numeroDePersonal = campoNumeroDePersonal.getText().trim();
        Utilidades utilidades = new Utilidades();

        if (numeroDePersonal.isEmpty()) {

            logger.warn("Campo de numero de personal vacio");
            utilidades.mostrarVentana("/CamposVaciosGUI.fxml");
            return;
        }

        try {

            if (!VerificacionUsuario.numeroPersonalValido(numeroDePersonal)) {

                logger.warn("Número de personal inválido");
                utilidades.mostrarVentana("/ErrorGUI.fxml");
                return;
            }

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

                utilidades.mostrarVentana("/ErrorGUI.fxml");
                logger.info("El academico no fue encontrado");
                campoNombreEncontrado.setText("");
                campoApellidoEncontrado.setText("");
                campoNumeroDePersonalEncontrado.setText("");
                campoCorreoEncontrado.setText("");
            }

        } catch (SQLException | IOException e) {

            utilidades.mostrarVentana("/ErrorGUI.fxml");
        }
    }

    private void mostrarDetallesDesdeTabla(AcademicoDTO academicoSeleccionado) {

        try {

            AcademicoDTO academicoDTO = academicoDAO.buscarAcademicoPorNumeroDePersonal(academicoSeleccionado.getNumeroDePersonal());
            CuentaDAO cuentaDAO = new CuentaDAO();

            if (academicoSeleccionado == null || academicoDTO.getIdUsuario() == -1) {

                logger.warn("El academico no fue encontrado");
                return;
            }

            campoNombreEncontrado.setText(academicoDTO.getNombre());
            campoApellidoEncontrado.setText(academicoDTO.getApellido());
            campoNumeroDePersonalEncontrado.setText(String.valueOf(academicoDTO.getNumeroDePersonal()));

            idAcademico = academicoDTO.getIdUsuario();
            CuentaDTO cuenta = cuentaDAO.buscarCuentaPorID(idAcademico);
            campoCorreoEncontrado.setText(cuenta.getCorreoElectronico());

        } catch (SQLException | IOException e) {

            new Utilidades().mostrarVentana("/ErrorGUI.fxml");
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
            new Utilidades().mostrarVentana("/ErrorGUI.fxml");
        }
    }
}
