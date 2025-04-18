package logica.interfaces;

import logica.DTOs.EvaluacionContieneDTO;
import java.io.IOException;
import java.sql.SQLException;

public interface IEvaluacionContieneDAO {

    boolean insertarEvaluacionContiene(EvaluacionContieneDTO evaluacion) throws SQLException, IOException;

    boolean modificarCalificacion(EvaluacionContieneDTO evaluacion) throws SQLException, IOException;

    EvaluacionContieneDTO buscarEvaluacionContienePorID(int idEvaluacion, int idCriterio) throws SQLException, IOException;
}
