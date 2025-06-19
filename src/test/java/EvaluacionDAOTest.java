import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.*;
import logica.DTOs.*;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class EvaluacionDAOTest {

    private PeriodoDAO periodoDAO;
    private UsuarioDAO usuarioDAO;
    private AcademicoDAO academicoDAO;
    private GrupoDAO grupoDAO;
    private EstudianteDAO estudianteDAO;
    private AcademicoEvaluadorDAO academicoEvaluadorDAO;
    private EvaluacionDAO evaluacionDAO;
    private Connection conexionBaseDeDatos;

    private final List<Integer> IDS_USUARIOS_INSERTADOS = new ArrayList<>();
    private final List<Integer> IDS_GRUPOS_INSERTADOS = new ArrayList<>();
    private final List<String> MATRICULAS_INSERTADAS = new ArrayList<>();
    private final List<Integer> IDS_EVALUACIONES_INSERTADAS = new ArrayList<>();

    @BeforeEach
    void prepararDatosDePrueba() {

        periodoDAO = new PeriodoDAO();
        usuarioDAO = new UsuarioDAO();
        academicoDAO = new AcademicoDAO();
        grupoDAO = new GrupoDAO();
        estudianteDAO = new EstudianteDAO();
        academicoEvaluadorDAO = new AcademicoEvaluadorDAO();
        evaluacionDAO = new EvaluacionDAO();

        MATRICULAS_INSERTADAS.clear();
        IDS_USUARIOS_INSERTADOS.clear();
        IDS_GRUPOS_INSERTADOS.clear();
        IDS_EVALUACIONES_INSERTADAS.clear();

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            conexionBaseDeDatos.prepareStatement("DELETE FROM evaluacion").executeUpdate();
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

            int idEvaluacion1DTO = evaluacionDAO.crearNuevaEvaluacion( new EvaluacionDTO(0, "Buen desempeño",
                    10.0F, 1002, estudiante1DTO.getMatricula(), 1));
            int idEvaluacion2DTO = evaluacionDAO.crearNuevaEvaluacion( new EvaluacionDTO(0, "Excelente trabajo",
                    9.5F, 1002, estudiante2DTO.getMatricula(), 1));

            IDS_EVALUACIONES_INSERTADAS.add(idEvaluacion1DTO);
            IDS_EVALUACIONES_INSERTADAS.add(idEvaluacion2DTO);

        } catch (SQLException | IOException e) {

            fail("Error en @BeforeEach al preparar datos: " + e);
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            conexionBaseDeDatos.prepareStatement("DELETE FROM evaluacion").executeUpdate();
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
    void testCrearNuevaEvaluacionConDatosValidos() {

        try {

            EvaluacionDTO nuevaEvaluacion = new EvaluacionDTO(0, "Evaluación de prueba", 8.5F,
                    1002, "S23014103", 1);
            int idEvaluacionInsertada = evaluacionDAO.crearNuevaEvaluacion(nuevaEvaluacion);

            assertTrue(idEvaluacionInsertada > 0,
                    "La evaluación se insertó correctamente en la base de datos.");
            IDS_EVALUACIONES_INSERTADAS.add(idEvaluacionInsertada);

        } catch (SQLException | IOException e) {

            fail("Error al crear nueva evaluación: " + e);
        }
    }

    @Test
    void testCrearNuevaEvaluacionConEstudianteInvalido() {

        EvaluacionDTO nuevaEvaluacion = new EvaluacionDTO(0, "Evaluación de prueba", 8.5F,
                1002, "S23099999", 1);

        assertThrows(SQLException.class, () -> {
            evaluacionDAO.crearNuevaEvaluacion(nuevaEvaluacion);
        }, "Se esperaba una excepción al intentar insertar una evaluación con un estudiante no existente.");
    }

    @Test
    void testEliminarEvaluacionPorIDExistente() {

        int idEvaluacionAEliminar = IDS_EVALUACIONES_INSERTADAS.get(0);

        try {

            boolean evaluacionEliminada = evaluacionDAO.eliminarEvaluacionPorID(0, idEvaluacionAEliminar);

            assertTrue(evaluacionEliminada, "La evaluación se eliminó correctamente por ID.");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar evaluación por ID: " + e);
        }
    }

    @Test
    void testEliminarEvaluacionPorIDInexistente() {

        int idEvaluacionInexistente = 9999;

        try {

            boolean evaluacionEliminada = evaluacionDAO.eliminarEvaluacionPorID(1, idEvaluacionInexistente);
            assertFalse(evaluacionEliminada,
                    "No se esperaba que se eliminara una evaluación con un ID inexistente.");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar evaluación por ID inexistente: " + e);
        }
    }

    @Test
    void testEliminarEvaluacionDefinitivamentePorIDValido() {

        int idEvaluacionAEliminar = IDS_EVALUACIONES_INSERTADAS.get(0);

        try {

            boolean evaluacionEliminada = evaluacionDAO.eliminarEvaluacionDefinitivamente(idEvaluacionAEliminar);
            assertTrue(evaluacionEliminada, "La evaluación se eliminó definitivamente por ID.");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar evaluación definitivamente por ID: " + e);
        }
    }

    @Test
    void testEliminarEvaluacionDefinitivamentePorIDInvalido() {

        int idEvaluacionInvalido = 9999;

        try {

            boolean evaluacionEliminada = evaluacionDAO.eliminarEvaluacionDefinitivamente(idEvaluacionInvalido);
            assertFalse(evaluacionEliminada,
                    "No se esperaba que se eliminara una evaluación con un ID inexistente.");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar evaluación definitivamente por ID inválido: " + e);
        }
    }

    @Test
    void testModificarEvaluacionConDatosValidos() {

        int idEvaluacionAModificar = IDS_EVALUACIONES_INSERTADAS.get(0);

        try {

            EvaluacionDTO evaluacionModificada = new EvaluacionDTO(idEvaluacionAModificar, "Evaluación modificada",
                    9.0F, 1002, "S23014102", 1);
            boolean modificacionExitosa = evaluacionDAO.modificarEvaluacion(evaluacionModificada);

            assertTrue(modificacionExitosa, "La evaluación se modificó correctamente.");

        } catch (SQLException | IOException e) {

            fail("Error al modificar evaluación: " + e);
        }
    }

    @Test
    void testModificarEvaluacionConIDInvalido() {

        EvaluacionDTO evaluacionModificada = new EvaluacionDTO(9999, "Evaluación modificada",
                9.0F, 1002, "S23014102", 1);

        try {

            boolean modificacionExitosa = evaluacionDAO.modificarEvaluacion(evaluacionModificada);
            assertFalse(modificacionExitosa,
                    "No se esperaba que se modificara una evaluación con un ID inexistente.");

        } catch (SQLException | IOException e) {

            fail("Error al modificar evaluación con ID inválido: " + e);
        }
    }

    @Test
    void testModificarEvaluacionConAcademicoEvaluadorInvalido() {

        int idEvaluacionAModificar = IDS_EVALUACIONES_INSERTADAS.get(0);

        EvaluacionDTO evaluacionModificada = new EvaluacionDTO(idEvaluacionAModificar, "Evaluación modificada",
                9.0F, 9999, "S23014102", 1);

        assertThrows(SQLException.class, () -> {
            evaluacionDAO.modificarEvaluacion(evaluacionModificada);
        }, "Se esperaba una excepción al intentar modificar una evaluación con un académico evaluador no existente.");
    }

    @Test
    void testBuscarEvaluacionPorIDValido() {

        EvaluacionDTO evaluacionEsperada = new EvaluacionDTO(IDS_EVALUACIONES_INSERTADAS.get(0),
                "Buen desempeño", 10.0F, 1002, "S23014102", 1);

        try {

            EvaluacionDTO evaluacionEncontrada = evaluacionDAO.buscarEvaluacionPorID(IDS_EVALUACIONES_INSERTADAS.get(0));
            assertEquals(evaluacionEsperada, evaluacionEncontrada,
                    "Se esperaba encontrar la evaluación con el ID proporcionado.");

        } catch (SQLException | IOException e) {

            fail("Error al buscar evaluación por ID válido: " + e);
        }
    }

    @Test
    void testBuscarEvaluacionPorIDInvalido() {

        EvaluacionDTO evaluacionEsperada = new EvaluacionDTO(-1, " ", 0, 0,
                " ", 0);

        try {

            EvaluacionDTO evaluacionEncontrada = evaluacionDAO.buscarEvaluacionPorID(9999);
            assertEquals(evaluacionEsperada, evaluacionEncontrada,
                    "Se esperaba que no se encontrara una evaluación con un ID inexistente.");

        } catch (SQLException | IOException e) {

            fail("Error al buscar evaluación por ID inválido: " + e);
        }
    }

    @Test
    void listarEvaluacionesPorMatriculaEstudianteValida() {

        List<EvaluacionDTO> evaluacionesEsperadas = List.of(

                new EvaluacionDTO(IDS_EVALUACIONES_INSERTADAS.get(0), "Buen desempeño",
                        10.0F, 1002, "S23014102", 1)
        );

        try {

            List<EvaluacionDTO> evaluacionesEncontradas = evaluacionDAO.listarEvaluacionesPorIdEstudiante("S23014102");

            for (int evaluacionDTO = 0; evaluacionDTO < evaluacionesEsperadas.size(); evaluacionDTO++) {

                assertEquals(evaluacionesEsperadas.get(evaluacionDTO), evaluacionesEncontradas.get(evaluacionDTO),
                        "Las evaluaciones deben coincidir en posición " + evaluacionDTO);
            }

        } catch (SQLException | IOException e) {

            fail("Error al listar evaluaciones por matrícula de estudiante válida: " + e);
        }
    }

    @Test
    void listarEvaluacionesPorMatriculaEstudianteInvalida() {

        String matriculaInvalida = "S99999999";

        try {

            List<EvaluacionDTO> evaluaciones = evaluacionDAO.listarEvaluacionesPorIdEstudiante(matriculaInvalida);
            assertTrue(evaluaciones.isEmpty(),
                    "Se esperaba que no se encontraran evaluaciones para una matrícula inexistente.");

        } catch (SQLException | IOException e) {

            fail("Error al listar evaluaciones por matrícula de estudiante inválida: " + e);
        }
    }

    @Test
    void listarEvaluacionesPorMatriculaSinEvaluacionesEnLaBase() {

        try  (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection()) {

            PreparedStatement eliminarEvaluaciones = conexionBaseDeDatos
                    .prepareStatement("DELETE FROM evaluacion");
            eliminarEvaluaciones.executeUpdate();

            List<EvaluacionDTO> evaluaciones = evaluacionDAO.listarEvaluacionesPorIdEstudiante("S23014102");
            assertTrue(evaluaciones.isEmpty(),
                    "Se esperaba que no se encontraran evaluaciones para una matrícula inexistente.");

        } catch (SQLException | IOException e) {

            fail("Error al listar evaluaciones por matrícula de estudiante sin datos en la base: " + e);

        }
    }

}