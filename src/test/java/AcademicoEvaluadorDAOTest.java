import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.AcademicoEvaluadorDAO;
import logica.DTOs.AcademicoEvaluadorDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AcademicoEvaluadorDAOTest {

    private AcademicoEvaluadorDAO dao;

    @BeforeEach
    void setUp() {

        dao = new AcademicoEvaluadorDAO();
    }

    @AfterAll
    public static void borrarDatos() throws SQLException, IOException {

        List<Integer> academicosEvaluadoresCreados = List.of(33333, 33333);


        for (int numeroDePersonal : academicosEvaluadoresCreados) {
            String deleteSQL = "DELETE FROM academicoEvaluador WHERE numeroDePersonal = ?";
            try (var statement = new ConexionBaseDeDatos().getConnection().prepareStatement(deleteSQL)) {
                statement.setInt(1, numeroDePersonal);
                statement.executeUpdate();
            }
        }

    }

    @Test
    void testInsertarAcademicoEvaluadorDatosValidos() throws SQLException, IOException {

        AcademicoEvaluadorDTO academicoEvaluador = new AcademicoEvaluadorDTO(33333, 1, "Prueba", "Academico Evaludor", 1);
        boolean resultado = dao.insertarAcademicoEvaluador(academicoEvaluador);
        assertTrue(resultado, "El académico evaluador debería haberse insertado correctamente.");
    }

    @Test
    void testEliminarAcademicoEvaluadorPorNumeroDePersonalDatosValidos() throws SQLException, IOException {

        int numeroDePersonal = 33333;
        boolean resultado = dao.eliminarAcademicoEvaluadorPorNumeroDePersonal(numeroDePersonal);
        assertTrue(resultado, "El académico evaluador debería haberse eliminado correctamente.");
    }

    @Test
    void testModificarAcademicoEvaluadorDatosValidos() throws SQLException, IOException {

        AcademicoEvaluadorDTO academicoEvaluador = new AcademicoEvaluadorDTO(33333, 2, "Prueba Modificacion", "Academico Evaluador", 1);
        boolean resultado = dao.modificarAcademicoEvaluador(academicoEvaluador);
        assertTrue(resultado, "El académico evaluador debería haberse modificado correctamente.");
    }

    @Test
    void testBuscarAcademicoEvaluadorPorNumeroDePersonalDatosValidos() throws SQLException, IOException {

        int numeroDePersonal = 33333;
        AcademicoEvaluadorDTO academicoEvaluador = dao.buscarAcademicoEvaluadorPorNumeroDePersonal(numeroDePersonal);
        assertEquals(numeroDePersonal, academicoEvaluador.getNumeroDePersonal(), "El número de personal debería coincidir.");
    }
}