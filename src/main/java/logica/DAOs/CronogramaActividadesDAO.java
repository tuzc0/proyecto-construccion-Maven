package logica.DAOs;

import accesoadatos.ConexionBD;
import logica.DTOs.CronogramaActividadesDTO;
import logica.interfaces.ICronogramaActividadesDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CronogramaActividadesDAO implements ICronogramaActividadesDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaCronograma = null;
    ResultSet resultadoConsultaCronograma;



    public boolean crearNuevoCronogramaDeActividades(CronogramaActividadesDTO cronograma) throws SQLException, IOException {

        String insertarSQLCronograma = "INSERT INTO cronogramaactividades (IDCronograma, fechaInicio, fechaFinal, idEstudiante) VALUES (?, ?, ?, ?)";
        boolean cronogramaInsertado = false;

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(insertarSQLCronograma);
            sentenciaCronograma.setInt(1, cronograma.getIDCronograma());
            sentenciaCronograma.setTimestamp(2, cronograma.getFechaInicio());
            sentenciaCronograma.setTimestamp(3, cronograma.getFechaFinal());
            sentenciaCronograma.setString(4, cronograma.getMatriculaEstudiante());
            sentenciaCronograma.executeUpdate();
            cronogramaInsertado = true;

        } finally {

            if (sentenciaCronograma != null) {

                sentenciaCronograma.close();
            }
        }

        return cronogramaInsertado;
    }

    public boolean modificarCronogramaDeActividades(CronogramaActividadesDTO cronograma) throws SQLException, IOException {

        String modificarSQLCronograma = "UPDATE cronogramaactividades SET fechaInicio = ?, fechaFinal = ? WHERE idCronograma = ?";
        boolean cronogramaModificado = false;

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(modificarSQLCronograma);
            sentenciaCronograma.setTimestamp(1, cronograma.getFechaInicio());
            sentenciaCronograma.setTimestamp(2, cronograma.getFechaFinal());
            sentenciaCronograma.setInt(3, cronograma.getIDCronograma());
            sentenciaCronograma.executeUpdate();
            cronogramaModificado = true;

        } finally {

            if (sentenciaCronograma != null) {

                sentenciaCronograma.close();
            }
        }

        return cronogramaModificado;
    }

    public CronogramaActividadesDTO buscarCronogramaDeActividadesPorID(int idCronograma) throws SQLException, IOException {

        CronogramaActividadesDTO cronogramaEncontrado = new CronogramaActividadesDTO(-1, null, null, "0");
        String buscarSQLCronograma = "SELECT * FROM cronogramaactividades WHERE IDCronograma = ?";


        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(buscarSQLCronograma);
            sentenciaCronograma.setInt(1, idCronograma);
            resultadoConsultaCronograma = sentenciaCronograma.executeQuery();

            if (resultadoConsultaCronograma.next()) {

                cronogramaEncontrado.setIDCronograma(resultadoConsultaCronograma.getInt("idCronograma"));
                cronogramaEncontrado.setFechaInicio(resultadoConsultaCronograma.getTimestamp("fechaInicio"));
                cronogramaEncontrado.setFechaFinal(resultadoConsultaCronograma.getTimestamp("fechaFinal"));
                cronogramaEncontrado.setMatriculaEstudiante(resultadoConsultaCronograma.getString("idEstudiante"));
            }

        } finally {

            if (sentenciaCronograma != null) {

                sentenciaCronograma.close();
            }
        }

        return cronogramaEncontrado;
    }
}

