import logica.DAOs.CriterioEvaluacionDAO;
import logica.DTOs.CriterioEvaluacionDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import accesoadatos.ConexionBaseDeDatos;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CriterioEvaluacionDAOTest {

    private CriterioEvaluacionDAO criterioDAO;
    private Connection conexionBaseDeDatos;

    private final List<Integer> NUMEROS_CRITERIOS_INSERTADOS = new ArrayList<>();

    @BeforeAll
    void prepararDatosDePrueba() {

        criterioDAO = new CriterioEvaluacionDAO();

        try {

            NUMEROS_CRITERIOS_INSERTADOS.clear();
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            for (int numeroCriterio : List.of(1, 2, 3, 4, 999)) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM criterioevaluacion WHERE numeroCriterio = " + numeroCriterio)
                        .executeUpdate();
            }

            criterioDAO.crearNuevoCriterioEvaluacion(new CriterioEvaluacionDTO(1, "Criterio 1", 1, 1));
            criterioDAO.crearNuevoCriterioEvaluacion(new CriterioEvaluacionDTO(2, "Criterio 2", 2, 1));
            criterioDAO.crearNuevoCriterioEvaluacion(new CriterioEvaluacionDTO(3, "Criterio 3", 3, 1));

            NUMEROS_CRITERIOS_INSERTADOS.addAll(List.of(1, 2, 3));

        } catch (SQLException | IOException e) {
            fail("Error en @BeforeAll al preparar datos: " + e.getMessage());
        }
    }

    @AfterAll
    void limpiarDatosDePrueba() {

        try {

            for (int numeroCriterio : NUMEROS_CRITERIOS_INSERTADOS) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM criterioevaluacion WHERE numeroCriterio = " + numeroCriterio)
                        .executeUpdate();
            }

            conexionBaseDeDatos.close();

        } catch (SQLException e) {

            fail("Error en @AfterAll al limpiar datos: " + e.getMessage());
        }
    }

    @Test
    void testCrearNuevoCriterioEvaluacionConDatosValidos() {

        try {

            CriterioEvaluacionDTO nuevoCriterio = new CriterioEvaluacionDTO(4, "Criterio 4", 4, 1);
            boolean resultadoInsercion = criterioDAO.crearNuevoCriterioEvaluacion(nuevoCriterio);

            assertTrue(resultadoInsercion, "El criterio debería ser creado correctamente.");
            NUMEROS_CRITERIOS_INSERTADOS.add(4);

        } catch (SQLException | IOException e) {
            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testCrearCriterioEvaluacionConDatosInvalidos() {

        CriterioEvaluacionDTO criterioInvalido = new CriterioEvaluacionDTO(-1, null, -1, -1);

        assertThrows(SQLException.class, () -> criterioDAO.crearNuevoCriterioEvaluacion(criterioInvalido),
                "Se esperaba una excepción debido a datos inválidos.");
    }

    @Test
    void testBuscarCriterioEvaluacionPorIDConDatosValidos() {

        try {

            CriterioEvaluacionDTO criterioEsperado = new CriterioEvaluacionDTO(3, "Criterio 3", 3, 1);
            CriterioEvaluacionDTO criterioObtenido = criterioDAO.buscarCriterioEvaluacionPorID(3);
            assertEquals(criterioEsperado, criterioObtenido, "El criterio debería ser encontrado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarCriterioEvaluacionPorIDConDatosInvalidos() {

        int idInvalido = -1;

        try {

            CriterioEvaluacionDTO criterioEncontrado = criterioDAO.buscarCriterioEvaluacionPorID(idInvalido);
            assertEquals(-1, criterioEncontrado.getNumeroCriterio(), "No debería encontrarse un criterio con un ID inválido.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarCriterioEvaluacionPorIDConDatosInexistentes() {

        try {

            CriterioEvaluacionDTO criterioEncontrado = criterioDAO.buscarCriterioEvaluacionPorID(999);
            assertEquals(-1, criterioEncontrado.getNumeroCriterio(), "No debería encontrarse un criterio inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testModificarCriterioEvaluacionConDatosValidos() {

        try {

            CriterioEvaluacionDTO criterioActualizado = new CriterioEvaluacionDTO(1, "Criterio Modificado", 1, 1);
            boolean resultadoModificacion = criterioDAO.modificarCriterioEvaluacion(criterioActualizado);
            assertTrue(resultadoModificacion, "El criterio debería ser modificado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarCriterioEvaluacionPorNumeroDeCriterioConDatosValidos() {

        try {

            boolean resultadoEliminacion = criterioDAO.eliminarCriterioEvaluacionPorID(2);
            assertTrue(resultadoEliminacion, "El criterio debería ser eliminado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }
}