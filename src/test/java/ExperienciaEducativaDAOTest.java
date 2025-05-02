import logica.DAOs.ExperienciaEducativaDAO;
import logica.DTOs.ExperienciaEducativaDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExperienciaEducativaDAOTest {

    private static ExperienciaEducativaDAO experienciaEducativaDAO;

    @BeforeAll
    static void setUp() throws SQLException, IOException {
        experienciaEducativaDAO = new ExperienciaEducativaDAO();
    }

    @Test
    @Order(1)
    void testCrearNuevaExperienciaEducativaDatosValidos() throws SQLException, IOException {
        ExperienciaEducativaDTO experiencia = new ExperienciaEducativaDTO(1, "Matemáticas");
        boolean resultado = experienciaEducativaDAO.crearNuevaExperienciaEducativa(experiencia);
        assertTrue(resultado, "La experiencia educativa debería haberse creado correctamente.");
    }

    @Test
    @Order(2)
    void testModificarExperienciaEducativaDatosValidos() throws SQLException, IOException {
        ExperienciaEducativaDTO experiencia = new ExperienciaEducativaDTO(1, "Matemáticas Avanzadas");
        boolean resultado = experienciaEducativaDAO.modificarExperienciaEducativa(experiencia);
        assertTrue(resultado, "La experiencia educativa debería haberse modificado correctamente.");
    }

    @Test
    @Order(3)
    void testMostrarExperienciaEducativaDatosValidos() throws SQLException, IOException {
        ExperienciaEducativaDTO experiencia = experienciaEducativaDAO.mostrarExperienciaEducativa();
        assertNotNull(experiencia, "La experiencia educativa no debería ser nula.");
        assertEquals(1, experiencia.getIdEE(), "El ID de la experiencia educativa debería coincidir.");
        assertEquals("Matemáticas Avanzadas", experiencia.getNombre(), "El nombre de la experiencia educativa debería coincidir.");
    }
}
