package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.RepresentanteDTO;
import logica.interfaces.IRepresentanteDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepresentanteDAO implements IRepresentanteDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaRepresentante = null;
    ResultSet resultadoConsultaRepresentante;



    public boolean insertarRepresentante(RepresentanteDTO representante) throws SQLException, IOException {

        boolean representanteInsertado = false;
        String insertarSQLRepresentante = "INSERT INTO representante VALUES(?, ?, ?, ?, ?, ?, ?)";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaRepresentante = conexionBaseDeDatos.prepareStatement(insertarSQLRepresentante);
            sentenciaRepresentante.setInt(1, representante.getIDRepresentante());
            sentenciaRepresentante.setString(2, representante.getCorreo());
            sentenciaRepresentante.setString(3, representante.getTelefono());
            sentenciaRepresentante.setString(4, representante.getNombre());
            sentenciaRepresentante.setString(5, representante.getApellidos());
            sentenciaRepresentante.setInt(6, representante.getIdOV());
            sentenciaRepresentante.setInt(7, representante.getEstadoActivo());
            sentenciaRepresentante.executeUpdate();
            representanteInsertado = true;

        } finally {

            if (sentenciaRepresentante != null) {

                sentenciaRepresentante.close();
            }
        }

        return representanteInsertado;
    }

    public boolean eliminarRepresentantePorID(int idRepresentante) throws SQLException, IOException {

        boolean representanteEliminado = false;
        String eliminarSQLRepresentante = "UPDATE representante SET estadoActivo = 0 WHERE IdRepresentante = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaRepresentante = conexionBaseDeDatos.prepareStatement(eliminarSQLRepresentante);
            sentenciaRepresentante.setInt(1, idRepresentante);
            sentenciaRepresentante.executeUpdate();
            representanteEliminado = true;

        } finally {

            if (sentenciaRepresentante != null) {

                sentenciaRepresentante.close();
            }
        }

        return representanteEliminado;
    }

    public boolean modificarRepresentante(RepresentanteDTO representante) throws SQLException, IOException {

        boolean representanteModificado = false;
        String modificarSQLRepresentante = "UPDATE representante SET correo = ?, telefono = ?, nombre = ?, apellidos = ?, idOV = ?, estadoActivo = ? WHERE IdRepresentante = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaRepresentante = conexionBaseDeDatos.prepareStatement(modificarSQLRepresentante);
            sentenciaRepresentante.setString(1, representante.getCorreo());
            sentenciaRepresentante.setString(2, representante.getTelefono());
            sentenciaRepresentante.setString(3, representante.getNombre());
            sentenciaRepresentante.setString(4, representante.getApellidos());
            sentenciaRepresentante.setInt(5, representante.getIdOV());
            sentenciaRepresentante.setInt(6, representante.getEstadoActivo());
            sentenciaRepresentante.setInt(7, representante.getIDRepresentante());
            sentenciaRepresentante.executeUpdate();
            representanteModificado = true;

        } finally {

            if (sentenciaRepresentante != null) {

                sentenciaRepresentante.close();
            }
        }

        return representanteModificado;
    }

    public RepresentanteDTO buscarRepresentantePorID(int idRepresentante) throws SQLException, IOException {

        RepresentanteDTO representante = new RepresentanteDTO(-1, "N/A", "N/A", "N/A", "N/A", 0, 0);
        String buscarSQLRepresentante = "SELECT * FROM representante WHERE IdRepresentante = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaRepresentante = conexionBaseDeDatos.prepareStatement(buscarSQLRepresentante);
            sentenciaRepresentante.setInt(1, idRepresentante);
            resultadoConsultaRepresentante = sentenciaRepresentante.executeQuery();

            if (resultadoConsultaRepresentante.next()) {

                representante = new RepresentanteDTO(
                        resultadoConsultaRepresentante.getInt("idRepresentante"),
                        resultadoConsultaRepresentante.getString("correo"),
                        resultadoConsultaRepresentante.getString("telefono"),
                        resultadoConsultaRepresentante.getString("nombre"),
                        resultadoConsultaRepresentante.getString("apellidos"),
                        resultadoConsultaRepresentante.getInt("idOV"),
                        resultadoConsultaRepresentante.getInt("estadoActivo")
                );
            }

        } finally {

            if (sentenciaRepresentante != null) {

                sentenciaRepresentante.close();
            }
        }

        return representante;
    }

    public RepresentanteDTO buscarRepresentantePorCorreo(String correo) throws SQLException, IOException {
        RepresentanteDTO representante = new RepresentanteDTO(-1, "N/A", "N/A", "N/A", "N/A", 0, 0);
        String buscarSQLRepresentante = "SELECT * FROM representante WHERE correo = ?";

        try {
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaRepresentante = conexionBaseDeDatos.prepareStatement(buscarSQLRepresentante);
            sentenciaRepresentante.setString(1, correo);
            resultadoConsultaRepresentante = sentenciaRepresentante.executeQuery();

            if (resultadoConsultaRepresentante.next()) {
                representante = new RepresentanteDTO(
                        resultadoConsultaRepresentante.getInt("idRepresentante"),
                        resultadoConsultaRepresentante.getString("correo"),
                        resultadoConsultaRepresentante.getString("telefono"),
                        resultadoConsultaRepresentante.getString("nombre"),
                        resultadoConsultaRepresentante.getString("apellidos"),
                        resultadoConsultaRepresentante.getInt("idOV"),
                        resultadoConsultaRepresentante.getInt("estadoActivo")
                );
            }
        } finally {
            if (sentenciaRepresentante != null) {
                sentenciaRepresentante.close();
            }
        }

        return representante;
    }

    public RepresentanteDTO buscarRepresentantePorTelefono(String telefono) throws SQLException, IOException {
        RepresentanteDTO representante = new RepresentanteDTO(-1, "N/A", "N/A", "N/A", "N/A", 0, 0);
        String buscarSQLRepresentante = "SELECT * FROM representante WHERE telefono = ?";

        try {
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaRepresentante = conexionBaseDeDatos.prepareStatement(buscarSQLRepresentante);
            sentenciaRepresentante.setString(1, telefono);
            resultadoConsultaRepresentante = sentenciaRepresentante.executeQuery();

            if (resultadoConsultaRepresentante.next()) {
                representante = new RepresentanteDTO(
                        resultadoConsultaRepresentante.getInt("idRepresentante"),
                        resultadoConsultaRepresentante.getString("correo"),
                        resultadoConsultaRepresentante.getString("telefono"),
                        resultadoConsultaRepresentante.getString("nombre"),
                        resultadoConsultaRepresentante.getString("apellidos"),
                        resultadoConsultaRepresentante.getInt("idOV"),
                        resultadoConsultaRepresentante.getInt("estadoActivo")
                );
            }
        } finally {
            if (sentenciaRepresentante != null) {
                sentenciaRepresentante.close();
            }
        }

        return representante;
    }
}

