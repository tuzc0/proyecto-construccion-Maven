package logica.interfaces;

import logica.DTOs.OrganizacionVinculadaDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IOvDAO {

    boolean crearNuevaOrganizacion(OrganizacionVinculadaDTO ov) throws SQLException, IOException;

    boolean eliminarOrganizacionPorID(int idOv) throws SQLException, IOException;

    boolean modificarOrganizacion(OrganizacionVinculadaDTO ov) throws SQLException, IOException;

    OrganizacionVinculadaDTO buscarOrganizacionPorID(int idOv) throws SQLException, IOException;
}