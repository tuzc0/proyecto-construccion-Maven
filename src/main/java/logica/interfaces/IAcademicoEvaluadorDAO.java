package logica.interfaces;

import logica.DTOs.AcademicoEvaluadorDTO;
import java.sql.SQLException;
import java.io.IOException;

public interface IAcademicoEvaluadorDAO {

    boolean insertarAcademicoEvaluador(AcademicoEvaluadorDTO academicoEvaluador) throws SQLException, IOException;

    boolean eliminarAcademicoEvaluadorPorNumeroDePersonal(int numeroDePersonal) throws SQLException, IOException;

    boolean modificarAcademicoEvaluador(AcademicoEvaluadorDTO academicoEvaluador) throws SQLException, IOException;

    AcademicoEvaluadorDTO buscarAcademicoEvaluadorPorNumeroDePersonal(int numeroDePersonal) throws SQLException, IOException;
}
