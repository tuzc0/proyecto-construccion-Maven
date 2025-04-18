package logica.interfaces;

import logica.DTOs.EvidenciaEvaluacionDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IEvidenciaEvaluacionDAO {

    boolean insertarEvidenciaEvaluacion(EvidenciaEvaluacionDTO evidenciaEvaluacionDTO) throws SQLException, IOException;

    EvidenciaEvaluacionDTO mostrarEvidenciaEvaluacionPorID(int idEvidencia) throws SQLException, IOException;
}
