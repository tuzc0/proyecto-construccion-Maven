package logica.interfaces;

import logica.DTOs.ActividadDTO;
import java.sql.SQLException;

public interface IActividadDAO {

    boolean crearNuevaActividad(ActividadDTO actividad) throws SQLException;

    boolean eliminarActividadPorID(int idActividad) throws SQLException;

    boolean modificarActividad(ActividadDTO actividad) throws SQLException;

    ActividadDTO buscarActividadPorID(int idActividad) throws SQLException;
}

