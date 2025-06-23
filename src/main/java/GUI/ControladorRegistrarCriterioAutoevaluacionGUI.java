package GUI;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import logica.DAOs.CriterioAutoevaluacionDAO;
import logica.DTOs.CriterioAutoevaluacionDTO;
import logica.ManejadorExcepciones;
import logica.VerificacionEntradas;
import logica.interfaces.IGestorAlertas;
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

    int numeroCriterioMasAlto = 0;
    int nuevoNumeroCriterio = 0;
    final int MAX_CARACTERES_DESCRIPCION = 255;

    Utilidades gestorVentana = new Utilidades();
    IGestorAlertas utilidades = new Utilidades();
    VerificicacionGeneral verificicacionGeneralUtilidad = new VerificicacionGeneral();
    VerificacionEntradas verificacionEntradas = new VerificacionEntradas();
    ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);

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

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado: " + e);
        }
    }

    @FXML
    public void guardarCriterio() {

        CriterioAutoevaluacionDAO criterioAutoevaluacionDAO = new CriterioAutoevaluacionDAO();
        String descripcion = textoDescripcionCriterio.getText();

        if (descripcion.isEmpty()) {

            gestorVentana.mostrarAlerta("Error",
                    "La descripción no puede estar vacía.",
                    "Por favor llene todos los campos.");
            return;
        }

        if (!verificacionEntradas.validarTextoAlfanumerico(descripcion)) {

            gestorVentana.mostrarAlerta("Error",
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

                gestorVentana.mostrarAlerta("Éxito",
                        "Criterio de autoevaluación guardado correctamente.",
                        "Se ha registrado el criterio de forma exitosa.");
                nuevoNumeroCriterio++;
                textoDescripcionCriterio.clear();
                etiquetaNumeroCriterio.setText(String.valueOf(nuevoNumeroCriterio));

            } else {

                gestorVentana.mostrarAlerta("Error",
                        "No se pudo guardar el criterio de autoevaluación.",
                        "Por favor intente de nuevo.");
            }

        } catch (SQLException e) {

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
