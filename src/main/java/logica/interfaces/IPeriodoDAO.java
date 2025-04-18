package logica.interfaces;

import logica.DTOs.PeriodoDTO;
import java.sql.SQLException;

public interface IPeriodoDAO {

    boolean crearNuevoPeriodo(PeriodoDTO periodo) throws SQLException;

    boolean eliminarPeriodoPorID(int idPeriodo) throws SQLException;

    boolean modificarPeriodo(PeriodoDTO periodo) throws SQLException;

    PeriodoDTO mostrarPeriodoActual() throws SQLException;
}
