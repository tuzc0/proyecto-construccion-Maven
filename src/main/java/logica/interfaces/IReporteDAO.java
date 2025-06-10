package logica.interfaces;

import logica.DTOs.ReporteDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IReporteDAO {

    int insertarReporte(ReporteDTO reporte) throws SQLException, IOException;

    boolean modificarReporte(ReporteDTO reporte) throws SQLException, IOException;

    ReporteDTO buscarReportePorID(int idReporte) throws SQLException, IOException;
}
