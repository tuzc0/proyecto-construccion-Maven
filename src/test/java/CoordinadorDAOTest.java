import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.CoordinadorDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.CoordinadorDTO;
import logica.DTOs.UsuarioDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoordinadorDAOTest {

    private CoordinadorDAO coordinadorDAO;
    private UsuarioDAO usuarioDAO;
    private Connection conexionBaseDeDatos;

    private final List<Integer> NUMEROS_DE_PERSONAL_INSERTADOS = new ArrayList<>();
    private final List<Integer> IDS_USUARIOS_INSERTADOS = new ArrayList<>();

    @BeforeEach
    void prepararDatosDePrueba() {

        usuarioDAO = new UsuarioDAO();
        coordinadorDAO = new CoordinadorDAO();

        try {

            NUMEROS_DE_PERSONAL_INSERTADOS.clear();
            IDS_USUARIOS_INSERTADOS.clear();

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            for (int numero : List.of(1001, 1002, 1003, 55555, 99999, 88888)) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM coordinador WHERE numeroDePersonal = " + numero)
                        .executeUpdate();
            }

            conexionBaseDeDatos
                    .prepareStatement("DELETE FROM usuario WHERE idUsuario BETWEEN 1000 AND 2000")
                    .executeUpdate();

            UsuarioDTO usuarioCoordinador1 = new UsuarioDTO(0, "Nombre1", "Apellido1", 1);
            UsuarioDTO usuarioCoordinador2 = new UsuarioDTO(0, "Nombre2", "Apellido2", 1);
            UsuarioDTO usuarioCoordinador3 = new UsuarioDTO(0, "Nombre3", "Apellido3", 1);

            int idUsuario1 = usuarioDAO.insertarUsuario(usuarioCoordinador1);
            int idUsuario2 = usuarioDAO.insertarUsuario(usuarioCoordinador2);
            int idUsuario3 = usuarioDAO.insertarUsuario(usuarioCoordinador3);

            IDS_USUARIOS_INSERTADOS.addAll(List.of(idUsuario1, idUsuario2, idUsuario3));

            coordinadorDAO.insertarCoordinador(new CoordinadorDTO(1001, idUsuario1, "Nombre1", "Apellido1", 1));
            coordinadorDAO.insertarCoordinador(new CoordinadorDTO(1002, idUsuario2, "Nombre2", "Apellido2", 1));
            coordinadorDAO.insertarCoordinador(new CoordinadorDTO(1003, idUsuario3, "Nombre3", "Apellido3", 1));

            NUMEROS_DE_PERSONAL_INSERTADOS.addAll(List.of(1001, 1002, 1003));

        } catch (SQLException | IOException e) {

            fail("Error en @BeforeEach al preparar datos: " + e);
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try {

            for (int numeroDePersonal : NUMEROS_DE_PERSONAL_INSERTADOS) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM coordinador WHERE numeroDePersonal = " + numeroDePersonal)
                        .executeUpdate();
            }

            for (int idUsuario : IDS_USUARIOS_INSERTADOS) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM usuario WHERE idUsuario = " + idUsuario)
                        .executeUpdate();
            }

            conexionBaseDeDatos.close();

        } catch (SQLException e) {

            fail("Error en @AfterEach al limpiar datos: " + e);
        }
    }

    @Test
    void insertarCoordinadorConDatosValidos() {

        try {

            UsuarioDTO usuarioDTO = new UsuarioDTO(0, "CoordinadorTest", "ApellidoTest", 1);
            int idUsuario = usuarioDAO.insertarUsuario(usuarioDTO);
            IDS_USUARIOS_INSERTADOS.add(idUsuario);

            CoordinadorDTO nuevoCoordinador = new CoordinadorDTO(55555, idUsuario, "Nombre", "Apellido", 1);
            boolean resultadoInsertar = coordinadorDAO.insertarCoordinador(nuevoCoordinador);

            assertTrue(resultadoInsertar, "El coordinador debería ser insertado correctamente.");
            NUMEROS_DE_PERSONAL_INSERTADOS.add(55555);

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void insertarCoordinadorConDatosInvalidos() {

        CoordinadorDTO coordinadorInvalido = new CoordinadorDTO(99999, -1, "Nombre", "Apellido", 1);
        assertThrows(SQLException.class, () -> coordinadorDAO.insertarCoordinador(coordinadorInvalido),
                "Se esperaba una excepción debido a datos inválidos.");
    }

    @Test
    void eliminarCoordinadorPorNumeroDePersonalConDatosValidos() {

        int numeroDePersonal = 1002;

        try {

            boolean resultadoEliminar = coordinadorDAO.eliminarCoordinadorPorNumeroDePersonal(numeroDePersonal);
            assertTrue(resultadoEliminar, "El coordinador debería ser eliminado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void eliminarCoordinadorPorNumeroDePersonalConDatosInexistentes() {

        int numeroDePersonalInexistente = 88888;

        try {

            boolean resultadoEliminar = coordinadorDAO.eliminarCoordinadorPorNumeroDePersonal(numeroDePersonalInexistente);
            assertFalse(resultadoEliminar, "No debería eliminarse ningún coordinador inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void modificarCoordinadorConDatosValidos() {

        try {

            CoordinadorDTO coordinadorExistente = coordinadorDAO.buscarCoordinadorPorNumeroDePersonal(1003);
            int idUsuario = coordinadorExistente.getIdUsuario();

            CoordinadorDTO coordinadorActualizado = new CoordinadorDTO(1003, idUsuario, "NuevoNombre", "NuevoApellido", 1);
            boolean resultadoModificar = coordinadorDAO.modificarCoordinador(coordinadorActualizado);

            assertTrue(resultadoModificar, "El coordinador debería ser modificado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void modificarCoordinadorConDatosInvalidos() {

        CoordinadorDTO coordinadorInvalido = new CoordinadorDTO(1003, -100, "Nombre", "Apellido", 1);

        try {

            boolean resultadoModificar = coordinadorDAO.modificarCoordinador(coordinadorInvalido);
            assertFalse(resultadoModificar, "No debería modificarse un coordinador con datos inválidos.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void modificarCoordinadorInexistente() {

        CoordinadorDTO coordinadorInexistente = new CoordinadorDTO(77777, 9999, "X", "Y", 1);

        try {

            boolean resultadoModificar = coordinadorDAO.modificarCoordinador(coordinadorInexistente);
            assertFalse(resultadoModificar, "No se debería modificar un coordinador inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void buscarCoordinadorPorNumeroDePersonalConDatosValidos() {

        CoordinadorDTO coordinadorEsperado = new CoordinadorDTO(1001, IDS_USUARIOS_INSERTADOS.get(0), "Nombre1", "Apellido1", 1);

        try {

            CoordinadorDTO coordinadorEncontrado =
                    coordinadorDAO.buscarCoordinadorPorNumeroDePersonal(coordinadorEsperado.getNumeroDePersonal());
            assertEquals(coordinadorEsperado, coordinadorEncontrado, "El coordinador debería ser encontrado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void buscarCoordinadorPorNumeroDePersonalInexistente() {

        int numeroDePersonalInexistente = 99999;

        try {

            CoordinadorDTO coordinadorEncontrado =
                    coordinadorDAO.buscarCoordinadorPorNumeroDePersonal(numeroDePersonalInexistente);
            assertEquals(-1, coordinadorEncontrado.getNumeroDePersonal(), "No debería encontrarse un coordinador con ese número.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void buscarCoordinadorPorNumeroDePersonalInvalido() {

        try {

            CoordinadorDTO coordinadorObtenido = coordinadorDAO.buscarCoordinadorPorNumeroDePersonal(-1);
            CoordinadorDTO coordinadorEsperado = new CoordinadorDTO(-1, -1, "N/A", "N/A", 0);

            assertEquals(coordinadorEsperado, coordinadorObtenido,
                    "Debería retornar el coordinador por defecto para número inválido");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void buscarCoordinadorPorIDInvalido() {

        try {

            CoordinadorDTO coordinadorObtenido = coordinadorDAO.buscarCoordinadorPorID(-1);
            CoordinadorDTO coordinadorEsperado = new CoordinadorDTO(-1, -1, "N/A", "N/A", 0);

            assertEquals(coordinadorEsperado, coordinadorObtenido,
                    "Debería retornar el coordinador por defecto para ID inválido");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void buscarCoordinadorPorIDValido() {

        try {

            int idValido = IDS_USUARIOS_INSERTADOS.get(0);
            CoordinadorDTO coordinadorEsperado = new CoordinadorDTO(
                    1001,
                    idValido,
                    "Nombre1",
                    "Apellido1",
                    1
            );

            CoordinadorDTO coordinadorObtenido = coordinadorDAO.buscarCoordinadorPorID(idValido);
            assertEquals(coordinadorEsperado, coordinadorObtenido,
                    "Debería encontrar el coordinador exacto para ID válido");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void buscarCoordinadorPorIDInexistente() {

        try {

            int idInexistente = 9999;
            CoordinadorDTO coordinadorObtenido = coordinadorDAO.buscarCoordinadorPorID(idInexistente);
            CoordinadorDTO coordinadorEsperado = new CoordinadorDTO(-1, -1, "N/A", "N/A", 0);

            assertEquals(coordinadorEsperado, coordinadorObtenido,
                    "Debería retornar el coordinador por defecto para ID inexistente");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }
}