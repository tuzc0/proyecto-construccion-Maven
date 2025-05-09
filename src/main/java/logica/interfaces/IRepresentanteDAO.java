package logica.interfaces;

import logica.DTOs.RepresentanteDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IRepresentanteDAO {

    boolean insertarRepresentante(RepresentanteDTO representante) throws SQLException, IOException;

    boolean eliminarRepresentantePorID(int idRepresentante) throws SQLException, IOException;

    boolean modificarRepresentante(RepresentanteDTO representante) throws SQLException, IOException;

    RepresentanteDTO buscarRepresentantePorID(int idRepresentante) throws SQLException, IOException;

    RepresentanteDTO buscarRepresentantePorCorreo(String correo) throws SQLException, IOException;

    RepresentanteDTO buscarRepresentantePorTelefono(String telefono) throws SQLException, IOException;
}
