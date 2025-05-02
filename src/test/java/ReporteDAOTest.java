import logica.DAOs.ReporteDAO;
import logica.DTOs.ReporteDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReporteDAOTest {

    private static ReporteDAO reporteDAO;

    @BeforeAll
    static void setUp() {
        reporteDAO = new ReporteDAO();
    }

    @Test
    @Order(1)
    void testInsertarReporteDatosValidos() throws SQLException, IOException {
        ReporteDTO reporte = new ReporteDTO(4, 5, "Metodología Ágil", "Observaciones iniciales",
                Timestamp.valueOf("2023-10-01 08:00:00"), "A12345");
        boolean resultado = reporteDAO.insertarReporte(reporte);
        assertTrue(resultado, "La inserción del reporte debería ser exitosa.");
    }

    @Test
    @Order(2)
    void testBuscarReportePorIDDatosValidos() throws SQLException, IOException {
        ReporteDTO reporte = reporteDAO.buscarReportePorID(1);
        assertNotNull(reporte, "El reporte no debería ser nulo.");
        assertEquals(1, reporte.getIDReporte(), "El ID del reporte debería ser 1.");
        assertEquals("Metodología Scrum", reporte.getMetodologia(), "La metodología debería ser 'Metodologia Scrum'.");
    }

    @Test
    @Order(3)
    void testModificarReporteDatosValidos() throws SQLException, IOException {
        ReporteDTO reporte = new ReporteDTO(1, 8, "Metodología Scrum", "Observaciones modificadas",
                Timestamp.valueOf("2023-10-02 09:00:00"), "A12345");
        boolean resultado = reporteDAO.modificarReporte(reporte);
        assertTrue(resultado, "La modificación del reporte debería ser exitosa.");

        ReporteDTO reporteModificado = reporteDAO.buscarReportePorID(1);
        assertEquals(8, reporteModificado.getNumeroHoras(), "El número de horas debería ser 8.");
        assertEquals("Metodología Scrum", reporteModificado.getMetodologia(), "La metodología debería ser 'Metodología Scrum'.");
    }
}
