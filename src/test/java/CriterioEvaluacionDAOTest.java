import logica.DAOs.CriterioEvaluacionDAO;
import logica.DTOs.CriterioEvaluacionDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CriterioEvaluacionDAOTest {

    private static CriterioEvaluacionDAO criterioDAO;

    @BeforeAll
    static void setUp() {

        criterioDAO = new CriterioEvaluacionDAO();
    }

    @Test
    @Order(1)
    void testCrearNuevoCriterioEvaluacionDatosValidos() throws SQLException, IOException {

        CriterioEvaluacionDTO criterio = new CriterioEvaluacionDTO(6, "Criterio de evaluación", 5, 1);
        boolean resultado = criterioDAO.crearNuevoCriterioEvaluacion(criterio);
        assertTrue(resultado, "El criterio de evaluación debería haberse creado correctamente.");
    }

    @Test
    @Order(2)
    void testBuscarCriterioEvaluacionPorIDDatosValidos() throws SQLException, IOException {

        CriterioEvaluacionDTO criterio = criterioDAO.buscarCriterioEvaluacionPorID(5);
        assertEquals(5, criterio.getNumeroCriterio(), "El número de criterio debería coincidir.");
    }

    @Test
    @Order(3)
    void testModificarCriterioEvaluacionDatosValidos() throws SQLException, IOException {

        CriterioEvaluacionDTO criterio = new CriterioEvaluacionDTO(5, "Criterio modificado", 5, 1);
        boolean resultado = criterioDAO.modificarCriterioEvaluacion(criterio);
        assertTrue(resultado, "El criterio de evaluación debería haberse modificado correctamente.");
    }

    @Test
    @Order(4)
    void testEliminarCriterioEvaluacionPorIDDatosValidos() throws SQLException, IOException {

        boolean resultado = criterioDAO.eliminarCriterioEvaluacionPorID(5);
        assertTrue(resultado, "El criterio de evaluación debería haberse eliminado correctamente.");
    }
}
