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

    public boolean modificarEstudiante(EstudianteDTO estudiante) throws SQLException, IOException {

        boolean estudianteModificado = false;

        String modificarSQLEstudiante = "UPDATE estudiante SET idUsuario = ? WHERE matricula = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(modificarSQLEstudiante);
            sentenciaEstudiante.setString(2, estudiante.getMatricula());
            sentenciaEstudiante.setInt(1, estudiante.getIdUsuario());

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


    public EstudianteDTO buscarEstudiantePorMatricula(String matricula) throws SQLException, IOException {

        EstudianteDTO estudiante = new EstudianteDTO(-1, "N/A", "N/A",
                "N/A", 0,0);

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

        String sql = """
        SELECT ve.matricula, ve.nombre, ve.apellidos
        FROM vista_estudiante ve
        JOIN estudiante e ON ve.matricula = e.matricula
        WHERE ve.estadoActivo = 1 AND e.NRC = ?
    """;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(sql);
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

    public List<EstudianteDTO> listarEstudiantesNoEvaluados(int numeroPersonal) throws SQLException, IOException {
        String sql = "SELECT ve.matricula, ve.nombre, ve.apellidos " +
                "FROM vista_estudiante ve " +
                "LEFT JOIN evaluacion e ON ve.matricula = e.idEstudiante AND e.numeroPersonal = ? " +
                "WHERE e.idEvaluacion IS NULL AND ve.estadoActivo = 1";

        List<EstudianteDTO> estudiantesNoEvaluados = new ArrayList<>();

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
             PreparedStatement sentencia = conexionBaseDeDatos.prepareStatement(sql)) {

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

}
