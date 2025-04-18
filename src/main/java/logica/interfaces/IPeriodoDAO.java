package logica.interfaces;

import logica.DTOs.PeriodoDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IPeriodoDAO {

    boolean crearNuevoPeriodo(PeriodoDTO periodo) throws SQLException, IOException;

    boolean eliminarPeriodoPorID(int idPeriodo) throws SQLException, IOException;

    boolean modificarPeriodo(PeriodoDTO periodo) throws SQLException, IOException;

    PeriodoDTO mostrarPeriodoActual() throws SQLException, IOException;
}
