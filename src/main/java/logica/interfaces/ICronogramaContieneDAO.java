package logica.interfaces;

import logica.DTOs.CronogramaContieneDTO;
import java.io.IOException;
import java.sql.SQLException;

public interface ICronogramaContieneDAO {

    boolean insertarCronogramaContiene(CronogramaContieneDTO cronograma) throws SQLException, IOException;

    boolean eliminarCronogramaContienePorID(int idCronograma) throws SQLException, IOException;

    boolean modificarActividadesDeCronograma(CronogramaContieneDTO cronograma) throws SQLException, IOException;

    CronogramaContieneDTO buscarCronogramaContienePorID(int idCronograma) throws SQLException, IOException;
}
