import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.*;
import logica.DTOs.*;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class AutoevaluacionDAOTest {

    private PeriodoDAO periodoDAO;
    private UsuarioDAO usuarioDAO;
    private AcademicoDAO academicoDAO;
    private GrupoDAO grupoDAO;
    private EstudianteDAO estudianteDAO;
    private AcademicoEvaluadorDAO academicoEvaluadorDAO;
    private AutoevaluacionDAO autoevaluacionDAO;
    private Connection conexionBaseDeDatos;

    private final List<Integer> IDS_USUARIOS_INSERTADOS = new ArrayList<>();
    private final List<Integer> IDS_GRUPOS_INSERTADOS = new ArrayList<>();
    private final List<String> MATRICULAS_INSERTADAS = new ArrayList<>();
    private final List<Integer> IDS_AUTOEVALUACIONES_INSERTADAS = new ArrayList<>();

    @BeforeEach
    void prepararDatosDePrueba() {

        periodoDAO = new PeriodoDAO();
        usuarioDAO = new UsuarioDAO();
        academicoDAO = new AcademicoDAO();
        grupoDAO = new GrupoDAO();
        estudianteDAO = new EstudianteDAO();
        academicoEvaluadorDAO = new AcademicoEvaluadorDAO();
        autoevaluacionDAO = new AutoevaluacionDAO();

        MATRICULAS_INSERTADAS.clear();
        IDS_USUARIOS_INSERTADOS.clear();
        IDS_GRUPOS_INSERTADOS.clear();
        IDS_AUTOEVALUACIONES_INSERTADAS.clear();

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            conexionBaseDeDatos.prepareStatement("DELETE FROM autoevaluacion").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM estudiante").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM grupo").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM academicoevaluador").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM academico").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM periodo").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM usuario").executeUpdate();

            periodoDAO.crearNuevoPeriodo(new PeriodoDTO(1, "Periodo 2025", 1,
                    Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31")));

            UsuarioDTO usuarioAcademicoDTO = new UsuarioDTO(0, "Nombre", "Prueba", 1);
            UsuarioDTO usuarioEstudiante1DTO = new UsuarioDTO(0, "Usuario2", "Apellido2", 1);
            UsuarioDTO usuarioEstudiante2DTO = new UsuarioDTO(0, "Usuario3", "Apellido3", 1);
            UsuarioDTO usuarioEstudiante3DTO = new UsuarioDTO(0, "Usuario4", "Apellido4", 1);
            UsuarioDTO usuarioAcademcioEvaluadorDTO = new UsuarioDTO(0, "Usuario Evaluador",
                    "Apellido Evaluador", 1);

            int idUsuarioAcademico = usuarioDAO.insertarUsuario(usuarioAcademicoDTO);
            int idUsuarioEstudiante1 = usuarioDAO.insertarUsuario(usuarioEstudiante1DTO);
            int idUsuarioEstudiante2 = usuarioDAO.insertarUsuario(usuarioEstudiante2DTO);
            int idUsuarioEstudiante3 = usuarioDAO.insertarUsuario(usuarioEstudiante3DTO);
            int idUsuarioAcademicoEvaluador = usuarioDAO.insertarUsuario(usuarioAcademcioEvaluadorDTO);

            IDS_USUARIOS_INSERTADOS.add(idUsuarioAcademico);
            IDS_USUARIOS_INSERTADOS.add(idUsuarioEstudiante1);
            IDS_USUARIOS_INSERTADOS.add(idUsuarioEstudiante2);
            IDS_USUARIOS_INSERTADOS.add(idUsuarioEstudiante3);
            IDS_USUARIOS_INSERTADOS.add(idUsuarioAcademicoEvaluador);

            academicoDAO.insertarAcademico(new AcademicoDTO(1001, idUsuarioAcademico,
                    usuarioAcademicoDTO.getNombre(), usuarioAcademicoDTO.getApellido(),
                    usuarioAcademicoDTO.getEstado()));

            academicoEvaluadorDAO.insertarAcademicoEvaluador(new AcademicoEvaluadorDTO(1002, idUsuarioAcademicoEvaluador,
                    usuarioAcademcioEvaluadorDTO.getNombre(), usuarioAcademcioEvaluadorDTO.getApellido(),
                    usuarioAcademcioEvaluadorDTO.getEstado()));

            grupoDAO.crearNuevoGrupo(new GrupoDTO(40776, "Grupo 1", 1001, 1, 1));
            grupoDAO.crearNuevoGrupo(new GrupoDTO(40789, "Grupo 2", 1001, 1, 1));

            IDS_GRUPOS_INSERTADOS.add(40776);
            IDS_GRUPOS_INSERTADOS.add(40789);

            EstudianteDTO estudiante1DTO = new EstudianteDTO(idUsuarioEstudiante1,
                    usuarioEstudiante1DTO.getNombre(), usuarioEstudiante1DTO.getApellido(),
                    "S23014102", usuarioEstudiante1DTO.getEstado(), 0, 40776, 10);

            EstudianteDTO estudiante2DTO = new EstudianteDTO(idUsuarioEstudiante2,
                    usuarioEstudiante2DTO.getNombre(), usuarioEstudiante2DTO.getApellido(),
                    "S23014095", usuarioEstudiante2DTO.getEstado(), 0, 40789, 7.5F);

            EstudianteDTO estudiante3DTO = new EstudianteDTO(idUsuarioEstudiante3,
                    usuarioEstudiante3DTO.getNombre(), usuarioEstudiante3DTO.getApellido(),
                    "S23014103", usuarioEstudiante3DTO.getEstado(), 0, 40776, 5.0F);

            estudianteDAO.insertarEstudiante(estudiante1DTO);
            estudianteDAO.insertarEstudiante(estudiante2DTO);
            estudianteDAO.insertarEstudiante(estudiante3DTO);

            MATRICULAS_INSERTADAS.add(estudiante1DTO.getMatricula());
            MATRICULAS_INSERTADAS.add(estudiante2DTO.getMatricula());
            MATRICULAS_INSERTADAS.add(estudiante3DTO.getMatricula());

            int idAutoevaluacion1DTO = autoevaluacionDAO.crearNuevaAutoevaluacion( new AutoevaluacionDTO(0,
                    Timestamp.valueOf("2023-10-01 10:00:00"), "Aula 101", 8.5F,
                    "S23014102", 1));
            int idAutoevaluacion2DTO = autoevaluacionDAO.crearNuevaAutoevaluacion( new AutoevaluacionDTO(0,
                    Timestamp.valueOf("2023-10-02 14:30:00"), "Laboratorio 202", 9.0F,
                    "S23014095", 1
            ));

            IDS_AUTOEVALUACIONES_INSERTADAS.add(idAutoevaluacion1DTO);
            IDS_AUTOEVALUACIONES_INSERTADAS.add(idAutoevaluacion2DTO);

        } catch (SQLException | IOException e) {

            fail("Error en @BeforeEach al preparar datos: " + e);
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            conexionBaseDeDatos.prepareStatement("DELETE FROM autoevaluacion").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM estudiante").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM grupo").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM academicoevaluador").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM academico").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM periodo").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM usuario").executeUpdate();

            conexionBaseDeDatos.close();

        } catch (SQLException | IOException e) {

            fail("Error en @AfterEach al limpiar datos: " + e);
        }
    }

    @Test
    void crearNuevaAutoevaluacionConDatosValidos() {

        try {

            AutoevaluacionDTO autoevaluacionDTO = new AutoevaluacionDTO(0,
                    Timestamp.valueOf("2023-10-03 09:00:00"), "Aula 102", 9.5F,
                    MATRICULAS_INSERTADAS.get(2), 1);
            int idAutoevaluacionCreada = autoevaluacionDAO.crearNuevaAutoevaluacion(autoevaluacionDTO);

            assertTrue(idAutoevaluacionCreada > 0,
                    "ID de autoevaluación creada debe ser mayor que 0");

        } catch (SQLException | IOException e) {

            fail("Error al crear nueva autoevaluación con datos válidos: " + e);
        }
    }

    @Test
    void crearNuevaAutoevaluacionConMatriculaInexistente() {

        AutoevaluacionDTO autoevaluacionDTO = new AutoevaluacionDTO(0,
                Timestamp.valueOf("2023-10-05 11:00:00"), "Aula 104", 8.0F,
                "S99999999", 1);

        assertThrows(SQLException.class, () -> {
            autoevaluacionDAO.crearNuevaAutoevaluacion(autoevaluacionDTO);
        }, "Se esperaba SQLException al crear autoevaluación con matrícula inexistente");
    }

    @Test
    void eliminarAutoevaluacionPorIDExistente() {

        try {

            int idAutoevaluacionAEliminar = IDS_AUTOEVALUACIONES_INSERTADAS.get(0);
            boolean resultadoPrueba = autoevaluacionDAO.eliminarAutoevaluacionPorID(idAutoevaluacionAEliminar);

            assertTrue(resultadoPrueba, "La autoevaluación debería haberse eliminado correctamente");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar autoevaluación por ID existente: " + e);
        }
    }

    @Test
    void eliminarAutoevaluacionPorIDInexistente() {

        try {

            int idAutoevaluacionInexistente = 9999;
            boolean resultadoPrueba = autoevaluacionDAO.eliminarAutoevaluacionPorID(idAutoevaluacionInexistente);
            assertFalse(resultadoPrueba, "No debería eliminar una autoevaluación con ID inexistente");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar autoevaluación por ID inexistente: " + e);
        }
    }

    @Test
    void modificarAutoevaluacionConDatosValidos() {

        try {

            AutoevaluacionDTO autoevaluacionAModificar = new AutoevaluacionDTO(IDS_AUTOEVALUACIONES_INSERTADAS.get(0),
                    Timestamp.valueOf("2023-10-06 12:00:00"), "Aula 105", 9.0F,
                    MATRICULAS_INSERTADAS.get(0), 1);
            boolean resultadoPrueba = autoevaluacionDAO.modificarAutoevaluacion(autoevaluacionAModificar);

            assertTrue(resultadoPrueba, "La autoevaluación debería haberse modificado correctamente");

        } catch (SQLException | IOException e) {

            fail("Error al modificar autoevaluación con datos válidos: " + e);
        }
    }

    @Test
    void modificarAutoevaluacionConIDInexistente() {

        AutoevaluacionDTO autoevaluacionAModificar = new AutoevaluacionDTO(9999,
                Timestamp.valueOf("2023-10-07 13:00:00"), "Aula 106", 8.5F,
                MATRICULAS_INSERTADAS.get(1), 1);

        try {

            boolean resultadoPrueba = autoevaluacionDAO.modificarAutoevaluacion(autoevaluacionAModificar);
            assertFalse(resultadoPrueba, "Se espera que no se modifique la autoevaluacion con un ID invalido");

        } catch (SQLException | IOException e) {

            fail("Error al modificar con datos inexistentes" +e );
        }
    }

    @Test
    void buscarAutoevaluacionPorIDExistente() {

        AutoevaluacionDTO autoevalucionEsperada = new AutoevaluacionDTO(IDS_AUTOEVALUACIONES_INSERTADAS.get(0),
                Timestamp.valueOf("2023-10-01 10:00:00"), "Aula 101", 8.5F,
                MATRICULAS_INSERTADAS.get(0), 1);

        try {

            AutoevaluacionDTO autoevaluacionEncontrada = autoevaluacionDAO.buscarAutoevaluacionPorID(
                    IDS_AUTOEVALUACIONES_INSERTADAS.get(0));

            assertEquals(autoevalucionEsperada, autoevaluacionEncontrada,
                    "La autoevaluación encontrada debería ser igual a la esperada");

        } catch (SQLException | IOException e) {

            fail("Error al buscar autoevaluación por ID existente: " + e);
        }
    }

    @Test
    void buscarAutoevaluacionPorIDInexistente() {

        AutoevaluacionDTO autoevaluacionEsperada = new AutoevaluacionDTO(-1, null, null,
                -1, null, -1);

        try {

            AutoevaluacionDTO autoevaluacionEncontrada = autoevaluacionDAO.buscarAutoevaluacionPorID(9999);

            assertEquals(autoevaluacionEsperada, autoevaluacionEncontrada,
                    "La autoevaluación encontrada debería ser igual a la esperada para ID inexistente");

        } catch (SQLException | IOException e) {

            fail("Error al buscar autoevaluación por ID inexistente: " + e);
        }
    }

    @Test
    void eliminarAutoevaluacionDefinitivamentePorIDExistente() {

        AutoevaluacionDTO autoevaluacionEsperada = new AutoevaluacionDTO(-1, null, null,
                -1, null, -1);

        try {

            int idAutoevaluacionAEliminar = IDS_AUTOEVALUACIONES_INSERTADAS.get(0);
            autoevaluacionDAO.eliminarAutoevaluacionDefinitivamentePorID(idAutoevaluacionAEliminar);

            AutoevaluacionDTO autoevaluacionEncontrada =
                    autoevaluacionDAO.buscarAutoevaluacionPorID(idAutoevaluacionAEliminar);
            assertEquals(autoevaluacionEsperada, autoevaluacionEncontrada,
                    "La autoevaluación debería haber sido eliminada definitivamente");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar autoevaluación definitivamente por ID existente: " + e);
        }
    }

    @Test
    void buscarAutoevaluacionPorMatriculaExistente() {

        AutoevaluacionDTO autoevaluacionEsperada = new AutoevaluacionDTO(IDS_AUTOEVALUACIONES_INSERTADAS.get(0),
                Timestamp.valueOf("2023-10-01 10:00:00"), "Aula 101", 8.5F,
                "S23014102", 1);

        try {

            AutoevaluacionDTO autoevaluacionesEncontradas =
                    autoevaluacionDAO.buscarAutoevaluacionPorMatricula(MATRICULAS_INSERTADAS.get(0));
            assertEquals(autoevaluacionEsperada, autoevaluacionesEncontradas,
                    "La autoevaluación encontrada debería ser igual a la esperada");

        } catch (SQLException | IOException e) {

            fail("Error al buscar autoevaluación por matrícula existente: " + e);
        }
    }

    @Test
    void buscarAutoevaluacionPorMatriculaInexistente() {

        AutoevaluacionDTO autoevaluacionEsperada = new AutoevaluacionDTO(-1, null, " ",
                -1, " ", -1);

        try {

            AutoevaluacionDTO autoevaluacionesEncontradas =
                    autoevaluacionDAO.buscarAutoevaluacionPorMatricula("S99999999");

            assertEquals(autoevaluacionEsperada, autoevaluacionesEncontradas,
                    "La autoevaluación encontrada debería ser igual a la esperada para matrícula inexistente");

        } catch (SQLException | IOException e) {

            fail("Error al buscar autoevaluación por matrícula inexistente: " + e);
        }
    }

    @Test
    void buscarAutoevaluacionPorMatriculaSinAuotoevaluacionesEnLaBase() {

        AutoevaluacionDTO autoevaluacionEsperada = new AutoevaluacionDTO(-1, null, " ",
                -1, " ", -1);

        try {

            PreparedStatement eliminarEvaluaciones = conexionBaseDeDatos
                    .prepareStatement("DELETE FROM autoevaluacion");
            eliminarEvaluaciones.executeUpdate();

            AutoevaluacionDTO autoevaluacionesEncontradas =
                    autoevaluacionDAO.buscarAutoevaluacionPorMatricula(MATRICULAS_INSERTADAS.get(0));

            assertEquals( autoevaluacionEsperada, autoevaluacionesEncontradas,
                    "La autoevaluación encontrada debería ser igual a la esperada");

        } catch (SQLException | IOException e) {

            fail("Error al buscar autoevaluación por matrícula sin autoevaluaciones en la base de datos: " + e);
        }
    }
}
