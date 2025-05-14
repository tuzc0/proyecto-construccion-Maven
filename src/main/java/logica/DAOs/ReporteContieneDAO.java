package logica.DAOs;

import logica.DTOs.ReporteContieneDTO;
import logica.interfaces.IReporteContieneDAO;
import accesoadatos.ConexionBaseDeDatos;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReporteContieneDAO implements IReporteContieneDAO {

    Connection conexion;
    PreparedStatement sentenciaReporte = null;
    ResultSet resultadoConsulta;

    public boolean insertarReporteContiene(ReporteContieneDTO reporte) throws SQLException, IOException {

        String consultaSQL = "INSERT INTO reportecontiene (idReporte, idActividad, fechaInicioReal, fechaInicioFin, estadoActivo) VALUES (?, ?, ?, ?, 1)";
        boolean insercionExitosa = false;

        try {

            conexion = new ConexionBaseDeDatos().getConnection();
            sentenciaReporte = conexion.prepareStatement(consultaSQL);
            sentenciaReporte.setInt(1, reporte.getIdReporte());
            sentenciaReporte.setInt(2, reporte.getIdActividad());
            sentenciaReporte.setTimestamp(3, reporte.getFechaInicioReal());
            sentenciaReporte.setTimestamp(4, reporte.getFechaFinReal());

            if (sentenciaReporte.executeUpdate() > 0) {
                insercionExitosa = true;
            }

        } finally {

            if (sentenciaReporte != null) {

                sentenciaReporte.close();
            }
        }
        return insercionExitosa;
    }

    public boolean eliminarReporteContienePorID(int idReporte) throws SQLException, IOException {

        String consultaSQL = "UPDATE reportecontiene SET estadoActivo = 0 WHERE idReporte = ?";
        boolean reporteEliminado = false;

        try {

            conexion = new ConexionBaseDeDatos().getConnection();
            sentenciaReporte = conexion.prepareStatement(consultaSQL);
            sentenciaReporte.setInt(1, idReporte);

            if (sentenciaReporte.executeUpdate() > 0) {
                reporteEliminado = true;
            }

        } finally {

            if (sentenciaReporte != null) {

                sentenciaReporte.close();
            }
        }
        return reporteEliminado;
    }

    public boolean modificarReporteContiene(ReporteContieneDTO reporte) throws SQLException, IOException {

        String consultaSQL = "UPDATE reportecontiene SET idActividad = ?, fechaInicioReal = ?, fechaInicioFin = ? WHERE idReporte = ?";
        boolean modificacionExitosa = false;

        try {

            conexion = new ConexionBaseDeDatos().getConnection();
            sentenciaReporte = conexion.prepareStatement(consultaSQL);
            sentenciaReporte.setInt(1, reporte.getIdActividad());
            sentenciaReporte.setTimestamp(2, reporte.getFechaInicioReal());
            sentenciaReporte.setTimestamp(3, reporte.getFechaFinReal());
            sentenciaReporte.setInt(4, reporte.getIdReporte());

            if (sentenciaReporte.executeUpdate() > 0) {
                modificacionExitosa = true;
            }

        } finally {

            if (sentenciaReporte != null) {

                sentenciaReporte.close();
            }
        }
        return modificacionExitosa;
    }

    public ReporteContieneDTO buscarReporteContienePorID(int idReporte) throws SQLException, IOException {

        String consultaSQL = "SELECT * FROM reportecontiene WHERE idReporte = ?";
        ReporteContieneDTO reporte = new ReporteContieneDTO(-1, -1, null, null, -1);

        try {

            conexion = new ConexionBaseDeDatos().getConnection();
            sentenciaReporte = conexion.prepareStatement(consultaSQL);
            sentenciaReporte.setInt(1, idReporte);
            resultadoConsulta = sentenciaReporte.executeQuery();

            if (resultadoConsulta.next()) {

                reporte.setIdReporte(resultadoConsulta.getInt("idReporte"));
                reporte.setIdActividad(resultadoConsulta.getInt("idActividad"));
                reporte.setFechaInicioReal(resultadoConsulta.getTimestamp("fechaInicioReal"));
                reporte.setFechaFinReal(resultadoConsulta.getTimestamp("fechaInicioFin"));
            }

        } finally {

            if (sentenciaReporte != null) {

                sentenciaReporte.close();
            }
        }
        return reporte;
    }
}
