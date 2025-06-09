package logica.interfaces;

import logica.DTOs.GrupoDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface IGrupoDAO {

    boolean crearNuevoGrupo(GrupoDTO grupo) throws SQLException, IOException;

    boolean eliminarGrupoPorNRC(int NRC) throws SQLException, IOException;

    boolean modificarGrupo(GrupoDTO grupo) throws SQLException, IOException;

    GrupoDTO buscarGrupoPorNRC(int NRC) throws SQLException, IOException;

    List<GrupoDTO> mostrarGruposActivos() throws SQLException, IOException;

}
