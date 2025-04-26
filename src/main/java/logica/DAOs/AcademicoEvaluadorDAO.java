package logica.DAOs;

import accesoadatos.ConexionBD;
import logica.DTOs.AcademicoEvaluadorDTO;
import logica.interfaces.IAcademicoEvaluadorDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AcademicoEvaluadorDAO implements IAcademicoEvaluadorDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement consultaPreparada = null;
    ResultSet resultadoConsulta;

    public boolean insertarAcademicoEvaluador(AcademicoEvaluadorDTO academicoEvaluador) throws SQLException, IOException {

        String consultaSQL = "INSERT INTO academicoevaluador (numeroDePersonal, idUsuario) VALUES (?, ?)";
        boolean insercionExitosa = false;

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, academicoEvaluador.getNumeroDePersonal());
            consultaPreparada.setInt(2, academicoEvaluador.getIdUsuario());
            consultaPreparada.executeUpdate();
            insercionExitosa = true;

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return insercionExitosa;
    }

    public boolean eliminarAcademicoEvaluadorPorNumeroDePersonal(int numeroDePersonal) throws SQLException, IOException {

        String consultaSQL = "UPDATE usuario SET estadoActivo = ? WHERE idUsuario = " +
                "(SELECT idUsuario FROM academicoevaluador WHERE numeroDePersonal = ?)";
        boolean eliminadoConExito = false;

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, 0);
            consultaPreparada.setInt(2, numeroDePersonal);
            consultaPreparada.executeUpdate();
            eliminadoConExito = true;

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return eliminadoConExito;
    }

    public boolean modificarAcademicoEvaluador(AcademicoEvaluadorDTO academicoEvaluador) throws SQLException, IOException {

        String consultaSQL = "UPDATE academicoevaluador SET numeroDePersonal = ?, idUsuario = ? " +
                "WHERE numeroDePersonal = ?";
        boolean modificacionExitosa = false;

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, academicoEvaluador.getNumeroDePersonal());
            consultaPreparada.setInt(2, academicoEvaluador.getIdUsuario());
            consultaPreparada.setInt(3, academicoEvaluador.getNumeroDePersonal());
            consultaPreparada.executeUpdate();
            modificacionExitosa = true;

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return modificacionExitosa;
    }

    public AcademicoEvaluadorDTO buscarAcademicoEvaluadorPorNumeroDePersonal(int numeroDePersonal) throws SQLException, IOException {

        String consultaSQL = "SELECT * FROM vista_evaluadores WHERE numeroDePersonal = ?";
        AcademicoEvaluadorDTO academicoEvaluador = new AcademicoEvaluadorDTO(-1,-1, "", "", 0);

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, numeroDePersonal);
            resultadoConsulta = consultaPreparada.executeQuery();

            if (resultadoConsulta.next()) {

                int numeroDePersonalAcademico = resultadoConsulta.getInt("numeroDePersonal");
                int idUsuario = resultadoConsulta.getInt("idUsuario");
                String nombre = resultadoConsulta.getString("nombre");
                String apellido = resultadoConsulta.getString("apellidos");
                int estadoActivo = resultadoConsulta.getInt("estadoActivo");

                academicoEvaluador = new AcademicoEvaluadorDTO(numeroDePersonalAcademico, idUsuario, nombre, apellido, estadoActivo);
            }

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return academicoEvaluador;
    }
}
