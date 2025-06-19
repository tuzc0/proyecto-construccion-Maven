import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.OrganizacionVinculadaDAO;
import logica.DAOs.RepresentanteDAO;
import logica.DTOs.OrganizacionVinculadaDTO;
import logica.DTOs.RepresentanteDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RepresentanteDAOTest {

    private RepresentanteDAO representanteDAO;
    private OrganizacionVinculadaDAO organizacionDAO;

    private final List<Integer> IDS_ORGANIZACIONES_INSERTADAS = new ArrayList<>();
    private final List<Integer> IDS_REPRESENTANTES_INSERTADOS = new ArrayList<>();

    @BeforeEach
    void prepararDatosDePrueba() {

        organizacionDAO = new OrganizacionVinculadaDAO();
        representanteDAO = new RepresentanteDAO();

        IDS_ORGANIZACIONES_INSERTADAS.clear();
        IDS_REPRESENTANTES_INSERTADOS.clear();

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection()) {

            for (int idOrganizacion : List.of(1, 2, 3)) {

                PreparedStatement eliminar = conexionBaseDeDatos.prepareStatement
                        ("DELETE FROM organizacionvinculada WHERE idOV = ?");
                eliminar.setInt(1, idOrganizacion);
                eliminar.executeUpdate();
            }

            for (int idRepresentante : List.of(1, 2)) {

                PreparedStatement eliminar = conexionBaseDeDatos.prepareStatement
                        ("DELETE FROM representante WHERE IDRepresentante = ?");
                eliminar.setInt(1, idRepresentante);
                eliminar.executeUpdate();
            }

            IDS_ORGANIZACIONES_INSERTADAS.add(organizacionDAO.crearNuevaOrganizacion
                    (new OrganizacionVinculadaDTO(1, "Empresa 1", "Dirección 1",
                            "empresa1@test.com", "5551111111", 1))
            );
            IDS_ORGANIZACIONES_INSERTADAS.add(organizacionDAO.crearNuevaOrganizacion
                    (new OrganizacionVinculadaDTO(2, "Empresa 2", "Dirección 2",
                            "empresa2@test.com", "5552222222", 1))
            );
            IDS_ORGANIZACIONES_INSERTADAS.add(organizacionDAO.crearNuevaOrganizacion
                    (new OrganizacionVinculadaDTO(3, "Empresa 3", "Dirección 3",
                            "empresa3@test.com", "5553333333", 1))
                    );

            representanteDAO.insertarRepresentante(
                    new RepresentanteDTO(1, "representate1@gmail.com", "5554444444",
                            "Representante 1", "Apellido 1", IDS_ORGANIZACIONES_INSERTADAS.get(0), 1)
            );
            representanteDAO.insertarRepresentante(
                    new RepresentanteDTO(2, "representante2@gmail.com", "5555555555",
                            "Representante 2", "Apellido 2", IDS_ORGANIZACIONES_INSERTADAS.get(1), 1)
            );

            IDS_REPRESENTANTES_INSERTADOS.add(1);
            IDS_REPRESENTANTES_INSERTADOS.add(2);

        } catch (SQLException | IOException e) {

            fail("Error en @BeforeEach al preparar datos: " + e);
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection()) {

            for (int idRepresentante : IDS_REPRESENTANTES_INSERTADOS) {

                PreparedStatement consultaPreparadaSQL = conexionBaseDeDatos.prepareStatement
                        ("DELETE FROM representante WHERE IDRepresentante = ?");
                consultaPreparadaSQL.setInt(1, idRepresentante);
                consultaPreparadaSQL.executeUpdate();
            }

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
    void insertarRepresentanteConDatosValidos() {

        RepresentanteDTO nuevoRepresentante = new RepresentanteDTO(
                3, "representante3@gmail.com", "5556666666", "Representante 3",
                "Apellido 3", IDS_ORGANIZACIONES_INSERTADAS.get(2), 1);

        try {

            boolean resultadoPrueba = representanteDAO.insertarRepresentante(nuevoRepresentante);
            assertTrue(resultadoPrueba, "Se pudo insertar el representante con datos válidos");
            IDS_REPRESENTANTES_INSERTADOS.add(3);

        } catch (SQLException | IOException e) {

            fail("Error al insertar representante con datos válidos: " + e);
        }
    }

    @Test
    void insertarRepresentanteConIDDeOrganizacionInvalido() {

        RepresentanteDTO nuevoRepresentante = new RepresentanteDTO(
                0, "invalid-email", "not-a-phone", null, null,
                0, 0);

        assertThrows(SQLException.class, () -> representanteDAO.insertarRepresentante(nuevoRepresentante));
    }

    @Test
    void insertarRepresentanteConTelefonoInvalido() {

        RepresentanteDTO nuevoRepresentante = new RepresentanteDTO(
                4, "representante04@test.com", "11111111111", "Claudio Josué",
                "González", IDS_ORGANIZACIONES_INSERTADAS.get(0), 1);

        assertThrows(SQLException.class, () -> representanteDAO.insertarRepresentante(nuevoRepresentante));
    }

    @Test
    void eliminarRepresentantePorIDExistente() {

        int idExistente = IDS_REPRESENTANTES_INSERTADOS.get(0);

        try {

            boolean resultadoPrueba = representanteDAO.eliminarRepresentantePorID(idExistente);
            assertTrue(resultadoPrueba);

        } catch (SQLException | IOException e) {

            fail("Error al eliminar representante existente: " + e);
        }
    }

    @Test
    void eliminarRepresentantePorIDInexistente() {

        int idInexistente = 999;

        try {

            boolean resultadoPrueba = representanteDAO.eliminarRepresentantePorID(idInexistente);
            assertFalse(resultadoPrueba);

        } catch (SQLException | IOException e) {

            fail("Error al eliminar representante inexistente: " + e);
        }
    }

    @Test
    void modificarRepresentanteConDatosValidos() {

        RepresentanteDTO representanteModificado = new RepresentanteDTO(1,
                "correoModificado@gmail.com", "5558888888", "Representante Nuevo",
                "Apellidos Nuevos", IDS_ORGANIZACIONES_INSERTADAS.get(0), 1);

        try {

            boolean resultadoPrueba = representanteDAO.modificarRepresentante(representanteModificado);
            assertTrue(resultadoPrueba, "Se pudo modificar el representante con datos válidos");

        } catch (SQLException | IOException e) {

            fail("Error al modificar representante con datos válidos: " + e);
        }
    }

    @Test
    void modificarRepresentanteConDatosInvalidos() {

        RepresentanteDTO representanteModificado = new RepresentanteDTO(
                1, "correo invalido", null, null, null,
                0, 0);

        assertThrows(SQLException.class, () -> representanteDAO.modificarRepresentante(representanteModificado));
    }

    @Test
    void modificarRepresentanteInexistente() {

        RepresentanteDTO representanteModificado = new RepresentanteDTO(
                999, "representate1@gmail.com", "5554444444",
                "Representante 1", "Apellido 1", IDS_ORGANIZACIONES_INSERTADAS.get(2), 1);

        try {

            boolean resultadoPrueba = representanteDAO.modificarRepresentante(representanteModificado);
            assertFalse(resultadoPrueba, "No se pudo modificar un representante inexistente");

        } catch (SQLException | IOException e) {

            fail("Error al modificar representante inexistente: " + e);
        }
    }

    @Test
    void modificarRepresentantePorIDConTelefonoInvalido() {

        RepresentanteDTO representanteModificado = new RepresentanteDTO(
                999, "representate1@gmail.com", "5554444444444",
                "Representante 1", "Apellido 1", IDS_ORGANIZACIONES_INSERTADAS.get(2), 1);

        try {

            boolean resultadoPrueba = representanteDAO.modificarRepresentante(representanteModificado);
            assertFalse(resultadoPrueba, "No se pudo modificar un representante con teléfono inválido");

        } catch (SQLException | IOException e) {

            fail("Error al modificar representante con teléfono inválido: " + e);
        }
    }

    @Test
    void buscarRepresentantePorIDExistente() {

        int idExistente = IDS_REPRESENTANTES_INSERTADOS.get(0);
        RepresentanteDTO representanteEsperado = new RepresentanteDTO(1, "representate1@gmail.com",
                "5554444444", "Representante 1", "Apellido 1", IDS_ORGANIZACIONES_INSERTADAS.get(0),
                1);

        try {

            RepresentanteDTO representanteEncontrado = representanteDAO.buscarRepresentantePorID(idExistente);
            assertEquals(representanteEsperado, representanteEncontrado,
                    "El representante encontrado coincide con el representante esperado.");;

        } catch (SQLException | IOException e) {

            fail("Error al buscar representante por ID existente: " + e);
        }
    }

    @Test
    void buscarRepresentantePorIDInexistente() {

        int idInexistente = 999;
        RepresentanteDTO representanteEsperado = new RepresentanteDTO(-1, "N/A", "N/A",
                "N/A", "N/A", 0, 0);


        try {

            RepresentanteDTO representanteEncontrado = representanteDAO.buscarRepresentantePorID(idInexistente);
            assertEquals(representanteEsperado,representanteEncontrado,
                    "El representante encontrado debe coinicidir con el representante esperado.");

        } catch (SQLException | IOException e) {

            fail("Error al buscar representante por ID inexistente: " + e);
        }
    }

    @Test
    void buscarRepresentantePorCorreoExistente() {

        String correoExistente = "representate1@gmail.com";
        RepresentanteDTO representanteEsperado = new RepresentanteDTO(1, "representate1@gmail.com",
                "5554444444", "Representante 1", "Apellido 1", IDS_ORGANIZACIONES_INSERTADAS.get(0),
                1);

        try {

            RepresentanteDTO representanteEncontrado = representanteDAO.buscarRepresentantePorCorreo(correoExistente);
            assertEquals(representanteEsperado, representanteEncontrado,
                    "El representante encontrado debe coincidir con el esperado.");

        } catch (SQLException | IOException e) {

            fail("Error al buscar representante por correo existente: " + e);
        }
    }

    @Test
    void buscarRepresentantePorCorreoInexistente() {

        String correoExistente = "correoInexistente@Invalido.test";
        RepresentanteDTO representanteEsperado = new RepresentanteDTO(-1, "N/A", "N/A",
                "N/A", "N/A", 0, 0);

        try {

            RepresentanteDTO representanteEncontrado = representanteDAO.buscarRepresentantePorCorreo(correoExistente);
            assertEquals(representanteEsperado, representanteEncontrado,
                    "El representante encontrado debe coincidir con el esperado.");

        } catch (SQLException | IOException e) {

            fail("Error al buscar representante por correo inexistente: " + e);
        }
    }

    @Test
    void buscarRepresentantePorTelefonoExistente() {

        String telefonoExistente = "5555555555";
        RepresentanteDTO representanteEsperado =  new RepresentanteDTO(2, "representante2@gmail.com",
                "5555555555", "Representante 2", "Apellido 2", IDS_ORGANIZACIONES_INSERTADAS.get(1),
                1);

        try {

            RepresentanteDTO representanteEncontrado = representanteDAO.buscarRepresentantePorTelefono(telefonoExistente);
            assertEquals(representanteEsperado, representanteEncontrado,
                    "El representante encontrado debe coincidir con el esperado.");

        } catch (SQLException | IOException e) {

            fail("Error al buscar representante por telefono existente: " + e);
        }
    }

    @Test
    void buscarRepresentantePorTelefonoInexistente() {

        String telefonoExistente = "9671350565";
        RepresentanteDTO representanteEsperado = new RepresentanteDTO(-1, "N/A", "N/A",
                "N/A", "N/A", 0, 0);

        try {

            RepresentanteDTO representanteEncontrado = representanteDAO.buscarRepresentantePorTelefono(telefonoExistente);
            assertEquals(representanteEsperado, representanteEncontrado,
                    "El representante encontrado debe coincidir con el esperado.");

        } catch (SQLException | IOException e) {

            fail("Error al buscar representante por telefono inexistente: " + e);
        }
    }

    @Test
    void obtenerTodosLosRepresentantesConDatosEnLaBase() {

        List<RepresentanteDTO> listaEsperada = List.of(
                new RepresentanteDTO(1, "representate1@gmail.com", "5554444444",
                        "Representante 1", "Apellido 1", IDS_ORGANIZACIONES_INSERTADAS.get(0), 1),
                new RepresentanteDTO(2, "representante2@gmail.com", "5555555555",
                        "Representante 2", "Apellido 2", IDS_ORGANIZACIONES_INSERTADAS.get(1), 1)
        );

        try {

            List<RepresentanteDTO> listaEncontrada = representanteDAO.obtenerTodosLosRepresentantes();

            for (int representanteEncontrado = 0; representanteEncontrado < listaEsperada.size(); representanteEncontrado++) {

                assertEquals(listaEsperada.get(representanteEncontrado), listaEncontrada.get(representanteEncontrado),
                        "El representante esperado debería ser igual al representante encontrado.");
            }

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void obtenerTodosLosRepresentantesSinDatosEnLaBase() {

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection()) {


            for (int idRepresentante : IDS_REPRESENTANTES_INSERTADOS) {

                PreparedStatement consultaPreparadaSQL = conexionBaseDeDatos.prepareStatement(
                        "DELETE FROM representante WHERE IDRepresentante = ?");
                consultaPreparadaSQL.setInt(1, idRepresentante);
                consultaPreparadaSQL.executeUpdate();
            }

            for (int idOrganizacion : IDS_ORGANIZACIONES_INSERTADAS) {

                PreparedStatement consultaPreparadaSQL = conexionBaseDeDatos.prepareStatement(
                        "DELETE FROM organizacionvinculada WHERE idOV = ?");
                consultaPreparadaSQL.setInt(1, idOrganizacion);
                consultaPreparadaSQL.executeUpdate();
            }

            List<RepresentanteDTO> representantes = representanteDAO.obtenerTodosLosRepresentantes();
            assertTrue(representantes.isEmpty(), "La lista de representantes debe estar vacía.");

        } catch (SQLException | IOException e) {

            fail("Error al obtener todos los representantes sin datos en la base: " + e);
        }
    }
}
