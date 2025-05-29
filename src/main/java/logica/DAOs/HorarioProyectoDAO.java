package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.HorarioProyectoDTO;
import logica.DTOs.ProyectoDTO;
import logica.interfaces.IHorarioProyectoDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HorarioProyectoDAO implements IHorarioProyectoDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaHorarioProyecto = null;
    ResultSet resultadoBusquedaHorario;


    public boolean crearNuevoHorarioProyecto(HorarioProyectoDTO horarioProyectoDTO) throws SQLException, IOException {

        String insertarHorario = "INSERT INTO horariosproyecto VALUES (?, ?, ?, ?, ?)";
        boolean proyectoInsertado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaHorarioProyecto = conexionBaseDeDatos.prepareStatement(insertarHorario);
            sentenciaHorarioProyecto.setInt(1, horarioProyectoDTO.getIdHorario());
            sentenciaHorarioProyecto.setInt(2, horarioProyectoDTO.getIdProyecto());
            sentenciaHorarioProyecto.setString(3, horarioProyectoDTO.getDiaSemana());
            sentenciaHorarioProyecto.setTimestamp(4, horarioProyectoDTO.getHoraInicio());
            sentenciaHorarioProyecto.setTimestamp(5, horarioProyectoDTO.getHoraFin());

            if (sentenciaHorarioProyecto.executeUpdate() > 0) {
                proyectoInsertado = true;
            }

        } finally {

            if (sentenciaHorarioProyecto != null) {

                sentenciaHorarioProyecto.close();
            }
        }

        return proyectoInsertado;
    }

    public boolean modificarHorario(HorarioProyectoDTO horarioProyectoDTO) throws SQLException, IOException {

        String modificarHorario = "UPDATE horariosproyecto SET diaSemana = ?, horaInicio = ?, horaFin = ? WHERE idHorario = ?";
        boolean horarioModificado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaHorarioProyecto = conexionBaseDeDatos.prepareStatement(modificarHorario);
            sentenciaHorarioProyecto.setString(1, horarioProyectoDTO.getDiaSemana());
            sentenciaHorarioProyecto.setTimestamp(2, horarioProyectoDTO.getHoraInicio());
            sentenciaHorarioProyecto.setTimestamp(3, horarioProyectoDTO.getHoraFin());
            sentenciaHorarioProyecto.setInt(4, horarioProyectoDTO.getIdHorario());

            if (sentenciaHorarioProyecto.executeUpdate() > 0) {
                horarioModificado = true;
            }

        } finally {

            if (sentenciaHorarioProyecto != null) {

                sentenciaHorarioProyecto.close();
            }
        }

        return horarioModificado;
    }

    public ProyectoDTO buscarHorarioPorIdDeProyecto(int idProyecto) throws SQLException, IOException {

        String buscarHorario = "SELECT * FROM horariosproyecto WHERE idProyecto = ?";
        ProyectoDTO proyectoDTO = null;


        return proyectoDTO;
    }
}
