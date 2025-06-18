package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.CriterioEvaluacionDTO;
import logica.DTOs.EvaluacionDTO;
import logica.interfaces.ICriterioEvaluacionDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public boolean eliminarCriterioEvaluacionPorID(int idCriterio) throws SQLException, IOException {

        String eliminarSQLCriterio = "UPDATE criterioevaluacion SET estadoActivo = 0 WHERE idCriterio = ?";
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

    public boolean modificarCriterioEvaluacion(CriterioEvaluacionDTO criterio) throws SQLException, IOException {

        String modificarSQLCriterio = "UPDATE criterioevaluacion SET descripcion = ? WHERE idCriterio = ?";
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

    public CriterioEvaluacionDTO buscarCriterioEvaluacionPorID(int numeroCriterio) throws SQLException, IOException {

        String buscarSQLCriterio = "SELECT * FROM criterioevaluacion WHERE idCriterio = ?";
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

    public List<CriterioEvaluacionDTO> listarCriteriosActivos() throws SQLException, IOException {

        List<CriterioEvaluacionDTO> criteriosActivos = new ArrayList<>();
        String consultaSQL = "SELECT * FROM criterioevaluacion WHERE estadoActivo = 1";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCriterio = conexionBaseDeDatos.prepareStatement(consultaSQL);
            resultadoConsultaCriterio = sentenciaCriterio.executeQuery();

            while (resultadoConsultaCriterio.next()) {

                CriterioEvaluacionDTO criterio = new CriterioEvaluacionDTO(
                        resultadoConsultaCriterio.getInt("idCriterio"),
                        resultadoConsultaCriterio.getString("descripcion"),
                        resultadoConsultaCriterio.getInt("numeroCriterio"),
                        resultadoConsultaCriterio.getInt("estadoActivo")
                );
                criteriosActivos.add(criterio);
            }

        } finally {

            if (sentenciaCriterio != null) {

                sentenciaCriterio.close();
            }
        }

        return criteriosActivos;
    }

    public CriterioEvaluacionDTO buscarCriterioActivoPorNumero(int numeroCriterio) throws SQLException, IOException {

        String buscarSQLCriterio = "SELECT * FROM criterioevaluacion WHERE numeroCriterio = ? AND estadoActivo = 1";
        CriterioEvaluacionDTO criterioEncontrado = new CriterioEvaluacionDTO(-1, "N/A", -1, -1);

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCriterio = conexionBaseDeDatos.prepareStatement(buscarSQLCriterio);
            sentenciaCriterio.setInt(1, numeroCriterio);

            try (ResultSet resultadoConsulta = sentenciaCriterio.executeQuery()) {

                if (resultadoConsulta.next()) {

                    int idCriterio = resultadoConsulta.getInt("idCriterio");
                    String descripcion = resultadoConsulta.getString("descripcion");
                    int numero = resultadoConsulta.getInt("numeroCriterio");
                    int estadoActivo = resultadoConsulta.getInt("estadoActivo");

                    criterioEncontrado = new CriterioEvaluacionDTO(idCriterio, descripcion, numero, estadoActivo);
                }
            }

        } finally {

            if (sentenciaCriterio != null) {

                sentenciaCriterio.close();
            }
        }

        return criterioEncontrado;
    }

    public int obtenerNumeroCriterioMasAlto() throws SQLException, IOException {

        String consultaSQL = "SELECT MAX(numeroCriterio) AS maxNumero FROM criterioevaluacion WHERE estadoActivo = 1";
        int numeroCriterioMasAlto = -1;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCriterio = conexionBaseDeDatos.prepareStatement(consultaSQL);
            resultadoConsultaCriterio = sentenciaCriterio.executeQuery();

            if (resultadoConsultaCriterio.next()) {
                numeroCriterioMasAlto = resultadoConsultaCriterio.getInt("maxNumero");

                if (resultadoConsultaCriterio.wasNull()) {
                    numeroCriterioMasAlto = -1;
                }
            }

        } finally {

            if (sentenciaCriterio != null) {
                sentenciaCriterio.close();
            }
        }

        return numeroCriterioMasAlto;
    }

    public void enumerarCriterios() throws SQLException, IOException {

        String obtenerCriteriosSQL = "SELECT * FROM criterioevaluacion WHERE estadoActivo = 1";
        String actualizarCriterioSQL = "UPDATE criterioevaluacion SET numeroCriterio = ? WHERE idCriterio = ?";

        PreparedStatement sentenciaActualizarCriterio = null;

        try {

            sentenciaCriterio = conexionBaseDeDatos.prepareStatement(obtenerCriteriosSQL);
            resultadoConsultaCriterio = sentenciaCriterio.executeQuery();

            int numeroCriterio = 1;

            while (resultadoConsultaCriterio.next()) {

                int idCriterio = resultadoConsultaCriterio.getInt("idCriterio");

                try {

                    sentenciaActualizarCriterio = conexionBaseDeDatos.prepareStatement(actualizarCriterioSQL);
                    sentenciaActualizarCriterio.setInt(1, numeroCriterio);
                    sentenciaActualizarCriterio.setInt(2, idCriterio);
                    sentenciaActualizarCriterio.executeUpdate();

                } finally {

                    if (sentenciaActualizarCriterio != null) {
                        sentenciaActualizarCriterio.close();
                    }
                }

                numeroCriterio++;
            }

        } finally {

            if (sentenciaCriterio != null) {
                sentenciaCriterio.close();
            }
        }
    }
}
