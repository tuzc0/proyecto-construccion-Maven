package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.CuentaDTO;
import logica.interfaces.ICuentaDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CuentaDAO implements ICuentaDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaCuenta = null;
    ResultSet resultadoConsultaCuenta;



    public boolean crearNuevaCuenta(CuentaDTO cuenta) throws SQLException, IOException {

        String insertarSQLCuenta = "INSERT INTO cuenta VALUES(?, ?, ?)";
        boolean cuentaCreada = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCuenta = conexionBaseDeDatos.prepareStatement(insertarSQLCuenta);
            sentenciaCuenta.setString(1, cuenta.getCorreoElectronico());
            sentenciaCuenta.setString(2, cuenta.getContrasena());
            sentenciaCuenta.setInt(3, cuenta.getIdUsuario());

            if (sentenciaCuenta.executeUpdate() > 0) {
                cuentaCreada = true;
            }

        } finally {

            if (sentenciaCuenta != null) {

                sentenciaCuenta.close();
            }
        }

        return cuentaCreada;
    }

    public boolean eliminarCuentaPorID(int idUsuario) throws SQLException, IOException {

        String eliminarSQLUsuario = "UPDATE usuario SET estadoActivo = 0 WHERE idUsuario = " +
                "(SELECT idUsuario FROM cuenta WHERE idUsuario = ?)";
        boolean cuentaEliminada = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCuenta = conexionBaseDeDatos.prepareStatement(eliminarSQLUsuario);
            sentenciaCuenta.setInt(1, idUsuario);

            if (sentenciaCuenta.executeUpdate() > 0) {
                cuentaEliminada = true;
            }

        } finally {

            if (sentenciaCuenta != null) {

                sentenciaCuenta.close();
            }
        }

        return cuentaEliminada;
    }

    public boolean modificarCuenta(CuentaDTO cuenta) throws SQLException, IOException {

        String modificarSQLUsuario = "UPDATE cuenta SET correoElectronico = ?, contrasena = ? WHERE idUsuario = ?";
        boolean cuentaModificada = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCuenta = conexionBaseDeDatos.prepareStatement(modificarSQLUsuario);
            sentenciaCuenta.setString(1, cuenta.getCorreoElectronico());
            sentenciaCuenta.setString(2, cuenta.getContrasena());
            sentenciaCuenta.setInt(3, cuenta.getIdUsuario());

            if (sentenciaCuenta.executeUpdate() > 0) {
                cuentaModificada = true;
            }

        } finally {

            if (sentenciaCuenta != null) {

                sentenciaCuenta.close();
            }
        }

        return cuentaModificada;
    }

    public CuentaDTO buscarCuentaPorCorreo(String correo) throws SQLException, IOException {

        String buscarSQLUsuario = "SELECT * FROM cuenta WHERE correoElectronico = ?";
        CuentaDTO cuentaEncontrada = new CuentaDTO("N/A","N/A", -1);

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCuenta = conexionBaseDeDatos.prepareStatement(buscarSQLUsuario);
            sentenciaCuenta.setString(1, correo);
            resultadoConsultaCuenta = sentenciaCuenta.executeQuery();

            if (resultadoConsultaCuenta.next()) {

                cuentaEncontrada.setCorreoElectronico(resultadoConsultaCuenta.getString("correoElectronico"));
                cuentaEncontrada.setContrasena(resultadoConsultaCuenta.getString("contrasena"));
                cuentaEncontrada.setIdUsuario(resultadoConsultaCuenta.getInt("idUsuario"));
            }

        } finally {

            if (sentenciaCuenta != null) {

                sentenciaCuenta.close();
            }
        }

        return cuentaEncontrada;
    }

    public CuentaDTO buscarCuentaPorID(int idUsuario) throws SQLException, IOException {

        String buscarSQLUsuario = "SELECT * FROM cuenta WHERE idUsuario = ?";
        CuentaDTO cuentaEncontrada = new CuentaDTO("N/A","N/A", -1);

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaCuenta = conexionBaseDeDatos.prepareStatement(buscarSQLUsuario);
            sentenciaCuenta.setInt(1, idUsuario);
            resultadoConsultaCuenta = sentenciaCuenta.executeQuery();

            if (resultadoConsultaCuenta.next()) {

                cuentaEncontrada.setCorreoElectronico(resultadoConsultaCuenta.getString("correoElectronico"));
                cuentaEncontrada.setContrasena(resultadoConsultaCuenta.getString("contrasena"));
                cuentaEncontrada.setIdUsuario(resultadoConsultaCuenta.getInt("idUsuario"));
            }

        } finally {

            if (sentenciaCuenta != null) {

                sentenciaCuenta.close();
            }
        }

        return cuentaEncontrada;
    }
}
