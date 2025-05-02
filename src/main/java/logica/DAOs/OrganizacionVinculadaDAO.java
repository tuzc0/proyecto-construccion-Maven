package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.OrganizacionVinculadaDTO;
import logica.interfaces.IOvDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrganizacionVinculadaDAO implements IOvDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaOrganizacionVinculada = null;
    ResultSet resultadoConsultaOrganizacionVinculada;



    public boolean crearNuevaOrganizacion(OrganizacionVinculadaDTO organizacionVinculada) throws SQLException, IOException {

        boolean organizacionVinculadaCreada = false;
        String insertarSQLOV = "INSERT INTO ov VALUES(?, ?, ?, ?, ?, ?)";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaOrganizacionVinculada = conexionBaseDeDatos.prepareStatement(insertarSQLOV);
            sentenciaOrganizacionVinculada.setInt(1, organizacionVinculada.getIdOrganizacion());
            sentenciaOrganizacionVinculada.setString(2, organizacionVinculada.getNombre());
            sentenciaOrganizacionVinculada.setString(3, organizacionVinculada.getCorreo());
            sentenciaOrganizacionVinculada.setString(4, organizacionVinculada.getNumeroDeContacto());
            sentenciaOrganizacionVinculada.setString(5, organizacionVinculada.getDireccion());
            sentenciaOrganizacionVinculada.setInt(6, organizacionVinculada.getEstadoActivo());
            sentenciaOrganizacionVinculada.executeUpdate();

            organizacionVinculadaCreada = true;

        } finally {

            if (sentenciaOrganizacionVinculada != null) {

                sentenciaOrganizacionVinculada.close();
            }
        }

        return organizacionVinculadaCreada;
    }

    public boolean eliminarOrganizacionPorID(int idOrganizacionVinculada) throws SQLException, IOException {

        boolean organizacionVinculadaEliminada = false;
        String eliminarSQLOrganizacion = "UPDATE ov SET estadoActivo = 0 WHERE idOV = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaOrganizacionVinculada = conexionBaseDeDatos.prepareStatement(eliminarSQLOrganizacion);
            sentenciaOrganizacionVinculada.setInt(1, idOrganizacionVinculada);
            sentenciaOrganizacionVinculada.executeUpdate();

            organizacionVinculadaEliminada = true;

        } finally {

            if (sentenciaOrganizacionVinculada != null) {

                sentenciaOrganizacionVinculada.close();
            }
        }

        return organizacionVinculadaEliminada;
    }

    public boolean modificarOrganizacion(OrganizacionVinculadaDTO organizacionVinculada) throws SQLException, IOException {

        boolean organizacionModificada = false;
        String modificarSQLOV = "UPDATE ov SET nombre = ?, correo = ?, numeroContacto = ?, direccion = ? WHERE idOV = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaOrganizacionVinculada = conexionBaseDeDatos.prepareStatement(modificarSQLOV);
            sentenciaOrganizacionVinculada.setString(1, organizacionVinculada.getNombre());
            sentenciaOrganizacionVinculada.setString(2, organizacionVinculada.getCorreo());
            sentenciaOrganizacionVinculada.setString(3, organizacionVinculada.getNumeroDeContacto());
            sentenciaOrganizacionVinculada.setString(4, organizacionVinculada.getDireccion());
            sentenciaOrganizacionVinculada.setInt(5, organizacionVinculada.getIdOrganizacion());
            sentenciaOrganizacionVinculada.executeUpdate();

            organizacionModificada = true;

        } finally {

            if (sentenciaOrganizacionVinculada != null) {

                sentenciaOrganizacionVinculada.close();
            }
        }

        return organizacionModificada;
    }

    public OrganizacionVinculadaDTO buscarOrganizacionPorID(int idOrganizacion) throws SQLException, IOException {

        OrganizacionVinculadaDTO organizacionVinculada = new OrganizacionVinculadaDTO(-1, "N/A", "N/A", "N/A", "N/A", 0);
        String buscarSQLOrganizacion = "SELECT * FROM ov WHERE idOV = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaOrganizacionVinculada = conexionBaseDeDatos.prepareStatement(buscarSQLOrganizacion);
            sentenciaOrganizacionVinculada.setInt(1, idOrganizacion);
            resultadoConsultaOrganizacionVinculada = sentenciaOrganizacionVinculada.executeQuery();

            if (resultadoConsultaOrganizacionVinculada.next()) {
                organizacionVinculada.setIdOrganizacion(resultadoConsultaOrganizacionVinculada.getInt("idOV"));
                organizacionVinculada.setNombre(resultadoConsultaOrganizacionVinculada.getString("nombre"));
                organizacionVinculada.setCorreo(resultadoConsultaOrganizacionVinculada.getString("correo"));
                organizacionVinculada.setNumeroDeContacto(resultadoConsultaOrganizacionVinculada.getString("numeroContacto"));
                organizacionVinculada.setDireccion(resultadoConsultaOrganizacionVinculada.getString("direccion"));
                organizacionVinculada.setEstadoActivo(resultadoConsultaOrganizacionVinculada.getInt("estadoActivo"));
            }

        } finally {

            if (sentenciaOrganizacionVinculada != null) {

                sentenciaOrganizacionVinculada.close();
            }
        }

        return organizacionVinculada;
    }
}

