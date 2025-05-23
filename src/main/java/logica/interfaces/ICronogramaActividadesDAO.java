package logica.interfaces;

import logica.DTOs.CronogramaActividadesDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface ICronogramaActividadesDAO {

    int crearNuevoCronogramaDeActividades(CronogramaActividadesDTO cronograma) throws SQLException, IOException;

    boolean modificarCronogramaDeActividades(CronogramaActividadesDTO cronograma) throws SQLException, IOException;

    CronogramaActividadesDTO buscarCronogramaDeActividadesPorID(int idCronograma) throws SQLException, IOException;
}
