package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.CronogramaActividadesDTO;
import logica.interfaces.ICronogramaActividadesDAO;
import java.io.IOException;
import java.sql.*;

public class CronogramaActividadesDAO implements ICronogramaActividadesDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaCronograma = null;
    ResultSet resultadoConsultaCronograma;



    public int crearNuevoCronogramaDeActividades(CronogramaActividadesDTO cronograma) throws SQLException, IOException {

        String insertarSQLCronograma = "INSERT INTO cronogramaactividades (idEstudiante, agosto_febrero, septiembre_marzo, octubre_abril, noviembre_mayo) VALUES (?, ?, ?, ?, ?)";
        int idCronogramaInsertado = -1;

        try {
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(insertarSQLCronograma, Statement.RETURN_GENERATED_KEYS);
            sentenciaCronograma.setString(1, cronograma.getMatriculaEstudiante());
            sentenciaCronograma.setString(2, cronograma.getAgostoFebrero());
            sentenciaCronograma.setString(3, cronograma.getSeptiembreMarzo());
            sentenciaCronograma.setString(4, cronograma.getOctubreAbril());
            sentenciaCronograma.setString(5, cronograma.getNoviembreMayo());

            int filasAfectadas = sentenciaCronograma.executeUpdate();

            if (filasAfectadas > 0) {

                ResultSet generadorDeLlave = sentenciaCronograma.getGeneratedKeys();

                if (generadorDeLlave.next()) {

                    idCronogramaInsertado = generadorDeLlave.getInt(1);
                }
            }

        } finally {

            if (sentenciaCronograma != null) {

                sentenciaCronograma.close();
            }
        }

        return idCronogramaInsertado;
    }

    public boolean modificarCronogramaDeActividades(CronogramaActividadesDTO cronograma) throws SQLException, IOException {

        String modificarSQLCronograma = "UPDATE cronogramaactividades SET idEstudiante = ?, agosto_febrero = ?, septiembre_marzo = ?, octubre_abril = ?, noviembre_mayo = ? WHERE idCronograma = ?";
        boolean cronogramaModificado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(modificarSQLCronograma);
            sentenciaCronograma.setString(1, cronograma.getMatriculaEstudiante());
            sentenciaCronograma.setString(2, cronograma.getAgostoFebrero());
            sentenciaCronograma.setString(3, cronograma.getSeptiembreMarzo());
            sentenciaCronograma.setString(4, cronograma.getOctubreAbril());
            sentenciaCronograma.setString(5, cronograma.getNoviembreMayo());
            sentenciaCronograma.setInt(6, cronograma.getIDCronograma());

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

    public CronogramaActividadesDTO buscarCronogramaDeActividadesPorID(int idCronograma) throws SQLException, IOException {

        CronogramaActividadesDTO cronogramaEncontrado = new CronogramaActividadesDTO(-1, "0", "0", "0", "0", "0");
        String buscarSQLCronograma = "SELECT * FROM cronogramaactividades WHERE idCronograma = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(buscarSQLCronograma);
            sentenciaCronograma.setInt(1, idCronograma);
            resultadoConsultaCronograma = sentenciaCronograma.executeQuery();

            if (resultadoConsultaCronograma.next()) {

                cronogramaEncontrado.setIDCronograma(resultadoConsultaCronograma.getInt("idCronograma"));
                cronogramaEncontrado.setMatriculaEstudiante(resultadoConsultaCronograma.getString("idEstudiante"));
                cronogramaEncontrado.setAgostoFebrero(resultadoConsultaCronograma.getString("agosto_febrero"));
                cronogramaEncontrado.setSeptiembreMarzo(resultadoConsultaCronograma.getString("septiembre_marzo"));
                cronogramaEncontrado.setOctubreAbril(resultadoConsultaCronograma.getString("octubre_abril"));
                cronogramaEncontrado.setNoviembreMayo(resultadoConsultaCronograma.getString("noviembre_mayo"));
            }

        } finally {

            if (sentenciaCronograma != null) {

                sentenciaCronograma.close();
            }
        }

        return cronogramaEncontrado;
    }
}

