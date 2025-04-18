package logica.interfaces;

import logica.DTOs.ReporteContieneDTO;
import java.io.IOException;
import java.sql.SQLException;

public interface IReporteContieneDAO {

    boolean insertarReporteContiene(ReporteContieneDTO reporte) throws SQLException, IOException;

    boolean eliminarReporteContienePorID(int idReporte) throws SQLException, IOException;

    boolean modificarReporteContiene(ReporteContieneDTO reporte) throws SQLException, IOException;

    ReporteContieneDTO buscarReporteContienePorID(int idReporte) throws SQLException, IOException;
}
