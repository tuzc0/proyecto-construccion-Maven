import logica.DAOs.ProyectoDAO;
import logica.DTOs.ProyectoDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProyectoDAOTest {

    private static ProyectoDAO proyectoDAO;

    @BeforeAll
    static void setUp() throws SQLException, IOException {

        proyectoDAO = new ProyectoDAO();
    }

    @Test
    @Order(1)
    void testCrearNuevoProyecto() throws SQLException, IOException {

        ProyectoDTO proyecto = new ProyectoDTO(1, "Proyecto A", "Descripción A",
                LocalDate.now().toString(), LocalDate.now().plusDays(30).toString(),
                11, "A12345", 1);
        boolean resultado = proyectoDAO.crearNuevoProyecto(proyecto);
        assertTrue(resultado, "El proyecto debería haberse creado correctamente.");
    }

    @Test
    @Order(2)
    void testEliminarProyectoPorID() throws SQLException, IOException {

        boolean resultado = proyectoDAO.eliminarProyectoPorID(1);
        assertTrue(resultado, "El proyecto debería haberse eliminado correctamente.");
    }

    @Test
    @Order(3)
    void testModificarProyecto() throws SQLException, IOException {

        ProyectoDTO proyecto = new ProyectoDTO(1, "Proyecto B", "Descripción Modificada",
                LocalDate.now().toString(), LocalDate.now().plusDays(60).toString(),
                10, "A12346", 1);
        boolean resultado = proyectoDAO.modificarProyecto(proyecto);
        assertTrue(resultado, "El proyecto debería haberse modificado correctamente.");
    }

    @Test
    @Order(4)
    void testBuscarProyectoPorID() throws SQLException, IOException {

        ProyectoDTO proyecto = proyectoDAO.buscarProyectoPorID(1);
        assertNotNull(proyecto, "El proyecto no debería ser nulo.");
        assertEquals(1, proyecto.getIDProyecto(), "El ID del proyecto debería coincidir.");
    }
}