package logica.interfaces;

import logica.DTOs.AcademicoDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IAcademicoDAO {

    boolean insertarAcademico(AcademicoDTO academico) throws SQLException, IOException;

    boolean eliminarAcademicoPorNumeroDePersonal(int estadoActivo, String numeroDePersonal) throws SQLException, IOException;

    boolean modificarAcademico(AcademicoDTO academico) throws SQLException;

    AcademicoDTO buscarAcademicoPorNumeroDePersonal(int numeroDePersonal) throws SQLException;
}
