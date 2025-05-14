package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.CronogramaContieneDTO;
import logica.interfaces.ICronogramaContieneDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CronogramaContieneDAO implements ICronogramaContieneDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaCronograma = null;
    ResultSet resultadoConsulta;

    public boolean insertarCronogramaContiene(CronogramaContieneDTO cronograma) throws SQLException, IOException {

        String consultaSQL = "INSERT INTO cronogramacontiene (idCronograma, idActividad, estadoActivo) VALUES (?, ?, 1)";
        boolean cronogramaInsertado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(consultaSQL);
            sentenciaCronograma.setInt(1, cronograma.getIdCronograma());
            sentenciaCronograma.setInt(2, cronograma.getIdActividad());

            if (sentenciaCronograma.executeUpdate() > 0) {
                cronogramaInsertado = true;
            }

        } finally {

            if (sentenciaCronograma != null) {

                sentenciaCronograma.close();
            }
        }

        return cronogramaInsertado;
    }

    public boolean eliminarCronogramaContienePorID(int idCronograma) throws SQLException, IOException {

        String consultaSQL = "UPDATE cronogramacontiene SET estadoActivo = 0 WHERE idCronograma = ? ";
        boolean cronogramaEliminado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(consultaSQL);
            sentenciaCronograma.setInt(1, idCronograma);

            if (sentenciaCronograma.executeUpdate() > 0) {
                cronogramaEliminado = true;
            }

        } finally {

            if (sentenciaCronograma != null) {

                sentenciaCronograma.close();
            }
        }

        return cronogramaEliminado;
    }

    public boolean modificarActividadesDeCronograma(CronogramaContieneDTO cronograma) throws SQLException, IOException {

        String consultaSQL = "UPDATE cronogramacontiene SET idActividad = ? WHERE idCronograma = ?";
        boolean cronogramaModificado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(consultaSQL);
            sentenciaCronograma.setInt(1, cronograma.getIdActividad());
            sentenciaCronograma.setInt(2, cronograma.getIdCronograma());

            if (sentenciaCronograma.executeUpdate() > 0) {
                cronogramaModificado = true;
            }

        } finally {

            if (sentenciaCronograma != null) {

                sentenciaCronograma.close();
            }
        }

        return cronogramaModificado;
    }

    public CronogramaContieneDTO buscarCronogramaContienePorID(int idCronograma) throws SQLException, IOException {

        CronogramaContieneDTO cronograma = new CronogramaContieneDTO(-1, -1,-1);

        String consultaSQL = "SELECT * FROM cronogramacontiene WHERE idCronograma = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(consultaSQL);
            sentenciaCronograma.setInt(1, idCronograma);
            resultadoConsulta = sentenciaCronograma.executeQuery();

            if (resultadoConsulta.next()) {

                int idActividad = resultadoConsulta.getInt("idActividad");
                int estadoActivo = resultadoConsulta.getInt("estadoActivo");
                cronograma = new CronogramaContieneDTO(idCronograma, idActividad, estadoActivo);
            }

        } finally {

            if (sentenciaCronograma != null) {

                sentenciaCronograma.close();
            }
        }

        return cronograma;
    }
}
