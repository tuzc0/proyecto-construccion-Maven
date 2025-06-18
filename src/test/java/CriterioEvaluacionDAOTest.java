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

    @BeforeEach
    void prepararDatosDePrueba() {

        criterioDAO = new CriterioEvaluacionDAO();
        NUMEROS_CRITERIOS_INSERTADOS.clear();

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            for (int numeroCriterio : List.of(1, 2, 3, 4)) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM criterioevaluacion WHERE numeroCriterio = " + numeroCriterio)
                        .executeUpdate();
            }

            criterioDAO.crearNuevoCriterioEvaluacion(new CriterioEvaluacionDTO(1, "Criterio 1",
                    1, 1));
            criterioDAO.crearNuevoCriterioEvaluacion(new CriterioEvaluacionDTO(2, "Criterio 2",
                    2, 1));
            criterioDAO.crearNuevoCriterioEvaluacion(new CriterioEvaluacionDTO(3, "Criterio 3",
                    3, 1));

            NUMEROS_CRITERIOS_INSERTADOS.addAll(List.of(1, 2, 3));

        } catch (SQLException | IOException e) {

            fail("Error en @BeforeAll al preparar datos: " + e);
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

            fail("Error en @AfterAll al limpiar datos: " + e);
        }
    }

    @Test
    void testCrearNuevoCriterioEvaluacionConDatosValidos() {

        CriterioEvaluacionDTO nuevoCriterio = new CriterioEvaluacionDTO(4, "Criterio 4", 4, 1);

        try {

            boolean resultadoInsercion = criterioDAO.crearNuevoCriterioEvaluacion(nuevoCriterio);

            assertTrue(resultadoInsercion, "El criterio debería ser creado correctamente.");
            NUMEROS_CRITERIOS_INSERTADOS.add(4);

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testCrearCriterioEvaluacionConDatosInvalidos() {

        CriterioEvaluacionDTO criterioInvalido = new CriterioEvaluacionDTO(-1, null, -1, -1);

        assertThrows(SQLException.class, () -> criterioDAO.crearNuevoCriterioEvaluacion(criterioInvalido),
                "Se esperaba una excepción debido a datos inválidos, la descripción no puede ser nula.");
    }

    @Test
    void testModificarCriterioEvaluacionConDatosValidos() {

        CriterioEvaluacionDTO criterioActualizado = new CriterioEvaluacionDTO(1, "Criterio Modificado", 1, 1);

        try {

            boolean resultadoModificacion = criterioDAO.modificarCriterioEvaluacion(criterioActualizado);
            assertTrue(resultadoModificacion, "El criterio debería ser modificado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testModificarCriterioEvaluacionConDatosInvalidos() {

        CriterioEvaluacionDTO criterioInvalido = new CriterioEvaluacionDTO(-1, null, -1, -1);

        try {

            boolean resultadoModificacion = criterioDAO.modificarCriterioEvaluacion(criterioInvalido);
            assertFalse(resultadoModificacion, "La modificación no debería ser exitosa con datos inválidos.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testModificacionCriterioEvaluacionConIDInexistente() {

        CriterioEvaluacionDTO criterioInexistente = new CriterioEvaluacionDTO(999, "Criterio Inexistente",
                999, 1);

        try {

            boolean resultadoModificacion = criterioDAO.modificarCriterioEvaluacion(criterioInexistente);
            assertFalse(resultadoModificacion, "La modificación no debería ser exitosa con un ID inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testEliminarCriterioEvaluacionPorIDValido() {

        try {

            boolean resultadoEliminacion = criterioDAO.eliminarCriterioEvaluacionPorID(2);
            assertTrue(resultadoEliminacion, "El criterio debería ser eliminado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testEliminarCriterioEvaluacionPorIDInvalido() {

        int idInvalido = -1;

        try {

            boolean resultadoEliminacion = criterioDAO.eliminarCriterioEvaluacionPorID(idInvalido);
            assertFalse(resultadoEliminacion, "La eliminación no debería ser exitosa con un ID inválido.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testBuscarCriterioEvaluacionPorIDValido() {

        CriterioEvaluacionDTO criterioEsperado = new CriterioEvaluacionDTO(3, "Criterio 3", 3, 1);

        try {

            CriterioEvaluacionDTO criterioObtenido = criterioDAO.buscarCriterioEvaluacionPorID(3);
            assertEquals(criterioEsperado, criterioObtenido, "El criterio debería ser encontrado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testBuscarCriterioEvaluacionPorIDInvalidos() {

        int idInvalido = -1;
        CriterioEvaluacionDTO criterioEsperado = new CriterioEvaluacionDTO(-1, "N/A", -1,-1);

        try {

            CriterioEvaluacionDTO criterioEncontrado = criterioDAO.buscarCriterioEvaluacionPorID(idInvalido);
            assertEquals(criterioEsperado, criterioEncontrado,
                    "El criterio esperado deberia ser igual al criterio esperado.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testBuscarCriterioEvaluacionPorIDConDatosInexistentes() {

        int idCriterioInexistente = 999;
        CriterioEvaluacionDTO criterioEsperado = new CriterioEvaluacionDTO(-1, "N/A", -1,-1);

        try {

            CriterioEvaluacionDTO criterioEncontrado = criterioDAO.buscarCriterioEvaluacionPorID(idCriterioInexistente);
            assertEquals(criterioEsperado, criterioEncontrado,
                    "El criterio esperado deberia ser igual al criterio esperado.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testListarCriteriosEvaluacionActivos() {

        List<CriterioEvaluacionDTO> criteriosEsperados = List.of(

                new CriterioEvaluacionDTO(1, "Criterio 1", 1, 1),
                new CriterioEvaluacionDTO(2, "Criterio 2", 2, 1),
                new CriterioEvaluacionDTO(3, "Criterio 3", 3, 1)
        );

        try {

            List<CriterioEvaluacionDTO> listaCriteriosEncontrados = criterioDAO.listarCriteriosActivos();

            for (int criterioEncontrado = 0; criterioEncontrado < criteriosEsperados.size(); criterioEncontrado++) {

                assertTrue(criteriosEsperados.get(criterioEncontrado).equals(listaCriteriosEncontrados.
                                get(criterioEncontrado)),
                        "El criterio esperado debería ser igual al criterio encontrado.");
            }

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testListarCriteriosEvaluacionActivosSinDatos() {

        try {

            for (int numeroCriterio : NUMEROS_CRITERIOS_INSERTADOS) {

                conexionBaseDeDatos.prepareStatement("DELETE FROM criterioevaluacion WHERE numeroCriterio = " +
                                numeroCriterio).executeUpdate();
            }

            List<CriterioEvaluacionDTO> listaCriteriosEncontrados = criterioDAO.listarCriteriosActivos();
            assertTrue(listaCriteriosEncontrados.isEmpty(), "La lista de criterios debería estar vacía.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void obtenerNumeroCriterioMasAltoConDatosExistentes() {

        int resultadoEsperado = 3;

        try {

            int numeroCriterioMasAlto = criterioDAO.obtenerNumeroCriterioMasAlto();
            assertEquals(resultadoEsperado, numeroCriterioMasAlto, "El número de criterio más alto debería ser 3.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void obtenerNumeroCriterioMasAltoSinDatos() {

        int resultadoEsperado = -1;

        try {

            for (int numeroCriterio : NUMEROS_CRITERIOS_INSERTADOS) {

                conexionBaseDeDatos.prepareStatement("DELETE FROM criterioevaluacion WHERE numeroCriterio = " +
                                numeroCriterio).executeUpdate();
            }

            int numeroCriterioMasAlto = criterioDAO.obtenerNumeroCriterioMasAlto();
            assertEquals(resultadoEsperado, numeroCriterioMasAlto, "El número de criterio más alto debería ser -1.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void enumerarCriteriosConDatosEnLaBase() {

        try {

            criterioDAO.enumerarCriterios();

            assertEquals(1, criterioDAO.buscarCriterioEvaluacionPorID(1).getNumeroCriterio(),
                    "El primer criterio activo debe renumerarse a 1");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void enumerarCriteriosSinDatosEnLaBase() {

        try {

            for (int numeroCriterio : NUMEROS_CRITERIOS_INSERTADOS) {

                criterioDAO.eliminarCriterioEvaluacionPorID(numeroCriterio);
            }

            NUMEROS_CRITERIOS_INSERTADOS.clear();

            assertDoesNotThrow(() -> criterioDAO.enumerarCriterios(),
                    "El método debe ejecutarse sin errores con tabla vacía");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }
}