import logica.DAOs.CriterioAutoevaluacionDAO;
import logica.DTOs.CriterioAutoevaluacionDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CriterioAutoevaluacionDAOTest {

    private static CriterioAutoevaluacionDAO criterioDAO;

    @BeforeAll
    static void setUp() {
        criterioDAO = new CriterioAutoevaluacionDAO();
    }

    @Test
    @Order(1)
    void testCrearNuevoCriterioAutoevaluacion() throws SQLException, IOException {
        CriterioAutoevaluacionDTO criterio = new CriterioAutoevaluacionDTO(0, "Descripción del criterio", 4);
        boolean resultado = criterioDAO.crearNuevoCriterioAutoevaluacion(criterio);
        assertTrue(resultado, "La creación del criterio debería ser exitosa.");
    }

    @Test
    @Order(2)
    void testBuscarCriterioAutoevaluacionPorID() throws SQLException, IOException {
        CriterioAutoevaluacionDTO criterio = criterioDAO.buscarCriterioAutoevaluacionPorID(4);
        assertNotNull(criterio, "El criterio no debería ser nulo.");
        assertEquals(4, criterio.getIDCriterio(), "El ID del criterio debería ser 4.");
        assertEquals("Descripción modificada", criterio.getDescripcion(), "La descripción debería coincidir.");
        assertEquals(4, criterio.getNumeroCriterio(), "El número del criterio debería ser 4.");
    }

    @Test
    @Order(3)
    void testModificarCriterioAutoevaluacion() throws SQLException, IOException {
        CriterioAutoevaluacionDTO criterio = new CriterioAutoevaluacionDTO(4, "Descripción modificada", 4);
        boolean resultado = criterioDAO.modificarCriterioAutoevaluacion(criterio);
        assertTrue(resultado, "La modificación del criterio debería ser exitosa.");

        CriterioAutoevaluacionDTO criterioModificado = criterioDAO.buscarCriterioAutoevaluacionPorID(4);
        assertEquals("Descripción modificada", criterioModificado.getDescripcion(), "La descripción debería estar actualizada.");
    }

    @Test
    @Order(4)
    void testEliminarCriterioAutoevaluacionPorNumeroDeCriterio() throws SQLException, IOException {
        boolean resultado = criterioDAO.eliminarCriterioAutoevaluacionPorNumeroDeCriterio(4);
        assertTrue(resultado, "La eliminación lógica del criterio debería ser exitosa.");

    }
}
