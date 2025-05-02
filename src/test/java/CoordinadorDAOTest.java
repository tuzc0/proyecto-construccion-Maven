import logica.DAOs.CoordinadorDAO;
import logica.DTOs.CoordinadorDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CoordinadorDAOTest {

    private static CoordinadorDAO coordinadorDAO;

    @BeforeAll
    static void setUp() {

        coordinadorDAO = new CoordinadorDAO();
    }

    @Test
    @Order(1)
    void testInsertarCoordinadorDatosValidos() throws SQLException, IOException {

        CoordinadorDTO coordinador = new CoordinadorDTO(55555, 51, "Prueba", "Coordinador", 1);
        boolean resultado = coordinadorDAO.insertarCoordinador(coordinador);
        assertTrue(resultado, "El coordinador debería haberse insertado correctamente.");
    }

    @Test
    @Order(2)
    void testEliminarCoordinadorPorNumeroDePersonalDatosValidos() throws SQLException, IOException {

        int numeroDePersonal = 55555;
        boolean resultado = coordinadorDAO.eliminarCoordinadorPorNumeroDePersonal(numeroDePersonal);
        assertTrue(resultado, "El coordinador debería haberse eliminado correctamente.");
    }

    @Test
    @Order(3)
    void testModificarCoordinadorDatosValidos() throws SQLException, IOException {

        CoordinadorDTO coordinador = new CoordinadorDTO(55555, 51, "Prueba", "Modificar Coordinador", 1);
        boolean resultado = coordinadorDAO.modificarCoordinador(coordinador);
        assertTrue(resultado, "El coordinador debería haberse modificado correctamente.");
    }

    @Test
    @Order(4)
    void testBuscarCoordinadorPorNumeroDePersonalDatosValidos() throws SQLException, IOException {

        int numeroDePersonal = 55555;
        CoordinadorDTO coordinador = coordinadorDAO.buscarCoordinadorPorNumeroDePersonal(numeroDePersonal);
        assertEquals(numeroDePersonal, coordinador.getNumeroDePersonal(), "El número de personal debería coincidir.");
    }
}