package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.GrupoDTO;
import logica.interfaces.IGrupoDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GrupoDAO implements IGrupoDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaGrupo = null;
    ResultSet resultadoGrupo;

    public boolean crearNuevoGrupo(GrupoDTO grupo) throws SQLException, IOException {

        boolean grupoCreadoConExito = false;
        String sql = "INSERT INTO Grupo (NRC, nombre, numeroPersonal, idPeriodo, estadoActivo) VALUES (?, ?, ?, ?, ?)";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaGrupo = conexionBaseDeDatos.prepareStatement(sql);
            sentenciaGrupo.setInt(1, grupo.getNRC());
            sentenciaGrupo.setString(2, grupo.getNombre());
            sentenciaGrupo.setInt(3, grupo.getNumeroPersonal());
            sentenciaGrupo.setInt(4, grupo.getIdPeriodo());
            sentenciaGrupo.setInt(5, 1);

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

    public boolean eliminarGruposPorPeriodo(int idPeriodo) throws SQLException, IOException {

        boolean gruposEliminados = false;
        String sql = "UPDATE Grupo SET estadoActivo = 0 WHERE idPeriodo = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaGrupo = conexionBaseDeDatos.prepareStatement(sql);
            sentenciaGrupo.setInt(1, idPeriodo);

            if (sentenciaGrupo.executeUpdate() > 0) {
                gruposEliminados = true;
            }

        } finally {

            if (sentenciaGrupo != null) {
                sentenciaGrupo.close();
            }
        }

        return gruposEliminados;
    }


    public boolean modificarGrupo(GrupoDTO grupo) throws SQLException, IOException {

        boolean grupoModificado = false;
        String sql = "UPDATE Grupo SET nombre = ?, numeroPersonal = ?, idPeriodo = ?, estadoActivo = ? WHERE NRC = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaGrupo = conexionBaseDeDatos.prepareStatement(sql);
            sentenciaGrupo.setString(1, grupo.getNombre());
            sentenciaGrupo.setInt(2, grupo.getNumeroPersonal());
            sentenciaGrupo.setInt(3, grupo.getIdPeriodo());
            sentenciaGrupo.setInt(4, grupo.getEstadoActivo());
            sentenciaGrupo.setInt(5, grupo.getNRC());

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

        GrupoDTO grupo = new GrupoDTO(-1, "N/A", -1, -1, -1);
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

    public List<GrupoDTO> mostrarGruposActivos() throws SQLException, IOException {

        List<GrupoDTO> grupos = new ArrayList<>();
        String sql = "SELECT * FROM Grupo WHERE estadoActivo = 1";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaGrupo = conexionBaseDeDatos.prepareStatement(sql);
            resultadoGrupo = sentenciaGrupo.executeQuery();

            while (resultadoGrupo.next()) {
                GrupoDTO grupo = new GrupoDTO(
                        resultadoGrupo.getInt("NRC"),
                        resultadoGrupo.getString("nombre"),
                        resultadoGrupo.getInt("numeroPersonal"),
                        resultadoGrupo.getInt("idPeriodo"),
                        resultadoGrupo.getInt("estadoActivo")
                );
                grupos.add(grupo);
            }

        } finally {

            if (sentenciaGrupo != null) {
                sentenciaGrupo.close();
            }
        }

        return grupos;
    }

    public List<GrupoDTO> mostrarGruposActivosEnPeriodoActivo() throws SQLException, IOException {

        List<GrupoDTO> grupos = new ArrayList<>();
        String sql = "SELECT g.* FROM Grupo g " +
                "JOIN Periodo p ON g.idPeriodo = p.idPeriodo " +
                "WHERE g.estadoActivo = 1 AND p.estadoActivo = 1";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaGrupo = conexionBaseDeDatos.prepareStatement(sql);
            resultadoGrupo = sentenciaGrupo.executeQuery();

            while (resultadoGrupo.next()) {
                GrupoDTO grupo = new GrupoDTO(
                        resultadoGrupo.getInt("NRC"),
                        resultadoGrupo.getString("nombre"),
                        resultadoGrupo.getInt("numeroPersonal"),
                        resultadoGrupo.getInt("idPeriodo"),
                        resultadoGrupo.getInt("estadoActivo")
                );
                grupos.add(grupo);
            }

        } finally {

            if (sentenciaGrupo != null) {
                sentenciaGrupo.close();
            }
        }

        return grupos;
    }

    public boolean existeGrupoPorNumeroAcademico (int numeroAcademico) throws SQLException, IOException {

        boolean existeGrupo = false;
        String sql = "SELECT EXISTS (\n" +
                "  SELECT 1 FROM grupo WHERE numeroPersonal = ? AND estadoActivo = 1\n" +
                ") AS existeGrupo;\n";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaGrupo = conexionBaseDeDatos.prepareStatement(sql);
            sentenciaGrupo.setInt(1, numeroAcademico);
            resultadoGrupo = sentenciaGrupo.executeQuery();

            if (resultadoGrupo.next()) {
                existeGrupo = resultadoGrupo.getBoolean("existeGrupo");
            }

        } finally {

            if (sentenciaGrupo != null) {
                sentenciaGrupo.close();
            }
        }

        return existeGrupo;
    }

    public int generarNRC() throws SQLException, IOException {

        int nuevoNRC = 101;
        String sql = "SELECT COALESCE(MAX(NRC), 0) + 1 AS nuevoNRC FROM Grupo";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaGrupo = conexionBaseDeDatos.prepareStatement(sql);
            resultadoGrupo = sentenciaGrupo.executeQuery();

            if (resultadoGrupo.next()) {
                nuevoNRC = resultadoGrupo.getInt("nuevoNRC");
            }

        } finally {

            if (sentenciaGrupo != null) {
                sentenciaGrupo.close();
            }
        }

        return nuevoNRC;
    }

    public GrupoDTO buscarGrupoActivoPorNumeroDePersonal(int numeroPersonal) throws SQLException, IOException {

        GrupoDTO grupo = new GrupoDTO(-1, "N/A", -1, -1, -1);
        String sql = "SELECT * FROM Grupo WHERE numeroPersonal = ? AND estadoActivo = 1 LIMIT 1";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaGrupo = conexionBaseDeDatos.prepareStatement(sql);
            sentenciaGrupo.setInt(1, numeroPersonal);
            resultadoGrupo = sentenciaGrupo.executeQuery();

            if (resultadoGrupo.next()) {
                grupo = new GrupoDTO(
                        resultadoGrupo.getInt("NRC"),
                        resultadoGrupo.getString("nombre"),
                        resultadoGrupo.getInt("numeroPersonal"),
                        resultadoGrupo.getInt("idPeriodo"),
                        resultadoGrupo.getInt("estadoActivo")
                );
            }

        } finally {

            if (sentenciaGrupo != null) {
                sentenciaGrupo.close();
            }
        }

        return grupo;
    }
}

