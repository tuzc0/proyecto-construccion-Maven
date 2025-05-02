import logica.DAOs.PeriodoDAO;
import logica.DTOs.PeriodoDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PeriodoDAOTest {

    private static PeriodoDAO periodoDAO;

    @BeforeAll
    static void setUp() throws SQLException, IOException {

        periodoDAO = new PeriodoDAO();
    }

    @Test
    @Order(1)
    void testCrearNuevoPeriodoDatosValidos() throws SQLException, IOException {

        PeriodoDTO periodo = new PeriodoDTO(1, "Periodo 2023", 1);
        boolean resultado = periodoDAO.crearNuevoPeriodo(periodo);
        assertTrue(resultado, "El periodo debería haberse creado correctamente.");
    }

    @Test
    @Order(2)
    void testEliminarPeriodoPorIDDatosValidos() throws SQLException, IOException {

        boolean resultado = periodoDAO.eliminarPeriodoPorID(1);
        assertTrue(resultado, "El periodo debería haberse eliminado correctamente.");
    }

    @Test
    @Order(3)
    void testModificarPeriodoDatosValidos() throws SQLException, IOException {

        PeriodoDTO periodo = new PeriodoDTO(1, "Periodo 2023 Modificado", 1);
        boolean resultado = periodoDAO.modificarPeriodo(periodo);
        assertTrue(resultado, "El periodo debería haberse modificado correctamente.");
    }

    @Test
    @Order(4)
    void testMostrarPeriodoActualDatosValidos() throws SQLException, IOException {

        PeriodoDTO periodo = periodoDAO.mostrarPeriodoActual();
        assertEquals(1, periodo.getEstadoActivo(), "El estado del periodo debería ser activo.");
    }
}
