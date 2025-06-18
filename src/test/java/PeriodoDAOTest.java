import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.PeriodoDAO;
import logica.DTOs.AcademicoDTO;
import logica.DTOs.PeriodoDTO;
import logica.DTOs.UsuarioDTO;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PeriodoDAOTest {

    private PeriodoDAO periodoDAO;
    private Connection conexionBaseDeDatos;

    private final List<Integer> IDS_PERIODOS_INSERTADOS = new ArrayList<>();

    @BeforeEach
    void prepararDatos() {

        periodoDAO = new PeriodoDAO();

        IDS_PERIODOS_INSERTADOS.clear();

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            for (int idPeriodo : List.of(1, 2, 3)) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM periodo WHERE idPeriodo = " + idPeriodo).executeUpdate();
            }

            periodoDAO.crearNuevoPeriodo(new PeriodoDTO(1, "Periodo 2023", 1,
                    Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31")));
            periodoDAO.crearNuevoPeriodo(new PeriodoDTO(2, "Periodo 2024", 2,
                    Date.valueOf("2024-01-01"), Date.valueOf("2024-12-31")));

            IDS_PERIODOS_INSERTADOS.addAll(List.of(1, 2));

        } catch (SQLException | IOException e) {

            fail("Error en @BeforeEach al preparar datos: " + e);
        }
    }

    @AfterEach
    void limpiarDatos() {

        try {

            for (int idPeriodo : IDS_PERIODOS_INSERTADOS) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM periodo WHERE idPeriodo = " + idPeriodo).executeUpdate();
            }

        } catch (SQLException e) {

            fail("Error en @AfterEach al limpiar datos: " + e);
        }
    }

    @Test
    void testCrearNuevoPeriodoDatosValidos() {

        PeriodoDTO periodoDTO = new PeriodoDTO(0, "Periodo 2025", 1,
                Date.valueOf("2025-01-01"), Date.valueOf("2025-12-31"));

        try {

            boolean resultadoDeLaPrueba = periodoDAO.crearNuevoPeriodo(periodoDTO);
            assertTrue(resultadoDeLaPrueba, "El periodo debería haberse creado correctamente.");
            IDS_PERIODOS_INSERTADOS.add(periodoDTO.getIDPeriodo());

        } catch (SQLException | IOException e) {

            fail("Error al crear el periodo: " + e);
        }
    }

    @Test
    void testEliminarPeriodoPorIDValidos() {

        int idPeriodo = IDS_PERIODOS_INSERTADOS.get(0);

        try {

            boolean resultadoPrueba = periodoDAO.eliminarPeriodoPorID(idPeriodo);
            assertTrue(resultadoPrueba, "El periodo debería haberse eliminado correctamente.");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar el periodo: " + e);
        }
    }

    @Test
    void testEliminarPeriodoPorIDInexistente() {

        int idPeriodoInexistente = 999;

        try {

            boolean resultadoPrueba = periodoDAO.eliminarPeriodoPorID(idPeriodoInexistente);
            assertFalse(resultadoPrueba, "No debería eliminar un periodo inexistente.");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar el periodo inexistente: " + e);
        }
    }

    @Test
    void testModificarPeriodoDatosValidos() {

        PeriodoDTO periodoDTO = new PeriodoDTO(1, "Periodo 2023 Modificado", 1);

        try {

            boolean resultadoPrueba = periodoDAO.modificarPeriodo(periodoDTO);
            assertTrue(resultadoPrueba, "El periodo debería haberse modificado correctamente.");

        } catch (SQLException | IOException e) {

            fail("Error al modificar el periodo: " + e);
        }
    }

    @Test
    void testModificarPeriodoInexistente() {

        PeriodoDTO periodoDTO = new PeriodoDTO(999, "Periodo Inexistente", 1);

        try {

            boolean resultadoPrueba = periodoDAO.modificarPeriodo(periodoDTO);
            assertFalse(resultadoPrueba, "No debería modificar un periodo inexistente.");

        } catch (SQLException | IOException e) {

            fail("Error al modificar el periodo inexistente: " + e);
        }
    }

    @Test
    void testMostrarPeriodoActualExistente() {

        PeriodoDTO periodoEsperado = new PeriodoDTO(1,
                "Periodo 2023", 1);

        try {

            PeriodoDTO periodoActual = periodoDAO.mostrarPeriodoActual();
            assertEquals(periodoEsperado, periodoActual,
                    "La descripción del periodo actual debería ser 'Periodo 2023'.");

        } catch (SQLException | IOException e) {

            fail("Error al mostrar el periodo actual: " + e);
        }
    }

    @Test
    void testMostrarPeriodoActualInexistente() {

        PeriodoDTO periodoEsperado = new PeriodoDTO(-1, "N/A", -1);

        try {

            conexionBaseDeDatos.prepareStatement("DELETE FROM periodo").executeUpdate();

            PeriodoDTO periodoActual = periodoDAO.mostrarPeriodoActual();

            assertEquals(periodoEsperado,periodoActual, "No debería haber un periodo actual.");

        } catch (SQLException | IOException e) {

            fail("Error al mostrar el periodo actual inexistente: " + e);
        }
    }

    @Test
    void testExistePeriodoActivo() {

        try {

            boolean existePeriodoActivo = periodoDAO.existePeriodoActivo();
            assertTrue(existePeriodoActivo, "Debería existir un periodo activo.");

        } catch (SQLException | IOException e) {

            fail("Error al verificar si existe un periodo activo: " + e);
        }
    }

    @Test
    void testExistePeriodoActivoInexistente() {

        try {

            conexionBaseDeDatos.prepareStatement("UPDATE periodo SET estadoActivo = 0").executeUpdate();

            boolean existePeriodoActivo = periodoDAO.existePeriodoActivo();
            assertFalse(existePeriodoActivo, "No debería existir un periodo activo.");

        } catch (SQLException | IOException e) {

            fail("Error al verificar si existe un periodo activo inexistente: " + e);
        }
    }
}
