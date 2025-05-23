import logica.DAOs.EvaluacionDAO;
import logica.DTOs.EvaluacionDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EvaluacionDAOTest {

    private static EvaluacionDAO evaluacionDAO;

    @BeforeAll
    static void setUp() throws SQLException, IOException {

        evaluacionDAO = new EvaluacionDAO();
    }



    @Test
    @Order(2)
    void testBuscarEvaluacionPorIDDatosValidos() throws SQLException, IOException {

        EvaluacionDTO evaluacion = evaluacionDAO.buscarEvaluacionPorID(1);
        assertEquals(1, evaluacion.getIDEvaluacion(), "El ID de la evaluación debería coincidir.");
    }

    @Test
    @Order(3)
    void testModificarEvaluacionDatosValidos() throws SQLException, IOException {

        EvaluacionDTO evaluacion = new EvaluacionDTO(1, "Trabajo excelente", 95, 33333, "A12345", 1);
        boolean resultado = evaluacionDAO.modificarEvaluacion(evaluacion);
        assertTrue(resultado, "La evaluación debería haberse modificado correctamente.");
    }

    @Test
    @Order(4)
    void testEliminarEvaluacionPorIDDatosValidos() throws SQLException, IOException {

        boolean resultado = evaluacionDAO.eliminarEvaluacionPorID(0, 1);
        assertTrue(resultado, "La evaluación debería haberse eliminado correctamente.");
    }
}