package logica.interfaces;


import logica.DTOs.CriterioAutoevaluacionDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface ICriterioAutoevaluacionDAO {

    boolean crearNuevoCriterioAutoevaluacion(CriterioAutoevaluacionDTO criterio) throws SQLException, IOException;

    boolean eliminarCriterioAutoevaluacionPorNumeroDeCriterio(int numeroDeCriterio) throws SQLException, IOException;

    boolean modificarCriterioAutoevaluacion(CriterioAutoevaluacionDTO criterio) throws SQLException, IOException;

    CriterioAutoevaluacionDTO buscarCriterioAutoevaluacionPorID(int numeroDeCriterio) throws SQLException, IOException;
}
