import logica.DAOs.OvDAO;
import logica.DTOs.OvDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OvDAOTest {

    private static OvDAO ovDAO;

    @BeforeAll
    static void setUp() throws SQLException, IOException {

        ovDAO = new OvDAO();
    }

    @Test
    @Order(1)
    void testCrearNuevaOv() throws SQLException, IOException {

        OvDTO ov = new OvDTO(11, "Organización A", "Calle 123, Ciudad A", "contacto@org.com", "123456789", 1);
        boolean resultado = ovDAO.crearNuevaOv(ov);
        assertTrue(resultado, "La OV debería haberse creado correctamente.");
    }

    @Test
    @Order(2)
    void testEliminarOvPorID() throws SQLException, IOException {

        boolean resultado = ovDAO.eliminarOvPorID(1);
        assertTrue(resultado, "La OV debería haberse eliminado correctamente.");
    }

    @Test
    @Order(3)
    void testModificarOv() throws SQLException, IOException {

        OvDTO ov = new OvDTO(11, "Organización B", "Nueva organizacion","contacto@org.com", "987654321", 1);
        boolean resultado = ovDAO.modificarOv(ov);
        assertTrue(resultado, "La OV debería haberse modificado correctamente.");
    }

    @Test
    @Order(4)
    void testBuscarOvPorID() throws SQLException, IOException {

        OvDTO ov = ovDAO.buscarOvPorID(1);
        assertNotNull(ov, "La OV no debería ser nula.");
        assertEquals(1, ov.getIdOV(), "El ID de la OV debería coincidir.");
    }
}
