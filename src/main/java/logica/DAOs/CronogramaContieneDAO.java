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
    PreparedStatement consultaPreparada = null;
    ResultSet resultadoConsulta;

    public boolean insertarCronogramaContiene(CronogramaContieneDTO cronograma) throws SQLException, IOException {

        String consultaSQL = "INSERT INTO cronogramacontiene (idCronograma, idActividad, estadoActivo) VALUES (?, ?, 1)";
        boolean insercionExitosa = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, cronograma.getIdCronograma());
            consultaPreparada.setInt(2, cronograma.getIdActividad());
            consultaPreparada.executeUpdate();
            insercionExitosa = true;

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return insercionExitosa;
    }

    public boolean eliminarCronogramaContienePorID(int idCronograma) throws SQLException, IOException {

        String consultaSQL = "UPDATE cronogramacontiene SET estadoActivo = 0 WHERE idCronograma = ? ";
        boolean cronogramaEliminado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, idCronograma);
            consultaPreparada.executeUpdate();
            cronogramaEliminado = true;

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return cronogramaEliminado;
    }

    public boolean modificarActividadesDeCronograma(CronogramaContieneDTO cronograma) throws SQLException, IOException {

        String consultaSQL = "UPDATE cronogramacontiene SET idActividad = ? WHERE idCronograma = ?";
        boolean cronogramaModificado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, cronograma.getIdActividad());
            consultaPreparada.setInt(2, cronograma.getIdCronograma());

            consultaPreparada.executeUpdate();
            cronogramaModificado = true;

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return cronogramaModificado;
    }

    public CronogramaContieneDTO buscarCronogramaContienePorID(int idCronograma) throws SQLException, IOException {

        CronogramaContieneDTO cronograma = new CronogramaContieneDTO(-1, -1,-1);

        String consultaSQL = "SELECT * FROM cronogramacontiene WHERE idCronograma = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, idCronograma);
            resultadoConsulta = consultaPreparada.executeQuery();

            if (resultadoConsulta.next()) {

                int idActividad = resultadoConsulta.getInt("idActividad");
                int estadoActivo = resultadoConsulta.getInt("estadoActivo");

                cronograma = new CronogramaContieneDTO(idCronograma, idActividad, estadoActivo);
            }

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return cronograma;
    }
}
