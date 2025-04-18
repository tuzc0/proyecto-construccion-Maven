package logica.interfaces;

import logica.DTOs.EstudianteDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IEstudianteDAO {

    boolean insertarEstudiante(EstudianteDTO estudiante) throws SQLException, IOException;

    boolean eliminarEstudiantePorMatricula(int estadoActivo,String matricula) throws SQLException, IOException;

    boolean modificarEstudiante(EstudianteDTO estudiante) throws SQLException, IOException;

    EstudianteDTO buscarEstudiantePorMatricula(String matricula) throws SQLException, IOException;
}
