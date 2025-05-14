import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.EstudianteDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.EstudianteDTO;
import logica.DTOs.UsuarioDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EstudianteDAOTest {

    private static EstudianteDAO estudianteDAO;
    private static final List<Integer> idsUsuariosCreados = new java.util.ArrayList<>();

    @BeforeAll
    static void setUp() throws SQLException, IOException {

        estudianteDAO = new EstudianteDAO();
    }

    @BeforeEach
    void prepararDatosDePrueba() throws SQLException, IOException {

        try (var conexion = new ConexionBaseDeDatos().getConnection()) {

            String deleteSQL = "DELETE FROM estudiante WHERE matricula = ?";
            try (var stmt = conexion.prepareStatement(deleteSQL)) {
                stmt.setString(1, "A12347");
                stmt.executeUpdate();
            }

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            UsuarioDTO usuario = new UsuarioDTO(0, "Nombre", "Apellido", 1);
            int idUsuario = usuarioDAO.insertarUsuario(usuario);

            EstudianteDTO estudiante = new EstudianteDTO(1, "Juan", "Pérez", "A12347", idUsuario);
            estudianteDAO.insertarEstudiante(estudiante);
        }


    }

    @AfterAll
    public static void limpiarDatosPrueba() throws SQLException, IOException {

        List<String> estudiantesCreados = List.of("A12347", "A12341");


        for (String matricula : estudiantesCreados) {
            String deleteSQL = "DELETE FROM estudiante WHERE matricula = ?";
            try (var statement = new ConexionBaseDeDatos().getConnection().prepareStatement(deleteSQL)) {
                statement.setString(1, matricula);
                statement.executeUpdate();
            }
        }

        for (int idUsuario : idsUsuariosCreados) {
            String deleteSQL = "DELETE FROM usuario WHERE idUsuario = ?";
            try (var statement = new ConexionBaseDeDatos().getConnection().prepareStatement(deleteSQL)) {
                statement.setInt(1, idUsuario);
                statement.executeUpdate();
            }
        }

    }

    @Test
    @Order(1)
    void testInsertarEstudianteDatosValidos() throws SQLException, IOException {

        UsuarioDTO usuario = new UsuarioDTO(0, "Nombre", "Apellido", 1);
        int idUsuario = new UsuarioDAO().insertarUsuario(usuario);

        EstudianteDTO estudiante = new EstudianteDTO(1, "Juan", "Pérez", "A12341", idUsuario);
        boolean resultado = estudianteDAO.insertarEstudiante(estudiante);
        assertTrue(resultado, "El estudiante debería haberse insertado correctamente.");
        idsUsuariosCreados.add(idUsuario);

    }

    @Test
    @Order(2)
    void testEliminarEstudiantePorMatriculaDatosValidos() throws SQLException, IOException {

        boolean resultado = estudianteDAO.eliminarEstudiantePorMatricula(0, "A12347");
        assertTrue(resultado, "El estudiante debería haberse eliminado correctamente.");
    }

    @Test
    @Order(3)
    void testModificarEstudianteDatosValidos() throws SQLException, IOException {

        EstudianteDTO estudiante = new EstudianteDTO(1, "Juan", "Pérez Lopez", "A12347", 1);
        boolean resultado = estudianteDAO.modificarEstudiante(estudiante);
        assertTrue(resultado, "El estudiante debería haberse modificado correctamente.");
    }

    @Test
    @Order(4)
    void testBuscarEstudiantePorMatriculaDatosValidos() throws SQLException, IOException {

        EstudianteDTO estudiante = estudianteDAO.buscarEstudiantePorMatricula("A12347");
        assertEquals("A12347", estudiante.getMatricula(), "La matrícula del estudiante debería coincidir.");
    }

    @Test
    @Order(5)
    void testListarEstudiantesDatosValidos() throws SQLException, IOException {

        List<EstudianteDTO> estudiantes = estudianteDAO.listarEstudiantes();
        assertNotNull(estudiantes, "La lista de estudiantes no debería ser nula.");
    }
}