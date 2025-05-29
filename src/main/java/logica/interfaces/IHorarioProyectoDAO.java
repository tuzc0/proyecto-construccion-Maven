package logica.interfaces;

import logica.DTOs.HorarioProyectoDTO;
import logica.DTOs.ProyectoDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface IHorarioProyectoDAO {

    boolean crearNuevoHorarioProyecto(HorarioProyectoDTO horarioProyectoDTO) throws SQLException, IOException;

    boolean modificarHorario(HorarioProyectoDTO horarioProyectoDTO) throws SQLException, IOException;

    ProyectoDTO buscarHorarioPorIdDeProyecto(int idProyecto) throws SQLException, IOException;
}
