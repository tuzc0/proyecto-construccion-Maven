package logica.interfaces;

import logica.DTOs.CoordinadorDTO;
import java.sql.SQLException;

public interface ICoordinadorDAO {

    boolean insertarCoordinador(CoordinadorDTO coordinador) throws SQLException;

    boolean eliminarCoordinadorPorNumeroDePersonal(int numeroDePersonal) throws SQLException;

    boolean modificarCoordinador(CoordinadorDTO coordinador) throws SQLException;

    CoordinadorDTO buscarCoordinadorPorNumeroDePersonal(int numeroDePersonal) throws SQLException;
}
