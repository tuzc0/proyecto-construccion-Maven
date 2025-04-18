package logica.DAOs;

import accesoadatos.ConexionBD;
import logica.DTOs.EvaluacionDTO;
import logica.interfaces.IEvaluacionDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EvaluacionDAO implements IEvaluacionDAO {


    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaEvaluacion = null;
    ResultSet resultadoConsultaEvaluacion;

    public EvaluacionDAO() throws SQLException, IOException {

        conexionBaseDeDatos = new ConexionBD().getConnection();
    }

    public boolean crearNuevaEvaluacion(EvaluacionDTO evaluacion) throws SQLException {

        String insertarSQLEvaluacion = "INSERT INTO evaluacion (idEvaluacion, comentarios, calificacionFinal, " +
                "numeroPersonal, idEstudiante, estadoActivo) VALUES (?, ?, ?, ?, ?, ?)";
        boolean evaluacionInsertada = false;

        try {

            sentenciaEvaluacion = conexionBaseDeDatos.prepareStatement(insertarSQLEvaluacion);
            sentenciaEvaluacion.setInt(1, evaluacion.getIDEvaluacion());
            sentenciaEvaluacion.setString(2, evaluacion.getComentarios());
            sentenciaEvaluacion.setInt(3, evaluacion.getCalificacionFinal());
            sentenciaEvaluacion.setInt(4, evaluacion.getNumeroDePersonal());
            sentenciaEvaluacion.setString(5, evaluacion.getMatriculaEstudiante());
            sentenciaEvaluacion.setInt(6, evaluacion.getEstadoActivo());
            sentenciaEvaluacion.executeUpdate();
            evaluacionInsertada = true;

        } finally {

            if (sentenciaEvaluacion != null) {

                sentenciaEvaluacion.close();
            }
        }

        return evaluacionInsertada;
    }

    public boolean eliminarEvaluacionPorID(int estadoActivo, int idEvaluacion) throws SQLException {

        String eliminarSQLEvaluacion = "UPDATE evaluacion SET estadoActivo = ? WHERE idEvaluacion = ?";
        boolean evaluacionEliminada = false;

        try {

            sentenciaEvaluacion = conexionBaseDeDatos.prepareStatement(eliminarSQLEvaluacion);
            sentenciaEvaluacion.setInt(1, idEvaluacion);
            sentenciaEvaluacion.setInt(2, estadoActivo);
            sentenciaEvaluacion.executeUpdate();
            evaluacionEliminada = true;

        } finally {

            if (sentenciaEvaluacion != null) {

                sentenciaEvaluacion.close();
            }
        }

        return evaluacionEliminada;
    }

    public boolean modificarEvaluacion(EvaluacionDTO evaluacion) throws SQLException {

        String modificarSQLEvaluacion = "UPDATE evaluacion SET comentarios = ?, calificacionFinal = ?, numeroPersonal = ?, " +
                "idEstudiante = ?, estadoActivo = ? WHERE idEvaluacion = ?";
        boolean evaluacionModificada = false;

        try {

            sentenciaEvaluacion = conexionBaseDeDatos.prepareStatement(modificarSQLEvaluacion);
            sentenciaEvaluacion.setString(1, evaluacion.getComentarios());
            sentenciaEvaluacion.setInt(2, evaluacion.getCalificacionFinal());
            sentenciaEvaluacion.setInt(3, evaluacion.getNumeroDePersonal());
            sentenciaEvaluacion.setString(4, evaluacion.getMatriculaEstudiante());
            sentenciaEvaluacion.setInt(5, evaluacion.getEstadoActivo());
            sentenciaEvaluacion.setInt(6, evaluacion.getIDEvaluacion());
            sentenciaEvaluacion.executeUpdate();
            evaluacionModificada = true;

        } finally {

            if (sentenciaEvaluacion != null) {

                sentenciaEvaluacion.close();
            }
        }

        return evaluacionModificada;
    }

    public EvaluacionDTO buscarEvaluacionPorID(int idEvaluacion) throws SQLException {

        String buscarSQLEvaluacion = "SELECT * FROM evaluacion WHERE idEvaluacion = ?";
        EvaluacionDTO evaluacion = new EvaluacionDTO(-1, " ", 0, 0, " ", 0);

        try {

            sentenciaEvaluacion = conexionBaseDeDatos.prepareStatement(buscarSQLEvaluacion);
            sentenciaEvaluacion.setInt(1, idEvaluacion);
            ResultSet resultadoConsultaEvaluacion = sentenciaEvaluacion.executeQuery();

            if (resultadoConsultaEvaluacion.next()) {

                int numeroDeEvaluacion = resultadoConsultaEvaluacion.getInt("idEvaluacion");
                String comentario = resultadoConsultaEvaluacion.getString("comentarios");
                int calificacion = resultadoConsultaEvaluacion.getInt("calificacionFinal");
                int numeroPersonal = resultadoConsultaEvaluacion.getInt("numeroPersonal");
                String matriculaEstudiante = resultadoConsultaEvaluacion.getString("idEstudiante");
                int estadoActivo = resultadoConsultaEvaluacion.getInt("estadoActivo");
                evaluacion = new EvaluacionDTO(numeroDeEvaluacion, comentario, calificacion, numeroPersonal, matriculaEstudiante, estadoActivo);
            }

        } finally {

            if (sentenciaEvaluacion != null) {

                sentenciaEvaluacion.close();
            }
        }

        return evaluacion;
    }
}

