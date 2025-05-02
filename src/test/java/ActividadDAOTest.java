import logica.DAOs.ActividadDAO;
import logica.DTOs.ActividadDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActividadDAOTest {

    private static ActividadDAO actividadDAO;

    @BeforeAll
    static void setUp() {

        actividadDAO = new ActividadDAO();
    }

    @Test
    @Order(1)
    void testCrearNuevaActividadDatosValidos() throws SQLException, IOException {

        ActividadDTO actividad = new ActividadDTO(0, "numero hitos", Timestamp.valueOf("2023-10-01 08:00:00"),
                Timestamp.valueOf("2023-10-01 10:00:00"), "2 horas", "Actividad", 1);
        boolean resultado = actividadDAO.crearNuevaActividad(actividad);
        assertTrue(resultado, "La creación de la actividad debería ser exitosa.");
    }

    @Test
    @Order(2)
    void testBuscarActividadPorIDDatosValidos() throws SQLException, IOException {

        ActividadDTO actividad = actividadDAO.buscarActividadPorID(1);
        assertEquals(1, actividad.getIDActividad(), "El ID de la actividad debería ser 1.");
    }

    @Test
    @Order(3)
    void testModificarActividadDatosValidos() throws SQLException, IOException {

        ActividadDTO actividad = new ActividadDTO(1, "Actividad Modificada", Timestamp.valueOf("2023-10-01 09:00:00"),
                Timestamp.valueOf("2023-10-01 12:00:00"), "3 horas", "Actividad Modificada", 1);

        boolean resultado = actividadDAO.modificarActividad(actividad);
        assertTrue(resultado, "La modificación de la actividad debería ser exitosa.");
    }

    @Test
    @Order(4)
    void testEliminarActividadPorIDDatosValidos() throws SQLException, IOException {

        boolean resultado = actividadDAO.eliminarActividadPorID(1);
        ActividadDTO actividadEliminada = actividadDAO.buscarActividadPorID(1);
        assertEquals(0, actividadEliminada.getEstadoActivo(), "El estado de la actividad eliminada debería ser 0.");
    }
}