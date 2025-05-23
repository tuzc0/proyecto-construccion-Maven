package GUI.gestioncronogramaactividades;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import logica.DAOs.CronogramaActividadesDAO;
import logica.DTOs.CronogramaActividadesDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import GUI.utilidades.Utilidades;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControladorRegistroCronogramaActividades {

    private static final Logger LOGGER = LogManager.getLogger(ControladorRegistroCronogramaActividades.class);

    @FXML private TextArea textoAgostoFebrero;
    @FXML private TextArea textoSeptiembreMarzo;
    @FXML private TextArea textoOctubreAbril;
    @FXML private TextArea textoNoviembreMayo;
    @FXML private Button botonRegistrar;
    @FXML private Button botonCancelar;

    private final Utilidades UTILIDADES = new Utilidades();
    private Stage escenarioActual;

    @FXML
    private void initialize() {

        botonRegistrar.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
    }

    public void setEscenarioActual(Stage escenario) {
        this.escenarioActual = escenario;
    }

    private Integer idCronogramaRegistrado = null;

    @FXML
    private int registrarCronogramaActividad() {

        String agostoFebrero = textoAgostoFebrero.getText();
        String septiembreMarzo = textoSeptiembreMarzo.getText();
        String octubreAbril = textoOctubreAbril.getText();
        String noviembreMayo = textoNoviembreMayo.getText();

        List<String> camposVacios = camposVaciosCronogramaActividades(agostoFebrero, septiembreMarzo,
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

        List<String> erroresValidacion = validarCamposCronograma(agostoFebrero, septiembreMarzo,
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
            int resultadoInvalido = -1;

            if (idCronogramaInsertado != resultadoInvalido) {

                this.idCronogramaRegistrado = idCronogramaInsertado;
                LOGGER.info("Cronograma de actividades registrado exitosamente.");
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

            return idCronogramaRegistrado != null ? idCronogramaRegistrado : -1;

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

    public Integer getIdCronogramaRegistrado() {

        return this.idCronogramaRegistrado;
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

    public static List<String> camposVaciosCronogramaActividades(String agostoFebrero,
                                                                 String septiembreMarzo,
                                                                 String octubreAbril,
                                                                 String noviembreMayo) {

        List<String> errores = new ArrayList<>();

        if (agostoFebrero.isEmpty()) {
            errores.add("El campo de actividades para agosto-febrero no puede estar vacío.");
        }

        if (septiembreMarzo.isEmpty()) {
            errores.add("El campo de actividades para septiembre-marzo no puede estar vacío.");
        }

        if (octubreAbril.isEmpty()) {
            errores.add("El campo de actividades para octubre-abril no puede estar vacío.");
        }

        if (noviembreMayo.isEmpty()) {
            errores.add("El campo de actividades para noviembre-mayo no puede estar vacío.");
        }

        return errores;
    }

    public static boolean validarTextoCronogramaActividades(String texto) {

        String[] caracteres = texto.trim().split("\\s+");
        return caracteres.length > 3 && texto.length() <= 255;
    }

    private List<String> validarCamposCronograma(String agostoFebrero, String septiembreMarzo,
                                                 String octubreAbril, String noviembreMayo) {

        List<String> errores = new ArrayList<>();

        if (!validarTextoCronogramaActividades(agostoFebrero)) {
            errores.add("El campo de actividades para agosto-febrero " +
                    "debe tener más de 3 palabras y no exceder 255 caracteres.");
        }
        if (!validarTextoCronogramaActividades(septiembreMarzo)) {
            errores.add("El campo de actividades para septiembre-marzo " +
                    "debe tener más de 3 palabras y no exceder 255 caracteres.");
        }
        if (!validarTextoCronogramaActividades(octubreAbril)) {
            errores.add("El campo de actividades para octubre-abril " +
                    "debe tener más de 3 palabras y no exceder 255 caracteres.");
        }
        if (!validarTextoCronogramaActividades(noviembreMayo)) {
            errores.add("El campo de actividades para noviembre-mayo debe " +
                    "tener más de 3 palabras y no exceder 255 caracteres.");
        }

        return errores;
    }
}
