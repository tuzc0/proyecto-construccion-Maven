package logica.interfaces;

import logica.DTOs.CuentaDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface ICuentaDAO {

    boolean crearNuevaCuenta(CuentaDTO cuenta) throws SQLException, IOException;

    boolean eliminarCuentaPorID(int idUsuario) throws SQLException, IOException;

    boolean modificarCuenta(CuentaDTO usuario) throws SQLException, IOException;

    CuentaDTO buscarCuentaPorCorreo(String correo) throws SQLException, IOException;

    CuentaDTO buscarCuentaPorID(int idUsuario) throws SQLException, IOException;
}
