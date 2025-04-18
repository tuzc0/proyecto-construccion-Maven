import logica.DAOs.AutoevaluacionContieneDAO;
import logica.DTOs.AutoEvaluacionContieneDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AutoevaluacionContieneDAOTest {

    private static AutoevaluacionContieneDAO autoevaluacionContieneDAO;

    @BeforeAll
    static void setUp() {
        autoevaluacionContieneDAO = new AutoevaluacionContieneDAO();
    }

    @Test
    @Order(1)
    void testInsertarAutoevaluacionContiene() throws SQLException, IOException {
        // Usar un idCriterio que no exista para idAutoevaluacion=4
        AutoEvaluacionContieneDTO autoevaluacion = new AutoEvaluacionContieneDTO(4, 4.5f, 5);
        boolean resultado = autoevaluacionContieneDAO.insertarAutoevaluacionContiene(autoevaluacion);
        assertTrue(resultado, "La inserción debería ser exitosa.");
    }

    @Test
    @Order(2)
    void testBuscarAutoevaluacionContienePorID() throws SQLException, IOException {
        AutoEvaluacionContieneDTO autoevaluacion = autoevaluacionContieneDAO.buscarAutoevaluacionContienePorID(4,4);
        assertNotNull(autoevaluacion, "La autoevaluación no debería ser nula.");
        assertEquals(4, autoevaluacion.getIdAutoevaluacion(), "El ID de la autoevaluación debería ser 4.");
        assertEquals(4, autoevaluacion.getIdCriterio(), "El ID del criterio debería ser 4.");
        assertEquals(5.0f, autoevaluacion.getCalificacion(), "La calificación debería ser 4.5.");
    }

    @Test
    @Order(3)
    void testModificarCalificacion() throws SQLException, IOException {
        AutoEvaluacionContieneDTO autoevaluacion = new AutoEvaluacionContieneDTO(4, 5.0f, 4);
        boolean resultado = autoevaluacionContieneDAO.modificarCalificacion(autoevaluacion);
        assertTrue(resultado, "La modificación de la calificación debería ser exitosa.");

        AutoEvaluacionContieneDTO autoevaluacionModificada = autoevaluacionContieneDAO.buscarAutoevaluacionContienePorID(4,4);
        assertEquals(5.0f, autoevaluacionModificada.getCalificacion(), "La calificación debería ser 5.0.");
    }
}
