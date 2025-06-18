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

        NUMEROS_CRITERIOS_INSERTADOS.clear();

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            for (int numeroCriterio : List.of(1, 2, 3, 4)) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM criterioautoevaluacion WHERE numeroCriterio = " + numeroCriterio)
                        .executeUpdate();
            }

            criterioDAO.crearNuevoCriterioAutoevaluacion(new CriterioAutoevaluacionDTO(1,
                    "Criterio 1", 1, 1));
            criterioDAO.crearNuevoCriterioAutoevaluacion(new CriterioAutoevaluacionDTO(2,
                    "Criterio 2", 2, 1));
            criterioDAO.crearNuevoCriterioAutoevaluacion(new CriterioAutoevaluacionDTO(3,
                    "Criterio 3", 3, 1));

            NUMEROS_CRITERIOS_INSERTADOS.addAll(List.of(1, 2, 3));

        } catch (SQLException | IOException e) {

            fail("Error en @BeforeEach al preparar datos: " + e);
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

            fail("Error en @AfterEach al limpiar datos: " + e);
        }
    }

    @Test
    void testCrearNuevoCriterioAutoevaluacionConDatosValidos() {

        CriterioAutoevaluacionDTO criterioDTO = new CriterioAutoevaluacionDTO(4, "Criterio 4",
                4, 1);

        try {

            boolean resultadoInsercion = criterioDAO.crearNuevoCriterioAutoevaluacion(criterioDTO);
            assertTrue(resultadoInsercion, "El criterio debería ser creado correctamente.");
            NUMEROS_CRITERIOS_INSERTADOS.add(4);

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testCrearCriterioAutoevaluacionConDatosInvalidos() {

        CriterioAutoevaluacionDTO criterioInvalido = new CriterioAutoevaluacionDTO(-1, null, -1, -1);

        assertThrows(SQLException.class, () -> criterioDAO.crearNuevoCriterioAutoevaluacion(criterioInvalido),
                "Se esperaba una excepción debido a datos inválidos.");
    }

    @Test
    void testModificarCriterioAutoevaluacionConDatosValidos() {

        CriterioAutoevaluacionDTO criterioActualizado = new CriterioAutoevaluacionDTO(1, "Criterio Modificado",
                1, 1);

        try {

            boolean resultadoModificacion = criterioDAO.modificarCriterioAutoevaluacion(criterioActualizado);
            assertTrue(resultadoModificacion, "El criterio debería ser modificado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testModificarCriterioAutoevaluacionConDatosInvalidos() {

        CriterioAutoevaluacionDTO criterioInvalido = new CriterioAutoevaluacionDTO(1, null, -1, -1);

        try {

            boolean resultadoModificacion = criterioDAO.modificarCriterioAutoevaluacion(criterioInvalido);
            assertFalse(resultadoModificacion, "No se debería modificar un criterio con datos inválidos.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testModificarCriterioAutoevaluacionConIDInexistente() {

        CriterioAutoevaluacionDTO criterioInexistente = new CriterioAutoevaluacionDTO(999, "Criterio Inexistente",
                999, 1);

        try {

            boolean resultadoModificacion = criterioDAO.modificarCriterioAutoevaluacion(criterioInexistente);
            assertFalse(resultadoModificacion, "No se debería modificar un criterio inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testEliminarCriterioAutoevaluacionPorNumeroDeCriterioConDatosValidos() {

        int idCriterio = NUMEROS_CRITERIOS_INSERTADOS.get(1);

        try {

            boolean resultadoEliminacion = criterioDAO.eliminarCriterioAutoevaluacionPorNumeroDeCriterio(idCriterio);
            assertTrue(resultadoEliminacion, "El criterio debería ser eliminado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testEliminarCriterioAutoevaluacionPorNumeroDeCriterioConDatosInvalidos() {

        int numeroCriterioInvalido = -1;

        try {

            boolean resultadoEliminacion =
                    criterioDAO.eliminarCriterioAutoevaluacionPorNumeroDeCriterio(numeroCriterioInvalido);
            assertFalse(resultadoEliminacion, "No se debería eliminar un criterio con número inválido.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testEliminarCriterioAutoevaluacionPorIDValido() {

        int idCriterio = NUMEROS_CRITERIOS_INSERTADOS.get(0);

        try {

            boolean resultadoEliminacion = criterioDAO.eliminarCriterioAutoevaluacionPorID(idCriterio);
            assertTrue(resultadoEliminacion, "El criterio debería ser eliminado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testEliminarCriterioAutoevaluacionPorIDInvalido() {

        int idCriterioInvalido = 9999;

        try {

            boolean resultadoEliminacion = criterioDAO.eliminarCriterioAutoevaluacionPorID(idCriterioInvalido);
            assertFalse(resultadoEliminacion, "No se debería eliminar un criterio con ID inválido.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testBuscarCriterioAutoevaluacionPorIDValidos() {

        CriterioAutoevaluacionDTO criterioEsperado = new CriterioAutoevaluacionDTO(1, "Criterio 1",
                1, 1);

        try {

            CriterioAutoevaluacionDTO criterioObtenido =
                    criterioDAO.buscarCriterioAutoevaluacionPorID(criterioEsperado.getNumeroCriterio());
            assertEquals(criterioEsperado, criterioObtenido, "El criterio debería ser encontrado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testBuscarCriterioAutoevaluacionPorIDInvalidos() {

        int idInvalido = -1;
        CriterioAutoevaluacionDTO criterioEsperado = new CriterioAutoevaluacionDTO(-1, "N/A", -1,-1);

        try {

            CriterioAutoevaluacionDTO criterioEncontrado = criterioDAO.buscarCriterioAutoevaluacionPorID(idInvalido);
            assertEquals(criterioEsperado, criterioEncontrado,
                    "El criterio encontrado y el criterio esperado deberían ser iguales.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void listarCriteriosAutoevaluacionActivosConDatos() {

        try {

            List<CriterioAutoevaluacionDTO> listaCriterios = criterioDAO.listarCriteriosAutoevaluacionActivos();
            assertFalse(listaCriterios.isEmpty(), "La lista de criterios no debería estar vacía.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void listarCriteriosAutoevaluacionActivosSinDatos() {

        try {

            for (int numeroCriterio : NUMEROS_CRITERIOS_INSERTADOS) {
                criterioDAO.eliminarCriterioAutoevaluacionPorNumeroDeCriterio(numeroCriterio);
            }

            List<CriterioAutoevaluacionDTO> listaCriterios = criterioDAO.listarCriteriosAutoevaluacionActivos();
            assertTrue(listaCriterios.isEmpty(), "La lista de criterios debería estar vacía.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción al eliminar criterios: " + e);
        }
    }

    @Test
    void testObtenerNumeroDeCriterioMasAltoConDatos() {

        int resultadoEsperado = 3;

        try {

            int numeroCriterioMasAlto = criterioDAO.obtenerNumeroCriterioMasAlto();
            assertEquals(resultadoEsperado,numeroCriterioMasAlto,
                    "El número de criterio más alto debería coincidir con el numero esperado.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testObtenerNumeroCriterioMasAltoSinDatos() {

        try {

            for (int numeroCriterio : NUMEROS_CRITERIOS_INSERTADOS) {
                criterioDAO.eliminarCriterioAutoevaluacionPorNumeroDeCriterio(numeroCriterio);
            }

            int numeroCriterioMasAlto = criterioDAO.obtenerNumeroCriterioMasAlto();
            assertEquals(-1, numeroCriterioMasAlto,
                    "El número de criterio más alto debería ser -1 cuando no hay criterios.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción al eliminar criterios: " + e);
        }
    }

    @Test
    void testEnumerarCriteriosConDatos() {

        int resultadoEsperado = 1;

        try {

            criterioDAO.crearNuevoCriterioAutoevaluacion(new CriterioAutoevaluacionDTO(5, "Criterio 5",
                    1, 1));
            NUMEROS_CRITERIOS_INSERTADOS.add(5);

            criterioDAO.enumerarCriterios();

            assertEquals(resultadoEsperado, criterioDAO.buscarCriterioAutoevaluacionPorID(1).getNumeroCriterio(),
                    "El primer criterio activo debe renumerarse a 1");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción al enumerar criterios: " + e);
        }
    }

    @Test
    void testEnumerarCriteriosSinDatos() {

        try {

            for (int numeroCriterio : NUMEROS_CRITERIOS_INSERTADOS) {
                criterioDAO.eliminarCriterioAutoevaluacionPorNumeroDeCriterio(numeroCriterio);
            }

            NUMEROS_CRITERIOS_INSERTADOS.clear();

            assertDoesNotThrow(() -> criterioDAO.enumerarCriterios(),
                    "El método no debe lanzar excepciones cuando la tabla está vacía");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción al enumerar criterios: " + e);
        }
    }
}