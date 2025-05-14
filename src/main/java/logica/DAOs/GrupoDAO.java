package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.GrupoDTO;
import logica.interfaces.IGrupoDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GrupoDAO implements IGrupoDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaGrupo = null;
    ResultSet resultadoGrupo;

    public boolean crearNuevoGrupo(GrupoDTO grupo) throws SQLException, IOException {

        boolean grupoCreadoConExito = false;
        String sql = "INSERT INTO Grupo (NRC, nombre, numeroPersonal, idEE, idPeriodo, estadoActivo) VALUES (?, ?, ?, ?, ?, ?)";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaGrupo = conexionBaseDeDatos.prepareStatement(sql);
            sentenciaGrupo.setInt(1, grupo.getNRC());
            sentenciaGrupo.setString(2, grupo.getNombre());
            sentenciaGrupo.setInt(3, grupo.getNumeroPersonal());
            sentenciaGrupo.setInt(4, grupo.getIdEE());
            sentenciaGrupo.setInt(5, grupo.getIdPeriodo());
            sentenciaGrupo.setInt(6, 1);

            if (sentenciaGrupo.executeUpdate() > 0) {
                grupoCreadoConExito = true;
            }

        } finally {

            if (sentenciaGrupo != null) {

                sentenciaGrupo.close();
            }
        }

        return grupoCreadoConExito;
    }

    public boolean eliminarGrupoPorNRC(int NRC) throws SQLException, IOException {

        boolean grupoEliminado = false;
        String sql = "UPDATE Grupo SET estadoActivo = 0 WHERE NRC = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaGrupo = conexionBaseDeDatos.prepareStatement(sql);
            sentenciaGrupo.setInt(1, NRC);

            if (sentenciaGrupo.executeUpdate() > 0) {
                grupoEliminado = true;
            }

        } finally {

            if (sentenciaGrupo != null) {

                sentenciaGrupo.close();
            }
        }

        return grupoEliminado;
    }

    public boolean modificarGrupo(GrupoDTO grupo) throws SQLException, IOException {

        boolean grupoModificado = false;
        String sql = "UPDATE Grupo SET nombre = ?, numeroPersonal = ?, idEE = ?, idPeriodo = ?, estadoActivo = ? WHERE NRC = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaGrupo = conexionBaseDeDatos.prepareStatement(sql);
            sentenciaGrupo.setString(1, grupo.getNombre());
            sentenciaGrupo.setInt(2, grupo.getNumeroPersonal());
            sentenciaGrupo.setInt(3, grupo.getIdEE());
            sentenciaGrupo.setInt(4, grupo.getIdPeriodo());
            sentenciaGrupo.setInt(5, grupo.getEstadoActivo());
            sentenciaGrupo.setInt(6, grupo.getNRC());

            if (sentenciaGrupo.executeUpdate() > 0) {
                grupoModificado = true;
            }

        } finally {

            if (sentenciaGrupo != null) {

                sentenciaGrupo.close();
            }
        }

        return grupoModificado;
    }

    public GrupoDTO buscarGrupoPorNRC(int NRC) throws SQLException, IOException {

        GrupoDTO grupo = new GrupoDTO(-1, "N/A", -1, -1, -1, -1);
        String sql = "SELECT * FROM Grupo WHERE NRC = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaGrupo = conexionBaseDeDatos.prepareStatement(sql);
            sentenciaGrupo.setInt(1, NRC);
            resultadoGrupo = sentenciaGrupo.executeQuery();

            if (resultadoGrupo.next()) {

                grupo.setNRC(resultadoGrupo.getInt("NRC"));
                grupo.setNombre(resultadoGrupo.getString("nombre"));
                grupo.setNumeroPersonal(resultadoGrupo.getInt("numeroPersonal"));
                grupo.setIdEE(resultadoGrupo.getInt("idEE"));
                grupo.setIdPeriodo(resultadoGrupo.getInt("idPeriodo"));
                grupo.setEstadoActivo(resultadoGrupo.getInt("estadoActivo"));
            }

        } finally {

            if (sentenciaGrupo != null) {

                sentenciaGrupo.close();
            }
        }

        return grupo;
    }

    public GrupoDTO mostrarGruposActivos() throws SQLException, IOException {

        GrupoDTO grupo = new GrupoDTO(-1, "N/A", -1, -1, -1, -1);
        String sql = "SELECT * FROM Grupo WHERE estadoActivo = 1";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaGrupo = conexionBaseDeDatos.prepareStatement(sql);
            resultadoGrupo = sentenciaGrupo.executeQuery();

            if (resultadoGrupo.next()) {

                grupo.setNRC(resultadoGrupo.getInt("NRC"));
                grupo.setNombre(resultadoGrupo.getString("nombre"));
                grupo.setNumeroPersonal(resultadoGrupo.getInt("numeroPersonal"));
                grupo.setIdEE(resultadoGrupo.getInt("idEE"));
                grupo.setIdPeriodo(resultadoGrupo.getInt("idPeriodo"));
                grupo.setEstadoActivo(resultadoGrupo.getInt("estadoActivo"));
            }

        } finally {

            if (sentenciaGrupo != null) {

                sentenciaGrupo.close();
            }
        }

        return grupo;
    }
}

