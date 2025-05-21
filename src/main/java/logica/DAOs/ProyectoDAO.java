package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.ProyectoDTO;
import logica.interfaces.IProyectoDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProyectoDAO implements IProyectoDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaProyecto = null;
    ResultSet resultadoProyecto;

    public boolean crearNuevoProyecto(ProyectoDTO proyecto) throws SQLException, IOException {

        String insertarSQLProyecto = "INSERT INTO proyecto VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        boolean proyectoInsertado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaProyecto = conexionBaseDeDatos.prepareStatement(insertarSQLProyecto);
            sentenciaProyecto.setInt(1, proyecto.getIdProyecto());
            sentenciaProyecto.setString(2, proyecto.getNombre());
            sentenciaProyecto.setString(3, proyecto.getObjetivoGeneral());
            sentenciaProyecto.setString(4, proyecto.getObjetivosInmediatos());
            sentenciaProyecto.setString(5, proyecto.getObjetivosMediatos());
            sentenciaProyecto.setString(6, proyecto.getMetodologia());
            sentenciaProyecto.setString(7, proyecto.getRecursos());
            sentenciaProyecto.setString(8, proyecto.getActividades());
            sentenciaProyecto.setString(9, proyecto.getResponsabilidades());
            sentenciaProyecto.setString(10, proyecto.getDuracion());
            sentenciaProyecto.setString(11, proyecto.getDiasYHorarios());
            sentenciaProyecto.setInt(12, proyecto.getIdCronograma());
            sentenciaProyecto.setInt(13, proyecto.getEstadoActivo());
            sentenciaProyecto.setInt(14, proyecto.getIdRepresentante());
            sentenciaProyecto.setString(15, proyecto.getDescripcion());

            if (sentenciaProyecto.executeUpdate() > 0) {
                proyectoInsertado = true;
            }

        } finally {

            if (sentenciaProyecto != null) {

                sentenciaProyecto.close();
            }
        }

        return proyectoInsertado;
    }

    public boolean eliminarProyectoPorID(int idProyecto) throws SQLException, IOException {

        String modificarSQLProyecto = "UPDATE proyecto SET estadoActivo = ? WHERE IDProyecto = ?";
        boolean proyectoEliminado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaProyecto = conexionBaseDeDatos.prepareStatement(modificarSQLProyecto);
            sentenciaProyecto.setInt(1, 0);
            sentenciaProyecto.setInt(2, idProyecto);

            if (sentenciaProyecto.executeUpdate() > 0) {
                proyectoEliminado = true;
            }

        } finally {

            if (sentenciaProyecto != null) {

                sentenciaProyecto.close();
            }
        }

        return proyectoEliminado;
    }

    public boolean modificarProyecto(ProyectoDTO proyecto) throws SQLException, IOException {

        String modificarSQLProyecto = "UPDATE proyecto SET nombre = ?, objetivogeneral = ?, " +
                "objetivosinmediatos = ?, objetivosmediatos = ?, metodologia = ?, recursos = ?, actividades = ?, " +
                "diasyhorarios = ?, idCronograma = ?, estadoActivo = ?, idRepresentante = ?, descripciongeneral = ? WHERE IDProyecto = ?";
        boolean proyectoModificado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaProyecto = conexionBaseDeDatos.prepareStatement(modificarSQLProyecto);
            sentenciaProyecto.setString(1, proyecto.getNombre());
            sentenciaProyecto.setString(2, proyecto.getObjetivoGeneral());
            sentenciaProyecto.setString(3, proyecto.getObjetivosInmediatos());
            sentenciaProyecto.setString(4, proyecto.getObjetivosMediatos());
            sentenciaProyecto.setString(5, proyecto.getMetodologia());
            sentenciaProyecto.setString(6, proyecto.getRecursos());
            sentenciaProyecto.setString(7, proyecto.getActividades());
            sentenciaProyecto.setString(8, proyecto.getResponsabilidades());
            sentenciaProyecto.setString(9, proyecto.getDiasYHorarios());
            sentenciaProyecto.setInt(10, proyecto.getIdCronograma());
            sentenciaProyecto.setInt(11, proyecto.getIdRepresentante());
            sentenciaProyecto.setString(12, proyecto.getDescripcion());
            sentenciaProyecto.setInt(12, proyecto.getIdProyecto());

            if (sentenciaProyecto.executeUpdate() > 0) {
                proyectoModificado = true;
            }

        } finally {

            if (sentenciaProyecto != null) {

                sentenciaProyecto.close();
            }
        }

        return proyectoModificado;
    }

    public ProyectoDTO buscarProyectoPorID(int idProyecto) throws SQLException, IOException {

        String consultaSQLProyecto = "SELECT * FROM proyecto WHERE IDProyecto = ?";
        ProyectoDTO proyecto = new ProyectoDTO(-1, null, null,
                null, null, null, null, null,
                null, null, null, -1, -1, -1, null);

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaProyecto = conexionBaseDeDatos.prepareStatement(consultaSQLProyecto);
            sentenciaProyecto.setInt(1, idProyecto);
            resultadoProyecto = sentenciaProyecto.executeQuery();

            if (resultadoProyecto.next()) {

                proyecto.setIdProyecto(resultadoProyecto.getInt("idProyecto"));
                proyecto.setNombre(resultadoProyecto.getString("nombre"));
                proyecto.setObjetivoGeneral(resultadoProyecto.getString("objetivogeneral"));
                proyecto.setObjetivosInmediatos(resultadoProyecto.getString("objetivosinmediatos"));
                proyecto.setObjetivosMediatos(resultadoProyecto.getString("objetivosmediatos"));
                proyecto.setMetodologia(resultadoProyecto.getString("metododologia"));
                proyecto.setRecursos(resultadoProyecto.getString("recursos"));
                proyecto.setActividades(resultadoProyecto.getString("actividades"));
                proyecto.setResponsabilidades(resultadoProyecto.getString("responsabilidades"));
                proyecto.setDuracion(resultadoProyecto.getString("duracion"));
                proyecto.setDiasYHorarios(resultadoProyecto.getString("diasyhorarios"));
                proyecto.setIdCronograma(resultadoProyecto.getInt("idCronograma"));
                proyecto.setEstadoActivo(resultadoProyecto.getInt("estadoActivo"));
                proyecto.setIdRepresentante(resultadoProyecto.getInt("idRepresentante"));
                proyecto.setDescripcion(resultadoProyecto.getString("descripciongeneral"));
            }

        } finally {

            if (sentenciaProyecto != null) {

                sentenciaProyecto.close();
            }
        }

        return proyecto;
    }
}