package logica.DAOs;

import accesoadatos.ConexionBD;
import logica.DTOs.OvDTO;
import logica.interfaces.IOvDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OvDAO implements IOvDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaOV = null;
    ResultSet resultadoConsultaOV;



    public boolean crearNuevaOv(OvDTO ov) throws SQLException, IOException {

        boolean ovCreada = false;
        String insertarSQLOV = "INSERT INTO ov VALUES(?, ?, ?, ?, ?, ?)";

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaOV = conexionBaseDeDatos.prepareStatement(insertarSQLOV);
            sentenciaOV.setInt(1, ov.getIdOV());
            sentenciaOV.setString(2, ov.getNombre());
            sentenciaOV.setString(3, ov.getCorreo());
            sentenciaOV.setString(4, ov.getNumeroDeContacto());
            sentenciaOV.setString(5, ov.getDireccion());
            sentenciaOV.setInt(6, ov.getEstadoActivo());
            sentenciaOV.executeUpdate();

            ovCreada = true;

        } finally {

            if (sentenciaOV != null) {

                sentenciaOV.close();
            }
        }

        return ovCreada;
    }

    public boolean eliminarOvPorID(int idOv) throws SQLException, IOException {

        boolean ovEliminada = false;
        String eliminarSQLOV = "UPDATE ov SET estadoActivo = 0 WHERE idOV = ?";

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaOV = conexionBaseDeDatos.prepareStatement(eliminarSQLOV);
            sentenciaOV.setInt(1, idOv);
            sentenciaOV.executeUpdate();

            ovEliminada = true;

        } finally {

            if (sentenciaOV != null) {

                sentenciaOV.close();
            }
        }

        return ovEliminada;
    }

    public boolean modificarOv(OvDTO ov) throws SQLException, IOException {

        boolean ovModificada = false;
        String modificarSQLOV = "UPDATE ov SET nombre = ?, correo = ?, numeroContacto = ?, direccion = ? WHERE idOV = ?";

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaOV = conexionBaseDeDatos.prepareStatement(modificarSQLOV);
            sentenciaOV.setString(1, ov.getNombre());
            sentenciaOV.setString(2, ov.getCorreo());
            sentenciaOV.setString(3, ov.getNumeroDeContacto());
            sentenciaOV.setString(4, ov.getDireccion());
            sentenciaOV.setInt(5, ov.getIdOV());
            sentenciaOV.executeUpdate();

            ovModificada = true;

        } finally {

            if (sentenciaOV != null) {

                sentenciaOV.close();
            }
        }

        return ovModificada;
    }

    public OvDTO buscarOvPorID(int idOv) throws SQLException, IOException {

        OvDTO ov = new OvDTO(-1, "N/A", "N/A", "N/A", "N/A", 0);
        String buscarSQLOV = "SELECT * FROM ov WHERE idOV = ?";

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaOV = conexionBaseDeDatos.prepareStatement(buscarSQLOV);
            sentenciaOV.setInt(1, idOv);
            resultadoConsultaOV = sentenciaOV.executeQuery();

            if (resultadoConsultaOV.next()) {
                ov.setIdOV(resultadoConsultaOV.getInt("idOV"));
                ov.setNombre(resultadoConsultaOV.getString("nombre"));
                ov.setCorreo(resultadoConsultaOV.getString("correo"));
                ov.setNumeroDeContacto(resultadoConsultaOV.getString("numeroContacto"));
                ov.setDireccion(resultadoConsultaOV.getString("direccion"));
                ov.setEstadoActivo(resultadoConsultaOV.getInt("estadoActivo"));
            }

        } finally {

            if (sentenciaOV != null) {

                sentenciaOV.close();
            }
        }

        return ov;
    }
}

