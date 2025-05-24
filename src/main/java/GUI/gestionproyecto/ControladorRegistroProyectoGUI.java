package GUI.gestionproyecto;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logica.DAOs.ProyectoDAO;
import logica.DTOs.OrganizacionVinculadaDTO;
import logica.DTOs.ProyectoDTO;
import logica.DTOs.RepresentanteDTO;
import logica.utilidadesproyecto.SeleccionRepresentanteOrganizacion;
import GUI.gestioncronogramaactividades.ControladorRegistroCronogramaActividades;
import logica.verificacion.VerificicacionGeneral;
import logica.verificacion.VerificacionDeProyecto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
    @FXML private Label contadorNombre;
    @FXML private Label contadorDescripcionGeneral;
    @FXML private Label contadorObjetivosGenerales;
    @FXML private Label contadorObjetivosInmediatos;
    @FXML private Label contadorObjetivosMediatos;
    @FXML private Label contadorMetodologia;
    @FXML private Label contadorRecursos;
    @FXML private Label contadorActividades;
    @FXML private Label contadorResponsabilidades;
    @FXML private Label contadorDias;
    @FXML private Button botonRegistrar;
    @FXML private Button botonCancelar;
    @FXML private Button botonRegistrarCalendario;
    @FXML private Button botonSeleccionarRepresentante;

    private final Utilidades UTILIDADES = new Utilidades();

    @FXML
    private void initialize() {

        VerificicacionGeneral verficacionGeneral = new VerificicacionGeneral();

        verficacionGeneral.contadorCaracteresTextField(campoNombre,
                contadorNombre,150);
        verficacionGeneral.contadorCaracteresTextField(campoDescripcionGeneral,
                contadorDescripcionGeneral,255);
        verficacionGeneral.contadorCaracteresTextField(campoObjetivosGenerales,
                contadorObjetivosGenerales,255);
        verficacionGeneral.contadorCaracteresTextField(campoObjetivosInmediatos,
                contadorObjetivosInmediatos,255);
        verficacionGeneral.contadorCaracteresTextField(campoObjetivosMediatos,
                contadorObjetivosMediatos,255);
        verficacionGeneral.contadorCaracteresTextField(campoMetodologia,
                contadorMetodologia,255);
        verficacionGeneral.contadorCaracteresTextField(campoRecursos,
                contadorRecursos,255);
        verficacionGeneral.contadorCaracteresTextField(campoActividades,
                contadorActividades,255);
        verficacionGeneral.contadorCaracteresTextField(campoResponsabilidades,
                contadorResponsabilidades,255);
        verficacionGeneral.contadorCaracteresTextField(campoDias,
                contadorDias,255);

        botonRegistrar.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
        botonRegistrarCalendario.setCursor(Cursor.HAND);
        botonSeleccionarRepresentante.setCursor(Cursor.HAND);
    }

    @FXML
    private void registrarProyecto() {

        String nombre = campoNombre.getText();
        String descripcionGeneral = campoDescripcionGeneral.getText();
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

        VerificacionDeProyecto verificacionDeDatos = new VerificacionDeProyecto();

        List<String> camposVacios = verificacionDeDatos.camposVaciosProyecto(
                nombre, descripcionGeneral, objetivosGenerales, objetivosInmediatos,
                objetivosMediatos, metodologia, recursos, actividades,
                responsabilidades, diasyhorarios);

        if (!camposVacios.isEmpty()) {

            String mensaje = String.join("\n", camposVacios);
            UTILIDADES.mostrarAlerta(
                    "Campos vacíos",
                    "Por favor, complete todos los campos requeridos.",
                    mensaje
            );

            return;
        }

        List<String> erroresValidacion = verificacionDeDatos.validarCamposProyecto(
                nombre, descripcionGeneral, objetivosGenerales, objetivosInmediatos,
                objetivosMediatos, metodologia, recursos, actividades,
                responsabilidades, diasyhorarios);

        if (!erroresValidacion.isEmpty()) {

            String mensaje = String.join("\n", erroresValidacion);
            UTILIDADES.mostrarAlerta(
                    "Errores de validación",
                    "Por favor, ingrese información válida en los campos.",
                    mensaje
            );

            return;
        }

        RepresentanteDTO representanteSeleccionado =
                SeleccionRepresentanteOrganizacion.getRepresentanteSeleccionado();

        if (representanteSeleccionado == null) {

            UTILIDADES.mostrarAlerta("Error.",
                    "Por favor, seleccione un representante para el proyecto.",
                    "De clic en el botón 'Seleccionar Representante'");
            return;
        }

        if (idCronogramaTemporal == null || idCronogramaTemporal == -1) {

            UTILIDADES.mostrarAlerta("Error",
                    "Primero debe registrar el cronograma de actividades del proyecto.",
                    "De clic en el botón 'Registrar Cronograma de Actividades'");
            return;
        }

        actualizarEstadoCronogramaActividades(true);
        int representanteID = obtenerIDRepresentanteSeleccionado();
        int estadoActivo = 1;

        ProyectoDTO proyectoDTO = new ProyectoDTO(0, nombre, objetivosGenerales,
                objetivosInmediatos, objetivosMediatos, metodologia, recursos, actividades,
                responsabilidades, duracion, diasyhorarios, idCronogramaTemporal,
                estadoActivo, representanteID, descripcionGeneral);

        ProyectoDAO proyectoDAO = new ProyectoDAO();

        try {

            boolean proyectoRegistrado = proyectoDAO.crearNuevoProyecto(proyectoDTO);

            if (proyectoRegistrado) {

                UTILIDADES.mostrarConfirmacion("Éxito", "Proyecto registrado exitosamente.", "");

            } else {

                UTILIDADES.mostrarAlerta("Error.",
                        "No se pudo registrar el proyecto. Por favor, inténtelo de nuevo más tarde.",
                        "Verifique la información ingresada.");
            }

        } catch (SQLException e) {

            LOGGER.error("Error durante el registro del académico.", e);
            UTILIDADES.mostrarAlerta("Error.",
                    "Ocurrió un error. Por favor, inténtelo de nuevo más tarde.",
                    "");

        }  catch (IOException e) {

            LOGGER.error("Error durante el registro del académico.", e);
            UTILIDADES.mostrarAlerta("Error.",
                    "Ocurrió un error. Por favor, inténtelo de nuevo más tarde.",
                    "");

        } catch (Exception e) {

            LOGGER.error("Error durante el registro del académico.", e);
            UTILIDADES.mostrarAlerta("Error.",
                    "Ocurrió un error. Por favor, inténtelo de nuevo más tarde.",
                    "");
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

    private Integer idCronogramaTemporal = null;

    @FXML
    private void registrarCronogramaActividades() {

        try {

            FXMLLoader cargadorFXML =
                    new FXMLLoader(getClass().getResource("/RegistroCronogramaActividadesGUI.fxml"));
            Parent raizVentana = cargadorFXML.load();

            Stage escenarioRegistroCronograma = new Stage();
            escenarioRegistroCronograma.setScene(new Scene(raizVentana));
            escenarioRegistroCronograma.setTitle("Registro de Cronograma de Actividades");

            ControladorRegistroCronogramaActividades controladorCronograma = cargadorFXML.getController();
            controladorCronograma.setEscenarioActual(escenarioRegistroCronograma);
            controladorCronograma.asignarControladorPadre(this);

            escenarioRegistroCronograma.initModality(Modality.APPLICATION_MODAL);
            escenarioRegistroCronograma.showAndWait();

            this.idCronogramaTemporal = controladorCronograma.getIdCronogramaRegistrado();

            if(idCronogramaTemporal != null && idCronogramaTemporal != -1) {
                actualizarEstadoCronogramaActividades(true);
            }

        } catch (IOException excepcionCargaVentana) {

            LOGGER.error("Error al cargar la ventana de registro de cronograma." + excepcionCargaVentana.getMessage());
            UTILIDADES.mostrarAlerta("Error",
                    "No se pudo cargar la ventana para registrar un cronograma de actividades.",
                    "Por favor, inténtelo de nuevo más tarde o contacte al administrador.");
        }
    }

    @FXML
    private void seleccionarRepresentante() {

        try {

            FXMLLoader cargadorFXML = new FXMLLoader(getClass().getResource("/SeleccionarRepresentante.fxml"));
            Stage escenarioSeleccionRepresentante = new Stage();
            escenarioSeleccionRepresentante.setScene(new Scene(cargadorFXML.load()));

            ControladorSeleccionRepresentanteGUI controladorSeleccionRepresentante = cargadorFXML.getController();

            controladorSeleccionRepresentante.setControladorRegistro(this);
            escenarioSeleccionRepresentante.show();

        } catch (IOException excepcionCargaVentana) {

            LOGGER.error("Error al cargar la ventana de selección de representante: " + excepcionCargaVentana.getMessage());
            UTILIDADES.mostrarAlerta("Error",
                    "No se pudo abrir la ventana de selección de representante.",
                    "Por favor, inténtelo de nuevo más tarde o contacte al administrador.");
        }
    }

    public void actualizarEstadoCronogramaActividades(boolean estadoCronogramaInsertado) {

        if (estadoCronogramaInsertado) {

            botonRegistrarCalendario.setDisable(true);
            botonRegistrarCalendario.setText("Cronograma Registrado");
            etiquetaCalendarizacion.setText("Cronograma de Actividades Registrado");

        } else {

            botonRegistrarCalendario.setDisable(false);
            botonRegistrarCalendario.setText("Registrar Cronograma");
            etiquetaCalendarizacion.setText("Registrar Cronograma de Actividades");
        }
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
