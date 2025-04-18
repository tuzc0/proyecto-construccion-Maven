

import logica.DAOs.EvidenciaEvaluacionDAO;
import logica.DTOs.EvidenciaEvaluacionDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class EvidenciaEvaluacionDAOTest {

    private EvidenciaEvaluacionDAO dao;

    @BeforeEach
    void setUp() {
        dao = new EvidenciaEvaluacionDAO();
    }

    @AfterEach
    void tearDown() {
        dao = null;
    }

    @Test
    void testInsertarEvidenciaEvaluacion() {
        EvidenciaEvaluacionDTO evidencia = new EvidenciaEvaluacionDTO();
        evidencia.setIdEvidencia(0);
        evidencia.setURL("http://example.com/evidencia");
        evidencia.setIdEvaluacion(1);

        try {
            boolean result = dao.insertarEvidenciaEvaluacion(evidencia);
            assertTrue(result, "The evidence should be inserted successfully.");
        } catch (SQLException | IOException e) {
            fail("Exception occurred during test: " + e.getMessage());
        }
    }

    @Test
    void testMostrarEvidenciaEvaluacionPorID() {
        int idEvidencia = 1;

        try {
            EvidenciaEvaluacionDTO evidencia = dao.mostrarEvidenciaEvaluacionPorID(idEvidencia);
            assertNotNull(evidencia, "The evidence should not be null.");
            assertEquals(idEvidencia, evidencia.getIdEvidencia(), "The ID should match the requested ID.");
            assertNotNull(evidencia.getURL(), "The URL should not be null.");
            assertTrue(evidencia.getIdEvaluacion() > 0, "The associated evaluation ID should be valid.");
        } catch (SQLException | IOException e) {
            fail("Exception occurred during test: " + e.getMessage());
        }
    }
}