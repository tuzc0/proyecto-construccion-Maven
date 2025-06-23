package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.EstudianteDTO;
import logica.interfaces.IEstudianteDAO;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO implements IEstudianteDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaEstudiante = null;
    ResultSet resultadoConsultaEstudiante;



    public boolean insertarEstudiante(EstudianteDTO estudiante) throws SQLException, IOException {

        boolean estudianteInsertado = false;

        String insertarSQLEstudiante = "INSERT INTO estudiante VALUES(?, ?, ?, ?, ?)";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(insertarSQLEstudiante);
            sentenciaEstudiante.setString(1, estudiante.getMatricula());
            sentenciaEstudiante.setInt(2, estudiante.getIdUsuario());

            if (estudiante.getIdProyecto() > 0) {
                sentenciaEstudiante.setInt(3, estudiante.getIdProyecto());
            } else {
                sentenciaEstudiante.setNull(3, Types.INTEGER);
            }

            sentenciaEstudiante.setInt(4, 0);
            sentenciaEstudiante.setInt(5,estudiante.getNRC());

            if (sentenciaEstudiante.executeUpdate() > 0) {

                estudianteInsertado = true;
            }


        } finally {

            if (sentenciaEstudiante != null) {

                sentenciaEstudiante.close();
            }
        }

        return estudianteInsertado;
    }



    public boolean eliminarEstudiantePorMatricula (int estadoActivo, String matricula) throws SQLException, IOException {

        boolean estudianteModificado = false;

        String modificarSQLEstudiante = "UPDATE usuario SET estadoActivo = ? WHERE idUsuario = (SELECT idUsuario FROM estudiante WHERE matricula = ?)";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(modificarSQLEstudiante);
            sentenciaEstudiante.setInt(1, estadoActivo);
            sentenciaEstudiante.setString(2, matricula);

            if (sentenciaEstudiante.executeUpdate() > 0) {

                estudianteModificado = true;
            }

        } finally {

            if (sentenciaEstudiante != null) {

                sentenciaEstudiante.close();
            }
        }

        return estudianteModificado;
    }

    public boolean reasignarProyecto(EstudianteDTO estudiante) throws SQLException, IOException {

        boolean estudianteReasignado = false;

        String modificarSQLEstudiante = "UPDATE estudiante SET idProyecto = ? WHERE matricula = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(modificarSQLEstudiante);
            sentenciaEstudiante.setInt(1, estudiante.getIdProyecto());
            sentenciaEstudiante.setString(2, estudiante.getMatricula());

            if (sentenciaEstudiante.executeUpdate() > 0) {

                estudianteReasignado = true;
            }

        } finally {

            if (sentenciaEstudiante != null) {

                sentenciaEstudiante.close();
            }
        }

        return estudianteReasignado;
    }

    public boolean modificarEstudiante(EstudianteDTO estudiante) throws SQLException, IOException {

        boolean estudianteModificado = false;

        String modificarSQLEstudiante = "UPDATE estudiante SET idUsuario = ? WHERE matricula = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(modificarSQLEstudiante);
            sentenciaEstudiante.setInt(1, estudiante.getIdUsuario());
            sentenciaEstudiante.setString(2, estudiante.getMatricula());

            if (sentenciaEstudiante.executeUpdate() > 0) {

                estudianteModificado = true;
            }

        } finally {

            if (sentenciaEstudiante != null) {

                sentenciaEstudiante.close();
            }
        }

        return estudianteModificado;
    }

    public boolean agregarEstudianteAGrupo(int nrc, String matricula) throws SQLException, IOException {

        boolean estudianteAgregado = false;

        String agregarEstudianteSQL = "UPDATE estudiante SET NRC = ? WHERE matricula = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(agregarEstudianteSQL);
            sentenciaEstudiante.setInt(1, nrc);
            sentenciaEstudiante.setString(2, matricula);

            if (sentenciaEstudiante.executeUpdate() > 0) {

                estudianteAgregado = true;
            }

        } finally {

            if (sentenciaEstudiante != null) {

                sentenciaEstudiante.close();
            }
        }

        return estudianteAgregado;
    }

    public boolean asignarProyecto(int idProyecto, String matricula) throws SQLException, IOException {

        boolean proyectoAsignado = false;

        String modificarEstudiante = "UPDATE estudiante SET idProyecto = ? WHERE matricula = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(modificarEstudiante);
            sentenciaEstudiante.setInt(1, idProyecto);
            sentenciaEstudiante.setString(2, matricula);

            if (sentenciaEstudiante.executeUpdate() > 0) {

                proyectoAsignado = true;
            }

        } finally {

            if (sentenciaEstudiante != null) {

                sentenciaEstudiante.close();
            }
        }

        return proyectoAsignado;
    }

    public boolean asignarCalificacion(double calificacion, String matricula) throws SQLException, IOException {

        boolean proyectoAsignado = false;

        String modificarEstudiante = "UPDATE estudiante SET  calificacionFinal = ? WHERE matricula = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(modificarEstudiante);
            sentenciaEstudiante.setDouble(1, calificacion);
            sentenciaEstudiante.setString(2, matricula);

            if (sentenciaEstudiante.executeUpdate() > 0) {

                proyectoAsignado = true;
            }

        } finally {

            if (sentenciaEstudiante != null) {

                sentenciaEstudiante.close();
            }
        }

        return proyectoAsignado;
    }

    public EstudianteDTO buscarEstudiantePorMatricula(String matricula) throws SQLException, IOException {

        EstudianteDTO estudiante = new EstudianteDTO(-1, "N/A", "N/A",
                "N/A", 0,-1);

        String buscarSQL = "SELECT * FROM vista_estudiante WHERE matricula = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(buscarSQL);
            sentenciaEstudiante.setString(1, matricula);
            resultadoConsultaEstudiante = sentenciaEstudiante.executeQuery();

            if (resultadoConsultaEstudiante.next()) {

                String matriculaEstudiante = resultadoConsultaEstudiante.getString("matricula");
                int idUsuario = resultadoConsultaEstudiante.getInt("idUsuario");
                String apellidos = resultadoConsultaEstudiante.getString("apellidos");
                String nombre = resultadoConsultaEstudiante.getString("nombre");
                int estadoActivo = resultadoConsultaEstudiante.getInt("estadoActivo");
                int idProyecto = resultadoConsultaEstudiante.getInt("idProyecto");

                estudiante = new EstudianteDTO(idUsuario, nombre, apellidos, matriculaEstudiante,
                        estadoActivo, idProyecto);
            }

        } finally {

            if (sentenciaEstudiante != null) {

                sentenciaEstudiante.close();
            }
        }

        return estudiante;
    }

    public List<EstudianteDTO> listarEstudiantes() throws SQLException, IOException {

        List<EstudianteDTO> listaEstudiantes = new ArrayList<>();

        String listarSQL = "SELECT * FROM vista_estudiante WHERE estadoactivo = 1";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(listarSQL);
            resultadoConsultaEstudiante = sentenciaEstudiante.executeQuery();

            while (resultadoConsultaEstudiante.next()) {

                String matricula = resultadoConsultaEstudiante.getString("matricula");
                int idUsuario = resultadoConsultaEstudiante.getInt("idUsuario");
                String apellidos = resultadoConsultaEstudiante.getString("apellidos");
                String nombre = resultadoConsultaEstudiante.getString("nombre");
                int estadoActivo = resultadoConsultaEstudiante.getInt("estadoActivo");
                int idProyecto = resultadoConsultaEstudiante.getInt("idProyecto");

                EstudianteDTO estudiante = new EstudianteDTO(idUsuario, nombre, apellidos, matricula,
                        estadoActivo, idProyecto);
                listaEstudiantes.add(estudiante);
            }

        } finally {

            if (sentenciaEstudiante != null) {

                sentenciaEstudiante.close();
            }
        }

        return listaEstudiantes;
    }

    public EstudianteDTO buscarEstudiantePorID(int idUsuario) throws SQLException, IOException {

        EstudianteDTO estudiante = new EstudianteDTO(-1, "N/A", "N/A",
                "N/A", 0,0);

        String buscarSQL = "SELECT * FROM vista_estudiante WHERE idUsuario = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(buscarSQL);
            sentenciaEstudiante.setInt(1, idUsuario);
            resultadoConsultaEstudiante = sentenciaEstudiante.executeQuery();

            if (resultadoConsultaEstudiante.next()) {

                String matricula = resultadoConsultaEstudiante.getString("matricula");
                String apellidos = resultadoConsultaEstudiante.getString("apellidos");
                String nombre = resultadoConsultaEstudiante.getString("nombre");
                int estadoActivo = resultadoConsultaEstudiante.getInt("estadoActivo");
                int idProyecto = resultadoConsultaEstudiante.getInt("idProyecto");

                estudiante = new EstudianteDTO(idUsuario, nombre, apellidos, matricula, estadoActivo, idProyecto);
            }

        } finally {

            if (sentenciaEstudiante != null) {
                sentenciaEstudiante.close();
            }
        }

        return estudiante;
    }

    public List<EstudianteDTO> obtenerEstudiantesActivosPorNRC(int nrc) throws SQLException, IOException {

        List<EstudianteDTO> estudiantes = new ArrayList<>();

        String consultaSql = """
        SELECT ve.matricula, ve.nombre, ve.apellidos
        FROM vista_estudiante ve
        JOIN estudiante e ON ve.matricula = e.matricula
        WHERE ve.estadoActivo = 1 AND e.NRC = ?
    """;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(consultaSql);
            sentenciaEstudiante.setInt(1, nrc);
            resultadoConsultaEstudiante = sentenciaEstudiante.executeQuery();

            while (resultadoConsultaEstudiante.next()) {
                String matricula = resultadoConsultaEstudiante.getString("matricula");
                String nombre = resultadoConsultaEstudiante.getString("nombre");
                String apellidos = resultadoConsultaEstudiante.getString("apellidos");


                EstudianteDTO estudiante = new EstudianteDTO(1, nombre, apellidos, matricula,
                        1, 1);
                estudiantes.add(estudiante);
            }

        } finally {

            if (sentenciaEstudiante != null) {
                sentenciaEstudiante.close();
            }
        }

        return estudiantes;
    }

    public List<EstudianteDTO> obtenerEstudiantesActivosConCalificacionPorNRC(int nrc) throws SQLException, IOException {
        List<EstudianteDTO> estudiantesActivos = new ArrayList<>();

        String consultaSql = """
        SELECT e.matricula, ve.nombre, ve.apellidos, e.idUsuario, e.idProyecto, e.calificacionFinal, e.NRC
        FROM estudiante e
        JOIN vista_estudiante ve ON e.matricula = ve.matricula
        WHERE e.NRC = ? AND ve.estadoActivo = 1
    """;

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
             PreparedStatement sentencia = conexionBaseDeDatos.prepareStatement(consultaSql)) {

            sentencia.setInt(1, nrc);

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    EstudianteDTO estudiante = new EstudianteDTO(
                            resultado.getInt("idUsuario"),
                            resultado.getString("nombre"),
                            resultado.getString("apellidos"),
                            resultado.getString("matricula"),
                            1, // estadoActivo
                            resultado.getInt("idProyecto"),
                            resultado.getInt("NRC"),
                            resultado.getFloat("calificacionFinal")

                    );
                    estudiantesActivos.add(estudiante);
                }
            }
        }

        return estudiantesActivos;
    }

    public List<EstudianteDTO> listarEstudiantesNoEvaluados(int numeroPersonal) throws SQLException, IOException {
        String ConsultaSql = """
        SELECT DISTINCT ve.matricula, ve.nombre, ve.apellidos 
        FROM vista_estudiante ve
        JOIN estudiante e ON ve.matricula = e.matricula
        JOIN grupo g ON e.NRC = g.NRC
        JOIN periodo p ON g.idPeriodo = p.idPeriodo
        LEFT JOIN evaluacion ev ON ve.matricula = ev.idEstudiante AND ev.numeroPersonal = ?
        WHERE ve.estadoActivo = 1
        AND g.estadoActivo = 1
        AND p.estadoActivo = 1
        AND ev.idEvaluacion IS NULL
    """;

        List<EstudianteDTO> estudiantesNoEvaluados = new ArrayList<>();

        try (Connection conexion = new ConexionBaseDeDatos().getConnection();
             PreparedStatement sentencia = conexion.prepareStatement(ConsultaSql)) {

            sentencia.setInt(1, numeroPersonal);

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    EstudianteDTO estudiante = new EstudianteDTO(
                            -1,
                            resultado.getString("nombre"),
                            resultado.getString("apellidos"),
                            resultado.getString("matricula"),
                            1,
                            0
                    );
                    estudiantesNoEvaluados.add(estudiante);
                }
            }
        }

        return estudiantesNoEvaluados;
    }

    public List<EstudianteDTO> listarEstudiantesConEvaluacionesPorGrupo(int nrc) throws SQLException, IOException {
        String sql = """
        SELECT ve.matricula, ve.nombre, ve.apellidos, COUNT(e.idEvaluacion) AS totalEvaluaciones
        FROM vista_estudiante ve
        JOIN estudiante es ON ve.matricula = es.matricula
        JOIN evaluacion e ON ve.matricula = e.idEstudiante
        WHERE ve.estadoActivo = 1 AND es.NRC = ?
        GROUP BY ve.matricula, ve.nombre, ve.apellidos
        HAVING COUNT(e.idEvaluacion) > 0
    """;

        List<EstudianteDTO> estudiantesConEvaluaciones = new ArrayList<>();

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
             PreparedStatement sentencia = conexionBaseDeDatos.prepareStatement(sql)) {

            sentencia.setInt(1, nrc);

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    EstudianteDTO estudiante = new EstudianteDTO(
                            -1,
                            resultado.getString("nombre"),
                            resultado.getString("apellidos"),
                            resultado.getString("matricula"),
                            1,
                            0
                    );
                    estudiantesConEvaluaciones.add(estudiante);
                }
            }
        }

        return estudiantesConEvaluaciones;
    }

    public boolean eliminarEstudiantesPorGrupo( int nrc) throws SQLException, IOException {

        boolean estudiantesModificados = false;

        String modificarSQLEstudiante = """
        UPDATE usuario 
        SET estadoActivo = ? 
        WHERE idUsuario IN (
            SELECT idUsuario 
            FROM estudiante 
            WHERE NRC = ?
        )
    """;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(modificarSQLEstudiante);
            sentenciaEstudiante.setInt(1, 0);
            sentenciaEstudiante.setInt(2, nrc);

            if (sentenciaEstudiante.executeUpdate() > 0) {
                estudiantesModificados = true;
            }

        } finally {

            if (sentenciaEstudiante != null) {
                sentenciaEstudiante.close();
            }
        }

        return estudiantesModificados;
    }

    public List<EstudianteDTO> listarEstudiantesConReporteMensualPorGrupo(int nrc) throws SQLException, IOException {
        List<EstudianteDTO> estudiantesConReporte = new ArrayList<>();

        String consultaSql = """
    SELECT e.matricula, ve.nombre, ve.apellidos, e.NRC
    FROM estudiante e
    JOIN vista_estudiante ve ON e.matricula = ve.matricula
    JOIN reporte rm ON e.matricula = rm.idEstudiante
    WHERE ve.estadoActivo = 1 AND e.NRC = ?
    GROUP BY e.NRC, e.matricula, ve.nombre, ve.apellidos
    """;

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
             PreparedStatement sentencia = conexionBaseDeDatos.prepareStatement(consultaSql)) {

            sentencia.setInt(1, nrc);

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    EstudianteDTO estudiante = new EstudianteDTO(
                            0,
                            resultado.getString("nombre"),
                            resultado.getString("apellidos"),
                            resultado.getString("matricula"),
                            1,
                            0,
                            resultado.getInt("NRC"),
                            0
                    );
                    estudiantesConReporte.add(estudiante);
                }
            }
        }

        return estudiantesConReporte;
    }

    public List<EstudianteDTO> listarEstudiantesConAutoevaluacion(int nrc) throws SQLException, IOException {
        List<EstudianteDTO> estudiantesConAutoevaluacion = new ArrayList<>();

        String consultaSql = """
        SELECT e.matricula, ve.nombre, ve.apellidos, e.NRC
        FROM estudiante e
        JOIN vista_estudiante ve ON e.matricula = ve.matricula
        JOIN autoevaluacion a ON e.matricula = a.idEstudiante
        WHERE ve.estadoActivo = 1 AND e.NRC = ?
        GROUP BY e.matricula, ve.nombre, ve.apellidos, e.NRC
    """;

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
             PreparedStatement sentencia = conexionBaseDeDatos.prepareStatement(consultaSql)) {

            sentencia.setInt(1, nrc);

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    EstudianteDTO estudiante = new EstudianteDTO(
                            0,
                            resultado.getString("nombre"),
                            resultado.getString("apellidos"),
                            resultado.getString("matricula"),
                            1,
                            0,
                            resultado.getInt("NRC"),
                            0
                    );
                    estudiantesConAutoevaluacion.add(estudiante);
                }
            }
        }

        return estudiantesConAutoevaluacion;
    }




}
