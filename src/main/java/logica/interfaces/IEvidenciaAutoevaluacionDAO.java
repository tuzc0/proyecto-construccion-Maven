package logica.interfaces;

import logica.DTOs.EvidenciaAutoevaluacionDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IEvidenciaAutoevaluacionDAO {

    boolean insertarEvidenciaAutoevaluacion(EvidenciaAutoevaluacionDTO evidencia) throws SQLException, IOException;

    EvidenciaAutoevaluacionDTO mostrarEvidenciaAutoevaluacionPorID(int idEvidencia) throws SQLException, IOException;
}
