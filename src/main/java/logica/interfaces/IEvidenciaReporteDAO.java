package logica.interfaces;

import logica.DTOs.EvidenciaReporteDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IEvidenciaReporteDAO {

    boolean insertarEvidenciaReporte(EvidenciaReporteDTO evidenciaReporteDTO) throws SQLException, IOException;

    EvidenciaReporteDTO mostrarEvidenciaReportePorID(int idEvidencia) throws SQLException, IOException;

}
