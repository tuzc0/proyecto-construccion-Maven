import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.UsuarioDTO;
import org.junit.jupiter.api.*;
import logica.DAOs.AcademicoDAO;
import logica.DTOs.AcademicoDTO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AcademicoDAOTest {

    private AcademicoDAO academicoDAO;
    private UsuarioDAO usuarioDAO;
    private final List<Integer> numerosAcademicosCreados = new java.util.ArrayList<>();
    private final List<Integer> idsUsuariosCreados = new java.util.ArrayList<>();

    @BeforeAll
    void setUp() {

        academicoDAO = new AcademicoDAO();
        usuarioDAO = new UsuarioDAO();
    }

    @BeforeEach
    void prepararDatosDePrueba() {

        try (var conexion = new ConexionBaseDeDatos().getConnection()) {

            for (int numero : List.of(1001, 1002, 1003)) {

                try (var stmt = conexion.prepareStatement("DELETE FROM academico WHERE numeroDePersonal = ?")) {

                    stmt.setInt(1, numero);
                    stmt.executeUpdate();
                }
            }

            UsuarioDTO usuario1 = new UsuarioDTO(0, "Nombre", "Prueba", 1);
            UsuarioDTO usuario2 = new UsuarioDTO(0, "Usuario2", "Apellido2", 1);
            UsuarioDTO usuario3 = new UsuarioDTO(0, "Usuario3", "Apellido3", 1);

            int id1 = usuarioDAO.insertarUsuario(usuario1);
            int id2 = usuarioDAO.insertarUsuario(usuario2);
            int id3 = usuarioDAO.insertarUsuario(usuario3);

            idsUsuariosCreados.add(id1);
            idsUsuariosCreados.add(id2);
            idsUsuariosCreados.add(id3);

            academicoDAO.insertarAcademico(new AcademicoDTO(1001, id1, "Usuario1", "Apellido1", 1));
            academicoDAO.insertarAcademico(new AcademicoDTO(1002, id2, "Usuario2", "Apellido2", 1));
            academicoDAO.insertarAcademico(new AcademicoDTO(1003, id3, "Usuario3", "Apellido3", 1));

            numerosAcademicosCreados.add(1001);
            numerosAcademicosCreados.add(1002);
            numerosAcademicosCreados.add(1003);

        } catch (SQLException | IOException e) {

            fail("Error al preparar los datos de prueba: " + e.getMessage());
        }
    }

    @AfterAll
    void limpiarBaseDeDatos() {

        try (var conexion = new ConexionBaseDeDatos().getConnection()) {

            numerosAcademicosCreados.add(22222);
            numerosAcademicosCreados.add(55555);

            for (int numero : numerosAcademicosCreados) {

                try (var stmt = conexion.prepareStatement("DELETE FROM academico WHERE numeroDePersonal = ?")) {

                    stmt.setInt(1, numero);
                    stmt.executeUpdate();
                }
            }

            for (int id : idsUsuariosCreados) {

                try (var stmt = conexion.prepareStatement("DELETE FROM usuario WHERE idUsuario = ?")) {

                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }
            }

        } catch (SQLException | IOException e) {

            fail("Error al limpiar la base de datos en @AfterAll: " + e.getMessage());
        }
    }


    @Test
    void testInsertarAcademicoDatosValidos() {

        try {

            UsuarioDTO usuario = new UsuarioDTO(0, "AcademicoTest", "ApellidoTest", 1);
            int idUsuario = usuarioDAO.insertarUsuario(usuario);
            idsUsuariosCreados.add(idUsuario);

            AcademicoDTO academico = new AcademicoDTO(55555, idUsuario, "Nombre", "Prueba", 1);
            boolean resultado = academicoDAO.insertarAcademico(academico);
            assertTrue(resultado, "El académico debería ser insertado correctamente.");
            numerosAcademicosCreados.add(55555);

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }


    @Test
    void testInsertarAcademicoDatosInvalidos() {

        AcademicoDTO academico = new AcademicoDTO(55555, -1, "Nombre", "Invalido", 1);

        try {

            boolean resultado = academicoDAO.insertarAcademico(academico);
            assertFalse(resultado, "El académico no debería ser insertado con datos inválidos.");

        } catch (SQLException | IOException e) {

            assertTrue(true);
        }
    }

    @Test
    void testBuscarAcademicoPorNumeroDePersonalDatosValidos() {

        int numeroDePersonal = 1001;

        try {

            AcademicoDTO academico = academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroDePersonal);
            assertEquals(numeroDePersonal, academico.getNumeroDePersonal(), "El número de personal debería coincidir.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarAcademicoPorNumeroDePersonalDatosInvalidos() {

        int numeroDePersonal = 99999;

        try {

            AcademicoDTO academico = academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroDePersonal);
            assertEquals(-1, academico.getNumeroDePersonal(), "No debería encontrarse un académico con ese número.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarAcademicoPorNumeroDePersonalDatosValidos() {

        int numeroDePersonal = 1002;

        try {

            boolean resultado = academicoDAO.eliminarAcademicoPorNumeroDePersonal(numeroDePersonal);
            assertTrue(resultado, "El académico debería ser eliminado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarAcademicoPorNumeroDePersonalDatosInvalidos() {

        int numeroDePersonal = 88888;

        try {

            boolean resultado = academicoDAO.eliminarAcademicoPorNumeroDePersonal(numeroDePersonal);
            assertFalse(resultado, "No debería eliminarse ningún académico inexistente.");

        } catch (SQLException | IOException e) {

            assertTrue(true);
        }
    }

    @Test
    void testModificarAcademicoDatosValidos() {

        try {

            AcademicoDTO academicoOriginal = academicoDAO.buscarAcademicoPorNumeroDePersonal(1003);
            int idUsuario = academicoOriginal.getIdUsuario();

            AcademicoDTO academicoModificado = new AcademicoDTO(1003, idUsuario, "NuevoNombre", "NuevoApellido", 1);
            boolean resultado = academicoDAO.modificarAcademico(academicoModificado);
            assertTrue(resultado, "El académico debería ser modificado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }


    @Test
    void testModificarAcademicoDatosInvalidos() {

        AcademicoDTO academico = new AcademicoDTO(1003, -100, "Nombre", "Apellido", 1);

        try {

            boolean resultado = academicoDAO.modificarAcademico(academico);
            assertFalse(resultado, "No debería modificarse un académico con datos inválidos.");

        } catch (SQLException | IOException e) {

            assertTrue(true);
        }
    }
}

