import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.ActividadDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import logica.DAOs.ActividadDAO;

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

            for (int idActividad : List.of(1001, 1002, 1003, 1004)) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM actividad WHERE IDActividad = " + idActividad)
                        .executeUpdate();
            }

            String insercionActividad = "INSERT INTO actividad (IDActividad, nombre, duracion, hitos, fechaInicio, " +
                    "fechaFin, estadoActivo) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertarActividad = conexionBaseDeDatos.prepareStatement(insercionActividad);
            insertarActividad.setInt(1, 1001);
            insertarActividad.setString(2, "Actividad de Prueba 1");
            insertarActividad.setString(3, "1 mes");
            insertarActividad.setString(4, "Hito 1, Hito 2");
            insertarActividad.setDate(5, Date.valueOf("2023-01-01"));
            insertarActividad.setDate(6, Date.valueOf("2023-02-01"));
            insertarActividad.setInt(7, 1);
            insertarActividad.executeUpdate();
            IDS_ACTIVIDADES_CREADAS.add(1001);

            insertarActividad.setInt(1, 1002);
            insertarActividad.setString(2, "Actividad de Prueba 2");
            insertarActividad.setString(3, "2 semanas");
            insertarActividad.setString(4, "Hito único");
            insertarActividad.setDate(5, Date.valueOf("2023-03-01"));
            insertarActividad.setDate(6, Date.valueOf("2023-03-15"));
            insertarActividad.setInt(7, 1);
            insertarActividad.executeUpdate();
            IDS_ACTIVIDADES_CREADAS.add(1002);

            insertarActividad.setInt(1, 1003);
            insertarActividad.setString(2, "Actividad Inactiva");
            insertarActividad.setString(3, "3 días");
            insertarActividad.setString(4, "");
            insertarActividad.setDate(5, Date.valueOf("2023-04-01"));
            insertarActividad.setDate(6, Date.valueOf("2023-04-04"));
            insertarActividad.setInt(7, 0);
            insertarActividad.executeUpdate();
            IDS_ACTIVIDADES_CREADAS.add(1003);

        } catch (SQLException | IOException e) {

            fail("Error al preparar los datos de prueba: " + e);
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

            fail("Error al limpiar los datos de prueba: " + e);
        }
    }

    @Test
    @DisplayName("Crear actividad con datos válidos")
    void testCrearActividadConDatosValidos() {

        try {

            ActividadDTO nuevaActividad = new ActividadDTO(0, "Nueva Actividad Válida", "3 semanas",
                    "Hito A, Hito B", new Date(System.currentTimeMillis()),
                    new Date(System.currentTimeMillis() + 2000000000), 1);

            int idGenerado = actividadDAO.crearNuevaActividad(nuevaActividad);

            assertTrue(idGenerado > 0, "Debería retornar un ID válido");
            IDS_ACTIVIDADES_CREADAS.add(idGenerado);

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    @DisplayName("Crear actividad con datos vacíos")
    void testCrearActividadConDatosVacios() {

        ActividadDTO actividadVacia = new ActividadDTO(0, null, null, null,
                null, null, 1);

        assertThrows(SQLException.class, () -> {

            actividadDAO.crearNuevaActividad(actividadVacia);
        }, "Debería lanzar excepción con datos vacíos");
    }

    @Test
    @DisplayName("Buscar actividad existente")
    void testBuscarActividadExistente() {

        ActividadDTO actividadEsperada = new ActividadDTO(1003, "Actividad Inactiva", "3 días",
                "", Date.valueOf("2023-04-01"), Date.valueOf("2023-04-04"), 0);

        try {

            ActividadDTO actividadObtenida = actividadDAO.buscarActividadPorID(1003);
            assertEquals(actividadEsperada, actividadObtenida, "Debería retornar un DTO igual al esperado.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    @DisplayName("Buscar actividad inexistente")
    void testBuscarActividadInexistente() {

        ActividadDTO actividadEsperada = new ActividadDTO(-1, "N/A", "N/A",
                "N/A", null, null, 0);

        try {

            ActividadDTO actividadObtenida = actividadDAO.buscarActividadPorID(9999);
            assertEquals(actividadEsperada, actividadObtenida,
                    "Debería retornar un DTO igual al esperado.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    @DisplayName("Modificar actividad con datos válidos")
    void testModificarActividadConDatosValidos() {

        try {

            ActividadDTO actividadModificada = new ActividadDTO(1001, "Nombre Modificado",
                    "Duración Modificada", "Hitos Modificados",
                    new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000000000), 1);

            boolean resultado = actividadDAO.modificarActividad(actividadModificada);
            assertTrue(resultado, "Debería modificar correctamente");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    @DisplayName("Modificar actividad con datos vacíos")
    void testModificarActividadConDatosVacios() {

        ActividadDTO actividadVacia = new ActividadDTO(1001, null, null, null,
                null, null, 1);

        assertThrows(SQLException.class, () -> {

            actividadDAO.modificarActividad(actividadVacia);
        }, "Debería lanzar excepción con datos vacíos");
    }

    @Test
    @DisplayName("Eliminar actividad existente")
    void testEliminarActividadExistente() {

        try {

            boolean resultado = actividadDAO.eliminarActividadPorID(1001);
            assertTrue(resultado, "Debería eliminar correctamente");

            ActividadDTO actividadEliminada = actividadDAO.buscarActividadPorID(1001);
            assertEquals(0, actividadEliminada.getEstadoActivo());

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    @DisplayName("Eliminar actividad inexistente")
    void testEliminarActividadInexistente() {

        try {

            boolean resultado = actividadDAO.eliminarActividadPorID(9999);
            assertFalse(resultado, "No debería eliminar actividad inexistente");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }
}