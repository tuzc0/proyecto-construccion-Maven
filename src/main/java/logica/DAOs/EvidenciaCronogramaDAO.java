package logica.DAOs;

import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.EvidenciaCronogramaDTO;
import logica.interfaces.IEvidenciaCronogramaDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EvidenciaCronogramaDAO implements IEvidenciaCronogramaDAO {

    Connection conexionBaseDeDatos;
    PreparedStatement sentenciaEvidenciaCronograma = null;
    ResultSet resultadoEvidenciaCronograma;

    public boolean insertarEvidenciaCronograma(EvidenciaCronogramaDTO evidenciaCronograma) throws SQLException, IOException {
        String insertarSQLEvidencia = "INSERT INTO evidenciacronograma (idEvidencia,URL,idCronograma ) VALUES (?, ?, ?)";
        boolean evidenciaInsertada = false;

        try {
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEvidenciaCronograma = conexionBaseDeDatos.prepareStatement(insertarSQLEvidencia);
            sentenciaEvidenciaCronograma.setInt(1, evidenciaCronograma.getIdEvidencia());
            sentenciaEvidenciaCronograma.setString(2, evidenciaCronograma.getURL());
            sentenciaEvidenciaCronograma.setInt(3, evidenciaCronograma.getIdCronograma());
            sentenciaEvidenciaCronograma.executeUpdate();
            evidenciaInsertada = true;

        } finally {
            if (sentenciaEvidenciaCronograma != null) {
                sentenciaEvidenciaCronograma.close();
            }
        }

        return evidenciaInsertada;
    }

    public EvidenciaCronogramaDTO mostrarEvidenciaCronogramaPorID(int idEvidencia) throws SQLException, IOException {
        String consultaSQLEvidencia = "SELECT * FROM evidenciacronograma WHERE idEvidencia = ?";
        EvidenciaCronogramaDTO evidenciaEncontrada = new EvidenciaCronogramaDTO(-1, " ", -1);

        try {
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            sentenciaEvidenciaCronograma = conexionBaseDeDatos.prepareStatement(consultaSQLEvidencia);
            sentenciaEvidenciaCronograma.setInt(1, idEvidencia);
            resultadoEvidenciaCronograma = sentenciaEvidenciaCronograma.executeQuery();

            if (resultadoEvidenciaCronograma.next()) {
                evidenciaEncontrada = new EvidenciaCronogramaDTO();
                evidenciaEncontrada.setIdEvidencia(resultadoEvidenciaCronograma.getInt("idEvidencia"));
                evidenciaEncontrada.setURL(resultadoEvidenciaCronograma.getString("url"));
                evidenciaEncontrada.setIdCronograma(resultadoEvidenciaCronograma.getInt("idCronograma"));
            }

        } finally {
            if (sentenciaEvidenciaCronograma != null) {
                sentenciaEvidenciaCronograma.close();
            }
        }

        return evidenciaEncontrada;
    }
}
