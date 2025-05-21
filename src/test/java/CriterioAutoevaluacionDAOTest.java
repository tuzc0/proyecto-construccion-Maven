import logica.DAOs.CriterioAutoevaluacionDAO;
import logica.DTOs.CriterioAutoevaluacionDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import accesoadatos.ConexionBaseDeDatos;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CriterioAutoevaluacionDAOTest {

    private CriterioAutoevaluacionDAO criterioDAO;
    private Connection conexionBaseDeDatos;

    private final List<Integer> NUMEROS_CRITERIOS_INSERTADOS = new ArrayList<>();

    @BeforeEach
    void prepararDatosDePrueba() {

        criterioDAO = new CriterioAutoevaluacionDAO();

        try {

            NUMEROS_CRITERIOS_INSERTADOS.clear();
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            for (int numeroCriterio : List.of(1, 2, 3, 4, 999)) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM criterioautoevaluacion WHERE numeroCriterio = " + numeroCriterio)
                        .executeUpdate();
            }

            criterioDAO.crearNuevoCriterioAutoevaluacion(new CriterioAutoevaluacionDTO(1, "Criterio 1", 1, 1));
            criterioDAO.crearNuevoCriterioAutoevaluacion(new CriterioAutoevaluacionDTO(2, "Criterio 2", 2, 1));
            criterioDAO.crearNuevoCriterioAutoevaluacion(new CriterioAutoevaluacionDTO(3, "Criterio 3", 3, 1));

            NUMEROS_CRITERIOS_INSERTADOS.addAll(List.of(1, 2, 3));

        } catch (SQLException | IOException e) {

            fail("Error en @BeforeEach al preparar datos: " + e.getMessage());
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try {

            for (int numeroCriterio : NUMEROS_CRITERIOS_INSERTADOS) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM criterioautoevaluacion WHERE numeroCriterio = " + numeroCriterio)
                        .executeUpdate();
            }

            conexionBaseDeDatos.close();

        } catch (SQLException e) {

            fail("Error en @AfterEach al limpiar datos: " + e.getMessage());
        }
    }

    @Test
    void testCrearNuevoCriterioAutoevaluacionConDatosValidos() {

        try {

            CriterioAutoevaluacionDTO criterioDTO = new CriterioAutoevaluacionDTO(4, "Criterio 4", 4, 1);
            boolean resultadoInsercion = criterioDAO.crearNuevoCriterioAutoevaluacion(criterioDTO);
            assertTrue(resultadoInsercion, "El criterio debería ser creado correctamente.");
            NUMEROS_CRITERIOS_INSERTADOS.add(4);

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testCrearCriterioAutoevaluacionConDatosInvalidos() {

        CriterioAutoevaluacionDTO criterioInvalido = new CriterioAutoevaluacionDTO(-1, null, -1, -1);
        assertThrows(SQLException.class, () -> criterioDAO.crearNuevoCriterioAutoevaluacion(criterioInvalido),
                "Se esperaba una excepción debido a datos inválidos.");
    }

    @Test
    void testBuscarCriterioAutoevaluacionPorIDConDatosValidos() {

        try {

            CriterioAutoevaluacionDTO criterioEsperado = new CriterioAutoevaluacionDTO(1, "Criterio 1", 1, 1);
            CriterioAutoevaluacionDTO criterioObtenido = criterioDAO.buscarCriterioAutoevaluacionPorID(1);
            assertEquals(criterioEsperado, criterioObtenido, "El criterio debería ser encontrado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarCriterioAutoevaluacionPorIDConDatosInvalidos() {

        int idInvalido = -1;

        try {

            CriterioAutoevaluacionDTO criterioEncontrado = criterioDAO.buscarCriterioAutoevaluacionPorID(idInvalido);
            assertEquals(-1, criterioEncontrado.getNumeroCriterio(), "No debería encontrarse un criterio con un ID inválido.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarCriterioAutoevaluacionPorIDConDatosInexistentes() {

        try {

            CriterioAutoevaluacionDTO criterioEncontrado = criterioDAO.buscarCriterioAutoevaluacionPorID(999);
            assertEquals(-1, criterioEncontrado.getNumeroCriterio(), "No debería encontrarse un criterio inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testModificarCriterioAutoevaluacionConDatosValidos() {

        try {

            CriterioAutoevaluacionDTO criterioActualizado = new CriterioAutoevaluacionDTO(1, "Criterio Modificado", 1, 1);
            boolean resultadoModificacion = criterioDAO.modificarCriterioAutoevaluacion(criterioActualizado);
            assertTrue(resultadoModificacion, "El criterio debería ser modificado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarCriterioAutoevaluacionPorNumeroDeCriterioConDatosValidos() {

        try {

            boolean resultadoEliminacion = criterioDAO.eliminarCriterioAutoevaluacionPorNumeroDeCriterio(2);
            assertTrue(resultadoEliminacion, "El criterio debería ser eliminado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }
}