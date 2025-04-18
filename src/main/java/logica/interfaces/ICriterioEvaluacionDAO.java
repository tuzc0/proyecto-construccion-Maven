package logica.interfaces;

import logica.DTOs.CriterioEvaluacionDTO;
import java.sql.SQLException;

public interface ICriterioEvaluacionDAO {

    boolean crearNuevoCriterioEvaluacion(CriterioEvaluacionDTO criterio) throws SQLException;

    boolean eliminarCriterioEvaluacionPorID(int idCriterio) throws SQLException;

    boolean modificarCriterioEvaluacion(CriterioEvaluacionDTO criterio) throws SQLException;

    CriterioEvaluacionDTO buscarCriterioEvaluacionPorID(int idCriterio) throws SQLException;
}
