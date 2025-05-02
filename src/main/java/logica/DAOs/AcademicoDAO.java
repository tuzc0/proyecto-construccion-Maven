package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.AcademicoDTO;
import logica.interfaces.IAcademicoDAO;
import java.util.List;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AcademicoDAO implements IAcademicoDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement consultaPreparada = null;
    ResultSet resultadoConsulta;

    public boolean insertarAcademico(AcademicoDTO academico) throws SQLException, IOException {

        String consultaSQL = "INSERT INTO academico (numeroDePersonal, idUsuario) VALUES (?, ?)";
        boolean academicoInsertado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, academico.getNumeroDePersonal());
            consultaPreparada.setInt(2, academico.getIdUsuario());
            consultaPreparada.executeUpdate();
            academicoInsertado = true;

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return academicoInsertado;
    }

    public boolean eliminarAcademicoPorNumeroDePersonal(String numeroDePersonal) throws SQLException, IOException {

        String consultaSQL = "UPDATE usuario SET estadoActivo = 0 WHERE idUsuario = " +
                "(SELECT idUsuario FROM academico WHERE numeroDePersonal = ?)";
        boolean AcademicoEliminado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setString(1, numeroDePersonal);
            consultaPreparada.executeUpdate();
            AcademicoEliminado = true;

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return AcademicoEliminado;
    }

    public boolean modificarAcademico(AcademicoDTO academico) throws SQLException, IOException {

        String consultaSQL = "UPDATE academico SET numeroDePersonal = ? WHERE idUsuario = ?";
        boolean academicoModificado = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, academico.getNumeroDePersonal());
            consultaPreparada.setInt(2, academico.getIdUsuario());
            consultaPreparada.executeUpdate();
            academicoModificado = true;

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return academicoModificado;
    }

    public AcademicoDTO buscarAcademicoPorNumeroDePersonal(int numeroDePersonal) throws SQLException, IOException {

        AcademicoDTO academico = new AcademicoDTO(-1, -1, "N/A", "N/A", 0);

        String consultaSQL = "SELECT * FROM vista_academico_usuario WHERE numeroDePersonal = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, numeroDePersonal);
            resultadoConsulta = consultaPreparada.executeQuery();

            if (resultadoConsulta.next()) {

                int numeroDePersonalAcademico = resultadoConsulta.getInt("numeroDePersonal");
                int idUsuario = resultadoConsulta.getInt("idUsuario");
                String apellidos = resultadoConsulta.getString("apellidos");
                String nombre = resultadoConsulta.getString("nombre");
                int estadoActivo = resultadoConsulta.getInt("estadoActivo");

                academico = new AcademicoDTO(numeroDePersonalAcademico,idUsuario, nombre, apellidos, estadoActivo);
            }

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return academico;
    }

    public List<AcademicoDTO> listarAcademicos() throws SQLException, IOException {

        List<AcademicoDTO> academicos = new ArrayList<>();

        String consultaSQL = "SELECT * FROM vista_academico_usuario WHERE estadoActivo = 1";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            resultadoConsulta = consultaPreparada.executeQuery();

            while (resultadoConsulta.next()) {

                int numeroDePersonalAcademico = resultadoConsulta.getInt("numeroDePersonal");
                int idUsuario = resultadoConsulta.getInt("idUsuario");
                String apellidos = resultadoConsulta.getString("apellidos");
                String nombreAcademico = resultadoConsulta.getString("nombre");
                int estadoActivo = resultadoConsulta.getInt("estadoActivo");

                AcademicoDTO academico = new AcademicoDTO(numeroDePersonalAcademico, idUsuario, nombreAcademico, apellidos, estadoActivo);
                academicos.add(academico);
            }

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return academicos;
    }
}

