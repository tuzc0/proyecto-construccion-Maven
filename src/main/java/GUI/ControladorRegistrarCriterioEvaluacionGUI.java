package GUI;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import logica.DAOs.CriterioEvaluacionDAO;
import logica.DTOs.CriterioEvaluacionDTO;
import logica.ManejadorExcepciones;
import logica.VerificacionEntradas;
import logica.interfaces.IGestorAlertas;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;


public class ControladorRegistrarCriterioEvaluacionGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorRegistrarCriterioEvaluacionGUI.class);

    @FXML
    TextArea textoDescripcionCriterio;

    @FXML
    Label numeroCriterio;

    @FXML
    Label etiquetaContadorDescripcion;

    Utilidades gestorVentanas = new Utilidades();
    IGestorAlertas utilidades = new Utilidades();
    VerificacionEntradas verificacionEntradas = new VerificacionEntradas();
    VerificicacionGeneral verificicacionGeneralUtilidad = new VerificicacionGeneral();
    ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);

    int numeroCriterioMasAlto;
    int nuevoNumeroCriterio;
    final int MAX_CARACTERES_DESCRIPCION = 255;

    @FXML
    public void initialize (){

        verificicacionGeneralUtilidad.contadorCaracteresTextArea(textoDescripcionCriterio,
                etiquetaContadorDescripcion, MAX_CARACTERES_DESCRIPCION);

        try {

            CriterioEvaluacionDAO criterioEvaluacionDAO = new CriterioEvaluacionDAO();
            numeroCriterioMasAlto = criterioEvaluacionDAO.obtenerNumeroCriterioMasAlto();
            nuevoNumeroCriterio = numeroCriterioMasAlto + 1;
            numeroCriterio.setText(String.valueOf(nuevoNumeroCriterio));

        } catch (SQLException e ) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado: " + e);
        }
    }

    @FXML
    public void guardarCriterio () {

        CriterioEvaluacionDAO criterioEvaluacionDAO = new CriterioEvaluacionDAO();
        String descripcion = textoDescripcionCriterio.getText();

        if (descripcion.isEmpty()) {

            gestorVentanas.mostrarAlerta("Error",
                    "La descripción no puede estar vacía.",
                    "porfavor llene todos los campos");
            return;
        }

        if (!verificacionEntradas.validarTextoAlfanumerico(descripcion)) {

            gestorVentanas.mostrarAlerta("Error",
                    "La descripción no es válida.",
                    "porfavor llene todos los campos");
            return;
        }

        try {

            CriterioEvaluacionDTO nuevoCriterio = new CriterioEvaluacionDTO();
            nuevoCriterio.setDescripcion(descripcion);
            nuevoCriterio.setNumeroCriterio(nuevoNumeroCriterio);
            nuevoCriterio.setEstadoActivo(1);
            nuevoCriterio.setIDCriterio(0);

            if (criterioEvaluacionDAO.crearNuevoCriterioEvaluacion(nuevoCriterio)) {
                gestorVentanas.mostrarAlerta("Éxito",
                        "Criterio de evaluación guardado correctamente.",
                        "se ha registrado el criterio de forma exitosa");
                nuevoNumeroCriterio ++;
                textoDescripcionCriterio.clear();
                numeroCriterio.setText(String.valueOf(nuevoNumeroCriterio));

            } else {
                gestorVentanas.mostrarAlerta("Error",
                        "No se pudo guardar el criterio de evaluación.",
                        "porfavor llene todos los campos");
            }

        } catch (SQLException e){

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
        }
    }

    @FXML
    public void cancelarCriterio() {

        textoDescripcionCriterio.clear();
    }
}
