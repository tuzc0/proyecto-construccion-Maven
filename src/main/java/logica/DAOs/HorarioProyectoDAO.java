package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.AcademicoDTO;
import logica.DTOs.HorarioProyectoDTO;
import logica.DTOs.ProyectoDTO;
import logica.interfaces.IHorarioProyectoDAO;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HorarioProyectoDAO implements IHorarioProyectoDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaHorarioProyecto = null;
    ResultSet resultadoBusquedaHorario;


    public boolean crearNuevoHorarioProyecto(HorarioProyectoDTO horarioProyectoDTO) throws SQLException, IOException {

        String insertarHorario = "INSERT INTO horariosproyecto VALUES (?, ?, ?, ?, ?, ?)";
        boolean proyectoInsertado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaHorarioProyecto = conexionBaseDeDatos.prepareStatement(insertarHorario);
            sentenciaHorarioProyecto.setInt(1, horarioProyectoDTO.getIdHorario());
            sentenciaHorarioProyecto.setInt(2, horarioProyectoDTO.getIdProyecto());
            sentenciaHorarioProyecto.setString(3, horarioProyectoDTO.getDiaSemana());
            sentenciaHorarioProyecto.setTime(4, horarioProyectoDTO.getHoraInicio());
            sentenciaHorarioProyecto.setTime(5, horarioProyectoDTO.getHoraFin());
            sentenciaHorarioProyecto.setString(6,horarioProyectoDTO.getIdEstudiante());

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
            sentenciaHorarioProyecto.setTime(2, horarioProyectoDTO.getHoraInicio());
            sentenciaHorarioProyecto.setTime(3, horarioProyectoDTO.getHoraFin());
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

    public boolean asignarHorarioAEstudiante(int matricula, int idProyecto) throws SQLException, IOException {

        String añadirEstudiante = "UPDATE horariosproyecto SET idEstudiante = ? WHERE idProyecto = ?";
        boolean operacionExistosa = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaHorarioProyecto = conexionBaseDeDatos.prepareStatement(añadirEstudiante);
            sentenciaHorarioProyecto.setInt(1, matricula);
            sentenciaHorarioProyecto.setInt(2, idProyecto);

            if (sentenciaHorarioProyecto.executeUpdate() > 0) {

                operacionExistosa = true;
            }

        } finally {

            if (sentenciaHorarioProyecto != null) {

                sentenciaHorarioProyecto.close();
            }
        }

        return operacionExistosa;
    }

    public List<HorarioProyectoDTO> buscarHorarioPorIdDeProyecto(int idProyecto) throws SQLException, IOException {

        String buscarHorarioSQL = "SELECT * FROM horariosproyecto WHERE idProyecto = ?";
        List<HorarioProyectoDTO> listaHorarios = new ArrayList<>();

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaHorarioProyecto = conexionBaseDeDatos.prepareStatement(buscarHorarioSQL);
            sentenciaHorarioProyecto.setInt(1, idProyecto);
            resultadoBusquedaHorario = sentenciaHorarioProyecto.executeQuery();

            while (resultadoBusquedaHorario.next()) {

                int iDHorario = resultadoBusquedaHorario.getInt("idHorario");
                int iDProyecto = resultadoBusquedaHorario.getInt("idProyecto");
                String dia = resultadoBusquedaHorario.getString("diaSemana");
                Time horaDeInicio = resultadoBusquedaHorario.getTime("horaInicio");
                Time horaDeFin = resultadoBusquedaHorario.getTime("horaFin");
                String idEstudiante = resultadoBusquedaHorario.getString("idEstudiante");

                HorarioProyectoDTO horarioProyectoDTO = new HorarioProyectoDTO(iDHorario, iDProyecto, dia, horaDeInicio, horaDeFin, idEstudiante);
                listaHorarios.add(horarioProyectoDTO);
            }

        } finally {

            if (sentenciaHorarioProyecto != null) {
                sentenciaHorarioProyecto.close();
            }
        }

        return listaHorarios;
    }
}