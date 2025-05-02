import logica.DAOs.AcademicoEvaluadorDAO;
import logica.DTOs.AcademicoEvaluadorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class AcademicoEvaluadorDAOTest {

    private AcademicoEvaluadorDAO dao;

    @BeforeEach
    void setUp() {

        dao = new AcademicoEvaluadorDAO();
    }

    @Test
    void testInsertarAcademicoEvaluador() throws SQLException, IOException {

        AcademicoEvaluadorDTO academicoEvaluador = new AcademicoEvaluadorDTO(33333, 50, "Prueba", "Academico Evaludor", 1);
        boolean resultado = dao.insertarAcademicoEvaluador(academicoEvaluador);
        assertTrue(resultado, "El académico evaluador debería haberse insertado correctamente.");
    }

    @Test
    void testEliminarAcademicoEvaluadorPorNumeroDePersonal() throws SQLException, IOException {

        int numeroDePersonal = 33333;
        boolean resultado = dao.eliminarAcademicoEvaluadorPorNumeroDePersonal(numeroDePersonal);
        assertTrue(resultado, "El académico evaluador debería haberse eliminado correctamente.");
    }

    @Test
    void testModificarAcademicoEvaluador() throws SQLException, IOException {

        AcademicoEvaluadorDTO academicoEvaluador = new AcademicoEvaluadorDTO(33333, 49, "Prueba Modificacion", "Academico Evaluador", 1);
        boolean resultado = dao.modificarAcademicoEvaluador(academicoEvaluador);
        assertTrue(resultado, "El académico evaluador debería haberse modificado correctamente.");
    }

    @Test
    void testBuscarAcademicoEvaluadorPorNumeroDePersonal() throws SQLException, IOException {

        int numeroDePersonal = 33333;
        AcademicoEvaluadorDTO academicoEvaluador = dao.buscarAcademicoEvaluadorPorNumeroDePersonal(numeroDePersonal);
        assertEquals(numeroDePersonal, academicoEvaluador.getNumeroDePersonal(), "El número de personal debería coincidir.");
    }
}