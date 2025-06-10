package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.ReporteDTO;
import logica.interfaces.IReporteDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReporteDAO implements IReporteDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaReporte = null;
    ResultSet resultadoReporte;

    public int insertarReporte(ReporteDTO reporte) throws SQLException, IOException {
        String insertarSQLReporte = "INSERT INTO reporte(numeroHoras, metodologia, observaciones, fecha, idEstudiante) VALUES (?, ?, ?, ?, ?)";
        int idGenerado = -1;

        try {
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaReporte = conexionBaseDeDatos.prepareStatement(insertarSQLReporte, PreparedStatement.RETURN_GENERATED_KEYS);
            sentenciaReporte.setInt(1, reporte.getNumeroHoras());
            sentenciaReporte.setString(2, reporte.getMetodologia());
            sentenciaReporte.setString(3, reporte.getObservaciones());
            sentenciaReporte.setTimestamp(4, reporte.getFecha());
            sentenciaReporte.setString(5, reporte.getMatricula());

            if (sentenciaReporte.executeUpdate() > 0) {
                ResultSet generatedKeys = sentenciaReporte.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idGenerado = generatedKeys.getInt(1);
                }
            }
        } finally {
            if (sentenciaReporte != null) {
                sentenciaReporte.close();
            }
        }

        return idGenerado;
    }

    public boolean modificarReporte(ReporteDTO reporte) throws SQLException, IOException {

        String modificarSQLReporte = "UPDATE reporte SET numeroHoras = ?, metodologia = ?, observaciones = ?, fecha = ?, IDEstudiante = ? WHERE IDReporte = ?";
        boolean reporteModificado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaReporte = conexionBaseDeDatos.prepareStatement(modificarSQLReporte);
            sentenciaReporte.setInt(1, reporte.getNumeroHoras());
            sentenciaReporte.setString(2, reporte.getMetodologia());
            sentenciaReporte.setString(3, reporte.getObservaciones());
            sentenciaReporte.setTimestamp(4, reporte.getFecha());
            sentenciaReporte.setString(5, reporte.getMatricula());
            sentenciaReporte.setInt(6, reporte.getIDReporte());

            if (sentenciaReporte.executeUpdate() > 0) {
                reporteModificado = true;
            }

        } finally {

            if (sentenciaReporte != null) {

                sentenciaReporte.close();
            }
        }

        return reporteModificado;
    }

    public ReporteDTO buscarReportePorID(int idReporte) throws SQLException, IOException {

        String buscarSQLReporte = "SELECT * FROM reporte WHERE IDReporte = ?";
        ReporteDTO reporteEncontrado = new ReporteDTO( -1, -1, "", "", null, "");

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaReporte = conexionBaseDeDatos.prepareStatement(buscarSQLReporte);
            sentenciaReporte.setInt(1, idReporte);
            resultadoReporte = sentenciaReporte.executeQuery();

            if (resultadoReporte.next()) {

                reporteEncontrado = new ReporteDTO();
                reporteEncontrado.setIDReporte(resultadoReporte.getInt("IdReporte"));
                reporteEncontrado.setNumeroHoras(resultadoReporte.getInt("numeroHoras"));
                reporteEncontrado.setMetodologia(resultadoReporte.getString("metodologia"));
                reporteEncontrado.setObservaciones(resultadoReporte.getString("observaciones"));
                reporteEncontrado.setFecha(resultadoReporte.getTimestamp("fecha"));
                reporteEncontrado.setMatricula(resultadoReporte.getString("IdEstudiante"));
            }

        } finally {

            if (sentenciaReporte != null) {

                sentenciaReporte.close();
            }
        }

        return reporteEncontrado;
    }
}
