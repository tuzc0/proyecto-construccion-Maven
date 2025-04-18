import org.junit.jupiter.api.*;
import logica.DAOs.AcademicoDAO;
import logica.DTOs.AcademicoDTO;
import java.io.IOException;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AcademicoDAOTest {

    private AcademicoDAO academicoDAO;

    @BeforeAll
    void setUp() {
        try {
            academicoDAO = new AcademicoDAO();
        } catch (IOException | SQLException e) {
            fail("Error al inicializar AcademicoDAO: " + e.getMessage());
        }
    }

    @Test
    void testInsertarAcademico() {
        AcademicoDTO academico = new AcademicoDTO(1002, 1, "Juan", "Perez", 1);

        try {
            boolean resultado = academicoDAO.insertarAcademico(academico);
            assertTrue(resultado, "El académico debería ser insertado correctamente.");
        } catch (SQLException e) {
            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarAcademicoPorNumeroDePersonal() {
        int estadoActivo = 0;
        String numeroDePersonal = "1001";

        try {
            boolean resultado = academicoDAO.eliminarAcademicoPorNumeroDePersonal(estadoActivo, numeroDePersonal);
            assertTrue(resultado, "El académico debería ser eliminado correctamente.");
        } catch (SQLException e) {
            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testModificarAcademico() {
        AcademicoDTO academico = new AcademicoDTO(1001, 2, "Carlos", "Lopez", 1);

        try {
            boolean resultado = academicoDAO.modificarAcademico(academico);
            assertTrue(resultado, "El académico debería ser modificado correctamente.");
        } catch (SQLException e) {
            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarAcademicoPorNumeroDePersonal() {
        int numeroDePersonal = 1001;

        try {
            AcademicoDTO academico = academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroDePersonal);
            assertNotNull(academico, "El académico no debería ser nulo.");
            assertEquals(numeroDePersonal, academico.getNumeroDePersonal(), "El número de personal debería coincidir.");
        } catch (SQLException e) {
            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }
}