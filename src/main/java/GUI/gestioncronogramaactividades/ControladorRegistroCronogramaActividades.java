package GUI.gestioncronogramaactividades;

import GUI.gestionproyecto.ControladorRegistroProyectoGUI;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import logica.DAOs.CronogramaActividadesDAO;
import logica.DTOs.CronogramaActividadesDTO;
import logica.verificacion.VerificicacionGeneral;
import logica.verificacion.VerificacionDeCronogramaActividades;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import GUI.utilidades.Utilidades;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorRegistroCronogramaActividades {

    private static final Logger LOGGER = LogManager.getLogger(ControladorRegistroCronogramaActividades.class);

    @FXML private TextArea textoAgostoFebrero;
    @FXML private TextArea textoSeptiembreMarzo;
    @FXML private TextArea textoOctubreAbril;
    @FXML private TextArea textoNoviembreMayo;
    @FXML private Label contadorAgostoFebrero;
    @FXML private Label contadorSeptiembreMarzo;
    @FXML private Label contadorOctubreAbril;
    @FXML private Label contadorNoviembreMayo;
    @FXML private Button botonRegistrar;
    @FXML private Button botonCancelar;

    private final Utilidades UTILIDADES = new Utilidades();
    private Stage escenarioActual;

    @FXML
    private void initialize() {

        VerificicacionGeneral verficacionGeneral = new VerificicacionGeneral();
        int numeroMaximoDeCaracteres = 255;

        verficacionGeneral.contadorCaracteresTextArea(textoAgostoFebrero, contadorAgostoFebrero, numeroMaximoDeCaracteres);
        verficacionGeneral.contadorCaracteresTextArea(textoSeptiembreMarzo, contadorSeptiembreMarzo, numeroMaximoDeCaracteres);
        verficacionGeneral.contadorCaracteresTextArea(textoOctubreAbril, contadorOctubreAbril, numeroMaximoDeCaracteres);
        verficacionGeneral.contadorCaracteresTextArea(textoNoviembreMayo, contadorNoviembreMayo, numeroMaximoDeCaracteres);

        botonRegistrar.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
    }

    private Integer idCronogramaRegistrado = null;

    @FXML
    private int registrarCronogramaActividad() {

        String agostoFebrero = textoAgostoFebrero.getText();
        String septiembreMarzo = textoSeptiembreMarzo.getText();
        String octubreAbril = textoOctubreAbril.getText();
        String noviembreMayo = textoNoviembreMayo.getText();

        VerificacionDeCronogramaActividades verificacionCronograma = new VerificacionDeCronogramaActividades();

        List<String> camposVacios = verificacionCronograma.camposVaciosCronogramaActividades(agostoFebrero, septiembreMarzo,
                octubreAbril, noviembreMayo);

        if (!camposVacios.isEmpty()) {

            String mensaje = String.join("\n", camposVacios);
            UTILIDADES.mostrarAlerta(
                    "Campos vacíos",
                    "Por favor, complete todos los campos requeridos.",
                    mensaje
            );
            return -1;
        }

        List<String> erroresValidacion = verificacionCronograma.validarCamposCronograma(agostoFebrero, septiembreMarzo,
                octubreAbril, noviembreMayo);

        if (!erroresValidacion.isEmpty()) {

            String mensaje = String.join("\n", erroresValidacion);
            UTILIDADES.mostrarAlerta(
                    "Errores de validación",
                    "Por favor, ingrese información válida en los campos.",
                    mensaje
            );
            return -1;
        }

        String estudiante = "000000000";
        CronogramaActividadesDTO cronogramaActividadesDTO = new CronogramaActividadesDTO(
                0, estudiante, agostoFebrero, septiembreMarzo,
                octubreAbril, noviembreMayo);
        CronogramaActividadesDAO cronogramaActividadesDAO= new CronogramaActividadesDAO();

        try {

            int idCronogramaInsertado = cronogramaActividadesDAO.
                    crearNuevoCronogramaDeActividades(cronogramaActividadesDTO);

            if (idCronogramaInsertado != -1) {

                this.idCronogramaRegistrado = idCronogramaInsertado;
                LOGGER.info("Cronograma de actividades registrado exitosamente.");

                if(controladorPadre != null) {
                    controladorPadre.actualizarEstadoCronogramaActividades(true);
                }

                UTILIDADES.mostrarConfirmacion(
                        "Registro exitoso",
                        "El cronograma de actividades se ha registrado correctamente.",
                        ""
                );

                cerrarVentana();

            } else {

                LOGGER.error("Error al registrar el cronograma de actividades.");
                UTILIDADES.mostrarAlerta(
                        "Error",
                        "No se pudo registrar el cronograma de actividades.",
                        "Por favor, intente de nuevo más tarde."
                );
            }

            if (idCronogramaRegistrado != null) {

                return idCronogramaRegistrado;
            } else {

                return -1;
            }

        } catch (SQLException e) {

            LOGGER.error("Error de base de datos al registrar cronograma: " + e.getMessage());
            UTILIDADES.mostrarAlerta(
                    "Error del sistema",
                    "Ocurrió un error de base de datos.",
                    e.getMessage()
            );

        } catch (IOException e) {

            LOGGER.error("Error de E/S al registrar cronograma: " + e.getMessage());
            UTILIDADES.mostrarAlerta(
                    "Error del sistema",
                    "Ocurrió un error de entrada/salida.",
                    e.getMessage()
            );
        }

        return -1;
    }

    @FXML
    private void botonCancelarSeleccion() {

        LOGGER.info("Cancelando registro de cronograma de actividades.");
        cerrarVentana();
    }

    private void cerrarVentana() {

        if (escenarioActual != null) {

            escenarioActual.close();

        } else {

            Stage stage = (Stage) botonCancelar.getScene().getWindow();
            stage.close();
        }
    }

    public Integer getIdCronogramaRegistrado() {

        return this.idCronogramaRegistrado;
    }

    public void setEscenarioActual(Stage escenario) {

        this.escenarioActual = escenario;
    }

    private ControladorRegistroProyectoGUI controladorPadre;

    public void asignarControladorPadre(ControladorRegistroProyectoGUI controladorRegistroProyecto) {
        this.controladorPadre = controladorRegistroProyecto;
    }
}
