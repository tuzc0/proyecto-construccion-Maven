package logica.interfaces;

import logica.DTOs.EstudianteDTO;
import java.sql.SQLException;

public interface IEstudianteDAO {

    boolean insertarEstudiante(EstudianteDTO estudiante) throws SQLException;

    boolean eliminarEstudiantePorMatricula(int estadoActivo,String matricula) throws SQLException;

    boolean modificarEstudiante(EstudianteDTO estudiante) throws SQLException;

    EstudianteDTO buscarEstudiantePorMatricula(String matricula) throws SQLException;
}
