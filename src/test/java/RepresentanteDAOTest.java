import logica.DAOs.RepresentanteDAO;
import logica.DTOs.RepresentanteDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RepresentanteDAOTest {

    private static RepresentanteDAO representanteDAO;

    @BeforeAll
    static void setUp() throws SQLException, IOException {

        representanteDAO = new RepresentanteDAO();
    }

    @Test
    @Order(1)
    void testInsertarRepresentante() throws SQLException, IOException {

        RepresentanteDTO representante = new RepresentanteDTO(11, "correo@ejemplo.com", "1234567890", "Juan", "Pérez", 1, 1);
        boolean resultado = representanteDAO.insertarRepresentante(representante);
        assertTrue(resultado, "El representante debería haberse insertado correctamente.");
    }

    @Test
    @Order(2)
    void testEliminarRepresentantePorID() throws SQLException, IOException {

        boolean resultado = representanteDAO.eliminarRepresentantePorID(1);
        assertTrue(resultado, "El representante debería haberse eliminado correctamente.");
    }

    @Test
    @Order(3)
    void testModificarRepresentante() throws SQLException, IOException {

        RepresentanteDTO representante = new RepresentanteDTO(1, "nuevo@ejemplo.com", "0987654321", "Carlos", "Gómez", 2, 1);
        boolean resultado = representanteDAO.modificarRepresentante(representante);
        assertTrue(resultado, "El representante debería haberse modificado correctamente.");
    }

    @Test
    @Order(4)
    void testBuscarRepresentantePorID() throws SQLException, IOException {

        RepresentanteDTO representante = representanteDAO.buscarRepresentantePorID(1);
        assertEquals(1, representante.getIDRepresentante(), "El ID del representante debería coincidir.");
    }
}
