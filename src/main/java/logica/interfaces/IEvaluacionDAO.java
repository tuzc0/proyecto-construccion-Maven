package logica.interfaces;

import logica.DTOs.EvaluacionDTO;
import java.sql.SQLException;

public interface IEvaluacionDAO {

    boolean crearNuevaEvaluacion(EvaluacionDTO evaluacion) throws SQLException;

    boolean eliminarEvaluacionPorID(int estadoActivo, int idEvaluacion) throws SQLException;

    boolean modificarEvaluacion(EvaluacionDTO evaluacion) throws SQLException;

    EvaluacionDTO buscarEvaluacionPorID(int idEvaluacion) throws SQLException;
}
