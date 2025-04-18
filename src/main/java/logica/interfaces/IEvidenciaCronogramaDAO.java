package logica.interfaces;

import logica.DAOs.EvidenciaCronogramaDAO;
import logica.DTOs.EvidenciaCronogramaDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IEvidenciaCronogramaDAO {

    boolean insertarEvidenciaCronograma(EvidenciaCronogramaDTO evidenciaCronogramaDTO) throws SQLException, IOException;

    EvidenciaCronogramaDTO mostrarEvidenciaCronogramaPorID(int idEvidencia) throws SQLException, IOException;
}
