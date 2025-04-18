package logica.DAOs;

import accesoadatos.ConexionBD;
import logica.DTOs.EvidenciaAutoevaluacionDTO;
import logica.interfaces.IEvidenciaAutoevaluacionDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EvidenciaAutoevaluacionDAO implements IEvidenciaAutoevaluacionDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaEvaluacion = null;
    ResultSet resultadoConsultaEvaluacion;

    public boolean insertarEvidenciaAutoevaluacion(EvidenciaAutoevaluacionDTO evidencia) throws SQLException, IOException {

        String insertarSQLEvidencia = "INSERT INTO evidenciaautoevaluacion (idEvidencia, URL, idAutoEvaluacion) VALUES (?, ?, ?)";
        boolean evidenciaInsertada = false;

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaEvaluacion = conexionBaseDeDatos.prepareStatement(insertarSQLEvidencia);
            sentenciaEvaluacion.setInt(1, evidencia.getIdEvidencia());
            sentenciaEvaluacion.setString(2, evidencia.getURL());
            sentenciaEvaluacion.setInt(3, evidencia.getIdAutoevaluacion());
            sentenciaEvaluacion.executeUpdate();
            evidenciaInsertada = true;

        } finally {

            if (sentenciaEvaluacion != null) {

                sentenciaEvaluacion.close();
            }
        }

        return evidenciaInsertada;
    }


    public boolean mostrarEvidenciaAutoevaluacionPorID(int idEvidencia) throws SQLException, IOException {

        String consultaSQLEvidencia = "SELECT * FROM evidenciaautoevaluacion WHERE idEvidencia = ?";
        boolean evidenciaEncontrada = false;

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaEvaluacion = conexionBaseDeDatos.prepareStatement(consultaSQLEvidencia);
            sentenciaEvaluacion.setInt(1, idEvidencia);
            resultadoConsultaEvaluacion = sentenciaEvaluacion.executeQuery();

            if (resultadoConsultaEvaluacion.next()) {
                evidenciaEncontrada = true;
            }

        } finally {

            if (resultadoConsultaEvaluacion != null) {

                resultadoConsultaEvaluacion.close();
            }
            if (sentenciaEvaluacion != null) {

                sentenciaEvaluacion.close();
            }
        }

        return evidenciaEncontrada;
    }
}
