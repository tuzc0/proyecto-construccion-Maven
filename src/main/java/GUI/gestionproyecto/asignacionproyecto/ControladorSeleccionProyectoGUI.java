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
import java.util.logging.Logger;

public class ControladorSeleccionProyectoGUI {

    private static final Logger LOGGER =
            Logger.getLogger(ControladorSeleccionProyectoGUI.class.getName());

    @FXML private Label campoNombreEstudiante;
    @FXML private Label campoMatricula;
    @FXML private ComboBox<ProyectoDTO> comboProyectos;
    @FXML private Button botonAsignar;
    @FXML private Button botonRegresar;

    private EstudianteDTO estudianteSeleccionado;
    private ControladorAsignacionEstudianteAProyectoGUI controladorAsignacionPrincipal;
    private Utilidades utilidades = new Utilidades();

    public void inicializarDatos(EstudianteDTO estudianteDTO,
                                 ControladorAsignacionEstudianteAProyectoGUI controladorPrincipal,
                                 List<ProyectoDTO> proyectos) {

        if (campoNombreEstudiante == null || comboProyectos == null) {
            return;
        }

        this.estudianteSeleccionado = estudianteDTO;
        this.controladorAsignacionPrincipal = controladorPrincipal;

        campoNombreEstudiante.setText(estudianteDTO.getNombre() + " " + estudianteDTO.getApellido());
        campoMatricula.setText(estudianteDTO.getMatricula());

        cargarProyectos(proyectos);

        if (estudianteDTO.getIdProyecto() == 0) {

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

        comboProyectos.setCellFactory(listaProyectosDTO -> new ListCell<>() {

            @Override
            protected void updateItem(ProyectoDTO proyectoDTO, boolean empty) {

                super.updateItem(proyectoDTO, empty);

                if (empty || proyectoDTO == null) {
                    setText(null);
                } else {
                    setText(proyectoDTO.getNombre());
                }
            }
        });

        comboProyectos.setButtonCell(new ListCell<>() {

            @Override
            protected void updateItem(ProyectoDTO proyectoDTO, boolean empty) {

                super.updateItem(proyectoDTO, empty);

                if (empty || proyectoDTO == null) {
                    setText(null);
                } else {
                    setText(proyectoDTO.getNombre());
                }
            }
        });
    }

    @FXML
    private void asignarProyecto() {

        ProyectoDTO proyectoSeleccionado = comboProyectos.getSelectionModel().getSelectedItem();

        if (proyectoSeleccionado != null) {

            utilidades.mostrarAlertaConfirmacion(

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

            utilidades.mostrarAlerta(
                    "Advertencia",
                    "No se ha seleccionado un proyecto",
                    "Por favor, seleccione un proyecto para asignar."
            );
        }
    }

    @FXML
    private void regresar() {

        utilidades.mostrarAlertaConfirmacion(
                "Confirmar acción",
                "¿Está seguro que desea regresar?",
                "Los cambios no guardados se perderán.",
                () -> cerrarVentana(),
                () -> {

                }
        );
    }

    private void cerrarVentana() {

        Stage ventanaActual = (Stage) botonRegresar.getScene().getWindow();
        ventanaActual.close();
    }
}
