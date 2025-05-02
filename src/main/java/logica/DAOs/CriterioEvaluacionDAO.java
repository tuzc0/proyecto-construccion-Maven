package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.CriterioEvaluacionDTO;
import logica.interfaces.ICriterioEvaluacionDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CriterioEvaluacionDAO implements ICriterioEvaluacionDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaCriterio = null;
    ResultSet resultadoConsultaCriterio;

    public boolean crearNuevoCriterioEvaluacion(CriterioEvaluacionDTO criterio) throws SQLException, IOException {

        String insertarSQLCriterio = "INSERT INTO criterioevaluacion (idCriterio, descripcion, numeroCriterio, estadoActivo) VALUES (?, ?, ?, ?)";
        boolean criterioInsertado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCriterio = conexionBaseDeDatos.prepareStatement(insertarSQLCriterio);
            sentenciaCriterio.setInt(1, criterio.getIDCriterio());
            sentenciaCriterio.setString(2, criterio.getDescripcion());
            sentenciaCriterio.setInt(3, criterio.getNumeroCriterio());
            sentenciaCriterio.setInt(4, criterio.getEstadoActivo());
            sentenciaCriterio.executeUpdate();
            criterioInsertado = true;

        } finally {

            if (sentenciaCriterio != null) {

                sentenciaCriterio.close();
            }
        }

        return criterioInsertado;
    }

    public boolean eliminarCriterioEvaluacionPorID(int idCriterio) throws SQLException, IOException {

        String eliminarSQLCriterio = "UPDATE criterioevaluacion SET estadoActivo = 0 WHERE numeroCriterio = ?";
        boolean criterioEliminado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCriterio = conexionBaseDeDatos.prepareStatement(eliminarSQLCriterio);
            sentenciaCriterio.setInt(1, idCriterio);
            sentenciaCriterio.executeUpdate();
            criterioEliminado = true;

        } finally {

            if (sentenciaCriterio != null) {

                sentenciaCriterio.close();
            }
        }

        return criterioEliminado;
    }

    public boolean modificarCriterioEvaluacion(CriterioEvaluacionDTO criterio) throws SQLException, IOException {

        String modificarSQLCriterio = "UPDATE criterioevaluacion SET descripcion = ? WHERE numeroCriterio = ?";
        boolean criterioModificado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
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

    public CriterioEvaluacionDTO buscarCriterioEvaluacionPorID(int numeroCriterio) throws SQLException, IOException {

        String buscarSQLCriterio = "SELECT * FROM criterioevaluacion WHERE numeroCriterio = ?";
        CriterioEvaluacionDTO criterioEncontrado = new CriterioEvaluacionDTO(-1, "N/A", -1,-1);

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCriterio = conexionBaseDeDatos.prepareStatement(buscarSQLCriterio);
            sentenciaCriterio.setInt(1, numeroCriterio);
            resultadoConsultaCriterio = sentenciaCriterio.executeQuery();

            if (resultadoConsultaCriterio.next()) {

                int identificadorCriterio = resultadoConsultaCriterio.getInt("idCriterio");
                String descripciones = resultadoConsultaCriterio.getString("descripcion");
                int numeroDeCriterio = resultadoConsultaCriterio.getInt("numeroCriterio");
                int estadoActivo = resultadoConsultaCriterio.getInt("estadoActivo");
                criterioEncontrado = new CriterioEvaluacionDTO(identificadorCriterio, descripciones, numeroDeCriterio, estadoActivo);
            }

        } finally {

            if (sentenciaCriterio != null) {

                sentenciaCriterio.close();
            }
        }

        return criterioEncontrado;
    }
}
