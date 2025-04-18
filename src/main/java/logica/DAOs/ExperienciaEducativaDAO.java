package logica.DAOs;

import accesoadatos.ConexionBD;
import logica.DTOs.ExperienciaEducativaDTO;
import logica.interfaces.IExperienciaEducativaDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExperienciaEducativaDAO implements IExperienciaEducativaDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaExperienciaEducativa = null;
    ResultSet resultadoExperienciaEducativa;



    public boolean crearNuevaExperienciaEducativa(ExperienciaEducativaDTO experienciaEducativa) throws SQLException, IOException {
        boolean experienciaEducativaInsertada = false;

        String insertarSQLEducativa = "INSERT INTO experienciaeducativa VALUES(?, ?)";

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaExperienciaEducativa = conexionBaseDeDatos.prepareStatement(insertarSQLEducativa);
            sentenciaExperienciaEducativa.setInt(1, experienciaEducativa.getIdEE());
            sentenciaExperienciaEducativa.setString(2, experienciaEducativa.getNombre());
            sentenciaExperienciaEducativa.executeUpdate();
            experienciaEducativaInsertada = true;

        } finally {

            if (sentenciaExperienciaEducativa != null) {

                sentenciaExperienciaEducativa.close();
            }
        }

        return experienciaEducativaInsertada;
    }

    public boolean modificarExperienciaEducativa(ExperienciaEducativaDTO experienciaEducativa) throws SQLException, IOException {
        boolean experienciaEducativaModificada = false;

        String modificarSQLEducativa = "UPDATE experienciaeducativa SET nombre = ? WHERE idEE = ?";

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaExperienciaEducativa = conexionBaseDeDatos.prepareStatement(modificarSQLEducativa);
            sentenciaExperienciaEducativa.setString(1, experienciaEducativa.getNombre());
            sentenciaExperienciaEducativa.setInt(2, experienciaEducativa.getIdEE());
            sentenciaExperienciaEducativa.executeUpdate();
            experienciaEducativaModificada = true;

        } finally {

            if (sentenciaExperienciaEducativa != null) {

                sentenciaExperienciaEducativa.close();
            }
        }

        return experienciaEducativaModificada;
    }

    public ExperienciaEducativaDTO mostrarExperienciaEducativa() throws SQLException, IOException {

        ExperienciaEducativaDTO experienciaEducativa = new ExperienciaEducativaDTO(-1,"N/A");

        String consultaSQL = "SELECT * FROM experienciaeducativa";

        try {

            conexionBaseDeDatos = new ConexionBD().getConnection();
            sentenciaExperienciaEducativa = conexionBaseDeDatos.prepareStatement(consultaSQL);
            resultadoExperienciaEducativa = sentenciaExperienciaEducativa.executeQuery();

            if (resultadoExperienciaEducativa.next()) {

                experienciaEducativa.setIdEE(resultadoExperienciaEducativa.getInt("idEE"));
                experienciaEducativa.setNombre(resultadoExperienciaEducativa.getString("nombre"));
            }

        } finally {

            if (sentenciaExperienciaEducativa != null) {

                sentenciaExperienciaEducativa.close();
            }
        }

        return experienciaEducativa;
    }
}

