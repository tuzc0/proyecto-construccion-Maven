package logica.interfaces;

import logica.DTOs.CoordinadorDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface ICoordinadorDAO {

    boolean insertarCoordinador(CoordinadorDTO coordinador) throws SQLException, IOException;

    boolean eliminarCoordinadorPorNumeroDePersonal(int numeroDePersonal) throws SQLException, IOException;

    boolean modificarCoordinador(CoordinadorDTO coordinador) throws SQLException, IOException;

    CoordinadorDTO buscarCoordinadorPorNumeroDePersonal(int numeroDePersonal) throws SQLException, IOException;
}
