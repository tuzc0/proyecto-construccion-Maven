package logica.interfaces;

import logica.DTOs.AcademicoEvaluadorDTO;
import java.sql.SQLException;

public interface IAcademicoEvaluadorDAO {

    boolean insertarAcademicoEvaluador(AcademicoEvaluadorDTO academicoEvaluador) throws SQLException;

    boolean eliminarAcademicoEvaluadorPorNumeroDePersonal(int numeroDePersonal) throws SQLException;

    boolean modificarAcademicoEvaluador(AcademicoEvaluadorDTO academicoEvaluador) throws SQLException;

    AcademicoEvaluadorDTO buscarAcademicoEvaluadorPorNumeroDePersonal(int numeroDePersonal) throws SQLException;
}
