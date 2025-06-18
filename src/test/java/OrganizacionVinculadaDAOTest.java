import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.OrganizacionVinculadaDAO;
import logica.DTOs.AcademicoEvaluadorDTO;
import logica.DTOs.OrganizacionVinculadaDTO;
import logica.DTOs.UsuarioDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrganizacionVinculadaDAOTest {

    private OrganizacionVinculadaDAO organizacionDAO;

    private final List<Integer> IDS_ORGANIZACIONES_INSERTADAS = new ArrayList<>();

    @BeforeEach
    void prepararDatosDePrueba() {

        organizacionDAO = new OrganizacionVinculadaDAO();
        IDS_ORGANIZACIONES_INSERTADAS.clear();

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection()) {

            for (int idOrganizacion : List.of(1, 2, 3, 4)) {

                PreparedStatement eliminar = conexionBaseDeDatos.prepareStatement
                        ("DELETE FROM organizacionvinculada WHERE idOV = ?");
                eliminar.setInt(1, idOrganizacion);
                eliminar.executeUpdate();
            }

            IDS_ORGANIZACIONES_INSERTADAS.add(organizacionDAO.crearNuevaOrganizacion
                    (new OrganizacionVinculadaDTO(1, "Empresa 1", "Dirección 1",
                            "empresa1@test.com", "5551111111", 1)));
            IDS_ORGANIZACIONES_INSERTADAS.add(organizacionDAO.crearNuevaOrganizacion
                    (new OrganizacionVinculadaDTO(2, "Empresa 2", "Dirección 2",
                            "empresa2@test.com", "5552222222", 1)));
            IDS_ORGANIZACIONES_INSERTADAS.add(organizacionDAO.crearNuevaOrganizacion
                    (new OrganizacionVinculadaDTO(3, "Empresa 3", "Dirección 3",
                            "empresa3@test.com", "5553333333", 1)));

        } catch (SQLException | IOException e) {

            fail("Error en @BeforeEach al preparar datos: " + e);
        }
    }


    @AfterEach
    void limpiarDatosDePrueba() {

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection()) {

            for (int idOrganizacion : IDS_ORGANIZACIONES_INSERTADAS) {

                PreparedStatement consultaPreparadaSQL = conexionBaseDeDatos.prepareStatement
                        ("DELETE FROM organizacionvinculada WHERE idOV = ?");
                consultaPreparadaSQL.setInt(1, idOrganizacion);
                consultaPreparadaSQL.executeUpdate();
            }

        } catch (SQLException | IOException e) {

            fail("Error al limpiar datos: " + e);
        }
    }



    @Test
    void crearOrganizacionVinculadaConDatosValidos() {

        OrganizacionVinculadaDTO organizacionDTO = new OrganizacionVinculadaDTO(0, "Empresa Test",
                "Calle Falsa 123","test@empresa.com", "5551234567", 1);

        try {

            int idOrganizacionGenerado = organizacionDAO.crearNuevaOrganizacion(organizacionDTO);
            assertTrue(idOrganizacionGenerado > 0, "Debería retornar ID válido");
            IDS_ORGANIZACIONES_INSERTADAS.add(idOrganizacionGenerado);

        } catch (SQLException | IOException e) {

            fail("Error al crear organización: " + e);
        }

    }

    @Test
    void crearOrganizacionVinculadaConDatosInvalidos() {

        OrganizacionVinculadaDTO organizacionInvalida = new OrganizacionVinculadaDTO(0, null,
                null, null, null, 1);

        assertThrows(SQLException.class, () -> organizacionDAO.crearNuevaOrganizacion(organizacionInvalida),
                "Debería lanzar excepción con datos inválidos");
    }

    @Test
    void eliminarOrganizacionConIDValido() {

        int idOrganizacionAEliminar = IDS_ORGANIZACIONES_INSERTADAS.get(0);

        try {

            boolean resultadoObtenido = organizacionDAO.eliminarOrganizacionPorID(idOrganizacionAEliminar);
            assertTrue(resultadoObtenido, "Debería eliminar correctamente");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar organización: " + e);
        }
    }

    @Test
    void eliminarOrganizacionConIDInvalido() {

        int idInvalido = -1;

        try {

            boolean resultadoObtenido = organizacionDAO.eliminarOrganizacionPorID(idInvalido);
            assertFalse(resultadoObtenido, "No debería eliminar con ID inválido");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar organización con ID inválido: " + e);
        }
    }

    @Test
    void eliminarOrganizacionInexistente() {

        try {

            boolean resultadoObtenido = organizacionDAO.eliminarOrganizacionPorID(99999);
            assertFalse(resultadoObtenido, "No debería eliminar organización inexistente");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar organización inexistente: " + e);
        }
    }

    @Test
    void modificarOrganizacionConDatosValidos() {

        int idOrganizacionAModificar = IDS_ORGANIZACIONES_INSERTADAS.get(1);
        OrganizacionVinculadaDTO organizacionModificada = new OrganizacionVinculadaDTO(idOrganizacionAModificar,
                "Empresa Modificada", "Calle Verdadera 456",
                "modificado@empresa.com", "5557654321",1);

        try {

            boolean resultadoObtenido = organizacionDAO.modificarOrganizacion(organizacionModificada);
            assertTrue(resultadoObtenido, "Debería modificar correctamente");

        } catch (SQLException | IOException e) {

            fail("Error al modificar organización: " + e);
        }
    }

    @Test
    void modificarOrganizacionConDatosInvalidos() {

        OrganizacionVinculadaDTO organizacionModificadaDatosInvalidos = new OrganizacionVinculadaDTO(2,
                null, null, null, null, 1);

        try {

            boolean resultadoPrueba = organizacionDAO.modificarOrganizacion(organizacionModificadaDatosInvalidos);
            assertFalse(resultadoPrueba, "No debería modificar con datos inválidos");

        } catch (SQLException | IOException e) {

            fail("Error al modificar organización con datos inválidos: " + e);
        }
    }

    @Test
    void modificarOrganizacionInexistente() {

        OrganizacionVinculadaDTO organizacionInexistente = new OrganizacionVinculadaDTO(99999,
                "Inexistente", "inexistente@empresa.com", "5550000000",
                "Dirección", 1);

        try {

            boolean resultadoPrueba = organizacionDAO.modificarOrganizacion(organizacionInexistente);
            assertFalse(resultadoPrueba, "No debería modificar organización inexistente");

        } catch (SQLException | IOException e) {

            fail("Error al modificar organización inexistente: " + e);
        }
    }

    @Test
    void buscarOrganizacionPorIDValido() {

        int idOrganizacionABuscar = IDS_ORGANIZACIONES_INSERTADAS.get(2);
        OrganizacionVinculadaDTO organizacionEsperada = new OrganizacionVinculadaDTO(idOrganizacionABuscar, "Empresa 3",
                "Dirección 3", "empresa3@test.com", "5553333333", 1);

        try {

            OrganizacionVinculadaDTO organizacionEncontrada =
                    organizacionDAO.buscarOrganizacionPorID(idOrganizacionABuscar);
            assertEquals(organizacionEsperada, organizacionEncontrada,
                    "Debería encontrar la organización correcta");

        } catch (SQLException | IOException e) {

            fail("Error al buscar organización por ID: " + e);
        }

    }

    @Test
    void buscarOrganizacionPorIDInvalido() {

        int idOrganizacionInvalido = -1;
        OrganizacionVinculadaDTO organizacionEsperada = new OrganizacionVinculadaDTO(-1, "N/A",
                "N/A", "N/A", "N/A", 0);

        try {

            OrganizacionVinculadaDTO organizacionEncontrada = organizacionDAO.buscarOrganizacionPorID(idOrganizacionInvalido);
            assertEquals(organizacionEsperada, organizacionEncontrada, "Debería retornar organización por defecto");

        } catch (SQLException | IOException e) {

            fail("Error al buscar organización por ID inválido: " + e);
        }

    }

    @Test
    void buscarOrganizacionInexistentePorID() {

        int idOrganizacionInexistente = 99999;
        OrganizacionVinculadaDTO organizacionEsperada = new OrganizacionVinculadaDTO(-1, "N/A",
                "N/A", "N/A", "N/A", 0);

        try {

            OrganizacionVinculadaDTO orgEncontrada = organizacionDAO.buscarOrganizacionPorID(idOrganizacionInexistente);
            assertEquals(organizacionEsperada, orgEncontrada, "Debería retornar organización por defecto");

        } catch (SQLException | IOException e) {

            fail("Error al buscar organización inexistente por ID: " + e);
        }
    }

    @Test
    void buscarOrganizacionPorCorreoValido() {

        int idOrganizacion = IDS_ORGANIZACIONES_INSERTADAS.get(0);
        String correoABuscar = "empresa1@test.com";
        OrganizacionVinculadaDTO organizacionEsperada = new OrganizacionVinculadaDTO(idOrganizacion, "Empresa 1",
                "Dirección 1","empresa1@test.com", "5551111111", 1);

        try {

            OrganizacionVinculadaDTO organizacionEncontrada =
                    organizacionDAO.buscarOrganizacionPorCorreo(correoABuscar);
            assertEquals(organizacionEsperada, organizacionEncontrada,
                    "Debería encontrar la organización correcta");

        } catch (SQLException | IOException e) {

            fail("Error al buscar organización por correo: " + e);
        }
    }

    @Test
    void buscarOrganizacionPorCorreoInvalido() {

        OrganizacionVinculadaDTO organizacionEsperada = new OrganizacionVinculadaDTO(-1, "N/A",
                "N/A", "N/A", "N/A", 0);

        try {

            OrganizacionVinculadaDTO orgEncontrada = organizacionDAO.buscarOrganizacionPorCorreo(null);
            assertEquals(organizacionEsperada, orgEncontrada, "Debería retornar organización por defecto");

        } catch (SQLException | IOException e) {

            fail("Error al buscar organización por correo inválido: " + e);
        }
    }

    @Test
    void buscarOrganizacionInexistentePorCorreo() {

        String correoInexistente = "noexiste@empresa.com";
        OrganizacionVinculadaDTO organizacionEsperada = new OrganizacionVinculadaDTO(-1, "N/A",
                "N/A", "N/A", "N/A", 0);

        try {

            OrganizacionVinculadaDTO organizacionEncontrada =
                    organizacionDAO.buscarOrganizacionPorCorreo(correoInexistente);
            assertEquals(organizacionEsperada, organizacionEncontrada,
                    "Debería retornar organización por defecto");

        } catch (SQLException | IOException e) {

            fail("Error al buscar organización inexistente por correo: " + e);
        }
    }

    @Test
    void buscarOrganizacionPorTelefonoValido() {

        int idOrganizacion = IDS_ORGANIZACIONES_INSERTADAS.get(1);
        String telefonoABuscar = "5552222222";
        OrganizacionVinculadaDTO organizacionEsperada = new OrganizacionVinculadaDTO(idOrganizacion, "Empresa 2",
                "Dirección 2", "empresa2@test.com", "5552222222",  1);

        try {

            OrganizacionVinculadaDTO organizacionEncontrada =
                    organizacionDAO.buscarOrganizacionPorTelefono(telefonoABuscar);
            assertEquals(organizacionEsperada, organizacionEncontrada, "Debería encontrar la organización correcta");

        } catch (SQLException | IOException e) {

            fail("Error al buscar organización por teléfono: " + e);
        }
    }

    @Test
    void buscarOrganizacionPorTelefonoInvalido() {

        OrganizacionVinculadaDTO organizacionEsperada = new OrganizacionVinculadaDTO(-1, "N/A",
                "N/A", "N/A", "N/A", 0);

        try {

            OrganizacionVinculadaDTO organizacionEncontrada = organizacionDAO.buscarOrganizacionPorTelefono("");
            assertEquals(organizacionEsperada, organizacionEncontrada, "Debería retornar organización por defecto");

        } catch (SQLException | IOException e) {

            fail("Error al buscar organización por teléfono inválido: " + e);
        }
    }

    @Test
    void buscarOrganizacionInexistentePorTelefono() {

        OrganizacionVinculadaDTO organizacionEsperada = new OrganizacionVinculadaDTO(-1, "N/A",
                "N/A", "N/A", "N/A", 0);

        try {

            OrganizacionVinculadaDTO organizacionEncontrada =
                    organizacionDAO.buscarOrganizacionPorTelefono("0000000000");
            assertEquals(organizacionEsperada, organizacionEncontrada,
                    "Debería retornar organización por defecto");

        } catch (SQLException | IOException e) {

            fail("Error al buscar organización inexistente por teléfono: " + e);
        }
    }

    @Test
    void obtenerTodasLasOrganizacionesConDatos() {

        List<OrganizacionVinculadaDTO> listaEsperada = List.of(

                new OrganizacionVinculadaDTO(IDS_ORGANIZACIONES_INSERTADAS.get(0), "Empresa 1", "Dirección 1",
                        "empresa1@test.com", "5551111111", 1),
                new OrganizacionVinculadaDTO(IDS_ORGANIZACIONES_INSERTADAS.get(1), "Empresa 2", "Dirección 2",
                        "empresa2@test.com", "5552222222", 1),
                new OrganizacionVinculadaDTO(IDS_ORGANIZACIONES_INSERTADAS.get(2), "Empresa 3", "Dirección 3",
                        "empresa3@test.com", "5553333333", 1)
        );

        try {

            List<OrganizacionVinculadaDTO> listaEncontrada = organizacionDAO.obtenerTodasLasOrganizaciones();

            for (int organizacionEncontrada = 0; organizacionEncontrada < listaEsperada.size(); organizacionEncontrada++) {

                assertTrue(listaEsperada.get(organizacionEncontrada).equals(listaEncontrada.get(organizacionEncontrada)),
                        "La organización esperada debería ser igual a la organización encontrada.");
            }

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void obtenerTodasLasOrganizacionesSinDatos() {

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection()) {

            conexionBaseDeDatos.prepareStatement("DELETE FROM organizacionvinculada").executeUpdate();

            List<OrganizacionVinculadaDTO> listaEncontrada = organizacionDAO.obtenerTodasLasOrganizaciones();
            assertTrue(listaEncontrada.isEmpty(), "La lista debería estar vacía si no hay organizaciones");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }
}