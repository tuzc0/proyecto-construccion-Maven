package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.EvidenciaAutoevaluacionDTO;
import logica.interfaces.IEvidenciaAutoevaluacionDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EvidenciaAutoevaluacionDAO implements IEvidenciaAutoevaluacionDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaEvidenciaAutoevaluacion = null;
    ResultSet resultadoConsultaEvidenciaAutoevaluacion;

    public boolean insertarEvidenciaAutoevaluacion(EvidenciaAutoevaluacionDTO evidencia) throws SQLException, IOException {

        String insertarSQLEvidencia = "INSERT INTO evidenciaautoevaluacion (idEvidencia, URL, idAutoEvaluacion) VALUES (?, ?, ?)";
        boolean evidenciaInsertada = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEvidenciaAutoevaluacion = conexionBaseDeDatos.prepareStatement(insertarSQLEvidencia);
            sentenciaEvidenciaAutoevaluacion.setInt(1, evidencia.getIdEvidencia());
            sentenciaEvidenciaAutoevaluacion.setString(2, evidencia.getURL());
            sentenciaEvidenciaAutoevaluacion.setInt(3, evidencia.getIdAutoevaluacion());

            if (sentenciaEvidenciaAutoevaluacion.executeUpdate() > 0) {
                evidenciaInsertada = true;
            }

        } finally {

            if (sentenciaEvidenciaAutoevaluacion != null) {

                sentenciaEvidenciaAutoevaluacion.close();
            }
        }

        return evidenciaInsertada;
    }

    public EvidenciaAutoevaluacionDTO mostrarEvidenciaAutoevaluacionPorID(int idEvidencia) throws SQLException, IOException {

        String consultaSQLEvidencia = "SELECT * FROM evidenciaautoevaluacion WHERE idEvidencia = ?";
        EvidenciaAutoevaluacionDTO evidenciaEncontrada = new EvidenciaAutoevaluacionDTO(-1, " ", -1);

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEvidenciaAutoevaluacion = conexionBaseDeDatos.prepareStatement(consultaSQLEvidencia);
            sentenciaEvidenciaAutoevaluacion.setInt(1, idEvidencia);
            resultadoConsultaEvidenciaAutoevaluacion = sentenciaEvidenciaAutoevaluacion.executeQuery();

            if (resultadoConsultaEvidenciaAutoevaluacion.next()) {

                evidenciaEncontrada = new EvidenciaAutoevaluacionDTO();
                evidenciaEncontrada.setIdEvidencia(resultadoConsultaEvidenciaAutoevaluacion.getInt("idEvidencia"));
                evidenciaEncontrada.setURL(resultadoConsultaEvidenciaAutoevaluacion.getString("url"));
                evidenciaEncontrada.setIdAutoevaluacion(resultadoConsultaEvidenciaAutoevaluacion.getInt("idAutoEvaluacion"));
            }

        } finally {

            if (sentenciaEvidenciaAutoevaluacion != null) {
                sentenciaEvidenciaAutoevaluacion.close();
            }
        }

        return evidenciaEncontrada;
    }
}
