package logica.DAOs;

import accesoadatos.ConexionBD;
import logica.DTOs.EstudianteDTO;
import logica.interfaces.IEstudianteDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EstudianteDAO implements IEstudianteDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaEstudiante = null;
    ResultSet resultadoConsultaEstudiante;

    public EstudianteDAO() throws SQLException, IOException {

        conexionBaseDeDatos = new ConexionBD().getConnection();
    }

    public boolean insertarEstudiante(EstudianteDTO estudiante) throws SQLException, IOException {

        boolean estudianteInsertado = false;

        String insertarSQLEstudiante = "INSERT INTO estudiante VALUES(?, ?)";

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(insertarSQLEstudiante);
            sentenciaEstudiante.setString(1, estudiante.getMatricula());
            sentenciaEstudiante.setInt(2, estudiante.getIdUsuario());
            sentenciaEstudiante.executeUpdate();
            estudianteInsertado = true;

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

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(modificarSQLEstudiante);
            sentenciaEstudiante.setInt(1, estadoActivo);
            sentenciaEstudiante.setString(2, matricula);
            sentenciaEstudiante.executeUpdate();
            estudianteModificado = true;

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

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaEstudiante = conexionBaseDeDatos.prepareStatement(modificarSQLEstudiante);
            sentenciaEstudiante.setString(2, estudiante.getMatricula());
            sentenciaEstudiante.setInt(1, estudiante.getIdUsuario());
            sentenciaEstudiante.executeUpdate();
            estudianteModificado = true;

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

            conexionBaseDeDatos = new ConexionBD().getConnection();
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
}
