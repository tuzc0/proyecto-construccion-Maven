package logica.interfaces;

import logica.DTOs.ProyectoDTO;
import java.sql.SQLException;

public interface IProyectoDAO {

    boolean crearNuevoProyecto(ProyectoDTO proyecto) throws SQLException;

    boolean eliminarProyectoPorID(int idProyecto) throws SQLException;

    boolean modificarProyecto(ProyectoDTO proyecto) throws SQLException;

    ProyectoDTO buscarProyectoPorID(int idProyecto) throws SQLException;
}
