package logica.DAOs;

import accesoadatos.ConexionBD;
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

    public AutoevaluacionDAO() throws SQLException, IOException {

        conexionBaseDeDatos = new ConexionBD().getConnection();
    }

    public boolean crearNuevaAutoevaluacion(AutoevaluacionDTO autoevaluacion) throws SQLException {

        String insertarSQLAutoevaluacion = "INSERT INTO autoevaluacion (idAutoevaluacion, fecha, lugar, " +
                "calificacionFinal, idEstudiante) VALUES (?, ?, ?, ?, ?)";
        boolean autoevaluacionInsertada = false;

        try {

            sentenciaAutoevaluacion = conexionBaseDeDatos.prepareStatement(insertarSQLAutoevaluacion);
            sentenciaAutoevaluacion.setInt(1, autoevaluacion.getIDAutoevaluacion());
            java.sql.Date sqlDate = new java.sql.Date(autoevaluacion.getFecha().getTime());
            sentenciaAutoevaluacion.setDate(2, sqlDate);
            sentenciaAutoevaluacion.setString(3, autoevaluacion.getLugar());
            sentenciaAutoevaluacion.setInt(4, autoevaluacion.getCalificacionFinal());
            sentenciaAutoevaluacion.setString(5, autoevaluacion.getidEstudiante());
            sentenciaAutoevaluacion.executeUpdate();
            autoevaluacionInsertada = true;

        } finally {

            if (sentenciaAutoevaluacion != null) {

                sentenciaAutoevaluacion.close();
            }
        }

        return autoevaluacionInsertada;
    }

    public boolean eliminarAutoevaluacionPorID(int idAutoevaluacion) throws SQLException {

        String eliminarSQLAutoevaluacion = "UPDATE autoevaluacion SET estadoActivo = ? WHERE idAutoevaluacion = ?";
        boolean autoevaluacionEliminada = false;

        try {

            sentenciaAutoevaluacion = conexionBaseDeDatos.prepareStatement(eliminarSQLAutoevaluacion);
            sentenciaAutoevaluacion.setInt(1, idAutoevaluacion);
            sentenciaAutoevaluacion.setInt(2, 0);
            sentenciaAutoevaluacion.executeUpdate();
            autoevaluacionEliminada = true;

        } finally {

            if (sentenciaAutoevaluacion != null) {

                sentenciaAutoevaluacion.close();
            }
        }

        return autoevaluacionEliminada;
    }

    public boolean modificarAutoevaluacion(AutoevaluacionDTO autoevaluacion) throws SQLException {

        String modificarSQLAutoevaluacion = "UPDATE autoevaluacion SET fecha = ?, lugar = ?, calificacionFinal = ?, idEstudiante = ? " +
                "WHERE idAutoevaluacion = ?";
        boolean autoevaluacionModificada = false;

        try {

            sentenciaAutoevaluacion = conexionBaseDeDatos.prepareStatement(modificarSQLAutoevaluacion);
            java.sql.Date sqlDate = new java.sql.Date(autoevaluacion.getFecha().getTime());
            sentenciaAutoevaluacion.setDate(1, sqlDate);
            sentenciaAutoevaluacion.setString(2, autoevaluacion.getLugar());
            sentenciaAutoevaluacion.setInt(3, autoevaluacion.getCalificacionFinal());
            sentenciaAutoevaluacion.setString(4, autoevaluacion.getidEstudiante());
            sentenciaAutoevaluacion.setInt(5, autoevaluacion.getIDAutoevaluacion());
            sentenciaAutoevaluacion.executeUpdate();
            autoevaluacionModificada = true;

        } finally {

            if (sentenciaAutoevaluacion != null) {

                sentenciaAutoevaluacion.close();
            }
        }

        return autoevaluacionModificada;
    }

    public AutoevaluacionDTO buscarAutoevaluacionPorID(int idAutoevaluacion) throws SQLException {

        String buscarSQLAutoevaluacion = "SELECT * FROM autoevaluacion WHERE idAutoevaluacion = ?";
        AutoevaluacionDTO autoevaluacion = new AutoevaluacionDTO(-1, null, null, 0, null);

        try {

            sentenciaAutoevaluacion = conexionBaseDeDatos.prepareStatement(buscarSQLAutoevaluacion);
            sentenciaAutoevaluacion.setInt(1, idAutoevaluacion);
            resultadoConsultaAutoevaluacion = sentenciaAutoevaluacion.executeQuery();

            if (resultadoConsultaAutoevaluacion.next()) {

                int IDAutoevaluacion = resultadoConsultaAutoevaluacion.getInt("idAutoevaluacion");
                Timestamp fecha= resultadoConsultaAutoevaluacion.getTimestamp("fecha");
                String lugar = resultadoConsultaAutoevaluacion.getString("lugar");
                int calificacionFinal = resultadoConsultaAutoevaluacion.getInt("calificacionFinal");
                String idEstudiante = resultadoConsultaAutoevaluacion.getString("idEstudiante");
                autoevaluacion = new AutoevaluacionDTO(IDAutoevaluacion, fecha, lugar, calificacionFinal, idEstudiante);
            }

        } finally {

            if (sentenciaAutoevaluacion != null) {

                sentenciaAutoevaluacion.close();
            }
        }

        return autoevaluacion;
    }
}

