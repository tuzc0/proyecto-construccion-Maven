package logica.DAOs;

import logica.DTOs.AutoEvaluacionContieneDTO;
import logica.interfaces.IAutoevaluacionContieneDAO;
import java.io.IOException;
import accesoadatos.ConexionBaseDeDatos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AutoevaluacionContieneDAO implements IAutoevaluacionContieneDAO {

    Connection conexion;
    PreparedStatement sentenciaAutoevaluacion = null;
    ResultSet resultadoConsulta;

    public boolean insertarAutoevaluacionContiene(AutoEvaluacionContieneDTO autoevalucion) throws SQLException, IOException {

        String consultaSQL = "INSERT INTO autoevaluacioncontiene (idAutoevaluacion, IDCriterio, calificacion) VALUES (?, ?, ?)";
        boolean insercionExitosa = false;

        try {

            conexion = new ConexionBaseDeDatos().getConnection();
            sentenciaAutoevaluacion = conexion.prepareStatement(consultaSQL);
            sentenciaAutoevaluacion.setInt(1, autoevalucion.getIdAutoevaluacion());
            sentenciaAutoevaluacion.setInt(2, autoevalucion.getIdCriterio());
            sentenciaAutoevaluacion.setFloat(3, autoevalucion.getCalificacion());

            if (sentenciaAutoevaluacion.executeUpdate() > 0) {
                insercionExitosa = true;
            }

        } finally {

            if (sentenciaAutoevaluacion != null) {

                sentenciaAutoevaluacion.close();
            }
        }

        return insercionExitosa;
    }

    public boolean modificarCalificacion(AutoEvaluacionContieneDTO autoevaluacion) throws SQLException, IOException {

        String consultaSQL = "UPDATE autoevaluacioncontiene SET calificacion = ? WHERE idAutoevaluacion = ? AND IDCriterio = ?";
        boolean modificacionExitosa = false;

        try {

            conexion = new ConexionBaseDeDatos().getConnection();
            sentenciaAutoevaluacion = conexion.prepareStatement(consultaSQL);
            sentenciaAutoevaluacion.setFloat(1, autoevaluacion.getCalificacion());
            sentenciaAutoevaluacion.setInt(2, autoevaluacion.getIdAutoevaluacion());
            sentenciaAutoevaluacion.setInt(3, autoevaluacion.getIdCriterio());

            if (sentenciaAutoevaluacion.executeUpdate() > 0) {
                modificacionExitosa = true;
            }

        } finally {

            if (sentenciaAutoevaluacion != null) {

                sentenciaAutoevaluacion.close();
            }
        }

        return modificacionExitosa;
    }

    public AutoEvaluacionContieneDTO buscarAutoevaluacionContienePorID(int idAutoevaluacion, int idCriterio) throws SQLException, IOException {

        String consultaSQL = "SELECT * FROM autoevaluacioncontiene WHERE idAutoevaluacion = ? AND idCriterio = ?";
        AutoEvaluacionContieneDTO autoevaluacionContiene = new AutoEvaluacionContieneDTO(-1, -1, -1);

        try {

            conexion = new ConexionBaseDeDatos().getConnection();
            sentenciaAutoevaluacion = conexion.prepareStatement(consultaSQL);
            sentenciaAutoevaluacion.setInt(1, idAutoevaluacion);
            sentenciaAutoevaluacion.setInt(2, idCriterio);
            resultadoConsulta = sentenciaAutoevaluacion.executeQuery();

            if (resultadoConsulta.next()) {

                autoevaluacionContiene.setIdAutoevaluacion(resultadoConsulta.getInt("idAutoevaluacion"));
                autoevaluacionContiene.setIdCriterio(resultadoConsulta.getInt("IDCriterio"));
                autoevaluacionContiene.setCalificacion(resultadoConsulta.getFloat("calificacion"));
            }

        } finally {

            if (sentenciaAutoevaluacion != null) {

                sentenciaAutoevaluacion.close();
            }
        }

        return autoevaluacionContiene;
    }


    public List<AutoEvaluacionContieneDTO> listarAutoevaluacionesPorIdAutoevaluacion(int idAutoevaluacion) throws SQLException, IOException {
        String consultaSQL = "SELECT * FROM autoevaluacioncontiene WHERE idAutoevaluacion = ?";
        List<AutoEvaluacionContieneDTO> listaAutoevaluaciones = new ArrayList<>();

        try {
            conexion = new ConexionBaseDeDatos().getConnection();
            sentenciaAutoevaluacion = conexion.prepareStatement(consultaSQL);
            sentenciaAutoevaluacion.setInt(1, idAutoevaluacion);
            resultadoConsulta = sentenciaAutoevaluacion.executeQuery();

            while (resultadoConsulta.next()) {
                AutoEvaluacionContieneDTO autoevaluacion = new AutoEvaluacionContieneDTO(
                        resultadoConsulta.getInt("idAutoevaluacion"),
                        resultadoConsulta.getFloat("calificacion"),
                        resultadoConsulta.getInt("IDCriterio")

                );
                listaAutoevaluaciones.add(autoevaluacion);
            }
        } finally {
            if (resultadoConsulta != null) {
                resultadoConsulta.close();
            }
            if (sentenciaAutoevaluacion != null) {
                sentenciaAutoevaluacion.close();
            }
        }

        return listaAutoevaluaciones;
    }

    public void eliminarCriteriosDefinitivamentePorIdAutoevaluacion(int idAutoevaluacion) throws SQLException, IOException {
        String consultaSQL = "DELETE FROM autoevaluacioncontiene WHERE idAutoevaluacion = ?";

        try {
            conexion = new ConexionBaseDeDatos().getConnection();
            sentenciaAutoevaluacion = conexion.prepareStatement(consultaSQL);
            sentenciaAutoevaluacion.setInt(1, idAutoevaluacion);
            sentenciaAutoevaluacion.executeUpdate();
        } finally {
            if (sentenciaAutoevaluacion != null) {
                sentenciaAutoevaluacion.close();
            }
        }
    }
}
