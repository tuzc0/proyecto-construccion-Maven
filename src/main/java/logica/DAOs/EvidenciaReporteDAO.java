package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.EvidenciaReporteDTO;
import logica.interfaces.IEvidenciaReporteDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EvidenciaReporteDAO implements IEvidenciaReporteDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaEvidenciaReporte = null;
    ResultSet resultadoEvidenciaReporte;

    public boolean insertarEvidenciaReporte(EvidenciaReporteDTO evidenciaReporteDTO) throws SQLException, IOException {

        String insertarSQL = "INSERT INTO evidenciareporte (idEvidencia, URL, idReporte) VALUES (?, ?, ?)";
        boolean evidenciaInsertada = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEvidenciaReporte = conexionBaseDeDatos.prepareStatement(insertarSQL);
            sentenciaEvidenciaReporte.setInt(1, evidenciaReporteDTO.getIdEvidencia());
            sentenciaEvidenciaReporte.setString(2, evidenciaReporteDTO.getURL());
            sentenciaEvidenciaReporte.setInt(3, evidenciaReporteDTO.getIdReporte());

            if (sentenciaEvidenciaReporte.executeUpdate() > 0) {
                evidenciaInsertada = true;
            }

        } finally {

            if (sentenciaEvidenciaReporte != null) {
                sentenciaEvidenciaReporte.close();
            }
        }

        return evidenciaInsertada;
    }

    public EvidenciaReporteDTO mostrarEvidenciaReportePorID(int idEvidencia) throws SQLException, IOException {

        String consultaSQL = "SELECT * FROM evidenciareporte WHERE idEvidencia = ?";
        EvidenciaReporteDTO evidenciaEncontrada = new EvidenciaReporteDTO(-1, " ", -1);

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEvidenciaReporte = conexionBaseDeDatos.prepareStatement(consultaSQL);
            sentenciaEvidenciaReporte.setInt(1, idEvidencia);
            resultadoEvidenciaReporte = sentenciaEvidenciaReporte.executeQuery();

            if (resultadoEvidenciaReporte.next()) {

                evidenciaEncontrada.setIdEvidencia(resultadoEvidenciaReporte.getInt("idEvidencia"));
                evidenciaEncontrada.setURL(resultadoEvidenciaReporte.getString("url"));
                evidenciaEncontrada.setIdReporte(resultadoEvidenciaReporte.getInt("idReporte"));
            }

        } finally {

            if (sentenciaEvidenciaReporte != null) {
                sentenciaEvidenciaReporte.close();
            }
        }

        return evidenciaEncontrada;
    }

    public List<EvidenciaReporteDTO> mostrarEvidenciasPorIdReporte(int idReporte) throws SQLException, IOException {
        List<EvidenciaReporteDTO> listaEvidencias = new ArrayList<>();
        String consultaSQL = "SELECT * FROM evidenciareporte WHERE idReporte = ?";

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
             PreparedStatement sentenciaEvidenciaReporte = conexionBaseDeDatos.prepareStatement(consultaSQL)) {

            sentenciaEvidenciaReporte.setInt(1, idReporte);
            try (ResultSet resultadoEvidenciaReporte = sentenciaEvidenciaReporte.executeQuery()) {
                while (resultadoEvidenciaReporte.next()) {
                    EvidenciaReporteDTO evidencia = new EvidenciaReporteDTO();
                    evidencia.setIdEvidencia(resultadoEvidenciaReporte.getInt("idEvidencia"));
                    evidencia.setURL(resultadoEvidenciaReporte.getString("URL"));
                    evidencia.setIdReporte(resultadoEvidenciaReporte.getInt("idReporte"));
                    listaEvidencias.add(evidencia);
                }
            }
        }

        return listaEvidencias;
    }
}


