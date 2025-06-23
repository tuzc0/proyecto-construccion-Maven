package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.CronogramaContieneDTO;
import logica.interfaces.ICronogramaContieneDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CronogramaContieneDAO implements ICronogramaContieneDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaCronograma = null;
    ResultSet resultadoConsulta;

    public boolean insertarCronogramaContiene(CronogramaContieneDTO cronograma) throws SQLException, IOException {

        String consultaSQL = "INSERT INTO cronogramacontiene (idCronograma, idActividad, mes, estadoActivo) VALUES (?, ?,?, ?)";
        boolean cronogramaInsertado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(consultaSQL);
            sentenciaCronograma.setInt(1, cronograma.getIdCronograma());
            sentenciaCronograma.setInt(2, cronograma.getIdActividad());
            sentenciaCronograma.setString(3, cronograma.getMes());
            sentenciaCronograma.setInt(4, cronograma.getEstadoActivo());

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

        String consultaSQL = "UPDATE cronogramacontiene SET idActividad = ?, mes = ? WHERE idCronograma = ?";
        boolean cronogramaModificado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(consultaSQL);
            sentenciaCronograma.setInt(1, cronograma.getIdActividad());
            sentenciaCronograma.setString(2, cronograma.getMes());
            sentenciaCronograma.setInt(3, cronograma.getIdCronograma());

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

        CronogramaContieneDTO cronogramaContieneDTO = new CronogramaContieneDTO(-1, -1,null,0);

        String consultaSQL = "SELECT * FROM cronogramacontiene WHERE idCronograma = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(consultaSQL);
            sentenciaCronograma.setInt(1, idCronograma);
            resultadoConsulta = sentenciaCronograma.executeQuery();

            if (resultadoConsulta.next()) {

                cronogramaContieneDTO.setIdCronograma(resultadoConsulta.getInt(1));
                cronogramaContieneDTO.setIdActividad(resultadoConsulta.getInt(2));
                cronogramaContieneDTO.setMes(resultadoConsulta.getString(3));
                cronogramaContieneDTO.setEstadoActivo(resultadoConsulta.getInt(4));
            }

        } finally {

            if (sentenciaCronograma != null) {

                sentenciaCronograma.close();
            }
        }

        return cronogramaContieneDTO;
    }

    public List<CronogramaContieneDTO> listarCronogramaContienePorID(int idCronograma) throws SQLException, IOException {
        List<CronogramaContieneDTO> listaCronogramaContiene = new ArrayList<>();
        String consultaSQL = "SELECT * FROM cronogramacontiene WHERE idCronograma = ?";

        try {
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(consultaSQL);
            sentenciaCronograma.setInt(1, idCronograma);
            resultadoConsulta = sentenciaCronograma.executeQuery();

            while (resultadoConsulta.next()) {
                CronogramaContieneDTO cronogramaContieneDTO = new CronogramaContieneDTO(
                        resultadoConsulta.getInt(1),
                        resultadoConsulta.getInt(2),
                        resultadoConsulta.getString(3),
                        resultadoConsulta.getInt(4)
                );
                listaCronogramaContiene.add(cronogramaContieneDTO);
            }
        } finally {
            if (sentenciaCronograma != null) {
                sentenciaCronograma.close();
            }
        }

        return listaCronogramaContiene;
    }

    public List<Integer> obtenerActividadesPorCronograma(int idCronograma) throws SQLException, IOException {

        List<Integer> idsActividades = new ArrayList<>();
        String consultaSQL = "SELECT idActividad FROM cronogramacontiene WHERE idCronograma = ? AND estadoActivo = 1";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(consultaSQL);
            sentenciaCronograma.setInt(1, idCronograma);
            resultadoConsulta = sentenciaCronograma.executeQuery();

            while (resultadoConsulta.next()) {

                idsActividades.add(resultadoConsulta.getInt("idActividad"));
            }

        } finally {

            if (sentenciaCronograma != null) {

                sentenciaCronograma.close();
            }
        }

        return idsActividades;
    }
}