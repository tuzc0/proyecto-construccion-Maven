package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.PeriodoDTO;
import logica.interfaces.IPeriodoDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PeriodoDAO implements IPeriodoDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaPeriodo = null;
    ResultSet resultadoConsultaPeriodo;

    public boolean crearNuevoPeriodo(PeriodoDTO periodo) throws SQLException, IOException {

        boolean periodoInsertado = false;
        String insertarSQLPeriodo = "INSERT INTO periodo VALUES(?, ?, ?, ?, ?)";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaPeriodo = conexionBaseDeDatos.prepareStatement(insertarSQLPeriodo);
            sentenciaPeriodo.setInt(1, periodo.getIDPeriodo());
            sentenciaPeriodo.setString(2, periodo.getDescripcion());
            sentenciaPeriodo.setInt(3, periodo.getEstadoActivo());
            sentenciaPeriodo.setDate(4, periodo.getFechaInicio());
            sentenciaPeriodo.setDate(5, periodo.getFechaFinal());

            if (sentenciaPeriodo.executeUpdate() > 0) {
                periodoInsertado = true;
            }

        } finally {

            if (sentenciaPeriodo != null) {

                sentenciaPeriodo.close();
            }
        }

        return periodoInsertado;
    }

    public boolean eliminarPeriodoPorID(int idPeriodo) throws SQLException, IOException {

        boolean periodoEliminado = false;
        String eliminarSQLPeriodo = "UPDATE periodo SET estadoActivo = 0 WHERE idPeriodo = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaPeriodo = conexionBaseDeDatos.prepareStatement(eliminarSQLPeriodo);
            sentenciaPeriodo.setInt(1, idPeriodo);

            if (sentenciaPeriodo.executeUpdate() > 0) {
                periodoEliminado = true;
            }

        } finally {

            if (sentenciaPeriodo != null) {

                sentenciaPeriodo.close();
            }
        }

        return periodoEliminado;
    }

    public boolean modificarPeriodo(PeriodoDTO periodo) throws SQLException, IOException {

        boolean periodoModificado = false;
        String modificarSQLPeriodo = "UPDATE periodo SET descripcion = ?, estadoActivo = ? WHERE idPeriodo = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaPeriodo = conexionBaseDeDatos.prepareStatement(modificarSQLPeriodo);
            sentenciaPeriodo.setInt(3, periodo.getIDPeriodo());
            sentenciaPeriodo.setString(1, periodo.getDescripcion());
            sentenciaPeriodo.setInt(2, periodo.getEstadoActivo());

            if (sentenciaPeriodo.executeUpdate() > 0) {
                periodoModificado = true;
            }

        } finally {

            if (sentenciaPeriodo != null) {

                sentenciaPeriodo.close();
            }
        }

        return periodoModificado;
    }

    public PeriodoDTO mostrarPeriodoActual ( ) throws SQLException, IOException {

        PeriodoDTO periodo = new PeriodoDTO(-1, "N/A", -1);
        String consultaSQLPeriodo = "SELECT * FROM periodo WHERE estadoActivo = 1";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaPeriodo = conexionBaseDeDatos.prepareStatement(consultaSQLPeriodo);
            resultadoConsultaPeriodo = sentenciaPeriodo.executeQuery( );

            if (resultadoConsultaPeriodo.next( )) {

                periodo.setIDPeriodo(resultadoConsultaPeriodo.getInt("idPeriodo"));
                periodo.setDescripcion(resultadoConsultaPeriodo.getString("descripcion"));
                periodo.setEstadoActivo(resultadoConsultaPeriodo.getInt("estadoActivo"));
            }

        } finally {

            if (sentenciaPeriodo != null) {

                sentenciaPeriodo.close();
            }
        }

        return periodo;
    }

    public boolean existePeriodoActivo() throws SQLException, IOException {

        boolean existeActivo = false;

        String consultaSQLPeriodo = "SELECT EXISTS (\n" +
                "  SELECT 1 FROM periodo WHERE estadoActivo = 1\n" +
                ") AS hayPeriodoActivo;\n";

        try {
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaPeriodo = conexionBaseDeDatos.prepareStatement(consultaSQLPeriodo);
            resultadoConsultaPeriodo = sentenciaPeriodo.executeQuery();

            if (resultadoConsultaPeriodo.next()) {
                existeActivo = resultadoConsultaPeriodo.getBoolean("hayPeriodoActivo");
            }

        } finally {

            if (sentenciaPeriodo != null) {
                sentenciaPeriodo.close();
            }
        }
        return existeActivo;
    }
}

