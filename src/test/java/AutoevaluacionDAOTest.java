import logica.DAOs.AutoevaluacionDAO;
import logica.DTOs.AutoevaluacionDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AutoevaluacionDAOTest {

    private static AutoevaluacionDAO autoevaluacionDAO;

    @BeforeAll
    static void setUp() {

        autoevaluacionDAO = new AutoevaluacionDAO();
    }

    @Test
    @Order(1)
    void testCrearNuevaAutoevaluacionDatosValidos() throws SQLException, IOException {

        AutoevaluacionDTO autoevaluacion = new AutoevaluacionDTO(11, Timestamp.valueOf("2023-10-01 08:00:00"), "Aula 101", 9.0F, "A12345",1);
        boolean resultado = autoevaluacionDAO.crearNuevaAutoevaluacion(autoevaluacion);
        assertTrue(resultado, "La autoevaluación debería haberse insertado correctamente.");
    }

    @Test
    @Order(2)
    void testEliminarAutoevaluacionPorIDDatosValidos() throws SQLException, IOException {

        boolean resultado = autoevaluacionDAO.eliminarAutoevaluacionPorID(11);
        assertTrue(resultado, "La autoevaluación debería haberse eliminado correctamente.");
    }

    @Test
    @Order(3)
    void testModificarAutoevaluacionDatosValidos() throws SQLException, IOException {

        AutoevaluacionDTO autoevaluacion = new AutoevaluacionDTO(11, Timestamp.valueOf("2025-10-01 08:00:00"), "Aula 103", 9.5F, "A12345",1);
        boolean resultado = autoevaluacionDAO.modificarAutoevaluacion(autoevaluacion);
        assertTrue(resultado, "La autoevaluación debería haberse modificado correctamente.");
    }

    @Test
    @Order(4)
    void testBuscarAutoevaluacionPorIDDatosValidos() throws SQLException, IOException {

        AutoevaluacionDTO autoevaluacion = autoevaluacionDAO.buscarAutoevaluacionPorID(11);
        assertEquals(0, autoevaluacion.getEstadoActivo(), "El estado de la autoevaluación debería ser 0.");
    }
}