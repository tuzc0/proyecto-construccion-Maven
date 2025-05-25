package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.CoordinadorDTO;
import logica.interfaces.ICoordinadorDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CoordinadorDAO implements ICoordinadorDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaCoordinador = null;
    ResultSet resultadoCoordinador;


    public boolean insertarCoordinador (CoordinadorDTO coordinador) throws SQLException, IOException {

        boolean coordinadorInsertado = false;

        String insertarSQLCoordinador = "INSERT INTO coordinador VALUES(?, ?)";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCoordinador = conexionBaseDeDatos.prepareStatement(insertarSQLCoordinador);
            sentenciaCoordinador.setInt(1, coordinador.getNumeroDePersonal());
            sentenciaCoordinador.setInt(2, coordinador.getIdUsuario());

            if (sentenciaCoordinador.executeUpdate() > 0) {
                coordinadorInsertado = true;
            }

        } finally {

            if (sentenciaCoordinador != null) {

                sentenciaCoordinador.close();
            }
        }

        return coordinadorInsertado;
    }

    public boolean eliminarCoordinadorPorNumeroDePersonal (int numeroDePersonal) throws SQLException, IOException {

        boolean coordinadorEliminado = false;

        String modificarSQLCoordinador = "UPDATE usuario SET estadoActivo = 0 WHERE idUsuario = " +
                "(SELECT idUsuario FROM coordinador WHERE numeroDePersonal = ?)";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCoordinador = conexionBaseDeDatos.prepareStatement(modificarSQLCoordinador);
            sentenciaCoordinador.setInt(1, numeroDePersonal);

            if (sentenciaCoordinador.executeUpdate() > 0) {
                coordinadorEliminado = true;
            }

        } finally {
            if (sentenciaCoordinador != null) {

                sentenciaCoordinador.close();
            }
        }

        return coordinadorEliminado;
    }


    public boolean modificarCoordinador (CoordinadorDTO coordinador) throws SQLException, IOException {

        boolean coordinadorModificado = false;

        String modificarSQLCoordinador = "UPDATE coordinador SET numeroDePersonal = ? WHERE idUsuario = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCoordinador = conexionBaseDeDatos.prepareStatement(modificarSQLCoordinador);
            sentenciaCoordinador.setInt(1, coordinador.getNumeroDePersonal());
            sentenciaCoordinador.setInt(2, coordinador.getIdUsuario());

            if (sentenciaCoordinador.executeUpdate() > 0) {
                coordinadorModificado = true;
            }

        } finally {

            if (sentenciaCoordinador != null) {

                sentenciaCoordinador.close();
            }
        }

        return coordinadorModificado;
    }

    public CoordinadorDTO buscarCoordinadorPorNumeroDePersonal (int numeroDePersonal) throws SQLException, IOException {

        CoordinadorDTO coordinador = new CoordinadorDTO(-1, -1, "N/A", "N/A", 0);

        String consultaSQL = "SELECT * FROM vista_coordinadores WHERE numeroDePersonal = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCoordinador = conexionBaseDeDatos.prepareStatement(consultaSQL);
            sentenciaCoordinador.setInt(1, numeroDePersonal);
            resultadoCoordinador = sentenciaCoordinador.executeQuery();

            if (resultadoCoordinador.next()) {

                coordinador.setNumeroDePersonal(resultadoCoordinador.getInt("numeroDePersonal"));
                coordinador.setIdUsuario(resultadoCoordinador.getInt("idUsuario"));
                coordinador.setNombre(resultadoCoordinador.getString("nombre"));
                coordinador.setApellido(resultadoCoordinador.getString("apellidos"));
                coordinador.setEstado(resultadoCoordinador.getInt("estadoActivo"));
            }

        } finally {

            if (sentenciaCoordinador != null) {

                sentenciaCoordinador.close();
            }
        }

        return coordinador;
    }

    public CoordinadorDTO buscarCoordinadorPorID(int idUsuario) throws SQLException, IOException {
        CoordinadorDTO coordinador = new CoordinadorDTO(-1, -1, "N/A", "N/A", 0);

        String consultaSQL = "SELECT * FROM vista_coordinadores WHERE idUsuario = ?";

        try {
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCoordinador = conexionBaseDeDatos.prepareStatement(consultaSQL);
            sentenciaCoordinador.setInt(1, idUsuario);
            resultadoCoordinador = sentenciaCoordinador.executeQuery();

            if (resultadoCoordinador.next()) {
                int numeroDePersonal = resultadoCoordinador.getInt("numeroDePersonal");
                String nombre = resultadoCoordinador.getString("nombre");
                String apellidos = resultadoCoordinador.getString("apellidos");
                int estadoActivo = resultadoCoordinador.getInt("estadoActivo");

                coordinador = new CoordinadorDTO(numeroDePersonal, idUsuario, nombre, apellidos, estadoActivo);
            }
        } finally {
            if (sentenciaCoordinador != null) {
                sentenciaCoordinador.close();
            }
        }

        return coordinador;
    }

}