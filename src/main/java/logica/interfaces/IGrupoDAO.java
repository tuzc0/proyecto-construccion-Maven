package logica.interfaces;

import logica.DTOs.GrupoDTO;
import java.sql.SQLException;

public interface IGrupoDAO {

    boolean crearNuevoGrupo(GrupoDTO grupo) throws SQLException;

    boolean eliminarGrupoPorNRC(int NRC) throws SQLException;

    boolean modificarGrupo(GrupoDTO grupo) throws SQLException;

    GrupoDTO buscarGrupoPorNRC(int NRC) throws SQLException;

    GrupoDTO mostrarGruposActivos() throws SQLException;
}
