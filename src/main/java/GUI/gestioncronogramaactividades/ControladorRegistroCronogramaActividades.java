package GUI.gestioncronogramaactividades;

import GUI.gestionproyecto.ControladorRegistroProyectoGUI;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import GUI.utilidades.Utilidades;

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
        return 0;
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
}
