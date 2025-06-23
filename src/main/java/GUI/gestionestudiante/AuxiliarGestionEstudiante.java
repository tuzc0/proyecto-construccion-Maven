package GUI.gestionestudiante;

import GUI.utilidades.Utilidades;
import logica.DAOs.GrupoDAO;
import logica.DTOs.GrupoDTO;
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

import static GUI.ControladorInicioDeSesionGUI.numeroDePersonal;

public class AuxiliarGestionEstudiante {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(AuxiliarGestionEstudiante.class);

    private IGestorAlertas utilidades = new Utilidades();

    private ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);

    public int obtenerNRC () {

        int NRC = -1;

        Utilidades utilidades = new Utilidades();

        GrupoDAO grupoDAO = new GrupoDAO();

        try{

            GrupoDTO grupo = grupoDAO.buscarGrupoActivoPorNumeroDePersonal(numeroDePersonal);
            if (grupo.getNRC() != -1) {

                NRC = grupo.getNRC();

            } else {

                logger.error("No se encontró un grupo activo para el número de personal: " + numeroDePersonal);
                utilidades.mostrarAlerta(
                        "Grupo no encontrado",
                        "No se encontró un grupo activo",
                        "Por favor, asegúrese de que el número de personal sea correcto y que exista un grupo activo."
                );

            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al obtener el NRC: " + e);
            utilidades.mostrarAlerta(
                    "Error inesperado",
                    "Error al obtener el NRC",
                    "Por favor, intente nuevamente más tarde."
            );
        }

        return NRC;
    }
}
