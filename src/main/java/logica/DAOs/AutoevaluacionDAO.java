package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.AutoevaluacionDTO;
import logica.interfaces.IAutoevaluacionDAO;
import java.sql.Timestamp;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutoevaluacionDAO implements IAutoevaluacionDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaAutoevaluacion = null;
    ResultSet resultadoConsultaAutoevaluacion;


    public int crearNuevaAutoevaluacion(AutoevaluacionDTO autoevaluacion) throws SQLException, IOException {


        String insertarSQLAutoevaluacion = "INSERT INTO autoevaluacion (idAutoevaluacion, fecha, lugar, " +
                "calificacionFinal, idEstudiante, estadoActivo) VALUES (?, ?, ?, ?, ?, ?)";
        int idAutoevaluacionCreada = -1;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaAutoevaluacion = conexionBaseDeDatos.prepareStatement(insertarSQLAutoevaluacion, PreparedStatement.RETURN_GENERATED_KEYS);
            sentenciaAutoevaluacion.setInt(1, autoevaluacion.getIDAutoevaluacion());
            sentenciaAutoevaluacion.setTimestamp(2, autoevaluacion.getFecha());
            sentenciaAutoevaluacion.setString(3, autoevaluacion.getLugar());
            sentenciaAutoevaluacion.setFloat(4, autoevaluacion.getCalificacionFinal());
            sentenciaAutoevaluacion.setString(5, autoevaluacion.getIdEstudiante());
            sentenciaAutoevaluacion.setInt(6, autoevaluacion.getEstadoActivo());

            if (sentenciaAutoevaluacion.executeUpdate() > 0) {
                ResultSet generatedKeys = sentenciaAutoevaluacion.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idAutoevaluacionCreada = generatedKeys.getInt(1);
                }
            }

        } finally {

            if (sentenciaAutoevaluacion != null) {
                sentenciaAutoevaluacion.close();
            }
        }

        return idAutoevaluacionCreada;
    }

    public boolean eliminarAutoevaluacionPorID(int idAutoevaluacion) throws SQLException, IOException {

        String eliminarSQLAutoevaluacion = "UPDATE autoevaluacion SET estadoActivo = ? WHERE idAutoevaluacion = ?";
        boolean autoevaluacionEliminada = false;
        int estadoActivo = 0;
        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaAutoevaluacion = conexionBaseDeDatos.prepareStatement(eliminarSQLAutoevaluacion);
            sentenciaAutoevaluacion.setInt(1, estadoActivo);
            sentenciaAutoevaluacion.setInt(2, idAutoevaluacion);

            if (sentenciaAutoevaluacion.executeUpdate() > 0) {
                autoevaluacionEliminada = true;
            }

        } finally {

            if (sentenciaAutoevaluacion != null) {

                sentenciaAutoevaluacion.close();
            }
        }

        return autoevaluacionEliminada;
    }

    public boolean modificarAutoevaluacion(AutoevaluacionDTO autoevaluacion) throws SQLException, IOException {

        String modificarSQLAutoevaluacion = "UPDATE autoevaluacion SET fecha = ?, lugar = ?, calificacionFinal = ?, idEstudiante = ? " +
                "WHERE idAutoevaluacion = ?";
        boolean autoevaluacionModificada = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaAutoevaluacion = conexionBaseDeDatos.prepareStatement(modificarSQLAutoevaluacion);
            sentenciaAutoevaluacion.setTimestamp(1, autoevaluacion.getFecha());
            sentenciaAutoevaluacion.setString(2, autoevaluacion.getLugar());
            sentenciaAutoevaluacion.setFloat(3, autoevaluacion.getCalificacionFinal());
            sentenciaAutoevaluacion.setString(4, autoevaluacion.getIdEstudiante());
            sentenciaAutoevaluacion.setInt(5, autoevaluacion.getIDAutoevaluacion());

            if (sentenciaAutoevaluacion.executeUpdate() > 0) {
                autoevaluacionModificada = true;
            }

        } finally {

            if (sentenciaAutoevaluacion != null) {

                sentenciaAutoevaluacion.close();
            }
        }

        return autoevaluacionModificada;
    }

    public AutoevaluacionDTO buscarAutoevaluacionPorID(int idAutoevaluacion) throws SQLException, IOException {

        String buscarSQLAutoevaluacion = "SELECT * FROM autoevaluacion WHERE idAutoevaluacion = ?";
        AutoevaluacionDTO autoevaluacion = new AutoevaluacionDTO(-1, null, null, -1, null,-1);

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaAutoevaluacion = conexionBaseDeDatos.prepareStatement(buscarSQLAutoevaluacion);
            sentenciaAutoevaluacion.setInt(1, idAutoevaluacion);
            resultadoConsultaAutoevaluacion = sentenciaAutoevaluacion.executeQuery();

            if (resultadoConsultaAutoevaluacion.next()) {

                int IDAutoevaluacion = resultadoConsultaAutoevaluacion.getInt("idAutoevaluacion");
                Timestamp fecha= resultadoConsultaAutoevaluacion.getTimestamp("fecha");
                String lugar = resultadoConsultaAutoevaluacion.getString("lugar");
                float calificacionFinal = resultadoConsultaAutoevaluacion.getFloat("calificacionFinal");
                String idEstudiante = resultadoConsultaAutoevaluacion.getString("idEstudiante");
                int estatoActivo = resultadoConsultaAutoevaluacion.getInt("estadoActivo");
                autoevaluacion = new AutoevaluacionDTO(IDAutoevaluacion, fecha, lugar, calificacionFinal, idEstudiante,estatoActivo);
            }

        } finally {

            if (sentenciaAutoevaluacion != null) {

                sentenciaAutoevaluacion.close();
            }
        }

        return autoevaluacion;
    }

    public void eliminarAutoevaluacionDefinitivamentePorID(int idAutoevaluacion) throws SQLException, IOException {
        String eliminarSQLAutoevaluacion = "DELETE FROM autoevaluacion WHERE idAutoevaluacion = ?";

        try {
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaAutoevaluacion = conexionBaseDeDatos.prepareStatement(eliminarSQLAutoevaluacion);
            sentenciaAutoevaluacion.setInt(1, idAutoevaluacion);
            sentenciaAutoevaluacion.executeUpdate();
        } finally {
            if (sentenciaAutoevaluacion != null) {
                sentenciaAutoevaluacion.close();
            }
        }
    }

    public AutoevaluacionDTO buscarAutoevaluacionPorMatricula(String matricula) throws SQLException, IOException {
        String buscarSQLAutoevaluacion = "SELECT * FROM autoevaluacion WHERE idEstudiante = ?";
        AutoevaluacionDTO autoevaluacion = new AutoevaluacionDTO(-1, null, " ", -1, " ", -1);

        try {
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaAutoevaluacion = conexionBaseDeDatos.prepareStatement(buscarSQLAutoevaluacion);
            sentenciaAutoevaluacion.setString(1, matricula);
            resultadoConsultaAutoevaluacion = sentenciaAutoevaluacion.executeQuery();

            if (resultadoConsultaAutoevaluacion.next()) {
                int IDAutoevaluacion = resultadoConsultaAutoevaluacion.getInt("idAutoevaluacion");
                Timestamp fecha = resultadoConsultaAutoevaluacion.getTimestamp("fecha");
                String lugar = resultadoConsultaAutoevaluacion.getString("lugar");
                float calificacionFinal = resultadoConsultaAutoevaluacion.getFloat("calificacionFinal");
                String idEstudiante = resultadoConsultaAutoevaluacion.getString("idEstudiante");
                int estatoActivo = resultadoConsultaAutoevaluacion.getInt("estadoActivo");
                autoevaluacion = new AutoevaluacionDTO(IDAutoevaluacion, fecha, lugar, calificacionFinal, idEstudiante, estatoActivo);
            }

        } finally {
            if (sentenciaAutoevaluacion != null) {
                sentenciaAutoevaluacion.close();
            }
        }
        return autoevaluacion;
    }

}

