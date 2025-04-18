
import logica.DAOs.CronogramaContieneDAO;
import logica.DTOs.CronogramaContieneDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CronogramaContieneDAOTest {

    private static CronogramaContieneDAO cronogramaContieneDAO;

    @BeforeAll
    static void setUp() {
        cronogramaContieneDAO = new CronogramaContieneDAO();
    }

    @Test
    @Order(1)
    void testInsertarCronogramaContiene() throws SQLException, IOException {
        CronogramaContieneDTO cronograma = new CronogramaContieneDTO(3, 10,1);
        boolean resultado = cronogramaContieneDAO.insertarCronogramaContiene(cronograma);
        assertTrue(resultado, "La inserción del cronograma debería ser exitosa.");
    }

    @Test
    @Order(2)
    void testBuscarCronogramaContienePorID() throws SQLException, IOException {
        CronogramaContieneDTO cronograma = cronogramaContieneDAO.buscarCronogramaContienePorID(1);
        assertEquals(11, cronograma.getIdActividad(), "El ID de la actividad debería ser 11.");
    }

    @Test
    @Order(3)
    void testModificarActividadesDeCronograma() throws SQLException, IOException {
        CronogramaContieneDTO cronograma = new CronogramaContieneDTO(1, 11,1);
        boolean resultado = cronogramaContieneDAO.modificarActividadesDeCronograma(cronograma);
        assertTrue(resultado, "La modificación del cronograma debería ser exitosa.");

        CronogramaContieneDTO cronogramaModificado = cronogramaContieneDAO.buscarCronogramaContienePorID(1);
        assertEquals(11, cronogramaModificado.getIdActividad(), "El ID de la actividad debería ser 11 después de la modificación.");
    }

    @Test
    @Order(4)
    void testEliminarCronogramaContienePorID() throws SQLException, IOException {
        boolean resultado = cronogramaContieneDAO.eliminarCronogramaContienePorID(1);
        assertTrue(resultado, "La eliminación del cronograma debería ser exitosa.");

        CronogramaContieneDTO cronogramaEliminado = cronogramaContieneDAO.buscarCronogramaContienePorID(1);
        assertEquals(0, cronogramaEliminado.getEstadoActivo(), "El cronograma eliminado debería tener un ID 0.");
    }
}