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

        String insertarSQLCronograma = "INSERT INTO cronograma (matriculaEstudiante, idProyecto, idPeriodo, estadoActivo) VALUES (?, ?, ?, ?)";
        int idCronogramaInsertado = -1;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(insertarSQLCronograma, Statement.RETURN_GENERATED_KEYS);
            sentenciaCronograma.setString(1, cronograma.getMatriculaEstudiante());
            sentenciaCronograma.setInt(2, cronograma.getIdProyecto());
            sentenciaCronograma.setInt(3, cronograma.getIdPeriodo());
            sentenciaCronograma.setInt(4, cronograma.getEstadoActivo());

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

    public boolean modificarCronogramaDeActividades(CronogramaActividadesDTO cronogramaActividadesDTO) throws SQLException, IOException {

        String modificarSQLCronograma = "UPDATE cronograma SET matriculaEstudiante = ?, idProyecto = ?, idPeriodo = ?  WHERE idCronograma = ?";
        boolean cronogramaModificado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(modificarSQLCronograma);
            sentenciaCronograma.setString(1, cronogramaActividadesDTO.getMatriculaEstudiante());
            sentenciaCronograma.setInt(2, cronogramaActividadesDTO.getIdProyecto());
            sentenciaCronograma.setInt(3, cronogramaActividadesDTO.getIdPeriodo());
            sentenciaCronograma.setInt(4, cronogramaActividadesDTO.getIDCronograma());

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

        CronogramaActividadesDTO cronogramaEncontrado = new CronogramaActividadesDTO(-1, "0", -1,-1,0);
        String buscarSQLCronograma = "SELECT * FROM cronogramaactividades WHERE idCronograma = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCronograma = conexionBaseDeDatos.prepareStatement(buscarSQLCronograma);
            sentenciaCronograma.setInt(1, idCronograma);
            resultadoConsultaCronograma = sentenciaCronograma.executeQuery();

            if (resultadoConsultaCronograma.next()) {

                cronogramaEncontrado.setIDCronograma(resultadoConsultaCronograma.getInt("idCronograma"));
                cronogramaEncontrado.setMatriculaEstudiante(resultadoConsultaCronograma.getString("idEstudiante"));
                cronogramaEncontrado.setIdProyecto(resultadoConsultaCronograma.getInt("idProyecto"));
                cronogramaEncontrado.setIdPeriodo(resultadoConsultaCronograma.getInt("idPeriodo"));
                cronogramaEncontrado.setEstadoActivo(resultadoConsultaCronograma.getInt("estadoActivo"));
            }

        } finally {

            if (sentenciaCronograma != null) {

                sentenciaCronograma.close();
            }
        }

        return cronogramaEncontrado;
    }
}