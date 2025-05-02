package logica.DAOs;

import accesoadatos.ConexionBD;
import logica.DTOs.CriterioAutoevaluacionDTO;
import logica.interfaces.ICriterioAutoevaluacionDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CriterioAutoevaluacionDAO implements ICriterioAutoevaluacionDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaCriterio = null;
    ResultSet resultadoConsultaCriterio;



    public boolean crearNuevoCriterioAutoevaluacion(CriterioAutoevaluacionDTO criterio) throws SQLException, IOException {

        String insertarSQLCriterio = "INSERT INTO criterioautoevaluacion (idCriterio, descripciones, numeroCriterio, estadoActivo) VALUES (?, ?, ?, ?)";
        boolean criterioInsertado = false;

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaCriterio = conexionBaseDeDatos.prepareStatement(insertarSQLCriterio);
            sentenciaCriterio.setInt(1, criterio.getIDCriterio());
            sentenciaCriterio.setString(2, criterio.getDescripcion());
            sentenciaCriterio.setInt(3, criterio.getNumeroCriterio());
            sentenciaCriterio.executeUpdate();
            criterioInsertado = true;

        } finally {

            if (sentenciaCriterio != null) {

                sentenciaCriterio.close();
            }
        }

        return criterioInsertado;
    }

    public boolean eliminarCriterioAutoevaluacionPorNumeroDeCriterio(int numeroDeCriterio) throws SQLException, IOException {

        String eliminarSQLCriterio = "UPDATE criterioautoevaluacion SET estadoActivo = 0 WHERE numeroCriterio = ?";
        boolean criterioEliminado = false;

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaCriterio = conexionBaseDeDatos.prepareStatement(eliminarSQLCriterio);
            sentenciaCriterio.setInt(1, numeroDeCriterio);
            sentenciaCriterio.executeUpdate();
            criterioEliminado = true;

        } finally {

            if (sentenciaCriterio != null) {

                sentenciaCriterio.close();
            }
        }

        return criterioEliminado;
    }

    public boolean modificarCriterioAutoevaluacion(CriterioAutoevaluacionDTO criterio) throws SQLException, IOException {

        String modificarSQLCriterio = "UPDATE criterioautoevaluacion SET descripciones = ? WHERE numeroCriterio = ?";
        boolean criterioModificado = false;

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaCriterio = conexionBaseDeDatos.prepareStatement(modificarSQLCriterio);
            sentenciaCriterio.setString(1, criterio.getDescripcion());
            sentenciaCriterio.setInt(2, criterio.getNumeroCriterio());
            sentenciaCriterio.executeUpdate();
            criterioModificado = true;

        } finally {

            if (sentenciaCriterio != null) {

                sentenciaCriterio.close();
            }
        }

        return criterioModificado;
    }

    public CriterioAutoevaluacionDTO buscarCriterioAutoevaluacionPorID(int numeroDeCriterio) throws SQLException, IOException {

        String buscarSQLCriterio = "SELECT * FROM criterioautoevaluacion WHERE numeroCriterio = ?";
        CriterioAutoevaluacionDTO criterioEncontrado = new CriterioAutoevaluacionDTO(-1, "N/A", -1,-1);

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaCriterio = conexionBaseDeDatos.prepareStatement(buscarSQLCriterio);
            sentenciaCriterio.setInt(1, numeroDeCriterio);
            resultadoConsultaCriterio = sentenciaCriterio.executeQuery();

            if (resultadoConsultaCriterio.next()) {

                int idCriterio = resultadoConsultaCriterio.getInt("idCriterio");
                String descripciones = resultadoConsultaCriterio.getString("descripciones");
                int numeroCriterio = resultadoConsultaCriterio.getInt("numeroCriterio");
                int estadoActivo = resultadoConsultaCriterio.getInt("estadoActivo");
                criterioEncontrado = new CriterioAutoevaluacionDTO(idCriterio, descripciones, numeroCriterio, estadoActivo);
            }

        } finally {

            if (sentenciaCriterio != null) {

                sentenciaCriterio.close();
            }
        }

        return criterioEncontrado;
    }
}

