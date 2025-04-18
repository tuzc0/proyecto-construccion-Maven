package logica.DAOs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import accesoadatos.ConexionBD;
import logica.DTOs.UsuarioDTO;
import logica.interfaces.IUsuarioDAO;

public class UsuarioDAO implements IUsuarioDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement consultaPreparada = null;
    ResultSet resultadoConsulta;



    public boolean insertarUsuario(UsuarioDTO usuario) throws SQLException, IOException {

        String consultaSQL = "INSERT INTO usuario (idUsuario, nombre, apellidos, estadoActivo) VALUES (?, ?, ?, ?)";
        boolean usuarioInsertado = false;

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, usuario.getIdUsuario());
            consultaPreparada.setString(2, usuario.getNombre());
            consultaPreparada.setString(3, usuario.getApellido());
            consultaPreparada.setInt(4, usuario.getEstado());
            consultaPreparada.executeUpdate();
            usuarioInsertado = true;

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return usuarioInsertado;
    }

    public boolean eliminarUsuarioPorID(int idUsuario) throws SQLException, IOException {

        String consultaSQL = "UPDATE usuario SET estadoActivo = ? WHERE idUsuario = ?";
        boolean usuarioEliminado = false;

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, 0);
            consultaPreparada.setInt(2, idUsuario);
            consultaPreparada.executeUpdate();
            usuarioEliminado = true;

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

            conexionBaseDeDatos = new ConexionBD().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(3, usuario.getIdUsuario());
            consultaPreparada.setString(2, usuario.getApellido());
            consultaPreparada.setString(1, usuario.getNombre());
            consultaPreparada.executeUpdate();
            usuarioModificado = true;

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

            conexionBaseDeDatos = new ConexionBD().getConnection();
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
