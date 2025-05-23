package logica.interfaces;

import logica.DTOs.EvaluacionDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IEvaluacionDAO {

    int crearNuevaEvaluacion(EvaluacionDTO evaluacion) throws SQLException, IOException;

    boolean eliminarEvaluacionPorID(int estadoActivo, int idEvaluacion) throws SQLException, IOException;

    boolean modificarEvaluacion(EvaluacionDTO evaluacion) throws SQLException, IOException;

    EvaluacionDTO buscarEvaluacionPorID(int idEvaluacion) throws SQLException, IOException;
}
