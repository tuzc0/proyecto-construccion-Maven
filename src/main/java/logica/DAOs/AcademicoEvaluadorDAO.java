package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.AcademicoEvaluadorDTO;
import logica.interfaces.IAcademicoEvaluadorDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AcademicoEvaluadorDAO implements IAcademicoEvaluadorDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement consultaPreparada = null;
    ResultSet resultadoConsulta;

    public boolean insertarAcademicoEvaluador(AcademicoEvaluadorDTO academicoEvaluador) throws SQLException, IOException {

        String consultaSQL = "INSERT INTO academicoevaluador (numeroDePersonal, idUsuario) VALUES (?, ?)";
        boolean insercionExitosa = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, academicoEvaluador.getNumeroDePersonal());
            consultaPreparada.setInt(2, academicoEvaluador.getIdUsuario());

            if (consultaPreparada.executeUpdate() > 0) {
                insercionExitosa = true;
            }

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

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, 0);
            consultaPreparada.setInt(2, numeroDePersonal);

            if (consultaPreparada.executeUpdate() > 0) {

                eliminadoConExito = true;
            }

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return eliminadoConExito;
    }

    public boolean modificarAcademicoEvaluador(AcademicoEvaluadorDTO academicoEvaluador) throws SQLException, IOException {

        String consultaSQL = "UPDATE academicoevaluador SET numeroDePersonal = ? WHERE idUsuario = ?";
        boolean modificacionExitosa = false;

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, academicoEvaluador.getNumeroDePersonal());
            consultaPreparada.setInt(2, academicoEvaluador.getIdUsuario());

            if (consultaPreparada.executeUpdate() > 0) {

                modificacionExitosa = true;
            }

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return modificacionExitosa;
    }

    public AcademicoEvaluadorDTO buscarAcademicoEvaluadorPorNumeroDePersonal(int numeroDePersonal) throws SQLException, IOException {

        String consultaSQL = "SELECT * FROM vista_evaluadores WHERE numeroDePersonal = ?";
        AcademicoEvaluadorDTO academicoEvaluador = new AcademicoEvaluadorDTO(-1,-1, "N/A", "N/A", 0);

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
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

    public List<AcademicoEvaluadorDTO> listarAcademicos() throws SQLException, IOException {

        List<AcademicoEvaluadorDTO> academicos = new ArrayList<>();

        String consultaSQL = "SELECT * FROM vista_evaluadores WHERE estadoActivo = 1";

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

                AcademicoEvaluadorDTO academico = new AcademicoEvaluadorDTO(numeroDePersonalAcademico, idUsuario,
                        nombreAcademico, apellidos, estadoActivo);
                academicos.add(academico);
            }

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return academicos;
    }

    public AcademicoEvaluadorDTO buscarAcademicoEvaluadorPorID(int idUsuario) throws SQLException, IOException {
        AcademicoEvaluadorDTO academicoEvaluador = new AcademicoEvaluadorDTO(-1, -1, "N/A", "N/A", 0);

        String consultaSQL = "SELECT * FROM vista_evaluadores WHERE idUsuario = ?";

        try {
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            consultaPreparada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            consultaPreparada.setInt(1, idUsuario);
            resultadoConsulta = consultaPreparada.executeQuery();

            if (resultadoConsulta.next()) {

                int numeroDePersonal = resultadoConsulta.getInt("numeroDePersonal");
                String apellidos = resultadoConsulta.getString("apellidos");
                String nombre = resultadoConsulta.getString("nombre");
                int estadoActivo = resultadoConsulta.getInt("estadoActivo");

                academicoEvaluador = new AcademicoEvaluadorDTO(numeroDePersonal, idUsuario, nombre, apellidos, estadoActivo);
            }

        } finally {

            if (consultaPreparada != null) {

                consultaPreparada.close();
            }
        }

        return academicoEvaluador;
    }
}
