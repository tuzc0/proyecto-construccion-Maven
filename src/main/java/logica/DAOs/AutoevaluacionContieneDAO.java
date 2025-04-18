package logica.DAOs;

import logica.DTOs.AutoEvaluacionContieneDTO;
import logica.interfaces.IAutoevaluacionContieneDAO;
import java.io.IOException;
import accesoadatos.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutoevaluacionContieneDAO implements IAutoevaluacionContieneDAO {

    Connection conexion;
    PreparedStatement sentencia = null;
    ResultSet resultadoConsulta;

    public boolean insertarAutoevaluacionContiene(AutoEvaluacionContieneDTO autoevalucion) throws SQLException, IOException {

        String consultaSQL = "INSERT INTO autoevaluacioncontiene (idAutoevaluacion, IDCriterio, calificacion) VALUES (?, ?, ?)";
        boolean insercionExitosa = false;

        try {

            conexion = new ConexionBD().getConnection();
            sentencia = conexion.prepareStatement(consultaSQL);
            sentencia.setInt(1, autoevalucion.getIdAutoevaluacion());
            sentencia.setInt(2, autoevalucion.getIdCriterio());
            sentencia.setFloat(3, autoevalucion.getCalificacion());
            sentencia.executeUpdate();
            insercionExitosa = true;

        } finally {

            if (sentencia != null) {

                sentencia.close();
            }
        }

        return insercionExitosa;
    }

    public boolean modificarCalificacion(AutoEvaluacionContieneDTO autoevaluacion) throws SQLException, IOException {

        String consultaSQL = "UPDATE autoevaluacioncontiene SET calificacion = ? WHERE idAutoevaluacion = ? AND IDCriterio = ?";
        boolean modificacionExitosa = false;

        try {

            conexion = new ConexionBD().getConnection();
            sentencia = conexion.prepareStatement(consultaSQL);
            sentencia.setFloat(1, autoevaluacion.getCalificacion());
            sentencia.setInt(2, autoevaluacion.getIdAutoevaluacion());
            sentencia.setInt(3, autoevaluacion.getIdCriterio());
            sentencia.executeUpdate();
            modificacionExitosa = true;

        } finally {

            if (sentencia != null) {

                sentencia.close();
            }
        }

        return modificacionExitosa;
    }

    public AutoEvaluacionContieneDTO buscarAutoevaluacionContienePorID(int idAutoevaluacion, int idCriterio) throws SQLException, IOException {

        String consultaSQL = "SELECT * FROM autoevaluacioncontiene WHERE idAutoevaluacion = ? AND idCriterio = ?";
        AutoEvaluacionContieneDTO autoevaluacionContiene = new AutoEvaluacionContieneDTO(-1, -1, -1);

        try {

            conexion = new ConexionBD().getConnection();
            sentencia = conexion.prepareStatement(consultaSQL);
            sentencia.setInt(1, idAutoevaluacion);
            sentencia.setInt(2, idCriterio);
            resultadoConsulta = sentencia.executeQuery();

            if (resultadoConsulta.next()) {

                autoevaluacionContiene.setIdAutoevaluacion(resultadoConsulta.getInt("idAutoevaluacion"));
                autoevaluacionContiene.setIdCriterio(resultadoConsulta.getInt("IDCriterio"));
                autoevaluacionContiene.setCalificacion(resultadoConsulta.getFloat("calificacion"));
            }

        } finally {

            if (sentencia != null) {

                sentencia.close();
            }
        }

        return autoevaluacionContiene;
    }
}
