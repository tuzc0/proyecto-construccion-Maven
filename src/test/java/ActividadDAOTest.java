import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.ActividadDAO;
import logica.DTOs.ActividadDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActividadDAOTest {

    private ActividadDAO actividadDAO;
    private Connection conexionBaseDeDatos;

    private final List<Integer> IDS_ACTIVIDADES_CREADAS = new ArrayList<>();

    @BeforeEach
    void prepararDatosDePrueba() {

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
            actividadDAO = new ActividadDAO();

            IDS_ACTIVIDADES_CREADAS.clear();

            for (int idActividad : List.of(1001, 1002, 1003)) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM actividad WHERE IDActividad = " + idActividad)
                        .executeUpdate();
            }

            String insercionActividad = "INSERT INTO actividad (IdActividad, nombre, duracion, hitos, fechaInicio, fechaFin, estadoActivo) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertarActividad = conexionBaseDeDatos.prepareStatement(insercionActividad);
            insertarActividad.setInt(1, 1001);
            insertarActividad.setString(2, "Actividad 1");
            insertarActividad.setString(3, "1 mes");
            insertarActividad.setString(4, "Hito 1");
            insertarActividad.setTimestamp(5, Timestamp.valueOf("2023-01-01 00:00:00"));
            insertarActividad.setTimestamp(6, Timestamp.valueOf("2023-02-01 00:00:00"));
            insertarActividad.setInt(7, 1);
            insertarActividad.executeUpdate();

            IDS_ACTIVIDADES_CREADAS.add(1001);

        } catch (SQLException | IOException e) {

            fail("Error al preparar los datos de prueba: " + e.getMessage());
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try {

            for (int idActividad : IDS_ACTIVIDADES_CREADAS) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM actividad WHERE IDActividad = " + idActividad)
                        .executeUpdate();
            }

            conexionBaseDeDatos.close();

        } catch (SQLException e) {

            fail("Error al limpiar los datos de prueba: " + e.getMessage());
        }
    }

    @Test
    void testInsertarActividadConDatosValidos() {

        try {

            ActividadDTO nuevaActividad = new ActividadDTO(1002, "Actividad 2", "2 meses", "Hito 2",
                    Timestamp.valueOf("2023-03-01 00:00:00"), Timestamp.valueOf("2023-05-01 00:00:00"), 1);

            boolean resultadoActividadCreada = actividadDAO.crearNuevaActividad(nuevaActividad);
            IDS_ACTIVIDADES_CREADAS.add(1002);
            assertTrue(resultadoActividadCreada, "La actividad debería ser insertada correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testInsertarActividadConDatosInvalidos() {

        ActividadDTO actividadInvalida = new ActividadDTO(-1, null, null, null, null, null, 1);

        assertThrows(SQLException.class, () -> actividadDAO.crearNuevaActividad(actividadInvalida),
                "Se esperaba una excepción debido a datos inválidos.");
    }

    @Test
    void testBuscarActividadPorIDConDatosValidos() {

        try {

            ActividadDTO actividadEsperada = new ActividadDTO(1001, "Actividad 1", "1 mes", "Hito 1",
                    Timestamp.valueOf("2023-01-01 00:00:00"), Timestamp.valueOf("2023-02-01 00:00:00"), 1);

            ActividadDTO actividadEncontrada = actividadDAO.buscarActividadPorID(1001);
            assertEquals(actividadEsperada, actividadEncontrada, "La actividad debería ser encontrada correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarActividadPorIDConDatosInvalidos() {

        try {

            ActividadDTO actividadEncontrada = actividadDAO.buscarActividadPorID(9999);
            assertEquals(-1, actividadEncontrada.getIDActividad(), "No debería encontrarse una actividad con ese ID.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testModificarActividadConDatosValidos() {

        try {

            ActividadDTO actividadActualizada = new ActividadDTO(1001, "Actividad Modificada", "2 meses", "Hito Modificado",
                    Timestamp.valueOf("2023-01-15 00:00:00"), Timestamp.valueOf("2023-03-15 00:00:00"), 1);

            boolean actualizarActividadResultado = actividadDAO.modificarActividad(actividadActualizada);
            assertTrue(actualizarActividadResultado, "La actividad debería ser modificada correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testModificarActividadConDatosInvalidos() {

        ActividadDTO actividadInvalida = new ActividadDTO(9999, null, null, null, null, null, 1);

        try {

            boolean modificacionActividadResultado = actividadDAO.modificarActividad(actividadInvalida);
            assertFalse(modificacionActividadResultado, "No debería modificarse una actividad con datos inválidos.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarActividadPorIDConDatosValidos() {

        try {

            boolean eliminarActividadResultado = actividadDAO.eliminarActividadPorID(1001);
            assertTrue(eliminarActividadResultado, "La actividad debería ser eliminada correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarActividadPorIDConDatosInvalidos() {

        try {

            boolean eliminarActividadResultado = actividadDAO.eliminarActividadPorID(9999);
            assertFalse(eliminarActividadResultado, "No debería eliminarse una actividad inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }
}