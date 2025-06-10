package GUI;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import logica.DAOs.CriterioEvaluacionDAO;
import logica.DTOs.CriterioEvaluacionDTO;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;


public class ControladorRegistrarCriterioEvaluacionGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorRegistrarCriterioEvaluacionGUI.class);

    @FXML
    TextArea textoDescripcionCriterio;

    @FXML
    Label numeroCriterio;

    Utilidades utilidades = new Utilidades();

    int numeroCriterioMasAlto;

    int nuevoNumeroCriterio;

    @FXML
    public void initialize (){

        try {

            CriterioEvaluacionDAO criterioEvaluacionDAO = new CriterioEvaluacionDAO();
            numeroCriterioMasAlto = criterioEvaluacionDAO.obtenerNumeroCriterioMasAlto();
            nuevoNumeroCriterio = numeroCriterioMasAlto + 1;
            numeroCriterio.setText(String.valueOf(nuevoNumeroCriterio));

        } catch (SQLException e ) {

            logger.error("Error al obtener el número de criterio más alto: " + e);

        } catch (IOException e) {

            logger.error("Error de IO: " + e);

        } catch (Exception e) {

            logger.error("Error inesperado: " + e);

        }

    }


    @FXML
    public void guardarCriterio () {

        CriterioEvaluacionDAO criterioEvaluacionDAO = new CriterioEvaluacionDAO();
        String descripcion = textoDescripcionCriterio.getText();

        if (descripcion.isEmpty()) {

            utilidades.mostrarAlerta("Error",
                    "La descripción no puede estar vacía.",
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
                utilidades.mostrarAlerta("Éxito",
                        "Criterio de evaluación guardado correctamente.",
                        "se ha registrado el criterio de forma exitosa");
                nuevoNumeroCriterio ++;
                textoDescripcionCriterio.clear();
                numeroCriterio.setText(String.valueOf(nuevoNumeroCriterio));

            } else {
                utilidades.mostrarAlerta("Error",
                        "No se pudo guardar el criterio de evaluación.",
                        "porfavor llene todos los campos");
            }

        } catch (SQLException e){

            utilidades.mostrarAlerta("Error",
                    "Error al obtener el número de criterio más alto.",
                    "porfavor llene todos los campos");
            logger.error("Error al obtener el número de criterio más alto: " + e);
        } catch (IOException e) {

            utilidades.mostrarAlerta("Error",
                    "Error al guardar el criterio de evaluación.",
                    "porfavor llene todos los campos");
            logger.error("Error al guardar el criterio de evaluación: " + e);
        }

    }

    @FXML
    public void cancelarCriterio() {

        textoDescripcionCriterio.clear();

    }
}
