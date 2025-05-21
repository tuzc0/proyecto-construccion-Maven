package GUI.proyecto;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logica.DAOs.ProyectoDAO;
import logica.DTOs.OrganizacionVinculadaDTO;
import logica.DTOs.ProyectoDTO;
import logica.DTOs.RepresentanteDTO;
import logica.utilidadesproyecto.SeleccionRepresentanteOrganizacion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;

public class ControladorRegistroProyectoGUI {

    private static final Logger LOGGER = LogManager.getLogger(ControladorRegistroProyectoGUI.class);

    @FXML private TextField campoNombre;
    @FXML private TextField campoDescripcionGeneral;
    @FXML private TextField campoObjetivosGenerales;
    @FXML private TextField campoObjetivosInmediatos;
    @FXML private TextField campoObjetivosMediatos;
    @FXML private TextField campoMetodologia;
    @FXML private TextField campoRecursos;
    @FXML private TextField campoActividades;
    @FXML private TextField campoResponsabilidades;
    @FXML private TextField campoDias;
    @FXML private Label etiquetaDuracion;
    @FXML private Label etiquetaRepresentante;
    @FXML private Label etiquetaCalendarizacion;
    @FXML private Button botonRegistrar;
    @FXML private Button botonCancelar;
    @FXML private Button botonSeleccionarCalendario;
    @FXML private Button botonSeleccionarRepresentante;

    private final Utilidades UTILIDADES = new Utilidades();

    @FXML
    private void initialize() {

        botonRegistrar.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
        botonSeleccionarCalendario.setCursor(Cursor.HAND);
        botonSeleccionarRepresentante.setCursor(Cursor.HAND);
    }

    @FXML
    private void registrarProyecto() {

        try {

            String nombre = campoNombre.getText();
            String descripcion = campoDescripcionGeneral.getText();
            String objetivosGenerales = campoObjetivosGenerales.getText();
            String objetivosInmediatos = campoObjetivosInmediatos.getText();
            String objetivosMediatos = campoObjetivosMediatos.getText();
            String metodologia = campoMetodologia.getText();
            String recursos = campoRecursos.getText();
            String actividades = campoActividades.getText();
            String responsabilidades = campoResponsabilidades.getText();
            String duracion = etiquetaDuracion.getText();
            String diasyhorarios = campoDias.getText();
            String calendarizacion = etiquetaCalendarizacion.getText();
            String representante = etiquetaRepresentante.getText();

            if (validarCamposVaciosReporte()) {

                UTILIDADES.mostrarAlerta("Error.", "Por favor, complete todos los campos obligatorios.", "Algunos campos se encuentran vacios.");
                return;
            }

            RepresentanteDTO representanteSeleccionado = SeleccionRepresentanteOrganizacion.getRepresentanteSeleccionado();

            if (representanteSeleccionado == null) {

                UTILIDADES.mostrarAlerta("Error.", "Por favor, seleccione un representante para el proyecto.", "De clic en el botón 'Seleccionar Representante'");
                return;
            }

            int calendarizacionID = 1;
            int representanteID = obtenerIDRepresentanteSeleccionado();
            int estadoActivo = 1;

            ProyectoDTO proyectoDTO = new ProyectoDTO(0, nombre, objetivosGenerales,
                    objetivosInmediatos, objetivosMediatos, metodologia, recursos, actividades,
                    responsabilidades, duracion, diasyhorarios, calendarizacionID, estadoActivo, representanteID, descripcion);

            ProyectoDAO proyectoDAO = new ProyectoDAO();
            boolean proyectoRegistrado = proyectoDAO.crearNuevoProyecto(proyectoDTO);

            if (proyectoRegistrado) {

                UTILIDADES.mostrarConfirmacion("Éxito", "Proyecto registrado exitosamente.", "");

            } else {

                UTILIDADES.mostrarAlerta("Error.", "No se pudo registrar el proyecto. Por favor, inténtelo de nuevo más tarde.", "");
            }

        } catch (SQLException e) {

            LOGGER.error("Error durante el registro del académico.", e);
            UTILIDADES.mostrarAlerta("Error.", "Ocurrió un error. Por favor, inténtelo de nuevo más tarde.", "");

        }  catch (IOException e) {

            LOGGER.error("Error durante el registro del académico.", e);
            UTILIDADES.mostrarAlerta("Error.", "Ocurrió un error. Por favor, inténtelo de nuevo más tarde.", "");

        } catch (Exception e) {

            LOGGER.error("Error durante el registro del académico.", e);
            UTILIDADES.mostrarAlerta("Error.", "Ocurrió un error. Por favor, inténtelo de nuevo más tarde.", "");
        }
    }

    @FXML
    private void cancelarRegistroProyecto() {

        LOGGER.info("Cancelando el registro del proyecto.");
        campoNombre.clear();
        campoDescripcionGeneral.clear();
        campoObjetivosGenerales.clear();
        campoObjetivosInmediatos.clear();
        campoObjetivosMediatos.clear();
        campoMetodologia.clear();
        campoRecursos.clear();
        campoActividades.clear();
        campoResponsabilidades.clear();
        campoDias.clear();
    }

    @FXML
    private void seleccionarCronogramaActividades() {

    }

    @FXML
    private void seleccionarRepresentante() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SeleccionarRepresentante.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            ControladorSeleccionRepresentanteGUI controladorSeleccion = loader.getController();

            controladorSeleccion.setControladorRegistro(this);
            stage.show();

        } catch (IOException e) {

            LOGGER.error("Error al cargar la ventana SeleccionarRepresentante.fxml: " + e.getMessage());
            UTILIDADES.mostrarAlerta("Error", "No se pudo abrir la ventana de selección de representante.", "");
        }
    }

    private boolean validarCamposVaciosReporte() {

        return campoNombre.getText().isEmpty() || campoDescripcionGeneral.getText().isEmpty() ||
               campoObjetivosGenerales.getText().isEmpty() || campoObjetivosInmediatos.getText().isEmpty() ||
               campoObjetivosMediatos.getText().isEmpty() || campoMetodologia.getText().isEmpty() ||
               campoRecursos.getText().isEmpty() || campoActividades.getText().isEmpty() ||
               campoResponsabilidades.getText().isEmpty() || campoDias.getText().isEmpty() || campoDescripcionGeneral.getText().isEmpty();
    }

    public void setRepresentanteSeleccionado(RepresentanteDTO representante, OrganizacionVinculadaDTO organizacion) {

        if (representante != null && organizacion != null) {

            etiquetaRepresentante.setText(representante.getNombre() + " " + representante.getApellidos() + " - " + organizacion.getNombre());
        }
    }

    public int obtenerIDRepresentanteSeleccionado() {

        return SeleccionRepresentanteOrganizacion.getRepresentanteSeleccionado().getIDRepresentante();
    }
}
