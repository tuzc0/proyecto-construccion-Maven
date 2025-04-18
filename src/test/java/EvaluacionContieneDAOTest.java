import logica.DAOs.EvaluacionContieneDAO;
import logica.DTOs.EvaluacionContieneDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EvaluacionContieneDAOTest {

    private static EvaluacionContieneDAO evaluacionContieneDAO;

    @BeforeAll
    static void setUp() {
        evaluacionContieneDAO = new EvaluacionContieneDAO();
    }

    @Test
    @Order(1)
    void testInsertarEvaluacionContiene() throws SQLException, IOException {
        EvaluacionContieneDTO evaluacion = new EvaluacionContieneDTO(1, 4.5f, 3);
        boolean resultado = evaluacionContieneDAO.insertarEvaluacionContiene(evaluacion);
        assertTrue(resultado, "La inserción de la evaluación debería ser exitosa.");
    }

    @Test
    @Order(2)
    void testBuscarEvaluacionContienePorID() throws SQLException, IOException {
        EvaluacionContieneDTO evaluacion = evaluacionContieneDAO.buscarEvaluacionContienePorID(1, 1);
        assertNotNull(evaluacion, "La evaluación no debería ser nula.");
        assertEquals(1, evaluacion.getIdEvaluacion(), "El ID de la evaluación debería ser 1.");
        assertEquals(1, evaluacion.getIdCriterio(), "El ID del criterio debería ser 101.");
        assertEquals(1, evaluacion.getCalificacion(), "La calificación debería ser 4.5.");
    }

    @Test
    @Order(3)
    void testModificarCalificacion() throws SQLException, IOException {
        boolean resultado = evaluacionContieneDAO.modificarCalificacion(new EvaluacionContieneDTO(1, 1, 1));
        assertTrue(resultado, "La modificación de la calificación debería ser exitosa.");

        EvaluacionContieneDTO evaluacionModificada = evaluacionContieneDAO.buscarEvaluacionContienePorID(1, 1);
        assertEquals(1, evaluacionModificada.getIdCriterio(), "El ID del criterio debería ser 101.");
    }
}