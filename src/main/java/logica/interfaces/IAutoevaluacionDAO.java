package logica.interfaces;

import logica.DTOs.AutoevaluacionDTO;
import java.sql.SQLException;

public interface IAutoevaluacionDAO {

    boolean crearNuevaAutoevaluacion(AutoevaluacionDTO autoevaluacion) throws SQLException;

    boolean eliminarAutoevaluacionPorID(int idAutoevaluacion) throws SQLException;

    boolean modificarAutoevaluacion(AutoevaluacionDTO autoevaluacion) throws SQLException;

    AutoevaluacionDTO buscarAutoevaluacionPorID(int idAutoevaluacion) throws SQLException;
}