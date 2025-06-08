package GUI.gestionproyecto.asignacionProyecto;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import logica.DTOs.EstudianteDTO;
import logica.DTOs.ProyectoDTO;

import java.util.List;

public class ControladorSeleccionProyectoGUI {

    @FXML private Label campoNombreEstudiante;
    @FXML private Label campoMatricula;
    @FXML private ComboBox<ProyectoDTO> comboProyectos;
    @FXML private Button botonAsignar;
    @FXML private Button botonRegresar;

    private EstudianteDTO estudiante;
    private ControladorAsignacionEstudianteAProyectoGUI controladorPrincipal;
    private final Utilidades utilidadesGUI = new Utilidades();

    public void inicializarDatos(EstudianteDTO estudiante, ControladorAsignacionEstudianteAProyectoGUI controladorPrincipal) {

        this.estudiante = estudiante;
        this.controladorPrincipal = controladorPrincipal;

        campoNombreEstudiante.setText(estudiante.getNombre() + " " + estudiante.getApellido());
        campoMatricula.setText(estudiante.getMatricula());
    }

    public void cargarProyectos(List<ProyectoDTO> proyectos) {

        comboProyectos.getItems().clear();
        comboProyectos.getItems().addAll(proyectos);
        comboProyectos.getSelectionModel().selectFirst();
    }

    @FXML
    private void initialize() {

        botonAsignar.setCursor(Cursor.HAND);
        botonRegresar.setCursor(Cursor.HAND);

        comboProyectos.setCellFactory(param -> new ListCell<>() {

            @Override
            protected void updateItem(ProyectoDTO proyecto, boolean empty) {

                super.updateItem(proyecto, empty);

                if (empty || proyecto == null) {
                    setText(null);
                } else {
                    setText(proyecto.getIdProyecto() + " - " + proyecto.getNombre());
                }
            }
        });

        comboProyectos.setButtonCell(new ListCell<>() {

            @Override
            protected void updateItem(ProyectoDTO proyecto, boolean empty) {

                super.updateItem(proyecto, empty);

                if (empty || proyecto == null) {

                    setText(null);

                } else {

                    setText(proyecto.getIdProyecto() + " - " + proyecto.getNombre());
                }
            }
        });
    }

    @FXML
    private void asignarProyecto() {

        ProyectoDTO proyectoSeleccionado = comboProyectos.getSelectionModel().getSelectedItem();

        if (proyectoSeleccionado != null) {

            controladorPrincipal.actualizarAsignacionEstudiante(estudiante, proyectoSeleccionado.getIdProyecto());
            cerrarVentana();

        } else {
            utilidadesGUI.mostrarAlerta("Advertencia",
                    "No se ha seleccionado un proyecto",
                    "Por favor, seleccione un proyecto para asignar.");
        }
    }

    @FXML
    private void regresar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) botonRegresar.getScene().getWindow();
        stage.close();
    }
}