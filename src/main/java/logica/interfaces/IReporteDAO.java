package logica.interfaces;

import logica.DTOs.ReporteDTO;
import java.sql.SQLException;

public interface IReporteDAO {

    boolean insertarReporte(ReporteDTO reporte) throws SQLException;

    boolean modificarReporte(ReporteDTO reporte) throws SQLException;

    ReporteDTO buscarReportePorID(int idReporte) throws SQLException;
}
