package logica.interfaces;

import logica.DTOs.CriterioEvaluacionDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface ICriterioEvaluacionDAO {

    boolean crearNuevoCriterioEvaluacion(CriterioEvaluacionDTO criterio) throws SQLException, IOException;

    boolean eliminarCriterioEvaluacionPorID(int idCriterio) throws SQLException, IOException;

    boolean modificarCriterioEvaluacion(CriterioEvaluacionDTO criterio) throws SQLException, IOException;

    CriterioEvaluacionDTO buscarCriterioEvaluacionPorID(int idCriterio) throws SQLException, IOException;
}
