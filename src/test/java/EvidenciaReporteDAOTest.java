

import logica.DAOs.EvidenciaReporteDAO;
import logica.DTOs.EvidenciaReporteDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class EvidenciaReporteDAOTest {

    private EvidenciaReporteDAO dao;

    @BeforeEach
    void setUp() {
        dao = new EvidenciaReporteDAO();
    }

    @AfterEach
    void tearDown() {
        dao = null;
    }

    @Test
    void testInsertarEvidenciaReporte() {
        EvidenciaReporteDTO evidencia = new EvidenciaReporteDTO();
        evidencia.setIdEvidencia(0);
        evidencia.setURL("http://example.com/evidencia");
        evidencia.setIdReporte(1);

        try {
            boolean result = dao.insertarEvidenciaReporte(evidencia);
            assertTrue(result, "The evidence should be inserted successfully.");
        } catch (SQLException | IOException e) {
            fail("Exception occurred during test: " + e.getMessage());
        }
    }

    @Test
    void testMostrarEvidenciaReportePorID() {
        int idEvidencia = 1;

        try {
            EvidenciaReporteDTO evidencia = dao.mostrarEvidenciaReportePorID(idEvidencia);
            assertNotNull(evidencia, "The evidence should not be null.");
            assertEquals(idEvidencia, evidencia.getIdEvidencia(), "The ID should match the requested ID.");
            assertNotNull(evidencia.getURL(), "The URL should not be null.");
            assertTrue(evidencia.getIdReporte() > 0, "The associated report ID should be valid.");
        } catch (SQLException | IOException e) {
            fail("Exception occurred during test: " + e.getMessage());
        }
    }
}