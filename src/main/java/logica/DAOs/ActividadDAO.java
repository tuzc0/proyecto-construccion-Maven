package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.ActividadDTO;
import logica.interfaces.IActividadDAO;
import java.io.IOException;
import java.security.Key;
import java.sql.*;

public class ActividadDAO implements IActividadDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaActividad = null;
    ResultSet resultadoConsulta;

    public int crearNuevaActividad(ActividadDTO actividad) throws SQLException, IOException {

        String insertarSQLActividad = "INSERT INTO actividad (nombre, duracion, hitos, fechaInicio, fechaFin, estadoActivo) " +
                "VALUES(?, ?, ?, ?, ?, ?)";
        int idActividadInsertada = -1;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaActividad = conexionBaseDeDatos.prepareStatement(insertarSQLActividad, Statement.RETURN_GENERATED_KEYS);
            sentenciaActividad.setString(1, actividad.getNombre());
            sentenciaActividad.setString(2, actividad.getDuracion());
            sentenciaActividad.setString(3, actividad.getHitos());
            sentenciaActividad.setDate(4, actividad.getFechaInicio());
            sentenciaActividad.setDate(5, actividad.getFechaFin());
            sentenciaActividad.setInt(6, actividad.getEstadoActivo());

            int filasAfectadas = sentenciaActividad.executeUpdate();

            if (filasAfectadas > 0) {

                ResultSet generadorDeLlave = sentenciaActividad.getGeneratedKeys();

                if (generadorDeLlave.next()) {

                    idActividadInsertada = generadorDeLlave.getInt(1);
                }
            }

        } finally {

            if (sentenciaActividad != null) {

                sentenciaActividad.close();
            }
        }

        return idActividadInsertada;
    }

    public boolean eliminarActividadPorID(int idActividad) throws SQLException, IOException {

        boolean actividadEliminada = false;

        String eliminarSQLActividad = "UPDATE actividad SET estadoActivo = 0 WHERE IDActividad = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaActividad = conexionBaseDeDatos.prepareStatement(eliminarSQLActividad);
            sentenciaActividad.setInt(1, idActividad);

            if (sentenciaActividad.executeUpdate() > 0) {
                actividadEliminada= true;
            }

        } finally {

            if (sentenciaActividad != null) {

                sentenciaActividad.close();
            }
        }

        return actividadEliminada;
    }

    public boolean modificarActividad(ActividadDTO actividad) throws SQLException, IOException {
        boolean actividadModificada = false;

        String modificarSQLActividad = "UPDATE actividad SET nombre = ?, duracion = ?, hitos = ?, fechaInicio = ?, fechaFin = ? , estadoActivo = ? WHERE IDActividad = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaActividad = conexionBaseDeDatos.prepareStatement(modificarSQLActividad);
            sentenciaActividad.setString(1, actividad.getNombre());
            sentenciaActividad.setString(2, actividad.getDuracion());
            sentenciaActividad.setString(3, actividad.getHitos());
            sentenciaActividad.setDate(4, actividad.getFechaInicio());
            sentenciaActividad.setDate(5, actividad.getFechaFin());
            sentenciaActividad.setInt(6, actividad.getEstadoActivo());
            sentenciaActividad.setInt(7, actividad.getIDActividad());

            if (sentenciaActividad.executeUpdate() > 0) {
                actividadModificada = true;
            }

        } finally {

            if (sentenciaActividad != null) {

                sentenciaActividad.close();
            }
        }

        return actividadModificada;
    }

    public ActividadDTO buscarActividadPorID(int idActividad) throws SQLException, IOException {

        ActividadDTO actividad = new ActividadDTO(-1, "Sin nombre", "Sin duración", "Sin hitos",null, null,  0);

        String buscarSQLActividad = "SELECT * FROM actividad WHERE IDActividad = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaActividad = conexionBaseDeDatos.prepareStatement(buscarSQLActividad);
            sentenciaActividad.setInt(1, idActividad);
            resultadoConsulta = sentenciaActividad.executeQuery();

            if (resultadoConsulta.next()) {

                actividad.setIDActividad(resultadoConsulta.getInt("IdActividad"));
                actividad.setNombre(resultadoConsulta.getString("nombre"));
                actividad.setDuracion(resultadoConsulta.getString("duracion"));
                actividad.setHitos(resultadoConsulta.getString("hitos"));
                actividad.setFechaFin(resultadoConsulta.getDate("fechaInicio"));
                actividad.setFechaFin(resultadoConsulta.getDate("fechaFin"));
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
