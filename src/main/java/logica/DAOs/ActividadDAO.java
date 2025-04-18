package logica.DAOs;

import accesoadatos.ConexionBD;
import logica.DTOs.ActividadDTO;
import logica.interfaces.IActividadDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActividadDAO implements IActividadDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaActividad = null;
    ResultSet resultadoConsulta;

    public ActividadDAO() throws SQLException, IOException {

        conexionBaseDeDatos = new ConexionBD().getConnection();
    }

    public boolean crearNuevaActividad(ActividadDTO actividad) throws SQLException {
        boolean actividadInsertada = false;

        String insertarSQLActividad = "INSERT INTO actividad VALUES(?, ?, ?, ?, ?, ?, ?)";

        try {
            sentenciaActividad = conexionBaseDeDatos.prepareStatement(insertarSQLActividad);
            sentenciaActividad.setInt(1, actividad.getIDActividad());
            sentenciaActividad.setString(2, actividad.getNombre());
            sentenciaActividad.setString(3, actividad.getDuracion());
            sentenciaActividad.setString(4, actividad.getHitos());
            sentenciaActividad.setTimestamp(5, actividad.getFechaInicio());
            sentenciaActividad.setTimestamp(6, actividad.getFechaFin());
            sentenciaActividad.setInt(7, 1);
            sentenciaActividad.executeUpdate();
            actividadInsertada = true;

        } finally {

            if (sentenciaActividad != null) {

                sentenciaActividad.close();
            }
        }

        return actividadInsertada;
    }

    public boolean eliminarActividadPorID(int idActividad) throws SQLException {
        boolean actividadEliminada = false;

        String eliminarSQLActividad = "UPDATE actividad SET estadoActivo = 0 WHERE IDActividad = ?";

        try {
            sentenciaActividad = conexionBaseDeDatos.prepareStatement(eliminarSQLActividad);
            sentenciaActividad.setInt(1, idActividad);
            sentenciaActividad.executeUpdate();
            actividadEliminada = true;

        } finally {

            if (sentenciaActividad != null) {

                sentenciaActividad.close();
            }
        }

        return actividadEliminada;
    }

    public boolean modificarActividad(ActividadDTO actividad) throws SQLException {
        boolean actividadModificada = false;

        String modificarSQLActividad = "UPDATE actividad SET nombre = ?, duracion = ?, hitos = ?, fechaInicio = ?, fechaFin = ? , estadoActivo = ? WHERE IDActividad = ?";

        try {
            sentenciaActividad = conexionBaseDeDatos.prepareStatement(modificarSQLActividad);
            sentenciaActividad.setString(1, actividad.getNombre());
            sentenciaActividad.setString(2, actividad.getDuracion());
            sentenciaActividad.setString(3, actividad.getHitos());
            sentenciaActividad.setTimestamp(4, actividad.getFechaInicio());
            sentenciaActividad.setTimestamp(5, actividad.getFechaFin());
            sentenciaActividad.setInt(6, actividad.getIDActividad());
            sentenciaActividad.setInt(7, actividad.getEstadoActivo());
            sentenciaActividad.executeUpdate();
            actividadModificada = true;

        } finally {

            if (sentenciaActividad != null) {

                sentenciaActividad.close();
            }
        }

        return actividadModificada;
    }

    public ActividadDTO buscarActividadPorID(int idActividad) throws SQLException {

        ActividadDTO actividad = new ActividadDTO(-1, "Sin nombre", null, null, "Sin duración", "Sin hitos", 0);

        String buscarSQLActividad = "SELECT * FROM actividad WHERE IDActividad = ?";

        try {
            sentenciaActividad = conexionBaseDeDatos.prepareStatement(buscarSQLActividad);
            sentenciaActividad.setInt(1, idActividad);
            resultadoConsulta = sentenciaActividad.executeQuery();

            if (resultadoConsulta.next()) {

                actividad.setIDActividad(resultadoConsulta.getInt("IdActividad"));
                actividad.setNombre(resultadoConsulta.getString("nombre"));
                actividad.setDuracion(resultadoConsulta.getString("duracion"));
                actividad.setHitos(resultadoConsulta.getString("hitos"));
                actividad.setFechaInicio(resultadoConsulta.getTimestamp("fechaInicio"));
                actividad.setFechaFin(resultadoConsulta.getTimestamp("fechaFin"));
                actividad.setEstadoActivo(resultadoConsulta.getInt("estadoActivo"));
            }

        } finally {

            if (sentenciaActividad != null) {

                sentenciaActividad.close();
            }
        }

        return actividad;
    }
}

