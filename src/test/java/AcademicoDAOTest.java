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

    @AfterAll
    void tearDown() {

        try {

            int[] numerosDePrueba = {55555, 1003};

            for (int numero : numerosDePrueba) {

                academicoDAO.eliminarAcademicoPorNumeroDePersonal(numero);
            }

        } catch (SQLException | IOException e) {

            System.err.println("Error al limpiar los datos de prueba: " + e.getMessage());
        }
    }

    @Test
    void testInsertarAcademicoDatosValidos() {

        AcademicoDTO academico = new AcademicoDTO(55555, 0, "Nombre", "Prueba", 1);

        try {

            boolean resultado = academicoDAO.insertarAcademico(academico);
            assertTrue(resultado, "El académico debería ser insertado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testInsertarAcademicoDatosInvalidos() {

        AcademicoDTO academico = new AcademicoDTO(55555, -1, "Nombre", "Invalido", 1);

        try {

            boolean resultado = academicoDAO.insertarAcademico(academico);
            assertFalse(resultado, "El académico no debería ser insertado con datos inválidos.");

        } catch (SQLException | IOException e) {

            assertTrue(true);
        }
    }

    @Test
    void testBuscarAcademicoPorNumeroDePersonalDatosValidos() {

        int numeroDePersonal = 1001;

        try {

            AcademicoDTO academico = academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroDePersonal);
            assertEquals(numeroDePersonal, academico.getNumeroDePersonal(), "El número de personal debería coincidir.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarAcademicoPorNumeroDePersonalDatosInvalidos() {

        int numeroDePersonal = 99999;

        try {

            AcademicoDTO academico = academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroDePersonal);
            assertEquals(-1, academico.getNumeroDePersonal(), "No debería encontrarse un académico con ese número.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }


    @Test
    void testEliminarAcademicoPorNumeroDePersonalDatosValidos() {

        int numeroDePersonal = 1002;

        try {

            boolean resultado = academicoDAO.eliminarAcademicoPorNumeroDePersonal(numeroDePersonal);
            assertTrue(resultado, "El académico debería ser eliminado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarAcademicoPorNumeroDePersonalDatosInvalidos() {

        int numeroDePersonal = 88888;

        try {

            boolean resultado = academicoDAO.eliminarAcademicoPorNumeroDePersonal(numeroDePersonal);
            assertFalse(resultado, "No debería eliminarse ningún académico inexistente.");

        } catch (SQLException | IOException e) {

            assertTrue(true);
        }
    }

    @Test
    void testModificarAcademicoDatosValidos() {

        AcademicoDTO academico = new AcademicoDTO(1003, 1, "NuevoNombre", "NuevoApellido", 1);

        try {

            boolean resultado = academicoDAO.modificarAcademico(academico);
            assertTrue(resultado, "El académico debería ser modificado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testModificarAcademicoDatosInvalidos() {

        AcademicoDTO academico = new AcademicoDTO(1003, -100, "Nombre", "Apellido", 1);

        try {

            boolean resultado = academicoDAO.modificarAcademico(academico);
            assertFalse(resultado, "No debería modificarse un académico con datos inválidos.");

        } catch (SQLException | IOException e) {

            assertTrue(true);
        }
    }
}
