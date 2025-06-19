package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.OrganizacionVinculadaDTO;
import logica.interfaces.IOvDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrganizacionVinculadaDAO implements IOvDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaOrganizacionVinculada = null;
    ResultSet resultadoConsultaOrganizacionVinculada;

    public int crearNuevaOrganizacion(OrganizacionVinculadaDTO organizacionVinculada) throws SQLException, IOException {

        int idOrganizacionGenerado = -1;
        String insertarSQLOV = "INSERT INTO organizacionvinculada(nombre, correo, numeroContacto, direccion, estadoActivo) VALUES(?, ?, ?, ?, ?)";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaOrganizacionVinculada = conexionBaseDeDatos.prepareStatement(insertarSQLOV, PreparedStatement.RETURN_GENERATED_KEYS);

            sentenciaOrganizacionVinculada.setString(1, organizacionVinculada.getNombre());
            sentenciaOrganizacionVinculada.setString(2, organizacionVinculada.getCorreo());
            sentenciaOrganizacionVinculada.setString(3, organizacionVinculada.getNumeroDeContacto());
            sentenciaOrganizacionVinculada.setString(4, organizacionVinculada.getDireccion());
            sentenciaOrganizacionVinculada.setInt(5, organizacionVinculada.getEstadoActivo());
            sentenciaOrganizacionVinculada.executeUpdate();

            ResultSet generatedKeys = sentenciaOrganizacionVinculada.getGeneratedKeys();

            if (generatedKeys.next()) {
                idOrganizacionGenerado = generatedKeys.getInt(1);
            }

        } finally {

            if (sentenciaOrganizacionVinculada != null) {
                sentenciaOrganizacionVinculada.close();
            }
        }

        return idOrganizacionGenerado;
    }

    public boolean eliminarOrganizacionPorID(int idOrganizacionVinculada) throws SQLException, IOException {

        boolean organizacionVinculadaEliminada = false;
        String eliminarSQLOrganizacion = "UPDATE organizacionvinculada SET estadoActivo = 0 WHERE idOV = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaOrganizacionVinculada = conexionBaseDeDatos.prepareStatement(eliminarSQLOrganizacion);
            sentenciaOrganizacionVinculada.setInt(1, idOrganizacionVinculada);

            if (sentenciaOrganizacionVinculada.executeUpdate() > 0) {
                organizacionVinculadaEliminada = true;
            }

        } finally {

            if (sentenciaOrganizacionVinculada != null) {

                sentenciaOrganizacionVinculada.close();
            }
        }

        return organizacionVinculadaEliminada;
    }

    public boolean modificarOrganizacion(OrganizacionVinculadaDTO organizacionVinculada) throws SQLException, IOException {

        boolean organizacionModificada = false;
        String modificarSQLOV = "UPDATE organizacionvinculada SET nombre = ?, correo = ?, numeroContacto = ?, direccion = ? WHERE idOV = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaOrganizacionVinculada = conexionBaseDeDatos.prepareStatement(modificarSQLOV);
            sentenciaOrganizacionVinculada.setString(1, organizacionVinculada.getNombre());
            sentenciaOrganizacionVinculada.setString(2, organizacionVinculada.getCorreo());
            sentenciaOrganizacionVinculada.setString(3, organizacionVinculada.getNumeroDeContacto());
            sentenciaOrganizacionVinculada.setString(4, organizacionVinculada.getDireccion());
            sentenciaOrganizacionVinculada.setInt(5, organizacionVinculada.getIdOrganizacion());

            if (sentenciaOrganizacionVinculada.executeUpdate() > 0) {
                organizacionModificada = true;
            }

        } finally {

            if (sentenciaOrganizacionVinculada != null) {

                sentenciaOrganizacionVinculada.close();
            }
        }

        return organizacionModificada;
    }

    public OrganizacionVinculadaDTO buscarOrganizacionPorID(int idOrganizacion) throws SQLException, IOException {

        OrganizacionVinculadaDTO organizacionVinculada = new OrganizacionVinculadaDTO(-1, "N/A", "N/A", "N/A", "N/A", 0);
        String buscarSQLOrganizacion = "SELECT * FROM organizacionvinculada WHERE idOV = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaOrganizacionVinculada = conexionBaseDeDatos.prepareStatement(buscarSQLOrganizacion);
            sentenciaOrganizacionVinculada.setInt(1, idOrganizacion);
            resultadoConsultaOrganizacionVinculada = sentenciaOrganizacionVinculada.executeQuery();

            if (resultadoConsultaOrganizacionVinculada.next()) {

                organizacionVinculada.setIdOrganizacion(resultadoConsultaOrganizacionVinculada.getInt("idOV"));
                organizacionVinculada.setNombre(resultadoConsultaOrganizacionVinculada.getString("nombre"));
                organizacionVinculada.setDireccion(resultadoConsultaOrganizacionVinculada.getString("direccion"));
                organizacionVinculada.setCorreo(resultadoConsultaOrganizacionVinculada.getString("correo"));
                organizacionVinculada.setNumeroDeContacto(resultadoConsultaOrganizacionVinculada.getString("numeroContacto"));
                organizacionVinculada.setEstadoActivo(resultadoConsultaOrganizacionVinculada.getInt("estadoActivo"));
            }

        } finally {

            if (sentenciaOrganizacionVinculada != null) {

                sentenciaOrganizacionVinculada.close();
            }
        }

        return organizacionVinculada;
    }

    public OrganizacionVinculadaDTO buscarOrganizacionPorCorreo(String correo) throws SQLException, IOException {

        OrganizacionVinculadaDTO organizacionVinculada = new OrganizacionVinculadaDTO(-1, "N/A", "N/A", "N/A", "N/A", 0);
        String buscarSQLOrganizacion = "SELECT * FROM organizacionvinculada WHERE correo = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaOrganizacionVinculada = conexionBaseDeDatos.prepareStatement(buscarSQLOrganizacion);
            sentenciaOrganizacionVinculada.setString(1, correo);
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

    public OrganizacionVinculadaDTO buscarOrganizacionPorTelefono(String numeroContacto) throws SQLException, IOException {

        OrganizacionVinculadaDTO organizacionVinculada = new OrganizacionVinculadaDTO(-1, "N/A", "N/A", "N/A", "N/A", 0);
        String buscarSQLOrganizacion = "SELECT * FROM organizacionvinculada WHERE numeroContacto = ?";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaOrganizacionVinculada = conexionBaseDeDatos.prepareStatement(buscarSQLOrganizacion);
            sentenciaOrganizacionVinculada.setString(1, numeroContacto);
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

    public List<OrganizacionVinculadaDTO> obtenerTodasLasOrganizaciones() throws SQLException, IOException {

        List<OrganizacionVinculadaDTO> listaOrganizaciones = new ArrayList<>();
        String consultaSQL = "SELECT * FROM organizacionvinculada WHERE estadoActivo = 1";

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaOrganizacionVinculada = conexionBaseDeDatos.prepareStatement(consultaSQL);
            resultadoConsultaOrganizacionVinculada = sentenciaOrganizacionVinculada.executeQuery();

            while (resultadoConsultaOrganizacionVinculada.next()) {

                OrganizacionVinculadaDTO organizacion = new OrganizacionVinculadaDTO(
                        resultadoConsultaOrganizacionVinculada.getInt("idOV"),
                        resultadoConsultaOrganizacionVinculada.getString("nombre"),
                        resultadoConsultaOrganizacionVinculada.getString("direccion"),
                        resultadoConsultaOrganizacionVinculada.getString("correo"),
                        resultadoConsultaOrganizacionVinculada.getString("numeroContacto"),
                        resultadoConsultaOrganizacionVinculada.getInt("estadoActivo")
                );
                listaOrganizaciones.add(organizacion);
            }

        } finally {

            if (sentenciaOrganizacionVinculada != null) {
                sentenciaOrganizacionVinculada.close();
            }
        }

        return listaOrganizaciones;
    }

    public List<OrganizacionVinculadaDTO> buscarOrganizacionesPorNombre(String nombre) throws SQLException, IOException {
        List<OrganizacionVinculadaDTO> listaOrganizaciones = new ArrayList<>();
        String consultaSQL = "SELECT * FROM organizacionvinculada WHERE estadoActivo = 1 AND LOWER(nombre) LIKE ?";

        try {
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaOrganizacionVinculada = conexionBaseDeDatos.prepareStatement(consultaSQL);


            String terminoBusqueda = "%" + nombre.toLowerCase().replace(" ", "%") + "%";
            sentenciaOrganizacionVinculada.setString(1, terminoBusqueda);

            resultadoConsultaOrganizacionVinculada = sentenciaOrganizacionVinculada.executeQuery();

            while (resultadoConsultaOrganizacionVinculada.next()) {
                OrganizacionVinculadaDTO organizacion = new OrganizacionVinculadaDTO(
                        resultadoConsultaOrganizacionVinculada.getInt("idOV"),
                        resultadoConsultaOrganizacionVinculada.getString("nombre"),
                        resultadoConsultaOrganizacionVinculada.getString("direccion"),
                        resultadoConsultaOrganizacionVinculada.getString("correo"),
                        resultadoConsultaOrganizacionVinculada.getString("numeroContacto"),
                        resultadoConsultaOrganizacionVinculada.getInt("estadoActivo")
                );
                listaOrganizaciones.add(organizacion);
            }
        } finally {
            if (sentenciaOrganizacionVinculada != null) {
                sentenciaOrganizacionVinculada.close();
            }
        }

        return listaOrganizaciones;
    }
}

