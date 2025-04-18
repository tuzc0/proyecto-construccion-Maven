package logica.interfaces;

import logica.DTOs.AutoEvaluacionContieneDTO;
import java.io.IOException;
import java.sql.SQLException;

public interface IAutoevaluacionContieneDAO {

    public boolean insertarAutoevaluacionContiene(AutoEvaluacionContieneDTO autoevalucion) throws SQLException, IOException;

    public boolean modificarCalificacion(AutoEvaluacionContieneDTO autoevaluacion) throws SQLException, IOException;

    public AutoEvaluacionContieneDTO buscarAutoevaluacionContienePorID(int idAutoevaluacion, int idCriterio) throws SQLException, IOException;
}
