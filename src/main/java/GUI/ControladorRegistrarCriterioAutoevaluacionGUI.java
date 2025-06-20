package GUI;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import logica.DAOs.CriterioAutoevaluacionDAO;
import logica.DTOs.CriterioAutoevaluacionDTO;
import logica.VerificacionEntradas;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class ControladorRegistrarCriterioAutoevaluacionGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorRegistrarCriterioAutoevaluacionGUI.class);

    @FXML
    TextArea textoDescripcionCriterio;

    @FXML
    Label etiquetaNumeroCriterio;

    @FXML
    Label etiquetaContadorDescripcion;

    Utilidades utilidades = new Utilidades();

    int numeroCriterioMasAlto = 0;

    int nuevoNumeroCriterio = 0;

    VerificicacionGeneral verificicacionGeneralUtilidad = new VerificicacionGeneral();

    VerificacionEntradas verificacionEntradas = new VerificacionEntradas();

    final int MAX_CARACTERES_DESCRIPCION = 255;

    @FXML
    public void initialize() {

        verificicacionGeneralUtilidad.contadorCaracteresTextArea(textoDescripcionCriterio,
                etiquetaContadorDescripcion, MAX_CARACTERES_DESCRIPCION);

        try {

            CriterioAutoevaluacionDAO criterioAutoevaluacionDAO = new CriterioAutoevaluacionDAO();
            numeroCriterioMasAlto = criterioAutoevaluacionDAO.obtenerNumeroCriterioMasAlto();
            nuevoNumeroCriterio = numeroCriterioMasAlto + 1;
            etiquetaNumeroCriterio.setText(String.valueOf(nuevoNumeroCriterio));

        } catch (SQLException e) {

            logger.error("Error al obtener el número de criterio más alto: " + e);
            e.printStackTrace();

        } catch (IOException e) {

            logger.error("Error de IO: " + e);

        } catch (Exception e) {

            logger.error("Error inesperado: " + e);
            e.printStackTrace();

        }
    }

    @FXML
    public void guardarCriterio() {

        CriterioAutoevaluacionDAO criterioAutoevaluacionDAO = new CriterioAutoevaluacionDAO();
        String descripcion = textoDescripcionCriterio.getText();

        if (descripcion.isEmpty()) {

            utilidades.mostrarAlerta("Error",
                    "La descripción no puede estar vacía.",
                    "Por favor llene todos los campos.");
            return;
        }

        if (!verificacionEntradas.validarTextoAlfanumerico(descripcion)) {

            utilidades.mostrarAlerta("Error",
                    "Descripción inválida.",
                    "La descripción no puede contener caracteres especiales.");
            return;
        }



        try {

            CriterioAutoevaluacionDTO nuevoCriterio = new CriterioAutoevaluacionDTO();
            nuevoCriterio.setDescripcion(descripcion);
            nuevoCriterio.setNumeroCriterio(nuevoNumeroCriterio);
            nuevoCriterio.setEstadoActivo(1);
            nuevoCriterio.setIDCriterio(0);

            if (criterioAutoevaluacionDAO.crearNuevoCriterioAutoevaluacion(nuevoCriterio)) {

                utilidades.mostrarAlerta("Éxito",
                        "Criterio de autoevaluación guardado correctamente.",
                        "Se ha registrado el criterio de forma exitosa.");
                nuevoNumeroCriterio++;
                textoDescripcionCriterio.clear();
                etiquetaNumeroCriterio.setText(String.valueOf(nuevoNumeroCriterio));

            } else {

                utilidades.mostrarAlerta("Error",
                        "No se pudo guardar el criterio de autoevaluación.",
                        "Por favor intente de nuevo.");
            }
        } catch (SQLException e) {

            utilidades.mostrarAlerta("Error",
                    "Error al guardar el criterio de autoevaluación.",
                    "Por favor intente de nuevo.");
            logger.error("Error al guardar el criterio de autoevaluación: " + e);

        } catch (IOException e) {

            utilidades.mostrarAlerta("Error",
                    "Error de IO.",
                    "Por favor intente de nuevo.");
            logger.error("Error de IO: " + e);

        }
    }

    @FXML
    public void cancelarCriterio() {
        textoDescripcionCriterio.clear();
    }


}
