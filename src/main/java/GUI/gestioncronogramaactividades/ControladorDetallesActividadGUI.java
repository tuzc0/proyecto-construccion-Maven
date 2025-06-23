package GUI.gestioncronogramaactividades;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import logica.DAOs.ActividadDAO;
import logica.DTOs.ActividadDTO;
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;

public class ControladorDetallesActividadGUI {

    private static final Logger LOGGER = LogManager.getLogger(ControladorDetallesActividadGUI.class);

    @FXML private Label etiquetaNombreActividad;
    @FXML private Label etiquetaDuracion;
    @FXML private Label etiquetaHitos;
    @FXML private DatePicker fechaInicio;
    @FXML private DatePicker fechaFin;

    private Utilidades gestorVentanas = new Utilidades();
    private IGestorAlertas gestorAlertas = new Utilidades();
    private ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(gestorAlertas, LOGGER);

    private int idActividad;

    public void setIdActividad(int idActividad) {

        this.idActividad = idActividad;
        cargarDatosActividad();
    }

    private void cargarDatosActividad() {

        ActividadDAO actividadDAO = new ActividadDAO();

        try {

            ActividadDTO actividadDTO = actividadDAO.buscarActividadPorID(idActividad);
            int actividadNoEncontrada = -1;

            if (actividadDTO.getIDActividad() != actividadNoEncontrada) {

                etiquetaNombreActividad.setText(actividadDTO.getNombre());
                etiquetaDuracion.setText(actividadDTO.getDuracion());
                etiquetaHitos.setText(actividadDTO.getHitos());
                fechaInicio.setValue(actividadDTO.getFechaInicio().toLocalDate());
                fechaFin.setValue(actividadDTO.getFechaFin().toLocalDate());

            } else {

                gestorVentanas.mostrarAlerta(
                        "Actividad no encontrada.",
                        "No se encontro la actividad dentro del sistema.",
                        "Por favor contacte al administrador."
                );
            }

        } catch (SQLException e){

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error al cargar la actividad dentro: " + e.getMessage(), e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error inesperado dentro dentro sistema.",
                    "Por favor intentelo de nuevo más tarde o contacte al administrador."
            );
        }
    }
}
