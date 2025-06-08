package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.CriterioAutoevaluacionDTO;
import logica.interfaces.ICriterioAutoevaluacionDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CriterioAutoevaluacionDAO implements ICriterioAutoevaluacionDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaCriterio = null;
    ResultSet resultadoConsultaCriterio;

    public boolean crearNuevoCriterioAutoevaluacion(CriterioAutoevaluacionDTO criterio) throws SQLException, IOException {

        String insertarSQLCriterio = "INSERT INTO criterioautoevaluacion (idCriterio, descripciones, numeroCriterio, estadoActivo) VALUES (?, ?, ?, ?)";
        boolean criterioInsertado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCriterio = conexionBaseDeDatos.prepareStatement(insertarSQLCriterio);
            sentenciaCriterio.setInt(1, criterio.getIDCriterio());
            sentenciaCriterio.setString(2, criterio.getDescripcion());
            sentenciaCriterio.setInt(3, criterio.getNumeroCriterio());
            sentenciaCriterio.setInt(4, criterio.getEstadoActivo());

            if (sentenciaCriterio.executeUpdate() > 0) {
                criterioInsertado = true;
            }

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

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCriterio = conexionBaseDeDatos.prepareStatement(eliminarSQLCriterio);
            sentenciaCriterio.setInt(1, numeroDeCriterio);

            if (sentenciaCriterio.executeUpdate() > 0) {
                criterioEliminado = true;
            }

        } finally {

            if (sentenciaCriterio != null) {

                sentenciaCriterio.close();
            }
        }

        return criterioEliminado;
    }

    public boolean eliminarCriterioAutoevaluacionPorID(int idCriterio) throws SQLException, IOException {

        String eliminarSQLCriterio = "UPDATE criterioautoevaluacion SET estadoActivo = 0 WHERE idCriterio = ?";
        boolean criterioEliminado = false;

        try {
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCriterio = conexionBaseDeDatos.prepareStatement(eliminarSQLCriterio);
            sentenciaCriterio.setInt(1, idCriterio);

            if (sentenciaCriterio.executeUpdate() > 0) {
                criterioEliminado = true;
            }
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

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCriterio = conexionBaseDeDatos.prepareStatement(modificarSQLCriterio);
            sentenciaCriterio.setString(1, criterio.getDescripcion());
            sentenciaCriterio.setInt(2, criterio.getNumeroCriterio());

            if (sentenciaCriterio.executeUpdate() > 0) {
                criterioModificado = true;
            }

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

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
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

    public List<CriterioAutoevaluacionDTO> listarCriteriosAutoevaluacionActivos() throws SQLException, IOException {
        List<CriterioAutoevaluacionDTO> listaCriterios = new ArrayList<>();

        String listarSQLCriterios = "SELECT * FROM criterioautoevaluacion WHERE estadoActivo = 1";

        try {
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCriterio = conexionBaseDeDatos.prepareStatement(listarSQLCriterios);
            resultadoConsultaCriterio = sentenciaCriterio.executeQuery();

            while (resultadoConsultaCriterio.next()) {
                int idCriterio = resultadoConsultaCriterio.getInt("idCriterio");
                String descripciones = resultadoConsultaCriterio.getString("descripciones");
                int numeroCriterio = resultadoConsultaCriterio.getInt("numeroCriterio");
                int estadoActivo = resultadoConsultaCriterio.getInt("estadoActivo");

                CriterioAutoevaluacionDTO criterio = new CriterioAutoevaluacionDTO(idCriterio, descripciones, numeroCriterio, estadoActivo);
                listaCriterios.add(criterio);
            }
        } finally {
            if (sentenciaCriterio != null) {
                sentenciaCriterio.close();
            }
        }

        return listaCriterios;
    }

    public int obtenerNumeroCriterioMasAlto() throws SQLException, IOException {

        String consultaSQL = "SELECT MAX(numeroCriterio) AS maxNumero FROM criterioautoevaluacion WHERE estadoActivo = 1";
        int numeroCriterioMasAlto = -1;

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
             PreparedStatement sentencia = conexionBaseDeDatos.prepareStatement(consultaSQL);
             ResultSet resultadoConsulta = sentencia.executeQuery()) {

            if (resultadoConsulta.next()) {
                numeroCriterioMasAlto = resultadoConsulta.getInt("maxNumero");
            }
        }

        return numeroCriterioMasAlto;
    }

    public void enumerarCriterios() throws SQLException, IOException {

        String obtenerCriteriosSQL = "SELECT * FROM criterioautoevaluacion WHERE estadoActivo = 1";
        String actualizarCriterioSQL = "UPDATE criterioautoevaluacion SET numeroCriterio = ? WHERE idCriterio = ?";

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
             PreparedStatement obtenerCriteriosStmt = conexionBaseDeDatos.prepareStatement(obtenerCriteriosSQL);
             ResultSet resultadoConsulta = obtenerCriteriosStmt.executeQuery()) {

            int numeroCriterio = 1;

            while (resultadoConsulta.next()) {
                int idCriterio = resultadoConsulta.getInt("idCriterio");

                try (PreparedStatement actualizarCriterioStmt = conexionBaseDeDatos.prepareStatement(actualizarCriterioSQL)) {
                    actualizarCriterioStmt.setInt(1, numeroCriterio);
                    actualizarCriterioStmt.setInt(2, idCriterio);
                    actualizarCriterioStmt.executeUpdate();
                }

                numeroCriterio++;
            }
        }
    }
}

