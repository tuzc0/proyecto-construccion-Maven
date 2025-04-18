package logica.interfaces;

import logica.DTOs.CuentaDTO;
import java.sql.SQLException;

public interface ICuentaDAO {

    boolean crearNuevaCuenta(CuentaDTO cuenta) throws SQLException;

    boolean eliminarCuentaPorID(int idUsuario) throws SQLException;

    boolean modificarCuenta(CuentaDTO usuario) throws SQLException;

    CuentaDTO buscarCuentaPorID(int idUsuario) throws SQLException;
}
