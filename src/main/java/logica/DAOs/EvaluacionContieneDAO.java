package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.EvaluacionContieneDTO;
import logica.interfaces.IEvaluacionContieneDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EvaluacionContieneDAO implements IEvaluacionContieneDAO {

    Connection conexion;
    PreparedStatement sentenciaEvaluacion = null;
    ResultSet resultadoConsulta;

    @Override
    public boolean insertarEvaluacionContiene(EvaluacionContieneDTO evaluacion) throws SQLException, IOException {

        String consultaSQL = "INSERT INTO evaluacioncontiene (idEvaluacion, idCriterio, calificacion) VALUES (?, ?, ?)";
        boolean insercionExitosa = false;

        try {

            conexion = new ConexionBaseDeDatos().getConnection();
            sentenciaEvaluacion = conexion.prepareStatement(consultaSQL);
            sentenciaEvaluacion.setInt(1, evaluacion.getIdEvaluacion());
            sentenciaEvaluacion.setInt(2, evaluacion.getIdCriterio());
            sentenciaEvaluacion.setFloat(3, evaluacion.getCalificacion());

            if (sentenciaEvaluacion.executeUpdate() > 0) {
                insercionExitosa = true;
            }

        } finally {

            if (sentenciaEvaluacion != null) {

                sentenciaEvaluacion.close();
            }
        }

        return insercionExitosa;
    }

    public boolean modificarCalificacion(EvaluacionContieneDTO evaluacion) throws SQLException, IOException {

        String consultaSQL = "UPDATE evaluacioncontiene SET calificacion = ? WHERE idEvaluacion = ? AND idCriterio = ?";
        boolean modificacionExitosa = false;

        try {

            conexion = new ConexionBaseDeDatos().getConnection();
            sentenciaEvaluacion = conexion.prepareStatement(consultaSQL);
            sentenciaEvaluacion.setFloat(1, evaluacion.getCalificacion());
            sentenciaEvaluacion.setInt(2, evaluacion.getIdEvaluacion());
            sentenciaEvaluacion.setFloat(3, evaluacion.getIdCriterio());

            if (sentenciaEvaluacion.executeUpdate() > 0) {
                modificacionExitosa = true;
            }

        } finally {

            if (sentenciaEvaluacion != null) {

                sentenciaEvaluacion.close();
            }
        }

        return modificacionExitosa;
    }

    @Override
    public EvaluacionContieneDTO buscarEvaluacionContienePorID(int idEvaluacion, int idCriterio) throws SQLException, IOException {

        String consultaSQL = "SELECT * FROM evaluacioncontiene WHERE idEvaluacion = ? AND idCriterio = ?";
        EvaluacionContieneDTO evaluacionEncontrada = new EvaluacionContieneDTO(-1, -1, -1);

        try {

            conexion = new ConexionBaseDeDatos().getConnection();
            sentenciaEvaluacion = conexion.prepareStatement(consultaSQL);
            sentenciaEvaluacion.setInt(1, idEvaluacion);
            sentenciaEvaluacion.setInt(2, idCriterio);
            resultadoConsulta = sentenciaEvaluacion.executeQuery();

            if (resultadoConsulta.next()) {
                evaluacionEncontrada.setIdEvaluacion(resultadoConsulta.getInt("idEvaluacion"));
                evaluacionEncontrada.setIdCriterio(resultadoConsulta.getInt("idCriterio"));
                evaluacionEncontrada.setCalificacion(resultadoConsulta.getFloat("calificacion"));
            }

        } finally {

            if (sentenciaEvaluacion != null) {

                sentenciaEvaluacion.close();
            }
        }

        return evaluacionEncontrada;
    }

    public List<EvaluacionContieneDTO> listarEvaluacionesPorIdEvaluacion(int idEvaluacion) throws SQLException, IOException {
        String consultaSQL = "SELECT * FROM evaluacioncontiene WHERE idEvaluacion = ?";
        List<EvaluacionContieneDTO> listaEvaluaciones = new ArrayList<>();

        try {
            conexion = new ConexionBaseDeDatos().getConnection();
            sentenciaEvaluacion = conexion.prepareStatement(consultaSQL);
            sentenciaEvaluacion.setInt(1, idEvaluacion);
            resultadoConsulta = sentenciaEvaluacion.executeQuery();

            while (resultadoConsulta.next()) {
                EvaluacionContieneDTO evaluacion = new EvaluacionContieneDTO(
                        resultadoConsulta.getInt("idEvaluacion"),
                        resultadoConsulta.getFloat("calificacion"),
                        resultadoConsulta.getInt("idCriterio")
                );
                listaEvaluaciones.add(evaluacion);
            }
        } finally {
            if (resultadoConsulta != null) {
                resultadoConsulta.close();
            }
            if (sentenciaEvaluacion != null) {
                sentenciaEvaluacion.close();
            }
        }

        return listaEvaluaciones;
    }

    public boolean eliminarCriteriosPorIdEvaluacion(int idEvaluacion) throws SQLException, IOException {
        String consultaSQL = "DELETE FROM evaluacionContiene WHERE idEvaluacion = ?";
        boolean eliminacionExitosa = false;

        try (Connection conexion = new ConexionBaseDeDatos().getConnection();
             PreparedStatement sentencia = conexion.prepareStatement(consultaSQL)) {

            sentencia.setInt(1, idEvaluacion);

            if (sentencia.executeUpdate() > 0) {
                eliminacionExitosa = true;
            }
        }

        return eliminacionExitosa;
    }
}
