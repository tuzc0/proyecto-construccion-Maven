package logica.interfaces;

import logica.DTOs.UsuarioDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IUsuarioDAO {

    int insertarUsuario(UsuarioDTO usuario) throws SQLException, IOException;

    boolean eliminarUsuarioPorID(int idUsuario) throws SQLException, IOException;

    boolean modificarUsuario(UsuarioDTO usuario) throws SQLException, IOException;

    UsuarioDTO buscarUsuarioPorID(int idUsuario) throws SQLException, IOException;
}