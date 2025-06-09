package GUI.gestionproyecto.asignacionproyecto;

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

    private EstudianteDTO estudianteSeleccionado;
    private ControladorAsignacionEstudianteAProyectoGUI controladorAsignacionPrincipal;
    private final Utilidades UTILIDADES = new Utilidades();

    public void inicializarDatos(EstudianteDTO estudiante,
                                 ControladorAsignacionEstudianteAProyectoGUI controladorPrincipal,
                                 List<ProyectoDTO> proyectos) {

        if (campoNombreEstudiante == null || comboProyectos == null) {
            System.err.println("Error: Componentes FXML no inicializados");
            return;
        }

        this.estudianteSeleccionado = estudiante;
        this.controladorAsignacionPrincipal = controladorPrincipal;

        campoNombreEstudiante.setText(estudiante.getNombre() + " " + estudiante.getApellido());
        campoMatricula.setText(estudiante.getMatricula());

        cargarProyectos(proyectos);

        if (estudiante.getIdProyecto() == 0) {

            botonAsignar.setText("Asignar");

        } else {

            botonAsignar.setText("Reasignar");
        }
    }

    public void cargarProyectos(List<ProyectoDTO> listaProyectos) {

        comboProyectos.getItems().clear();
        comboProyectos.getItems().addAll(listaProyectos);
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
                    setText(proyecto.getNombre());
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
                    setText(proyecto.getNombre());
                }
            }
        });
    }

    @FXML
    private void asignarProyecto() {

        ProyectoDTO proyectoSeleccionado = comboProyectos.getSelectionModel().getSelectedItem();

        if (proyectoSeleccionado != null) {

            UTILIDADES.mostrarAlertaConfirmacion(

                    "Confirmar asignacion",
                    "¿Está seguro que desea asignar este proyecto al estudiante?",
                    "",
                    () -> {

                        controladorAsignacionPrincipal.actualizarAsignacion(estudianteSeleccionado,
                                proyectoSeleccionado.getIdProyecto());
                        cerrarVentana();
                    },
                    () -> {

                        Stage ventanaActual = (Stage) botonRegresar.getScene().getWindow();
                        ventanaActual.close();
                    }
            );

        } else {

            UTILIDADES.mostrarAlerta("Advertencia",
                    "No se ha seleccionado un proyecto",
                    "Por favor, seleccione un proyecto para asignar.");
        }
    }

    @FXML
    private void regresar() {
        cerrarVentana();
    }

    private void cerrarVentana() {

        Stage ventanaActual = (Stage) botonRegresar.getScene().getWindow();
        ventanaActual.close();
    }
}
