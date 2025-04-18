package logica.interfaces;

import logica.DTOs.AcademicoDTO;
import java.sql.SQLException;

public interface IAcademicoDAO {

    boolean insertarAcademico(AcademicoDTO academico) throws SQLException;

    boolean eliminarAcademicoPorNumeroDePersonal(int estadoActivo, String numeroDePersonal) throws SQLException;

    boolean modificarAcademico(AcademicoDTO academico) throws SQLException;

    AcademicoDTO buscarAcademicoPorNumeroDePersonal(int numeroDePersonal) throws SQLException;
}
