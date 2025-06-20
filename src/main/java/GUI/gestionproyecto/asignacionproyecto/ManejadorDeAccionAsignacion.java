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

    public void abrirVentanaSeleccionProyecto(EstudianteDTO estudiante,
                                              ControladorAsignacionEstudianteAProyectoGUI controladorPadre,
                                              List<ProyectoDTO> proyectos) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SeleccionarProyectoGUI.fxml"));
        Stage escenario = new Stage();
        escenario.setScene(new Scene(loader.load()));

        ControladorSeleccionProyectoGUI controlador = loader.getController();
        controlador.inicializarDatos(estudiante, controladorPadre, proyectos);

        if (estudiante.getIdProyecto() == 0) {
            escenario.setTitle("Asignar Proyecto");
        } else {
            escenario.setTitle("Reasignar Proyecto");
        }

        escenario.initModality(Modality.APPLICATION_MODAL);
        escenario.showAndWait();
    }

    public void abrirVentanaDetallesAsignacionProyecto(ProyectoDTO proyecto, EstudianteDTO estudiante,
                                                       ControladorAsignacionEstudianteAProyectoGUI controladorPadre)
            throws IOException {

        FXMLLoader cargarVentana = new FXMLLoader(getClass().getResource("/DetallesAsignacionProyectoGUI.fxml"));
        Parent root = cargarVentana.load();

        ControladorDetallesAsignacionProyectoGUI controlador = cargarVentana.getController();
        controlador.setEsVistaDeCoordinador(true);
        controlador.inicializarDatos(proyecto, estudiante, controladorPadre);
        controlador.configurarVista();

        Stage escenario = new Stage();
        escenario.setScene(new Scene(root));
        escenario.setTitle("Detalles Asignación");
        escenario.initModality(Modality.APPLICATION_MODAL);
        escenario.showAndWait();
    }
}
