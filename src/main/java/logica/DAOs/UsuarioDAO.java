package logica.DAOs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.UsuarioDTO;
import logica.interfaces.IUsuarioDAO;

public class UsuarioDAO implements IUsuarioDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement consultaPreparada = null;
    ResultSet resultadoConsulta;

    public int insertarUsuario(UsuarioDTO usuario) throws SQLException, IOException {

        String consultaSQL = "INSERT INTO usuario (nombre, apellidos, estadoActivo) VALUES (?, ?, ?)";
        int idUsuarioGenerado = -1;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            consultaPreparada.setString(1, usuario.getNombre());
            consultaPreparada.setString(2, usuario.getApellido());
            consultaPreparada.setInt(3, usuario.getEstado());
            consultaPreparada.executeUpdate();

            ResultSet generatedKeys = consultaPreparada.getGeneratedKeys();

            if (generatedKeys.next()) {

                idUsuarioGenerado = generatedKeys.getInt(1);
            }

        } finally {

            if (consultaPreparada != null) {
                consultaPreparada.close();
            }
        }

        return idUsuarioGenerado;
    }

    public boolean eliminarUsuarioPorID(int idUsuario) throws SQLException, IOException {

        String consultaSQL = "UPDATE usuario SET estadoActivo = ? WHERE idUsuario = ?";
        boolean usuarioEliminado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, 0);
            consultaPreparada.setInt(2, idUsuario);

            if (consultaPreparada.executeUpdate() > 0) {
                usuarioEliminado = true;
            }

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return usuarioEliminado;
    }

    public boolean modificarUsuario(UsuarioDTO usuario) throws SQLException, IOException {

        String consultaSQL = "UPDATE usuario SET nombre = ?, apellidos = ? WHERE idUsuario = ?";
        boolean usuarioModificado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(3, usuario.getIdUsuario());
            consultaPreparada.setString(2, usuario.getApellido());
            consultaPreparada.setString(1, usuario.getNombre());

            if (consultaPreparada.executeUpdate() > 0) {
                usuarioModificado = true;
            }

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return usuarioModificado;
    }

    public UsuarioDTO buscarUsuarioPorID(int idUsuario) throws SQLException, IOException {

        String consultaSQL = "SELECT * FROM usuario WHERE idUsuario = ?";
        UsuarioDTO usuarioEncontrado = new UsuarioDTO(-1, "N/A", "N/A", 0);

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, idUsuario);
            resultadoConsulta = consultaPreparada.executeQuery();

            if (resultadoConsulta.next()) {

                String nombre = resultadoConsulta.getString("nombre");
                String apellidos = resultadoConsulta.getString("apellidos");
                int estadoActivo = resultadoConsulta.getInt("estadoActivo");

                usuarioEncontrado = new UsuarioDTO (idUsuario, nombre, apellidos, estadoActivo);
            }

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return usuarioEncontrado;
    }
}
