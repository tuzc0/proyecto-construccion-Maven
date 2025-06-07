package logica.interfaces;

import logica.DTOs.HorarioProyectoDTO;
import logica.DTOs.ProyectoDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface IHorarioProyectoDAO {

    boolean crearNuevoHorarioProyecto(HorarioProyectoDTO horarioProyectoDTO) throws SQLException, IOException;

    boolean modificarHorario(HorarioProyectoDTO horarioProyectoDTO) throws SQLException, IOException;

    List<HorarioProyectoDTO> buscarHorarioPorIdDeProyecto(int idProyecto) throws SQLException, IOException;
}
