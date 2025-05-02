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
    void testCrearNuevoCriterioAutoevaluacionDatosValidos() throws SQLException, IOException {

        CriterioAutoevaluacionDTO criterio = new CriterioAutoevaluacionDTO(0, "Descripción del criterio", 4,1);
        boolean resultado = criterioDAO.crearNuevoCriterioAutoevaluacion(criterio);
        assertTrue(resultado, "La creación del criterio debería ser exitosa.");
    }

    @Test
    @Order(2)
    void testBuscarCriterioAutoevaluacionPorIDDatosValidos() throws SQLException, IOException {

        CriterioAutoevaluacionDTO criterio = criterioDAO.buscarCriterioAutoevaluacionPorID(4);
        assertEquals(0, criterio.getEstadoActivo(), "El estado del criterio debería ser 0.");
    }

    @Test
    @Order(3)
    void testModificarCriterioAutoevaluacionDatosValidos() throws SQLException, IOException {

        CriterioAutoevaluacionDTO criterio = new CriterioAutoevaluacionDTO(4, "Descripción", 4,1);
        boolean resultado = criterioDAO.modificarCriterioAutoevaluacion(criterio);
        assertTrue(resultado, "La modificación del criterio debería ser exitosa.");
    }

    @Test
    @Order(4)
    void testEliminarCriterioAutoevaluacionPorNumeroDeCriterioDatosValidos() throws SQLException, IOException {

        boolean resultado = criterioDAO.eliminarCriterioAutoevaluacionPorNumeroDeCriterio(3);
        assertTrue(resultado, "La eliminación lógica del criterio debería ser exitosa.");

    }
}
