import logica.DAOs.OrganizacionVinculadaDAO;
import logica.DTOs.OrganizacionVinculadaDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrganizacionVinculadaDAOTest {

    private static OrganizacionVinculadaDAO organizacionVinculadaDAO;

    @BeforeAll
    static void setUp() throws SQLException, IOException {

        organizacionVinculadaDAO = new OrganizacionVinculadaDAO();
    }

    @Test
    @Order(1)
    void testCrearNuevaOrganizacionDatosValidos() throws SQLException, IOException {

        OrganizacionVinculadaDTO ov = new OrganizacionVinculadaDTO(11, "Organización A", "Calle 123, Ciudad A", "contacto@org.com", "123456789", 1);
        boolean resultado = organizacionVinculadaDAO.crearNuevaOrganizacion(ov);
        assertTrue(resultado, "La OV debería haberse creado correctamente.");
    }

    @Test
    @Order(2)
    void testEliminarOrganizacionPorIDDatosValidos() throws SQLException, IOException {

        boolean resultado = organizacionVinculadaDAO.eliminarOrganizacionPorID(1);
        assertTrue(resultado, "La OV debería haberse eliminado correctamente.");
    }

    @Test
    @Order(3)
    void testModificarOrganizacionDatosValidos() throws SQLException, IOException {

        OrganizacionVinculadaDTO ov = new OrganizacionVinculadaDTO(11, "Organización B", "Nueva organizacion","contacto@org.com", "987654321", 1);
        boolean resultado = organizacionVinculadaDAO.modificarOrganizacion(ov);
        assertTrue(resultado, "La OV debería haberse modificado correctamente.");
    }

    @Test
    @Order(4)
    void testBuscarOrganizacionPorIDDatosValidos() throws SQLException, IOException {

        OrganizacionVinculadaDTO ov = organizacionVinculadaDAO.buscarOrganizacionPorID(1);
        assertNotNull(ov, "La OV no debería ser nula.");
        assertEquals(1, ov.getIdOrganizacion(), "El ID de la OV debería coincidir.");
    }
}
