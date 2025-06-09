package GUI.gestionproyecto.asignacionproyecto;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logica.DTOs.EstudianteDTO;
import logica.DTOs.ProyectoDTO;
import java.io.IOException;
import java.util.List;

public class ManejadorDeAccionAsignacion {

    public void mostrarSeleccionProyecto(EstudianteDTO estudiante,
                                         ControladorAsignacionEstudianteAProyectoGUI controladorPadre,
                                         List<ProyectoDTO> proyectos) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SeleccionarProyectoGUI.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));

        ControladorSeleccionProyectoGUI controlador = loader.getController();
        controlador.inicializarDatos(estudiante, controladorPadre, proyectos);

        if (estudiante.getIdProyecto() == 0) {

            stage.setTitle("Asignar Proyecto");

        } else {

            stage.setTitle("Reasignar Proyecto");
        }

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void mostrarDetallesAsignacion(ProyectoDTO proyecto, EstudianteDTO estudiante,
                                          ControladorAsignacionEstudianteAProyectoGUI controladorPadre) throws IOException {

        FXMLLoader cargarVentana = new FXMLLoader(getClass().getResource("/DetallesAsignacionProyectoGUI.fxml"));
        Parent root = cargarVentana.load();

        ControladorDetallesAsignacionProyectoGUI controlador = cargarVentana.getController();
        controlador.inicializarDatos(proyecto, estudiante, controladorPadre);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Detalles Asignación");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
