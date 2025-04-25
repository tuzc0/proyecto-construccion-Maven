package logica.DAOs;

import accesoadatos.ConexionBD;
import logica.DTOs.AcademicoDTO;
import logica.interfaces.IAcademicoDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AcademicoDAO implements IAcademicoDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement consultaPreparada = null;
    ResultSet resultadoConsulta;

    public boolean insertarAcademico(AcademicoDTO academico) throws SQLException, IOException {

        String consultaSQL = "INSERT INTO academico (numeroDePersonal, idUsuario) VALUES (?, ?)";
        boolean academicoInsertado = false;

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
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

    public boolean eliminarAcademicoPorNumeroDePersonal(int estadoActivo, String numeroDePersonal) throws SQLException, IOException {

        String consultaSQL = "UPDATE usuario SET estadoActivo = ? WHERE idUsuario = " +
                "(SELECT idUsuario FROM academico WHERE numeroDePersonal = ?)";
        boolean AcademicoEliminado = false;

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, estadoActivo);
            consultaPreparada.setString(2, numeroDePersonal);
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

        String consultaSQL = "UPDATE academico SET numeroDePersonal = ?, idUsuario = ? " +
                "WHERE numeroDePersonal = ?";
        boolean academicoModificado = false;

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, academico.getNumeroDePersonal());
            consultaPreparada.setInt(2, academico.getIdUsuario());
            consultaPreparada.setInt(3, academico.getNumeroDePersonal());
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

            conexionBaseDeDatos = new ConexionBD().getConnection();
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
}

