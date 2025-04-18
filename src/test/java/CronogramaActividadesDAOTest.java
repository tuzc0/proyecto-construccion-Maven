import logica.DAOs.CronogramaActividadesDAO;
import logica.DTOs.CronogramaActividadesDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CronogramaActividadesDAOTest {

    private static CronogramaActividadesDAO cronogramaActividadesDAO;

    @BeforeAll
    static void setUp() {
        cronogramaActividadesDAO = new CronogramaActividadesDAO();
    }

    @Test
    @Order(1)
    void testCrearNuevoCronogramaDeActividades() throws SQLException, IOException {
        CronogramaActividadesDTO cronograma = new CronogramaActividadesDTO(0,
                Timestamp.valueOf("2023-10-01 08:00:00"),
                Timestamp.valueOf("2023-10-31 18:00:00"),
                "A12345");
        boolean resultado = cronogramaActividadesDAO.crearNuevoCronogramaDeActividades(cronograma);
        assertTrue(resultado, "La creación del cronograma debería ser exitosa.");
    }

    @Test
    @Order(2)
    void testBuscarCronogramaDeActividadesPorID() throws SQLException, IOException {
        CronogramaActividadesDTO cronograma = cronogramaActividadesDAO.buscarCronogramaDeActividadesPorID(1);
        assertNotNull(cronograma, "El cronograma no debería ser nulo.");
        assertEquals(1, cronograma.getIDCronograma(), "El ID del cronograma debería ser 1.");
        assertEquals("A12345", cronograma.getMatriculaEstudiante(), "La matrícula del estudiante debería ser A12345.");
    }

    @Test
    @Order(3)
    void testModificarCronogramaDeActividades() throws SQLException, IOException {
        CronogramaActividadesDTO cronograma = new CronogramaActividadesDTO(1,
                Timestamp.valueOf("2023-11-01 08:00:00"),
                Timestamp.valueOf("2023-11-30 18:00:00"),
                "A12345");
        boolean resultado = cronogramaActividadesDAO.modificarCronogramaDeActividades(cronograma);
        assertTrue(resultado, "La modificación del cronograma debería ser exitosa.");

        CronogramaActividadesDTO cronogramaModificado = cronogramaActividadesDAO.buscarCronogramaDeActividadesPorID(1);
        assertEquals(Timestamp.valueOf("2023-11-01 08:00:00"), cronogramaModificado.getFechaInicio(), "La fecha de inicio debería ser 2023-11-01 08:00:00.");
        assertEquals(Timestamp.valueOf("2023-11-30 18:00:00"), cronogramaModificado.getFechaFinal(), "La fecha final debería ser 2023-11-30 18:00:00.");
    }

    @Test
    @Order(4)
    void testEliminarCronogramaDeActividades() throws SQLException, IOException {
        boolean resultado = cronogramaActividadesDAO.modificarCronogramaDeActividades(
                new CronogramaActividadesDTO(1, null, null, "A12345")); // Simula eliminación lógica
        assertTrue(resultado, "La eliminación lógica del cronograma debería ser exitosa.");

        CronogramaActividadesDTO cronogramaEliminado = cronogramaActividadesDAO.buscarCronogramaDeActividadesPorID(1);
        assertEquals("A12345", cronogramaEliminado.getMatriculaEstudiante(), "El cronograma eliminado debería tener matrícula 'A12345'.");
    }
}
