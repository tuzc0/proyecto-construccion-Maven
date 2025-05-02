import logica.DAOs.ReporteContieneDAO;
import logica.DTOs.ReporteContieneDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReporteContieneDAOTest {

    private static ReporteContieneDAO reporteContieneDAO;

    @BeforeAll
    static void setUp() {
        reporteContieneDAO = new ReporteContieneDAO();
    }

    @Test
    @Order(1)
    void testInsertarReporteContieneDatosValidos() throws SQLException, IOException {
        ReporteContieneDTO reporte = new ReporteContieneDTO(3, 10,
                Timestamp.valueOf("2023-10-01 08:00:00"),
                Timestamp.valueOf("2023-10-01 10:00:00"), 1);
        boolean resultado = reporteContieneDAO.insertarReporteContiene(reporte);
        assertTrue(resultado, "La inserción del reporte debería ser exitosa.");
    }

    @Test
    @Order(2)
    void testBuscarReporteContienePorIDDatosValidos() throws SQLException, IOException {
        ReporteContieneDTO reporte = reporteContieneDAO.buscarReporteContienePorID(1);
        assertNotNull(reporte, "El reporte no debería ser nulo.");
        assertEquals(1, reporte.getIdReporte(), "El ID del reporte debería ser 1.");
        assertEquals(11, reporte.getIdActividad(), "El ID de la actividad debería ser 10.");
    }

    @Test
    @Order(3)
    void testModificarReporteContieneDatosValidos() throws SQLException, IOException {
        ReporteContieneDTO reporte = new ReporteContieneDTO(1, 11,
                Timestamp.valueOf("2023-10-01 09:00:00"),
                Timestamp.valueOf("2023-10-01 11:00:00"), 1);
        boolean resultado = reporteContieneDAO.modificarReporteContiene(reporte);
        assertTrue(resultado, "La modificación del reporte debería ser exitosa.");

        ReporteContieneDTO reporteModificado = reporteContieneDAO.buscarReporteContienePorID(1);
        assertEquals(11, reporteModificado.getIdActividad(), "El ID de la actividad debería ser 11.");
        assertEquals(Timestamp.valueOf("2023-10-01 09:00:00"), reporteModificado.getFechaInicioReal(), "La fecha de inicio real debería ser 2023-10-01 09:00:00.");
    }

    @Test
    @Order(4)
    void testEliminarReporteContienePorIDDatosValidos() throws SQLException, IOException {
        boolean resultado = reporteContieneDAO.eliminarReporteContienePorID(1);
        assertTrue(resultado, "La eliminación lógica del reporte debería ser exitosa.");

        ReporteContieneDTO reporteEliminado = reporteContieneDAO.buscarReporteContienePorID(1);
        assertEquals(-1, reporteEliminado.getEstadoActivo(), "El estado del reporte eliminado debería ser -1.");
    }
}