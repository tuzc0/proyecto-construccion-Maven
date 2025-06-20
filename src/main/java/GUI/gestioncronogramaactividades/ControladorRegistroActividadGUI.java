package GUI.gestioncronogramaactividades;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logica.DTOs.ActividadDTO;
import logica.verificacion.VerificacionDeActividad;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.Logger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ControladorRegistroActividadGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorRegistroActividadGUI.class);

    @FXML private TextField campoNombreActividad;
    @FXML private TextField campoDuracion;
    @FXML private TextField campoHitos;
    @FXML private DatePicker fechaInicioActividad;
    @FXML private DatePicker fechaFinActividad;
    @FXML private Button botonAñadir;
    @FXML private Button botonCancelar;
    @FXML private Label etiquetaContadorNombreActividad;
    @FXML private Label etiquetaContadorDuracion;
    @FXML private Label etiquetaContadorHitos;

    private ControladorRegistroCronogramaActividadesGUI controladorCronogramaActividadesGUI;
    private String matriculaEstudiante;
    private Utilidades UTILIDADES = new Utilidades();

    @FXML
    private void initialize() {

        final int MAX_CARACTERES_NOMBRE_ACTIVIDAD = 255;
        final int MAX_CARACTERES_DURACION_ACTIVIDAD = 50;
        final int MAX_CARACTERES_HITOS_ACTIVIDAD = 100;

        VerificicacionGeneral verificacionGeneralUtilidad = new VerificicacionGeneral();

        verificacionGeneralUtilidad.contadorCaracteresTextField(campoNombreActividad,
                etiquetaContadorNombreActividad, MAX_CARACTERES_NOMBRE_ACTIVIDAD);
        verificacionGeneralUtilidad.contadorCaracteresTextField(campoDuracion,
                etiquetaContadorDuracion, MAX_CARACTERES_DURACION_ACTIVIDAD);
        verificacionGeneralUtilidad.contadorCaracteresTextField(campoHitos,
                etiquetaContadorHitos, MAX_CARACTERES_HITOS_ACTIVIDAD);

        botonAñadir.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
    }

    private List<ActividadDTO> actividadesSecundarias = new ArrayList<>();

    @FXML
    private void añadirActividad() {

        String nombreActividad = campoNombreActividad.getText();
        String duracion = campoDuracion.getText();
        String hitos = campoHitos.getText();
        LocalDate fechaInicio = fechaInicioActividad.getValue();
        LocalDate fechaFin = fechaFinActividad.getValue();

        if (fechaInicio == null || fechaFin == null) {

            UTILIDADES.mostrarAlerta(
                    "Fechas vacías",
                    "Se necesitan ambas fechas para la actividad",
                    "Por favor, seleccione la fecha de inicio y fin"
            );
            return;
        }

        if (!fechaInicio.isBefore(fechaFin)) {

            UTILIDADES.mostrarAlerta(
                    "Error en fechas",
                    "La fecha inicio debe ser anterior a la fecha fin",
                    "Por favor, corrija las fechas"
            );
            return;
        }

        int idActividad = 0;
        int estadoActivo = 1;

        ActividadDTO actividadDTO = new ActividadDTO(
                idActividad,
                nombreActividad,
                duracion,
                hitos,
                Date.valueOf(fechaInicio),
                Date.valueOf(fechaFin),
                estadoActivo
        );

        VerificacionDeActividad verificacionActividad = new VerificacionDeActividad();

        List<String> camposVacios = verificacionActividad.validarCamposVacios(nombreActividad, duracion, hitos);

        if (!camposVacios.isEmpty()) {

            UTILIDADES.mostrarAlerta(
                    "Campos vacíos",
                    "Complete los campos requeridos",
                    String.join("\n", camposVacios)
            );
            return;
        }

        List<String> datosInvalidos = verificacionActividad.validarCamposInvalidos(nombreActividad, duracion, hitos);

        if (!datosInvalidos.isEmpty()) {

            UTILIDADES.mostrarAlerta(
                    "Datos inválidos",
                    "Verifique los campos",
                    String.join("\n", datosInvalidos)
            );
            return;
        }

        if (controladorCronogramaActividadesGUI != null) {
            controladorCronogramaActividadesGUI.agregarActividadSecundaria(actividadDTO);
        }

        ((Stage) botonAñadir.getScene().getWindow()).close();

    }

    @FXML
    private void cancelar() {
        ((Stage) botonCancelar.getScene().getWindow()).close();
    }


    public void setControladorPrincipal(ControladorRegistroCronogramaActividadesGUI controlador) {
        this.controladorCronogramaActividadesGUI = controlador;
    }

    public void setDatosIniciales(String matriculaEstudiante) {
        this.matriculaEstudiante = matriculaEstudiante;
    }
}