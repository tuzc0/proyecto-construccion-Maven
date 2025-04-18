

import logica.DAOs.EvidenciaCronogramaDAO;
import logica.DTOs.EvidenciaCronogramaDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class EvidenciaCronogramaDAOTest {

    private EvidenciaCronogramaDAO dao;

    @BeforeEach
    void setUp() {
        dao = new EvidenciaCronogramaDAO();
    }

    @AfterEach
    void tearDown() {
        dao = null;
    }

    @Test
    void testInsertarEvidenciaCronograma() {
        EvidenciaCronogramaDTO evidencia = new EvidenciaCronogramaDTO();
        evidencia.setIdEvidencia(0);
        evidencia.setURL("http://example.com/evidencia");
        evidencia.setIdCronograma(1);

        try {
            boolean result = dao.insertarEvidenciaCronograma(evidencia);
            assertTrue(result, "The evidence should be inserted successfully.");
        } catch (SQLException | IOException e) {
            fail("Exception occurred during test: " + e.getMessage());
        }
    }

    @Test
    void testMostrarEvidenciaCronogramaPorID() {
        int idEvidencia = 1;

        try {
            EvidenciaCronogramaDTO evidencia = dao.mostrarEvidenciaCronogramaPorID(idEvidencia);
            assertNotNull(evidencia, "The evidence should not be null.");
            assertEquals(idEvidencia, evidencia.getIdEvidencia(), "The ID should match the requested ID.");
            assertNotNull(evidencia.getURL(), "The URL should not be null.");
            assertTrue(evidencia.getIdCronograma() > 0, "The associated cronograma ID should be valid.");
        } catch (SQLException | IOException e) {
            fail("Exception occurred during test: " + e.getMessage());
        }
    }
}
