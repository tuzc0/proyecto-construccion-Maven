package logica.interfaces;

import logica.DTOs.ActividadDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IActividadDAO {

    int crearNuevaActividad(ActividadDTO actividad) throws SQLException, IOException;

    boolean eliminarActividadPorID(int idActividad) throws SQLException, IOException;

    boolean modificarActividad(ActividadDTO actividad) throws SQLException, IOException;

    ActividadDTO buscarActividadPorID(int idActividad) throws SQLException, IOException;
}

