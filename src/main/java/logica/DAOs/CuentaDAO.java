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
    PreparedStatement consultaCuenta = null;
    ResultSet resultadoConsultaCuenta;



    public boolean crearNuevaCuenta(CuentaDTO cuenta) throws SQLException, IOException {

        String insertarSQLCuenta = "INSERT INTO cuenta VALUES(?, ?, ?)";
        boolean usuarioInsertado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaCuenta = conexionBaseDeDatos.prepareStatement(insertarSQLCuenta);
            consultaCuenta.setString(1, cuenta.getCorreoElectronico());
            consultaCuenta.setString(2, cuenta.getContrasena());
            consultaCuenta.setInt(3, cuenta.getIdUsuario());
            consultaCuenta.executeUpdate();
            usuarioInsertado = true;

        } finally {

            if (consultaCuenta != null) {

                consultaCuenta.close();
            }
        }

        return usuarioInsertado;
    }

    public boolean eliminarCuentaPorID(int idUsuario) throws SQLException, IOException {

        String eliminarSQLUsuario = "UPDATE usuario SET estadoActivo = 0 WHERE idUsuario = " +
                "(SELECT idUsuario FROM cuenta WHERE idUsuario = ?)";
        boolean cuentaEliminada = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaCuenta = conexionBaseDeDatos.prepareStatement(eliminarSQLUsuario);
            consultaCuenta.setInt(1, idUsuario);
            consultaCuenta.executeUpdate();
            cuentaEliminada = true;

        } finally {

            if (consultaCuenta != null) {

                consultaCuenta.close();
            }
        }

        return cuentaEliminada;
    }

    public boolean modificarCuenta(CuentaDTO cuenta) throws SQLException, IOException {

        String modificarSQLUsuario = "UPDATE cuenta SET correoElectronico = ?, contrasena = ? WHERE idUsuario = ?";
        boolean cuentaModificada = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaCuenta = conexionBaseDeDatos.prepareStatement(modificarSQLUsuario);
            consultaCuenta.setString(1, cuenta.getCorreoElectronico());
            consultaCuenta.setString(2, cuenta.getContrasena());
            consultaCuenta.setInt(3, cuenta.getIdUsuario());
            consultaCuenta.executeUpdate();
            cuentaModificada = true;

        } finally {

            if (consultaCuenta != null) {

                consultaCuenta.close();
            }
        }

        return cuentaModificada;
    }

    public CuentaDTO buscarCuentaPorCorreo(String correo) throws SQLException, IOException {

        String buscarSQLUsuario = "SELECT * FROM cuenta WHERE correoElectronico = ?";
        CuentaDTO cuentaEncontrada = new CuentaDTO("N/A","N/A", -1);

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaCuenta = conexionBaseDeDatos.prepareStatement(buscarSQLUsuario);
            consultaCuenta.setString(1, correo);
            resultadoConsultaCuenta = consultaCuenta.executeQuery();

            if (resultadoConsultaCuenta.next()) {

                cuentaEncontrada.setCorreoElectronico(resultadoConsultaCuenta.getString("correoElectronico"));
                cuentaEncontrada.setContrasena(resultadoConsultaCuenta.getString("contrasena"));
                cuentaEncontrada.setIdUsuario(resultadoConsultaCuenta.getInt("idUsuario"));
            }

        } finally {

            if (consultaCuenta != null) {

                consultaCuenta.close();
            }
        }

        return cuentaEncontrada;
    }

    public CuentaDTO buscarCuentaPorID(int idUsuario) throws SQLException, IOException {

        String buscarSQLUsuario = "SELECT * FROM cuenta WHERE idUsuario = ?";
        CuentaDTO cuentaEncontrada = new CuentaDTO("N/A","N/A", -1);

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaCuenta = conexionBaseDeDatos.prepareStatement(buscarSQLUsuario);
            consultaCuenta.setInt(1, idUsuario);
            resultadoConsultaCuenta = consultaCuenta.executeQuery();

            if (resultadoConsultaCuenta.next()) {

                cuentaEncontrada.setCorreoElectronico(resultadoConsultaCuenta.getString("correoElectronico"));
                cuentaEncontrada.setContrasena(resultadoConsultaCuenta.getString("contrasena"));
                cuentaEncontrada.setIdUsuario(resultadoConsultaCuenta.getInt("idUsuario"));
            }

        } finally {

            if (consultaCuenta != null) {

                consultaCuenta.close();
            }
        }

        return cuentaEncontrada;
    }
}
