import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.UsuarioDTO;
import org.junit.jupiter.api.*;
import logica.DAOs.AcademicoDAO;
import logica.DTOs.AcademicoDTO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AcademicoDAOTest {

    private AcademicoDAO academicoDAO;
    private UsuarioDAO usuarioDAO;
    private final List<Integer> numerosDePersonalInsertados = new java.util.ArrayList<>();
    private final List<Integer> idsUsuariosInsertados = new java.util.ArrayList<>();

    @BeforeAll
    void inicializarDAOs() {

        academicoDAO = new AcademicoDAO();
        usuarioDAO = new UsuarioDAO();
    }

    @BeforeEach
    void prepararDatosDePrueba() {

        try {

            Connection conexion = new ConexionBaseDeDatos().getConnection();

            for (int numeroDePersonal : List.of(1001, 1002, 1003)) {

                conexion.prepareStatement("DELETE FROM academico WHERE numeroDePersonal = " + numeroDePersonal).executeUpdate();
            }

            UsuarioDTO primerUsuario = new UsuarioDTO(0, "Nombre", "Prueba", 1);
            UsuarioDTO segundoUsuario = new UsuarioDTO(0, "Usuario2", "Apellido2", 1);
            UsuarioDTO tercerUsuario = new UsuarioDTO(0, "Usuario3", "Apellido3", 1);

            int idPrimerUsuario = usuarioDAO.insertarUsuario(primerUsuario);
            int idSegundoUsuario = usuarioDAO.insertarUsuario(segundoUsuario);
            int idTercerUsuario = usuarioDAO.insertarUsuario(tercerUsuario);

            idsUsuariosInsertados.add(idPrimerUsuario);
            idsUsuariosInsertados.add(idSegundoUsuario);
            idsUsuariosInsertados.add(idTercerUsuario);

            academicoDAO.insertarAcademico(new AcademicoDTO(1001, idPrimerUsuario, "Usuario1", "Apellido1", 1));
            academicoDAO.insertarAcademico(new AcademicoDTO(1002, idSegundoUsuario, "Usuario2", "Apellido2", 1));
            academicoDAO.insertarAcademico(new AcademicoDTO(1003, idTercerUsuario, "Usuario3", "Apellido3", 1));

            numerosDePersonalInsertados.add(1001);
            numerosDePersonalInsertados.add(1002);
            numerosDePersonalInsertados.add(1003);

        } catch (SQLException | IOException e) {

            fail("Error al preparar los datos de prueba: " + e.getMessage());
        }
    }

    @AfterAll
    void limpiarBaseDeDatos() {

        try {

            Connection conexion = new ConexionBaseDeDatos().getConnection();

            numerosDePersonalInsertados.add(22222);
            numerosDePersonalInsertados.add(55555);

            for (int numeroDePersonal : numerosDePersonalInsertados) {

                conexion.prepareStatement("DELETE FROM academico WHERE numeroDePersonal = " + numeroDePersonal).executeUpdate();
            }

            for (int idUsuario : idsUsuariosInsertados) {

                conexion.prepareStatement("DELETE FROM usuario WHERE idUsuario = " + idUsuario).executeUpdate();
            }

        } catch (SQLException | IOException e) {

            fail("Error al limpiar la base de datos en @AfterAll: " + e.getMessage());
        }
    }

    @Test
    void testInsertarAcademicoConDatosValidos() {

        try {

            UsuarioDTO usuarioPrueba = new UsuarioDTO(0, "AcademicoTest", "ApellidoTest", 1);
            int idNuevoUsuario = usuarioDAO.insertarUsuario(usuarioPrueba);
            idsUsuariosInsertados.add(idNuevoUsuario);

            AcademicoDTO nuevoAcademico = new AcademicoDTO(55555, idNuevoUsuario, "Nombre", "Prueba", 1);
            boolean insercionExitosa = academicoDAO.insertarAcademico(nuevoAcademico);
            assertTrue(insercionExitosa, "El académico debería ser insertado correctamente.");
            numerosDePersonalInsertados.add(55555);

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testInsertarAcademicoConDatosInvalidos() {

        AcademicoDTO academicoInvalido = new AcademicoDTO(55555, -1, "Nombre", "Invalido", 1);

        try {

            boolean resultado = academicoDAO.insertarAcademico(academicoInvalido);
            assertFalse(resultado, "El académico no debería ser insertado con datos inválidos.");

        } catch (SQLException | IOException e) {

            assertTrue(true);
        }
    }

    @Test
    void testBuscarAcademicoPorNumeroDePersonalConDatosValidos() {

        int numeroDePersonalExistente = 1001;

        try {

            AcademicoDTO academicoEncontrado = academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroDePersonalExistente);
            assertEquals(numeroDePersonalExistente, academicoEncontrado.getNumeroDePersonal(), "El número de personal debería coincidir.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarAcademicoPorNumeroDePersonalConDatosInvalidos() {

        int numeroDePersonalInexistente = 99999;

        try {

            AcademicoDTO academicoEncontrado = academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroDePersonalInexistente);
            assertEquals(-1, academicoEncontrado.getNumeroDePersonal(), "No debería encontrarse un académico con ese número.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarAcademicoPorNumeroDePersonalConDatosValidos() {

        int numeroDePersonalAEliminar = 1002;

        try {

            boolean eliminadoCorrectamente = academicoDAO.eliminarAcademicoPorNumeroDePersonal(numeroDePersonalAEliminar);
            assertTrue(eliminadoCorrectamente, "El académico debería ser eliminado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarAcademicoPorNumeroDePersonalConDatosInvalidos() {

        int numeroDePersonalInexistente = 88888;

        try {

            boolean resultadoEliminacion = academicoDAO.eliminarAcademicoPorNumeroDePersonal(numeroDePersonalInexistente);
            assertFalse(resultadoEliminacion, "No debería eliminarse ningún académico inexistente.");

        } catch (SQLException | IOException e) {

            assertTrue(true);
        }
    }

    @Test
    void testModificarAcademicoConDatosValidos() {

        try {

            AcademicoDTO academicoExistente = academicoDAO.buscarAcademicoPorNumeroDePersonal(1003);
            int idUsuario = academicoExistente.getIdUsuario();

            AcademicoDTO academicoActualizado = new AcademicoDTO(1003, idUsuario, "NuevoNombre", "NuevoApellido", 1);
            boolean modificadoCorrectamente = academicoDAO.modificarAcademico(academicoActualizado);
            assertTrue(modificadoCorrectamente, "El académico debería ser modificado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testModificarAcademicoConDatosInvalidos() {

        AcademicoDTO academicoInvalido = new AcademicoDTO(1003, -100, "Nombre", "Apellido", 1);

        try {

            boolean resultado = academicoDAO.modificarAcademico(academicoInvalido);
            assertFalse(resultado, "No debería modificarse un académico con datos inválidos.");

        } catch (SQLException | IOException e) {

            assertTrue(true);
        }
    }
}
