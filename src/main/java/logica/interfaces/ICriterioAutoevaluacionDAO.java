package logica.interfaces;


import logica.DTOs.CriterioAutoevaluacionDTO;
import java.sql.SQLException;

public interface ICriterioAutoevaluacionDAO {

    boolean crearNuevoCriterioAutoevaluacion(CriterioAutoevaluacionDTO criterio) throws SQLException;

    boolean eliminarCriterioAutoevaluacionPorNumeroDeCriterio(int numeroDeCriterio) throws SQLException;

    boolean modificarCriterioAutoevaluacion(CriterioAutoevaluacionDTO criterio) throws SQLException;

    CriterioAutoevaluacionDTO buscarCriterioAutoevaluacionPorID(int numeroDeCriterio) throws SQLException;
}
