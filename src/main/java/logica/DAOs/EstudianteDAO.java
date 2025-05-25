package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.EstudianteDTO;
import logica.interfaces.IEstudianteDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO implements IEstudianteDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaEstudiante = null;
    ResultSet resultadoConsultaEstudiante;

    public EstudianteDAO() throws SQLException, IOException {

        conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
    }

    public boolean insertarEstudiante(EstudianteDTO estudiante) throws SQLException, IOException {

        boolean estudianteInsertado = false;

        String insertarSQLEstudiante = "INSERT INTO estudiante VALUES(?, ?)";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(insertarSQLEstudiante);
            sentenciaEstudiante.setString(1, estudiante.getMatricula());
            sentenciaEstudiante.setInt(2, estudiante.getIdUsuario());

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


    public EstudianteDTO buscarEstudiantePorMatricula(String matricula) throws SQLException, IOException {

        EstudianteDTO estudiante = new EstudianteDTO(-1, "N/A", "N/A", "N/A", 0);

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

                estudiante = new EstudianteDTO(idUsuario, nombre, apellidos, matriculaEstudiante, estadoActivo);
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

                EstudianteDTO estudiante = new EstudianteDTO(idUsuario, nombre, apellidos, matricula, estadoActivo);
                listaEstudiantes.add(estudiante);
            }

        } finally {

            if (sentenciaEstudiante != null) {

                sentenciaEstudiante.close();
            }
        }

        return listaEstudiantes;
    }

}
