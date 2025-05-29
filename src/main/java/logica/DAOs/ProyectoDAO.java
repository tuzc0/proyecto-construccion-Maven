package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.ProyectoDTO;
import logica.interfaces.IProyectoDAO;
import java.io.IOException;
import java.sql.*;

public class ProyectoDAO implements IProyectoDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaProyecto = null;
    ResultSet resultadoProyecto;

    public int crearNuevoProyecto(ProyectoDTO proyectoDTO) throws SQLException, IOException {

        String sentenciaSQL = """
        INSERT INTO proyecto (
            nombre, objetivogeneral, objetivosinmediatos, 
            objetivosmediatos, metodologia, recursos,
            actividades, responsabilidades, duracion,
            idCronograma, estadoActivo, idRepresentante,
            descripciongeneral, usuariosDirectos, usuariosIndirectos
        ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)""";

        int idGenerado = -1;

        try {

            Connection conn = new ConexionBaseDeDatos().getConnection();
            sentenciaProyecto = conn.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS);

            sentenciaProyecto.setString(1, proyectoDTO.getNombre());
            sentenciaProyecto.setString(2, proyectoDTO.getObjetivoGeneral());
            sentenciaProyecto.setString(3, proyectoDTO.getObjetivosInmediatos());
            sentenciaProyecto.setString(4, proyectoDTO.getObjetivosMediatos());
            sentenciaProyecto.setString(5, proyectoDTO.getMetodologia());
            sentenciaProyecto.setString(6, proyectoDTO.getRecursos());
            sentenciaProyecto.setString(7, proyectoDTO.getActividades());
            sentenciaProyecto.setString(8, proyectoDTO.getResponsabilidades());
            sentenciaProyecto.setString(9, proyectoDTO.getDuracion());

            if (proyectoDTO.getIdCronograma() > 0) {
                sentenciaProyecto.setInt(10, proyectoDTO.getIdCronograma());
            } else {
                sentenciaProyecto.setNull(10, Types.INTEGER);
            }

            sentenciaProyecto.setInt(11, proyectoDTO.getEstadoActivo());
            sentenciaProyecto.setInt(12, proyectoDTO.getIdRepresentante());
            sentenciaProyecto.setString(13, proyectoDTO.getDescripcion());
            sentenciaProyecto.setInt(14, proyectoDTO.getUsuariosDirectos());
            sentenciaProyecto.setInt(15, proyectoDTO.getUsuariosIndirectos());
            sentenciaProyecto.executeUpdate();

            ResultSet rs = sentenciaProyecto.getGeneratedKeys();

            if (rs.next()) {

                idGenerado = rs.getInt(1);
            }

        } finally {

            if (sentenciaProyecto != null) {
                sentenciaProyecto.close();
            }
        }

        return idGenerado;
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
                "diasyhorarios = ?, estadoActivo = ?, idRepresentante = ?, descripciongeneral = ? " +
                "usuariosDirectos = ?, usuariosIndirectos = ? WHERE IDProyecto = ?";
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
            sentenciaProyecto.setInt(9, proyecto.getIdRepresentante());
            sentenciaProyecto.setString(10, proyecto.getDescripcion());
            sentenciaProyecto.setInt(11, proyecto.getUsuariosDirectos());
            sentenciaProyecto.setInt(12, proyecto.getUsuariosIndirectos());
            sentenciaProyecto.setInt(13, proyecto.getIdProyecto());

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

    public boolean adjuntarCronogramaProyecto(int idProyecto, int idCronograma) throws SQLException, IOException {

        String modificarSQLProyecto = "UPDATE proyecto SET idCronograma = ? WHERE IDProyecto = ?";
        boolean cronogramaAdjuntado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaProyecto = conexionBaseDeDatos.prepareStatement(modificarSQLProyecto);
            sentenciaProyecto.setInt(1, idCronograma);
            sentenciaProyecto.setInt(2, idProyecto);

            if (sentenciaProyecto.executeUpdate() > 0) {
                cronogramaAdjuntado = true;
            }

        } finally {

            if (sentenciaProyecto != null) {

                sentenciaProyecto.close();
            }
        }

        return cronogramaAdjuntado;
    }

    public ProyectoDTO buscarProyectoPorID(int idProyecto) throws SQLException, IOException {

        String consultaSQLProyecto = "SELECT * FROM proyecto WHERE IDProyecto = ?";
        ProyectoDTO proyecto = new ProyectoDTO(-1, null, null,
                null, null, null, null,
                null, null, null, -1, -1,
                -1, null, -1, -1);

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
                proyecto.setIdCronograma(resultadoProyecto.getInt("idCronograma"));
                proyecto.setEstadoActivo(resultadoProyecto.getInt("estadoActivo"));
                proyecto.setIdRepresentante(resultadoProyecto.getInt("idRepresentante"));
                proyecto.setDescripcion(resultadoProyecto.getString("descripciongeneral"));
                proyecto.setUsuariosDirectos(resultadoProyecto.getInt("usuariosDirectos"));
                proyecto.setUsuariosIndirectos(resultadoProyecto.getInt("usuariosIndirectos"));
            }

        } finally {

            if (sentenciaProyecto != null) {

                sentenciaProyecto.close();
            }
        }

        return proyecto;
    }
}