package logica.interfaces;

import logica.DTOs.OvDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IOvDAO {

    boolean crearNuevaOv(OvDTO ov) throws SQLException, IOException;

    boolean eliminarOvPorID(int idOv) throws SQLException, IOException;

    boolean modificarOv(OvDTO ov) throws SQLException, IOException;

    OvDTO buscarOvPorID(int idOv) throws SQLException, IOException;
}