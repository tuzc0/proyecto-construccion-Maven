package logica.interfaces;

import logica.DTOs.AcademicoDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface IAcademicoDAO {

    boolean insertarAcademico(AcademicoDTO academico) throws SQLException, IOException;

    boolean eliminarAcademicoPorNumeroDePersonal(String numeroDePersonal) throws SQLException, IOException;

    boolean modificarAcademico(AcademicoDTO academico) throws SQLException, IOException;

    AcademicoDTO buscarAcademicoPorNumeroDePersonal(int numeroDePersonal) throws SQLException, IOException;

    List<AcademicoDTO> listarAcademicos() throws SQLException, IOException;
}
