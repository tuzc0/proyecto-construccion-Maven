package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.EvidenciaEvaluacionDTO;
import logica.interfaces.IEvidenciaEvaluacionDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EvidenciaEvaluacionDAO implements IEvidenciaEvaluacionDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaEvidenciaEvaluacion = null;
    ResultSet resultadoEvidenciaEvaluacion;

    public boolean insertarEvidenciaEvaluacion(EvidenciaEvaluacionDTO evidencia) throws SQLException, IOException {

        String insertarSQLEvidencia = "INSERT INTO evidenciaevaluacion (idEvidencia, URL, idEvaluacion) VALUES (?, ?, ?)";
        boolean evidenciaInsertada = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEvidenciaEvaluacion = conexionBaseDeDatos.prepareStatement(insertarSQLEvidencia);
            sentenciaEvidenciaEvaluacion.setInt(1, evidencia.getIdEvidencia());
            sentenciaEvidenciaEvaluacion.setString(2, evidencia.getURL());
            sentenciaEvidenciaEvaluacion.setInt(3, evidencia.getIdEvaluacion());
            sentenciaEvidenciaEvaluacion.executeUpdate();
            evidenciaInsertada = true;

        } finally {

            if (sentenciaEvidenciaEvaluacion != null) {

                sentenciaEvidenciaEvaluacion.close();
            }
        }

        return evidenciaInsertada;
    }

    public EvidenciaEvaluacionDTO mostrarEvidenciaEvaluacionPorID(int idEvidencia) throws SQLException, IOException {

        String consultaSQLEvidencia = "SELECT * FROM evidenciaevaluacion WHERE idEvidencia = ?";
        EvidenciaEvaluacionDTO evidenciaEncontrada = new EvidenciaEvaluacionDTO(-1, " ", -1);

        try {
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEvidenciaEvaluacion = conexionBaseDeDatos.prepareStatement(consultaSQLEvidencia);
            sentenciaEvidenciaEvaluacion.setInt(1, idEvidencia);
            resultadoEvidenciaEvaluacion = sentenciaEvidenciaEvaluacion.executeQuery();

            if (resultadoEvidenciaEvaluacion.next()) {
                evidenciaEncontrada = new EvidenciaEvaluacionDTO();
                evidenciaEncontrada.setIdEvidencia(resultadoEvidenciaEvaluacion.getInt("idEvidencia"));
                evidenciaEncontrada.setURL(resultadoEvidenciaEvaluacion.getString("url"));
                evidenciaEncontrada.setIdEvaluacion(resultadoEvidenciaEvaluacion.getInt("idEvaluacion"));
            }

        } finally {
            if (sentenciaEvidenciaEvaluacion != null) {
                sentenciaEvidenciaEvaluacion.close();
            }
        }

        return evidenciaEncontrada;
    }
}
