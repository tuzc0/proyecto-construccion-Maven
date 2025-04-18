package logica.DAOs;

import accesoadatos.ConexionBD;
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

    public CuentaDAO() throws SQLException, IOException {

        conexionBaseDeDatos = new ConexionBD().getConnection();
    }

    public boolean crearNuevaCuenta(CuentaDTO cuenta) throws SQLException {

        String insertarSQLCuenta = "INSERT INTO cuenta VALUES(?, ?, ?)";
        boolean usuarioInsertado = false;

        try {

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

    public boolean eliminarCuentaPorID(int idUsuario) throws SQLException {

        String eliminarSQLUsuario = "UPDATE usuario SET estadoActivo = ? WHERE idUsuario = " +
                "(SELECT idUsuario FROM cuenta WHERE idUsuario = ?)";
        boolean cuentaEliminada = false;

        try {

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

    public boolean modificarCuenta(CuentaDTO cuenta) throws SQLException {

        String modificarSQLUsuario = "UPDATE cuenta SET correoElectronico = ?, contrasena = ? WHERE idUsuario = ?";
        boolean cuentaModificada = false;

        try {

            consultaCuenta = conexionBaseDeDatos.prepareStatement(modificarSQLUsuario);
            consultaCuenta.setString(1, cuenta.getCorreoElectronico());
            consultaCuenta.setString(2, cuenta.getContrasena());
            consultaCuenta.executeUpdate();
            cuentaModificada = true;

        } finally {

            if (consultaCuenta != null) {

                consultaCuenta.close();
            }
        }

        return cuentaModificada;
    }

    public CuentaDTO buscarCuentaPorID(int idUsuario) throws SQLException {

        String buscarSQLUsuario = "SELECT * FROM cuenta WHERE idUsuario = ?";
        CuentaDTO cuentaEncontrada = new CuentaDTO("N/A","N/A", -1);

        try {

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
