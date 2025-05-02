

import logica.DAOs.EvidenciaAutoevaluacionDAO;
import logica.DTOs.EvidenciaAutoevaluacionDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class EvidenciaAutoevaluacionDAOTest {

    private EvidenciaAutoevaluacionDAO dao;

    @BeforeEach
    void setUp() {
        dao = new EvidenciaAutoevaluacionDAO();
    }

    @AfterEach
    void tearDown() {
        dao = null;
    }

    @Test
    void testInsertarEvidenciaAutoevaluacionDatosValidos() {
        EvidenciaAutoevaluacionDTO evidencia = new EvidenciaAutoevaluacionDTO();
        evidencia.setIdEvidencia(1);
        evidencia.setURL("http://example.com/evidencia");
        evidencia.setIdAutoevaluacion(1);

        try {
            boolean result = dao.insertarEvidenciaAutoevaluacion(evidencia);
            assertTrue(result, "The evidence should be inserted successfully.");
        } catch (SQLException | IOException e) {
            fail("Exception occurred during test: " + e.getMessage());
        }
    }

    @Test
    void testMostrarEvidenciaAutoevaluacionPorIDDatosValidos() {
        int idEvidencia = 1;

        try {
            EvidenciaAutoevaluacionDTO evidencia = dao.mostrarEvidenciaAutoevaluacionPorID(idEvidencia);
            assertNotNull(evidencia, "The evidence should not be null.");
            assertEquals(idEvidencia, evidencia.getIdEvidencia(), "The ID should match the requested ID.");
            assertNotNull(evidencia.getURL(), "The URL should not be null.");
            assertTrue(evidencia.getIdAutoevaluacion() > 0, "The associated autoevaluation ID should be valid.");
        } catch (SQLException | IOException e) {
            fail("Exception occurred during test: " + e.getMessage());
        }
    }
}