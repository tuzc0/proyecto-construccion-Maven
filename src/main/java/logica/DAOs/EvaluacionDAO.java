package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.EvaluacionDTO;
import logica.interfaces.IEvaluacionDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EvaluacionDAO implements IEvaluacionDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaEvaluacion = null;
    ResultSet resultadoConsultaEvaluacion;

    public int crearNuevaEvaluacion(EvaluacionDTO evaluacion) throws SQLException, IOException {

        String insertarSQLEvaluacion = "INSERT INTO evaluacion (idEvaluacion, comentarios, calificacionFinal, " +
                "numeroPersonal, idEstudiante, estadoActivo) VALUES (?, ?, ?, ?, ?, ?)";
        int idEvaluacionInsertada = -1;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEvaluacion = conexionBaseDeDatos.prepareStatement(insertarSQLEvaluacion, PreparedStatement.RETURN_GENERATED_KEYS);
            sentenciaEvaluacion.setInt(1, evaluacion.getIDEvaluacion());
            sentenciaEvaluacion.setString(2, evaluacion.getComentarios());
            sentenciaEvaluacion.setFloat(3, evaluacion.getCalificacionFinal());
            sentenciaEvaluacion.setInt(4, evaluacion.getNumeroDePersonal());
            sentenciaEvaluacion.setString(5, evaluacion.getMatriculaEstudiante());
            sentenciaEvaluacion.setInt(6, evaluacion.getEstadoActivo());

            if (sentenciaEvaluacion.executeUpdate() > 0) {

                ResultSet generatedKeys = sentenciaEvaluacion.getGeneratedKeys();

                if (generatedKeys.next()) {
                    idEvaluacionInsertada = generatedKeys.getInt(1);
                }
            }

        } finally {

            if (sentenciaEvaluacion != null) {
                sentenciaEvaluacion.close();
            }
        }

        return idEvaluacionInsertada;
    }

    public boolean eliminarEvaluacionPorID(int estadoActivo, int idEvaluacion) throws SQLException, IOException {

        String eliminarSQLEvaluacion = "UPDATE evaluacion SET estadoActivo = ? WHERE idEvaluacion = ?";
        boolean evaluacionEliminada = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEvaluacion = conexionBaseDeDatos.prepareStatement(eliminarSQLEvaluacion);
            sentenciaEvaluacion.setInt(2, idEvaluacion);
            sentenciaEvaluacion.setInt(1, estadoActivo);

            if (sentenciaEvaluacion.executeUpdate() > 0) {
                evaluacionEliminada = true;
            }

        } finally {

            if (sentenciaEvaluacion != null) {

                sentenciaEvaluacion.close();
            }
        }

        return evaluacionEliminada;
    }

    public boolean eliminarEvaluacionDefinitivamente(int idEvaluacion) throws SQLException, IOException {

        String eliminarSQL = "DELETE FROM evaluacion WHERE idEvaluacion = ?";
        boolean evaluacionEliminada = false;

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
             PreparedStatement sentencia = conexionBaseDeDatos.prepareStatement(eliminarSQL)) {

            sentencia.setInt(1, idEvaluacion);

            if (sentencia.executeUpdate() > 0) {
                evaluacionEliminada = true;
            }
        }

        return evaluacionEliminada;
    }

    public boolean modificarEvaluacion(EvaluacionDTO evaluacion) throws SQLException, IOException {

        String modificarSQLEvaluacion = "UPDATE evaluacion SET comentarios = ?, calificacionFinal = ?, numeroPersonal = ?, " +
                "idEstudiante = ?, estadoActivo = ? WHERE idEvaluacion = ?";
        boolean evaluacionModificada = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEvaluacion = conexionBaseDeDatos.prepareStatement(modificarSQLEvaluacion);
            sentenciaEvaluacion.setString(1, evaluacion.getComentarios());
            sentenciaEvaluacion.setFloat(2, evaluacion.getCalificacionFinal());
            sentenciaEvaluacion.setInt(3, evaluacion.getNumeroDePersonal());
            sentenciaEvaluacion.setString(4, evaluacion.getMatriculaEstudiante());
            sentenciaEvaluacion.setInt(5, evaluacion.getEstadoActivo());
            sentenciaEvaluacion.setInt(6, evaluacion.getIDEvaluacion());

            if (sentenciaEvaluacion.executeUpdate() > 0) {
                evaluacionModificada = true;
            }

        } finally {

            if (sentenciaEvaluacion != null) {

                sentenciaEvaluacion.close();
            }
        }

        return evaluacionModificada;
    }

    public EvaluacionDTO buscarEvaluacionPorID(int idEvaluacion) throws SQLException, IOException {

        String buscarSQLEvaluacion = "SELECT * FROM evaluacion WHERE idEvaluacion = ?";
        EvaluacionDTO evaluacion = new EvaluacionDTO(-1, " ", 0, 0,
                " ", 0);

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEvaluacion = conexionBaseDeDatos.prepareStatement(buscarSQLEvaluacion);
            sentenciaEvaluacion.setInt(1, idEvaluacion);
            ResultSet resultadoConsultaEvaluacion = sentenciaEvaluacion.executeQuery();

            if (resultadoConsultaEvaluacion.next()) {

                int numeroDeEvaluacion = resultadoConsultaEvaluacion.getInt("idEvaluacion");
                String comentario = resultadoConsultaEvaluacion.getString("comentarios");
                float calificacion = resultadoConsultaEvaluacion.getFloat("calificacionFinal");
                int numeroPersonal = resultadoConsultaEvaluacion.getInt("numeroPersonal");
                String matriculaEstudiante = resultadoConsultaEvaluacion.getString("idEstudiante");
                int estadoActivo = resultadoConsultaEvaluacion.getInt("estadoActivo");

                evaluacion = new EvaluacionDTO(numeroDeEvaluacion, comentario, calificacion, numeroPersonal,
                        matriculaEstudiante, estadoActivo);
            }

        } finally {

            if (sentenciaEvaluacion != null) {

                sentenciaEvaluacion.close();
            }
        }

        return evaluacion;
    }

    public List<EvaluacionDTO> listarEvaluacionesPorIdEstudiante(String idEstudiante) throws SQLException, IOException {

        String listarSQLEvaluaciones = "SELECT * FROM evaluacion WHERE idEstudiante = ?";
        List<EvaluacionDTO> listaEvaluaciones = new ArrayList<>();

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
             PreparedStatement sentencia = conexionBaseDeDatos.prepareStatement(listarSQLEvaluaciones)) {

            sentencia.setString(1, idEstudiante);

            try (ResultSet resultadoConsulta = sentencia.executeQuery()) {

                while (resultadoConsulta.next()) {

                    int idEvaluacion = resultadoConsulta.getInt("idEvaluacion");
                    String comentarios = resultadoConsulta.getString("comentarios");
                    float calificacionFinal = resultadoConsulta.getFloat("calificacionFinal");
                    int numeroPersonal = resultadoConsulta.getInt("numeroPersonal");
                    int estadoActivo = resultadoConsulta.getInt("estadoActivo");

                    EvaluacionDTO evaluacion = new EvaluacionDTO(
                            idEvaluacion, comentarios, calificacionFinal, numeroPersonal, idEstudiante, estadoActivo
                    );

                    listaEvaluaciones.add(evaluacion);
                }
            }
        }

        return listaEvaluaciones;
    }

    public List<EvaluacionDTO> listarEvaluacionesNumeroPersonal(int numeroPersonal) throws SQLException, IOException {

        String listarSQLEvaluaciones = "SELECT * FROM evaluacion WHERE numeroPersonal = ?";
        List<EvaluacionDTO> listaEvaluaciones = new ArrayList<>();

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
             PreparedStatement sentencia = conexionBaseDeDatos.prepareStatement(listarSQLEvaluaciones)) {

            sentencia.setInt(1, numeroPersonal);

            try (ResultSet resultadoConsulta = sentencia.executeQuery()) {

                while (resultadoConsulta.next()) {

                    int idEvaluacion = resultadoConsulta.getInt("idEvaluacion");
                    String comentarios = resultadoConsulta.getString("comentarios");
                    float calificacionFinal = resultadoConsulta.getFloat("calificacionFinal");
                    String matriculaEstudiante = resultadoConsulta.getString("idEstudiante");
                    int estadoActivo = resultadoConsulta.getInt("estadoActivo");

                    EvaluacionDTO evaluacion = new EvaluacionDTO(
                            idEvaluacion, comentarios, calificacionFinal, numeroPersonal, matriculaEstudiante, estadoActivo
                    );

                    listaEvaluaciones.add(evaluacion);
                }
            }
        }

        return listaEvaluaciones;
    }
}

