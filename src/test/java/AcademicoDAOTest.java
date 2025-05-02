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

        academicoDAO = new AcademicoDAO();
    }

    @Test
    void testInsertarAcademico() {
        AcademicoDTO academico = new AcademicoDTO(22222, 49, "Prueba", "Insercion", 1);

        try {

            boolean resultado = academicoDAO.insertarAcademico(academico);
            assertTrue(resultado, "El académico debería ser insertado correctamente.");

        } catch (SQLException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    @Test
    void testEliminarAcademicoPorNumeroDePersonal() {

        String numeroDePersonal = "4";

        try {

            boolean resultado = academicoDAO.eliminarAcademicoPorNumeroDePersonal(numeroDePersonal);
            assertTrue(resultado, "El académico debería ser eliminado correctamente.");

        } catch (SQLException e) {

            fail("No se esperaba una excepción: " + e.getMessage());

        }  catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    @Test
    void testModificarAcademico() {

        AcademicoDTO academico = new AcademicoDTO(12348, 39, "Carlos", "Lopez", 1);

        try {

            boolean resultado = academicoDAO.modificarAcademico(academico);
            assertTrue(resultado, "El académico debería ser modificado correctamente.");

        } catch (SQLException e) {

            fail("No se esperaba una excepción: " + e.getMessage());

        }  catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    @Test
    void testBuscarAcademicoPorNumeroDePersonal() {

        int numeroDePersonal = 1001;

        try {

            AcademicoDTO academico = academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroDePersonal);
            assertEquals(numeroDePersonal, academico.getNumeroDePersonal(), "El número de personal debería coincidir.");

        } catch (SQLException e) {

            fail("No se esperaba una excepción: " + e.getMessage());

        }  catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}