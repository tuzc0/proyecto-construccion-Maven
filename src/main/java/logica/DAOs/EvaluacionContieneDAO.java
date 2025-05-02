package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.EvaluacionContieneDTO;
import logica.interfaces.IEvaluacionContieneDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EvaluacionContieneDAO implements IEvaluacionContieneDAO {

    Connection conexion;
    PreparedStatement sentencia = null;
    ResultSet resultadoConsulta;

    @Override
    public boolean insertarEvaluacionContiene(EvaluacionContieneDTO evaluacion) throws SQLException, IOException {

        String consultaSQL = "INSERT INTO evaluacioncontiene (idEvaluacion, idCriterio, calificacion) VALUES (?, ?, ?)";
        boolean insercionExitosa = false;

        try {

            conexion = new ConexionBaseDeDatos().getConnection();
            sentencia = conexion.prepareStatement(consultaSQL);
            sentencia.setInt(1, evaluacion.getIdEvaluacion());
            sentencia.setInt(2, evaluacion.getIdCriterio());
            sentencia.setFloat(3, evaluacion.getCalificacion());
            sentencia.executeUpdate();
            insercionExitosa = true;

        } finally {

            if (sentencia != null) {

                sentencia.close();
            }
        }

        return insercionExitosa;
    }

    public boolean modificarCalificacion(EvaluacionContieneDTO evaluacion) throws SQLException, IOException {

        String consultaSQL = "UPDATE evaluacioncontiene SET calificacion = ? WHERE idEvaluacion = ? AND idCriterio = ?";
        boolean modificacionExitosa = false;

        try {

            conexion = new ConexionBaseDeDatos().getConnection();
            sentencia = conexion.prepareStatement(consultaSQL);
            sentencia.setFloat(1, evaluacion.getCalificacion());
            sentencia.setInt(2, evaluacion.getIdEvaluacion());
            sentencia.setFloat(3, evaluacion.getIdCriterio());
            sentencia.executeUpdate();
            modificacionExitosa = true;

        } finally {

            if (sentencia != null) {

                sentencia.close();
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
            sentencia = conexion.prepareStatement(consultaSQL);
            sentencia.setInt(1, idEvaluacion);
            sentencia.setInt(2, idCriterio);
            resultadoConsulta = sentencia.executeQuery();

            if (resultadoConsulta.next()) {
                evaluacionEncontrada.setIdEvaluacion(resultadoConsulta.getInt("idEvaluacion"));
                evaluacionEncontrada.setIdCriterio(resultadoConsulta.getInt("idCriterio"));
                evaluacionEncontrada.setCalificacion(resultadoConsulta.getFloat("calificacion"));
            }

        } finally {

            if (sentencia != null) {

                sentencia.close();
            }
        }

        return evaluacionEncontrada;
    }
}
