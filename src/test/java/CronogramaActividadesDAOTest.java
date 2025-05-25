import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.CronogramaActividadesDAO;
import logica.DAOs.EstudianteDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.CronogramaActividadesDTO;
import logica.DTOs.EstudianteDTO;
import logica.DTOs.UsuarioDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CronogramaActividadesDAOTest {

    private static final String MATRICULA_ESTUDIANTE_TEST = "S20000001";
    private static final String MATRICULA_ESTUDIANTE_INEXISTENTE = "S99999999";

    private CronogramaActividadesDAO cronogramaDAO;
    private EstudianteDAO estudianteDAO;
    private UsuarioDAO usuarioDAO;
    private Connection conexionBaseDeDatos;

    private final List<Integer> IDS_CRONOGRAMAS_INSERTADOS = new ArrayList<>();
    private final List<String> MATRICULAS_ESTUDIANTES_INSERTADOS = new ArrayList<>();
    private final List<Integer> IDS_USUARIOS_INSERTADOS = new ArrayList<>();

    @BeforeAll
    static void configurarEntornoDePrueba() throws SQLException, IOException {

        Connection conexion = new ConexionBaseDeDatos().getConnection();
        Statement statement = conexion.createStatement();

        statement.executeUpdate("DELETE FROM cronogramaactividades WHERE idEstudiante = '" + MATRICULA_ESTUDIANTE_TEST + "'");
        statement.executeUpdate("DELETE FROM estudiante WHERE matricula = '" + MATRICULA_ESTUDIANTE_TEST + "'");
        statement.executeUpdate("DELETE FROM usuario WHERE nombre = 'EstudiantePruebaCronograma'");

        statement.close();
        conexion.close();
    }

    @BeforeEach
    void prepararDatosDePrueba() throws SQLException, IOException {

        cronogramaDAO = new CronogramaActividadesDAO();
        estudianteDAO = new EstudianteDAO();
        usuarioDAO = new UsuarioDAO();

        conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();
        IDS_CRONOGRAMAS_INSERTADOS.clear();
        MATRICULAS_ESTUDIANTES_INSERTADOS.clear();
        IDS_USUARIOS_INSERTADOS.clear();

        UsuarioDTO usuarioEstudiante = new UsuarioDTO(0, "EstudiantePruebaCronograma", "ApellidoPrueba", 1);
        int idUsuario = usuarioDAO.insertarUsuario(usuarioEstudiante);
        IDS_USUARIOS_INSERTADOS.add(idUsuario);

        EstudianteDTO estudianteDTO = new EstudianteDTO(idUsuario, "EstudiantePruebaCronograma",
                "ApellidoPrueba", MATRICULA_ESTUDIANTE_TEST, 1);
        estudianteDAO.insertarEstudiante(estudianteDTO);
        MATRICULAS_ESTUDIANTES_INSERTADOS.add(MATRICULA_ESTUDIANTE_TEST);

        CronogramaActividadesDTO cronogramaInicial = new CronogramaActividadesDTO(
                0, MATRICULA_ESTUDIANTE_TEST,
                "Actividades agosto", "Actividades septiembre",
                "Actividades octubre", "Actividades noviembre"
        );

        int idCronograma = cronogramaDAO.crearNuevoCronogramaDeActividades(cronogramaInicial);
        IDS_CRONOGRAMAS_INSERTADOS.add(idCronograma);
    }

    @AfterEach
    void limpiarDatosDePrueba() throws SQLException {

        for (int idCronograma : IDS_CRONOGRAMAS_INSERTADOS) {

            conexionBaseDeDatos.prepareStatement(
                    "DELETE FROM cronogramaactividades WHERE idCronograma = " + idCronograma
            ).executeUpdate();
        }

        for (String matricula : MATRICULAS_ESTUDIANTES_INSERTADOS) {

            conexionBaseDeDatos.prepareStatement(
                    "DELETE FROM estudiante WHERE matricula = '" + matricula + "'"
            ).executeUpdate();
        }

        for (int idUsuario : IDS_USUARIOS_INSERTADOS) {

            conexionBaseDeDatos.prepareStatement(
                    "DELETE FROM usuario WHERE idUsuario = " + idUsuario
            ).executeUpdate();
        }

        if (conexionBaseDeDatos != null) {

            conexionBaseDeDatos.close();
        }
    }

    @AfterAll
    static void limpiezaFinal() throws SQLException, IOException {

        Connection conexion = new ConexionBaseDeDatos().getConnection();
        Statement statement = conexion.createStatement();

        statement.executeUpdate("DELETE FROM cronogramaactividades WHERE idEstudiante = '" + MATRICULA_ESTUDIANTE_TEST + "'");
        statement.executeUpdate("DELETE FROM estudiante WHERE matricula = '" + MATRICULA_ESTUDIANTE_TEST + "'");
        statement.executeUpdate("DELETE FROM usuario WHERE nombre = 'EstudiantePruebaCronograma'");

        statement.close();
        conexion.close();
    }

    @Test
    void testCrearNuevoCronogramaDeActividadesConDatosValidos() throws SQLException, IOException {

        CronogramaActividadesDTO nuevoCronograma = new CronogramaActividadesDTO(
                0, MATRICULA_ESTUDIANTE_TEST,
                "Nuevas actividades agosto", "Nuevas actividades septiembre",
                "Nuevas actividades octubre", "Nuevas actividades noviembre"
        );

        int idCronogramaGenerado = cronogramaDAO.crearNuevoCronogramaDeActividades(nuevoCronograma);
        assertTrue(idCronogramaGenerado > 0, "Debería retornar un ID válido para el cronograma creado");

        IDS_CRONOGRAMAS_INSERTADOS.add(idCronogramaGenerado);
    }

    @Test
    void testCrearNuevoCronogramaDeActividadesConEstudianteInexistente() {

        CronogramaActividadesDTO cronogramaInvalido = new CronogramaActividadesDTO(
                0, MATRICULA_ESTUDIANTE_INEXISTENTE,
                "Actividades agosto", "Actividades septiembre",
                "Actividades octubre", "Actividades noviembre"
        );

        assertThrows(SQLException.class, () -> {
            cronogramaDAO.crearNuevoCronogramaDeActividades(cronogramaInvalido);
        }, "Debería lanzar excepción al intentar crear cronograma para estudiante inexistente");
    }

    @Test
    void testModificarCronogramaDeActividadesConDatosValidos() throws SQLException, IOException {

        int idCronogramaExistente = IDS_CRONOGRAMAS_INSERTADOS.get(0);


        CronogramaActividadesDTO modificaciones = new CronogramaActividadesDTO(
                idCronogramaExistente, MATRICULA_ESTUDIANTE_TEST,
                "Actividades agosto MODIFICADO", "Actividades septiembre MODIFICADO",
                "Actividades octubre MODIFICADO", "Actividades noviembre MODIFICADO"
        );

        boolean resultadoModificacion = cronogramaDAO.modificarCronogramaDeActividades(modificaciones);
        assertTrue(resultadoModificacion, "Debería retornar true indicando que la modificación fue exitosa");

        CronogramaActividadesDTO cronogramaModificado = cronogramaDAO.buscarCronogramaDeActividadesPorID(idCronogramaExistente);
        assertEquals("Actividades agosto MODIFICADO", cronogramaModificado.getAgostoFebrero());
        assertEquals("Actividades septiembre MODIFICADO", cronogramaModificado.getSeptiembreMarzo());
    }

    @Test
    void testModificarCronogramaDeActividadesInexistente() throws SQLException, IOException {

        CronogramaActividadesDTO cronogramaInexistente = new CronogramaActividadesDTO(
                -1, MATRICULA_ESTUDIANTE_TEST,
                "Actividades agosto", "Actividades septiembre",
                "Actividades octubre", "Actividades noviembre"
        );

        boolean resultadoModificacion = cronogramaDAO.modificarCronogramaDeActividades(cronogramaInexistente);
        assertFalse(resultadoModificacion, "Debería retornar false al intentar modificar cronograma inexistente");
    }

    @Test
    void testBuscarCronogramaDeActividadesPorIDConDatosValidos() throws SQLException, IOException {

        int idCronogramaExistente = IDS_CRONOGRAMAS_INSERTADOS.get(0);

        CronogramaActividadesDTO cronogramaEncontrado =
                cronogramaDAO.buscarCronogramaDeActividadesPorID(idCronogramaExistente);
        assertEquals(idCronogramaExistente, cronogramaEncontrado.getIDCronograma(),
                "Debería encontrar el cronograma con el ID especificado");
        assertEquals(MATRICULA_ESTUDIANTE_TEST, cronogramaEncontrado.getMatriculaEstudiante(),
                "La matrícula del estudiante debería coincidir");
    }

    @Test
    void testBuscarCronogramaDeActividadesPorIDInexistente() throws SQLException, IOException {

        int idCronogramaInexistente = -1;

        CronogramaActividadesDTO cronogramaEncontrado =
                cronogramaDAO.buscarCronogramaDeActividadesPorID(idCronogramaInexistente);
        assertEquals(-1, cronogramaEncontrado.getIDCronograma(),
                "Debería retornar un DTO con ID -1 indicando que no se encontró");
    }
}