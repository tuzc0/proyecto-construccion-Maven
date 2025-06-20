package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.ProyectoDTO;
import logica.interfaces.IProyectoDAO;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
            estadoActivo, idRepresentante,
            descripciongeneral, usuariosDirectos, usuariosIndirectos, estudiantesrequeridos
        ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)""";

        int idProyectoGenerado = -1;

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
            sentenciaProyecto.setInt(10, proyectoDTO.getEstadoActivo());
            sentenciaProyecto.setInt(11, proyectoDTO.getIdRepresentante());
            sentenciaProyecto.setString(12, proyectoDTO.getDescripcion());
            sentenciaProyecto.setInt(13, proyectoDTO.getUsuariosDirectos());
            sentenciaProyecto.setInt(14, proyectoDTO.getUsuariosIndirectos());
            sentenciaProyecto.setInt(15, proyectoDTO.getEstudiantesRequeridos());
            sentenciaProyecto.executeUpdate();

            ResultSet idNuevoProyecto = sentenciaProyecto.getGeneratedKeys();

            if (idNuevoProyecto.next()) {

                idProyectoGenerado = idNuevoProyecto.getInt(1);
            }

        } finally {

            if (sentenciaProyecto != null) {
                sentenciaProyecto.close();
            }
        }

        return idProyectoGenerado;
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
                "responsabilidades = ?, idRepresentante = ?, descripciongeneral = ?, " +
                "usuariosDirectos = ?, usuariosIndirectos = ?, estudiantesrequeridos = ? WHERE idProyecto = ?";

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
            sentenciaProyecto.setInt(13, proyecto.getEstudiantesRequeridos());
            sentenciaProyecto.setInt(14, proyecto.getIdProyecto());

            int filasAfectadas = sentenciaProyecto.executeUpdate();

            if (filasAfectadas > 0) {
                proyectoModificado = true;
            }

        } finally {

            if (sentenciaProyecto != null) {
                sentenciaProyecto.close();
            }
        }

        return proyectoModificado;
    }

    public ProyectoDTO buscarProyectoPorNombre(String nombreProyecto) throws SQLException, IOException {

        String consultaSQLProyecto = "SELECT * FROM proyecto WHERE LOWER(nombre) = LOWER(?)";
        ProyectoDTO proyecto = new ProyectoDTO(-1, null, null,
                null, null, null, null,
                null, null, null, -1,
                -1, null, -1, -1,-1);

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaProyecto = conexionBaseDeDatos.prepareStatement(consultaSQLProyecto);
            sentenciaProyecto.setString(1, nombreProyecto);
            resultadoProyecto = sentenciaProyecto.executeQuery();

            if (resultadoProyecto.next()) {

                proyecto.setIdProyecto(resultadoProyecto.getInt("idProyecto"));
                proyecto.setNombre(resultadoProyecto.getString("nombre"));
                proyecto.setObjetivoGeneral(resultadoProyecto.getString("objetivogeneral"));
                proyecto.setObjetivosInmediatos(resultadoProyecto.getString("objetivosinmediatos"));
                proyecto.setObjetivosMediatos(resultadoProyecto.getString("objetivosmediatos"));
                proyecto.setMetodologia(resultadoProyecto.getString("metodologia"));
                proyecto.setRecursos(resultadoProyecto.getString("recursos"));
                proyecto.setActividades(resultadoProyecto.getString("actividades"));
                proyecto.setResponsabilidades(resultadoProyecto.getString("responsabilidades"));
                proyecto.setDuracion(resultadoProyecto.getString("duracion"));
                proyecto.setEstadoActivo(resultadoProyecto.getInt("estadoActivo"));
                proyecto.setIdRepresentante(resultadoProyecto.getInt("idRepresentante"));
                proyecto.setDescripcion(resultadoProyecto.getString("descripciongeneral"));
                proyecto.setUsuariosDirectos(resultadoProyecto.getInt("usuariosDirectos"));
                proyecto.setUsuariosIndirectos(resultadoProyecto.getInt("usuariosIndirectos"));
                proyecto.setEstudiantesRequeridos(resultadoProyecto.getInt("estudiantesrequeridos"));
            }

        } finally {

            if (sentenciaProyecto != null) {

                sentenciaProyecto.close();
            }
        }

        return proyecto;
    }

    public ProyectoDTO buscarProyectoPorID(int idProyecto) throws SQLException, IOException {

        String consultaSQLProyecto = "SELECT * FROM proyecto WHERE idProyecto = ?";
        ProyectoDTO proyecto = new ProyectoDTO(-1, null, null,
                null, null, null, null,
                null, null, null, -1,
                -1, null, -1, -1,-1);

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
                proyecto.setMetodologia(resultadoProyecto.getString("metodologia"));
                proyecto.setRecursos(resultadoProyecto.getString("recursos"));
                proyecto.setActividades(resultadoProyecto.getString("actividades"));
                proyecto.setResponsabilidades(resultadoProyecto.getString("responsabilidades"));
                proyecto.setDuracion(resultadoProyecto.getString("duracion"));
                proyecto.setEstadoActivo(resultadoProyecto.getInt("estadoActivo"));
                proyecto.setIdRepresentante(resultadoProyecto.getInt("idRepresentante"));
                proyecto.setDescripcion(resultadoProyecto.getString("descripciongeneral"));
                proyecto.setUsuariosDirectos(resultadoProyecto.getInt("usuariosDirectos"));
                proyecto.setUsuariosIndirectos(resultadoProyecto.getInt("usuariosIndirectos"));
                proyecto.setEstudiantesRequeridos(resultadoProyecto.getInt("estudiantesrequeridos"));
            }

        } finally {

            if (sentenciaProyecto != null) {

                sentenciaProyecto.close();
            }
        }

        return proyecto;
    }

    public List<ProyectoDTO> listarProyectos() throws SQLException, IOException {

        List<ProyectoDTO> proyectos = new ArrayList<>();
        String consultaSQL = "SELECT * FROM proyecto WHERE estadoActivo = 1";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaProyecto = conexionBaseDeDatos.prepareStatement(consultaSQL);
            resultadoProyecto = sentenciaProyecto.executeQuery();

            while (resultadoProyecto.next()) {

                int idProyecto = resultadoProyecto.getInt("idProyecto");
                String nombreProyecto = resultadoProyecto.getString("nombre");
                String objetivoGeneral = resultadoProyecto.getString("objetivogeneral");
                String objetivosInmediatos = resultadoProyecto.getString("objetivosinmediatos");
                String objetivosMediatos = resultadoProyecto.getString("objetivosmediatos");
                String metodologia = resultadoProyecto.getString("metodologia");
                String recursos = resultadoProyecto.getString("recursos");
                String actividades = resultadoProyecto.getString("actividades");
                String responsabilidades = resultadoProyecto.getString("responsabilidades");
                String duracion = resultadoProyecto.getString("duracion");
                int estadoActivo = resultadoProyecto.getInt("estadoActivo");
                int idRepresentante = resultadoProyecto.getInt("idRepresentante");
                String descripcion = resultadoProyecto.getString("descripciongeneral");
                int usuariosDirectos = resultadoProyecto.getInt("usuariosDirectos");
                int usuariosIndirectos = resultadoProyecto.getInt("usuariosIndirectos");
                int estudiantesRequeridos = resultadoProyecto.getInt("estudiantesrequeridos");

                ProyectoDTO proyecto = new ProyectoDTO(idProyecto, nombreProyecto, objetivoGeneral,
                        objetivosInmediatos, objetivosMediatos, metodologia, recursos, actividades, responsabilidades,
                        duracion, estadoActivo, idRepresentante, descripcion, usuariosDirectos, usuariosIndirectos,
                        estudiantesRequeridos);
                proyectos.add(proyecto);
            }

        } finally {
             if (sentenciaProyecto != null) {

                 sentenciaProyecto.close();
             }
        }

        return proyectos;
    }

    public List<ProyectoDTO> listarProyectosConCupo() throws SQLException, IOException {

        List<ProyectoDTO> proyectos = new ArrayList<>();

        String consultaSQL = """
        SELECT 
            idProyecto,
            nombreProyecto,
            objetivoGeneralProyecto,
            objetivosInmediatosProyecto,
            objetivosMediatosProyecto,
            metodologiaProyecto,
            recursosProyecto,
            actividadesProyecto,
            responsabilidadesProyecto,
            duracionProyecto,
            estadoActivoProyecto,
            idRepresentanteProyecto,
            descripcionProyecto,
            cantidadUsuariosDirectosProyecto,
            cantidadUsuariosIndirectosProyecto,
            estudiantesRequeridosProyecto,
            estudiantesAsignadosProyecto
        FROM vistaProyectosConCupo;
        """;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaProyecto = conexionBaseDeDatos.prepareStatement(consultaSQL);
            resultadoProyecto = sentenciaProyecto.executeQuery();

            while (resultadoProyecto.next()) {

                ProyectoDTO proyecto = new ProyectoDTO();

                proyecto.setIdProyecto(resultadoProyecto.getInt("idProyecto"));
                proyecto.setNombre(resultadoProyecto.getString("nombreProyecto"));
                proyecto.setObjetivoGeneral(resultadoProyecto.getString("objetivoGeneralProyecto"));
                proyecto.setObjetivosInmediatos(resultadoProyecto.getString("objetivosinmediatosProyecto"));
                proyecto.setObjetivosMediatos(resultadoProyecto.getString("objetivosmediatosProyecto"));
                proyecto.setMetodologia(resultadoProyecto.getString("metodologiaProyecto"));
                proyecto.setRecursos(resultadoProyecto.getString("recursosProyecto"));
                proyecto.setActividades(resultadoProyecto.getString("actividadesProyecto"));
                proyecto.setResponsabilidades(resultadoProyecto.getString("responsabilidadesProyecto"));
                proyecto.setDuracion(resultadoProyecto.getString("duracionProyecto"));
                proyecto.setEstadoActivo(resultadoProyecto.getInt("estadoActivoProyecto"));
                proyecto.setIdRepresentante(resultadoProyecto.getInt("idRepresentanteProyecto"));
                proyecto.setDescripcion(resultadoProyecto.getString("descripcionProyecto"));
                proyecto.setUsuariosDirectos(resultadoProyecto.getInt("cantidadUsuariosDirectosProyecto"));
                proyecto.setUsuariosIndirectos(resultadoProyecto.getInt("cantidadUsuariosIndirectosProyecto"));
                proyecto.setEstudiantesRequeridos(resultadoProyecto.getInt("estudiantesrequeridosProyecto"));
                proyecto.setEstudiantesAsignados(resultadoProyecto.getInt("estudiantesAsignadosProyecto"));
                proyectos.add(proyecto);
            }

        } finally {

            if (sentenciaProyecto != null) {
                sentenciaProyecto.close();
            }
        }

        return proyectos;
    }
}