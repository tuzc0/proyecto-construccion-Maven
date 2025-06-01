package logica.interfaces;

import logica.DTOs.ProyectoDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IProyectoDAO {

    int crearNuevoProyecto(ProyectoDTO proyecto) throws SQLException, IOException;

    boolean eliminarProyectoPorID(int idProyecto) throws SQLException, IOException;

    boolean modificarProyecto(ProyectoDTO proyecto) throws SQLException, IOException;

    ProyectoDTO buscarProyectoPorNombre(String nombreProyecto) throws SQLException, IOException;
}
