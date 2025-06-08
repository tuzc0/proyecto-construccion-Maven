package logica.interfaces;

import logica.DTOs.AutoevaluacionDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IAutoevaluacionDAO {

    int crearNuevaAutoevaluacion(AutoevaluacionDTO autoevaluacion) throws SQLException, IOException;

    boolean eliminarAutoevaluacionPorID(int idAutoevaluacion) throws SQLException, IOException;

    boolean modificarAutoevaluacion(AutoevaluacionDTO autoevaluacion) throws SQLException, IOException;

    AutoevaluacionDTO buscarAutoevaluacionPorID(int idAutoevaluacion) throws SQLException, IOException;
}