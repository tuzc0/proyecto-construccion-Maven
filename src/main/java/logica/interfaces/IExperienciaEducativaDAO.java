package logica.interfaces;

import logica.DTOs.ExperienciaEducativaDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IExperienciaEducativaDAO {

    boolean crearNuevaExperienciaEducativa(ExperienciaEducativaDTO experienciaEducativa) throws SQLException, IOException;

    boolean modificarExperienciaEducativa(ExperienciaEducativaDTO experienciaEducativa) throws SQLException, IOException;

    ExperienciaEducativaDTO mostrarExperienciaEducativa( ) throws SQLException, IOException;
}
