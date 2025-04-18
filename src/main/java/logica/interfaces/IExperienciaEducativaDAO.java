package logica.interfaces;

import logica.DTOs.ExperienciaEducativaDTO;
import java.sql.SQLException;

public interface IExperienciaEducativaDAO {

    boolean crearNuevaExperienciaEducativa(ExperienciaEducativaDTO experienciaEducativa) throws SQLException;

    boolean modificarExperienciaEducativa(ExperienciaEducativaDTO experienciaEducativa) throws SQLException;

    ExperienciaEducativaDTO mostrarExperienciaEducativa( ) throws SQLException;
}
