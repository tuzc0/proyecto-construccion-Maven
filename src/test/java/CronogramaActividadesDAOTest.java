import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.*;
import logica.DTOs.*;
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import utilidadesPruebas.UtilidadesConsola;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CronogramaActividadesDAOTest {

    private static final Logger LOGGER = LogManager.getLogger(CronogramaActividadesDAOTest.class);

    private PeriodoDAO periodoDAO;
    private UsuarioDAO usuarioDAO;
    private AcademicoDAO academicoDAO;
    private GrupoDAO grupoDAO;
    private OrganizacionVinculadaDAO organizacionDAO;
    private RepresentanteDAO representanteDAO;
    private ProyectoDAO proyectoDAO;
    private EstudianteDAO estudianteDAO;
    private AcademicoEvaluadorDAO academicoEvaluadorDAO;
    private ManejadorExcepciones manejadorExcepciones;
    private CronogramaActividadesDAO cronogramaDAO;
    private static Connection conexionBaseDeDatos;

    private final List<Integer> IDS_USUARIOS_INSERTADOS = new ArrayList<>();
    private final List<Integer> IDS_GRUPOS_INSERTADOS = new ArrayList<>();
    private final List<Integer> IDS_PROYECTOS_INSERTADOS = new ArrayList<>();
    private final List<String> MATRICULAS_INSERTADAS = new ArrayList<>();
    private final List<Integer> IDS_CRONOGRAMAS_INSERTADOS = new ArrayList<>();

    @BeforeAll
    static void inicializarConexion() {

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

        } catch (SQLException e) {

            String estadoSQL = e.getSQLState();

            switch (estadoSQL) {

                case "08S01" -> {

                    LOGGER.error("Error de conexión con la base de datos: " + e);
                    System.out.println(
                            "Error de conexión :" +
                                    "No se pudo establecer una conexión con la base de datos: " +
                                    "La base de datos se encuentra desactivada o hay un problema de red."
                    );
                }

                case "42000" -> {

                    LOGGER.error("Error de sintaxis SQL o base de datos no existe: " + e);
                    System.out.println(
                            "Error de conexión :" +
                                    "No se pudo establecer conexión con la base de datos: " +
                                    "La base de datos no existe o hay un error de sintaxis en la consulta."
                    );
                }

                case "28000" -> {

                    LOGGER.error("Credenciales inválidas: " + e);
                    System.out.println(
                            "Credenciales inválidas: " +
                                    "Usuario o contraseña incorrectos: " +
                                    "Verifique los datos de acceso a la base de datos."
                    );
                }

                case "23000" -> {

                    LOGGER.error("Violación de restricción de integridad: " + e);
                    System.out.println(
                            "Dato duplicado o relación inválida: " +
                                    "No se puede completar la operación: " +
                                    "Verifique que los datos no estén repetidos o que las relaciones sean válidas."
                    );
                }

                case "42S02" -> {

                    LOGGER.error("Tabla no encontrada: " + e);
                    System.out.println(
                            "Tabla inexistente: " +
                                    "No se encontró una tabla necesaria para ejecutar la operación: " +
                                    "Verifique que todas las tablas estén correctamente creadas."
                    );
                }

                case "42S22" -> {

                    LOGGER.error("Columna no encontrada: " + e);
                    System.out.println(
                            "Columna inexistente: " +
                                    "No se encontró una columna requerida: " +
                                    "Revise los nombres de las columnas en su consulta."
                    );
                }

                default -> {

                    LOGGER.error("SQLState desconocido (" + estadoSQL + "): " + e);
                    System.out.println(
                            "Error desconocido: " +
                                    "Se produjo un error inesperado al acceder a la base de datos: " +
                                    "Contacte a soporte técnico."
                    );
                }
            }

        } catch (IOException e) {

            if (e instanceof FileNotFoundException) {

                LOGGER.error("Archivo no encontrado: " + e);
                System.out.println(
                        "Archivo no encontrado: " +
                                "No se pudo encontrar el archivo especificado: " +
                                "Verifique que el archivo exista."
                );

            } else if (e instanceof EOFException) {

                LOGGER.error("Fin inesperado del archivo: " + e);
                System.out.println(
                        "Lectura incompleta: " +
                                "Se alcanzó el final del archivo antes de lo esperado: " +
                                "El archivo puede estar incompleto o dañado."
                );

            } else if (e instanceof ConnectException) {

                LOGGER.error("Error de conexión de red: " + e);
                System.out.println(
                        "Fallo de conexión: " +
                                "No se pudo conectar con el recurso: " +
                                "Revise su conexión o intente más tarde."
                );
            } else {

                LOGGER.error("Error de E/S desconocido: " + e);
                System.out.println(
                        "Error de entrada/salida: " +
                                "Se produjo un error inesperado: " +
                                "Verifique los recursos utilizados o contacte soporte."
                );
            }

        } catch (Exception e) {

            fail("Error durante el @BeforeAll: " + e.getMessage(), e);
        }
    }

    @BeforeEach
    void prepararDatosDePrueba() {

        periodoDAO = new PeriodoDAO();
        usuarioDAO = new UsuarioDAO();
        academicoDAO = new AcademicoDAO();
        grupoDAO = new GrupoDAO();
        organizacionDAO = new OrganizacionVinculadaDAO();
        representanteDAO = new RepresentanteDAO();
        proyectoDAO = new ProyectoDAO();
        estudianteDAO = new EstudianteDAO();
        academicoEvaluadorDAO = new AcademicoEvaluadorDAO();
        cronogramaDAO = new CronogramaActividadesDAO();
        IGestorAlertas utilidadesConsola = new UtilidadesConsola();
        manejadorExcepciones = new ManejadorExcepciones(utilidadesConsola, LOGGER);

        MATRICULAS_INSERTADAS.clear();
        IDS_USUARIOS_INSERTADOS.clear();
        IDS_GRUPOS_INSERTADOS.clear();
        IDS_PROYECTOS_INSERTADOS.clear();
        IDS_CRONOGRAMAS_INSERTADOS.clear();

        try {

            conexionBaseDeDatos.prepareStatement("DELETE FROM cronograma").execute();
            conexionBaseDeDatos.prepareStatement("DELETE FROM estudiante").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM grupo").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM proyecto").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM academicoevaluador").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM academico").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM representante").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM organizacionvinculada").executeUpdate();
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

            int idOrganizacion = organizacionDAO.crearNuevaOrganizacion(
                    new OrganizacionVinculadaDTO(1, "Empresa 1", "Dirección 1",
                            "empresa1@test.com", "5551111111", 1)
            );

            representanteDAO.insertarRepresentante(
                    new RepresentanteDTO(1, "representante1@gmail.com", "5554444444",
                            "Representante 1", "Apellido 1", idOrganizacion, 1)
            );

            int idProyecto1 = proyectoDAO.crearNuevoProyecto(
                    new ProyectoDTO(
                            0,
                            "Proyecto Innovador",
                            "Desarrollar una solución tecnológica avanzada",
                            "Implementar funcionalidades clave",
                            "Mejorar la experiencia del usuario",
                            "Metodología ágil",
                            "Recursos tecnológicos y humanos",
                            "Desarrollo, pruebas y despliegue",
                            "Responsabilidad del equipo de desarrollo",
                            "6 meses",
                            1,
                            1,
                            "Proyecto para optimizar procesos internos",
                            50,
                            200,
                            5
                    )
            );

            int idProyecto2 = proyectoDAO.crearNuevoProyecto(
                    new ProyectoDTO(
                            0,
                            "Proyecto Educativo",
                            "Fomentar el aprendizaje interactivo en estudiantes",
                            "Desarrollar una plataforma educativa",
                            "Promover el uso de tecnología en la educación",
                            "Metodología basada en diseño centrado en el usuario",
                            "Recursos digitales y soporte técnico",
                            "Análisis, diseño, desarrollo y evaluación",
                            "Responsabilidad del equipo de diseño y desarrollo",
                            "12 meses",
                            1,
                            1,
                            "Proyecto para mejorar la calidad educativa mediante tecnología",
                            100,
                            500,
                            0
                    )
            );

            IDS_PROYECTOS_INSERTADOS.add(idProyecto1);
            IDS_PROYECTOS_INSERTADOS.add(idProyecto2);

            EstudianteDTO estudiante1DTO = new EstudianteDTO(idUsuarioEstudiante1,
                    usuarioEstudiante1DTO.getNombre(), usuarioEstudiante1DTO.getApellido(),
                    "S23014102", usuarioEstudiante1DTO.getEstado(), idProyecto1, 40776, 10);

            EstudianteDTO estudiante2DTO = new EstudianteDTO(idUsuarioEstudiante2,
                    usuarioEstudiante2DTO.getNombre(), usuarioEstudiante2DTO.getApellido(),
                    "S23014095", usuarioEstudiante2DTO.getEstado(), idProyecto2, 40789, 7.5F);

            estudianteDAO.insertarEstudiante(estudiante1DTO);
            estudianteDAO.insertarEstudiante(estudiante2DTO);

            MATRICULAS_INSERTADAS.add(estudiante1DTO.getMatricula());

            CronogramaActividadesDTO cronogramaActividadesDTO = new CronogramaActividadesDTO(
                    1,
                    "S23014102",
                    IDS_PROYECTOS_INSERTADOS.get(0),
                    1,
                    1
            );

            int idCronogramaActividades =
                    cronogramaDAO.crearNuevoCronogramaDeActividades(cronogramaActividadesDTO);
            IDS_CRONOGRAMAS_INSERTADOS.add(idCronogramaActividades);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Error en @BeforeEach al preparar datos: " + e.getMessage(), e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("Error en @BeforeEach al preparar datos: " + e.getMessage(), e);

        } catch (Exception e) {

            fail("Error en @BeforeEach al preparar datos: " + e.getMessage(), e);
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try {

            conexionBaseDeDatos.prepareStatement("DELETE FROM cronograma").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM estudiante").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM grupo").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM proyecto").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM academicoevaluador").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM academico").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM representante").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM organizacionvinculada").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM periodo").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM usuario").executeUpdate();

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Error al limpiar datos: " + e.getMessage(), e);

        } catch (Exception e) {

            fail("Error con la conexión con la base base de datos." + e.getMessage(), e);
        }
    }

    @AfterAll
    static void cerrarConexion() {

        try {

            if (conexionBaseDeDatos != null && !conexionBaseDeDatos.isClosed()) {

                conexionBaseDeDatos.close();
            }

        } catch (SQLException e) {

            String estadoSQL = e.getSQLState();

            switch (estadoSQL) {

                case "08S01" -> {

                    LOGGER.error("Error de conexión con la base de datos: " + e);
                    System.out.println(
                            "Error de conexión :" +
                                    "No se pudo establecer una conexión con la base de datos: " +
                                    "La base de datos se encuentra desactivada o hay un problema de red."
                    );
                }

                case "42000" -> {

                    LOGGER.error("Error de sintaxis SQL o base de datos no existe: " + e);
                    System.out.println(
                            "Error de conexión :" +
                                    "No se pudo establecer conexión con la base de datos: " +
                                    "La base de datos no existe o hay un error de sintaxis en la consulta."
                    );
                }

                case "28000" -> {

                    LOGGER.error("Credenciales inválidas: " + e);
                    System.out.println(
                            "Credenciales inválidas: " +
                                    "Usuario o contraseña incorrectos: " +
                                    "Verifique los datos de acceso a la base de datos."
                    );
                }

                case "23000" -> {

                    LOGGER.error("Violación de restricción de integridad: " + e);
                    System.out.println(
                            "Dato duplicado o relación inválida: " +
                                    "No se puede completar la operación: " +
                                    "Verifique que los datos no estén repetidos o que las relaciones sean válidas."
                    );
                }

                case "42S02" -> {

                    LOGGER.error("Tabla no encontrada: " + e);
                    System.out.println(
                            "Tabla inexistente: " +
                                    "No se encontró una tabla necesaria para ejecutar la operación: " +
                                    "Verifique que todas las tablas estén correctamente creadas."
                    );
                }

                case "42S22" -> {

                    LOGGER.error("Columna no encontrada: " + e);
                    System.out.println(
                            "Columna inexistente: " +
                                    "No se encontró una columna requerida: " +
                                    "Revise los nombres de las columnas en su consulta."
                    );
                }

                default -> {

                    LOGGER.error("SQLState desconocido (" + estadoSQL + "): " + e);
                    System.out.println(
                            "Error desconocido: " +
                                    "Se produjo un error inesperado al acceder a la base de datos: " +
                                    "Contacte a soporte técnico."
                    );
                }
            }

        } catch (Exception e) {

            fail("Error con la conexión con la base base de datos." + e.getMessage(), e);
        }
    }

    @Test
    void testCrearNuevoCronogramaDeActividadesConDatosValidos() {

        CronogramaActividadesDTO nuevoCronograma = new CronogramaActividadesDTO(
                0, "S23014095", IDS_PROYECTOS_INSERTADOS.get(1), 1, 1);

        try {

            int idCronograma = cronogramaDAO.crearNuevoCronogramaDeActividades(nuevoCronograma);
            IDS_CRONOGRAMAS_INSERTADOS.add(idCronograma);

            assertTrue(idCronograma > 0);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Ocurrió una SQLException al ejecutar el crear nuevo cronograma con datos validos" +
                    e.getMessage(), e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("Ocurrió una IOException al ejecutar el crear nuevo cronograma con datos validos" +
                    e.getMessage(), e);

        } catch (Exception e) {

            fail("Ocurrió una Exception al ejecutar el crear nuevo cronograma con datos validos" +
                    e.getMessage(), e);
        }
    }

    @Test
    void testCrearNuevoCronogramaDeActividadesConMatriculaInvalida() {

        CronogramaActividadesDTO cronogramaInvalido = new CronogramaActividadesDTO(
                0, "MATRICULA_INVALIDA", IDS_PROYECTOS_INSERTADOS.get(0), 1, 1);

        assertThrows(SQLException.class, () ->
                cronogramaDAO.crearNuevoCronogramaDeActividades(cronogramaInvalido));
    }

    @Test
    void testModificarCronogramaDeActividadesConDatosValidos() {

        CronogramaActividadesDTO modificaciones = new CronogramaActividadesDTO(
                IDS_CRONOGRAMAS_INSERTADOS.get(0),
                "S23014102",
                IDS_PROYECTOS_INSERTADOS.get(1),
                1,
                0);

        try {

            boolean resultado = cronogramaDAO.modificarCronogramaDeActividades(modificaciones);
            assertTrue(resultado);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Ocurrió una SQLException al ejecutar el modificar cronograma con datos validos" +
                    e.getMessage(), e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("Ocurrió una IOException al ejecutar el modificar cronograma con datos validos" +
                    e.getMessage(), e);

        } catch (Exception e) {

            fail("Ocurrió una Exception al ejecutar el modificar cronograma con datos validos" +
                    e.getMessage(), e);
        }
    }

    @Test
    void testModificarCronogramaDeActividadesInexistente() {

        CronogramaActividadesDTO cronogramaInexistente = new CronogramaActividadesDTO(
                99999, "S23014095", IDS_PROYECTOS_INSERTADOS.get(0), 1, 1);

        try {

            boolean resultado = cronogramaDAO.modificarCronogramaDeActividades(cronogramaInexistente);
            assertFalse(resultado);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Ocurrió una SQLException al ejecutar el modificar cronograma inexistente " +
                    e.getMessage(), e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("Ocurrió una IOException al ejecutar el modificar cronograma inexistente " +
                    e.getMessage(), e);

        } catch (Exception e) {

            fail("Ocurrió una Exception al ejecutar el modificar cronograma inexistente " +
                    e.getMessage(), e);
        }
    }

    @Test
    void testBuscarCronogramaDeActividadesPorIDExistente() {

        CronogramaActividadesDTO esperado = new CronogramaActividadesDTO(
                IDS_CRONOGRAMAS_INSERTADOS.get(0),
                "S23014102",
                IDS_PROYECTOS_INSERTADOS.get(0),
                1,
                1);

        try {

            CronogramaActividadesDTO resultadoPrueba = cronogramaDAO.buscarCronogramaDeActividadesPorID(IDS_CRONOGRAMAS_INSERTADOS.get(0));
            assertEquals(esperado, resultadoPrueba);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Ocurrió una SQLException al ejecutar el buscar cronograma por id existente. " +
                    e.getMessage(), e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("Ocurrió una IOException al ejecutar el buscar cronograma por id existente.  " +
                    e.getMessage(), e);

        } catch (Exception e) {

            fail("Ocurrió una Exception al ejecutar el buscar cronograma por id existente " +
                    e.getMessage(), e);
        }
    }

    @Test
    void testBuscarCronogramaDeActividadesPorIDInexistente() {

        CronogramaActividadesDTO cronogramaEsperado = new CronogramaActividadesDTO(-1, "0",
                -1, -1, 0);

        try {

            CronogramaActividadesDTO resultado =
                    cronogramaDAO.buscarCronogramaDeActividadesPorID(99999);
            assertEquals(cronogramaEsperado, resultado);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Ocurrió una SQLException al ejecutar el buscar cronograma por id inexistente. " +
                    e.getMessage(), e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("Ocurrió una IOException al ejecutar el buscar cronograma por id inexistente.  " +
                    e.getMessage(), e);

        } catch (Exception e) {

            fail("Ocurrió una Exception al ejecutar el buscar cronograma por id inexistente " +
                    e.getMessage(), e);
        }
    }

    @Test
    void testBuscarCronogramaPorMatriculaExistente() {

        CronogramaActividadesDTO cronogramaEsperado = new CronogramaActividadesDTO(
                IDS_CRONOGRAMAS_INSERTADOS.get(0),
                "S23014102",
                IDS_PROYECTOS_INSERTADOS.get(0),
                1,
                1);

        try {

            CronogramaActividadesDTO cronogramaEncontrado =
                    cronogramaDAO.buscarCronogramaPorMatricula("S23014102");
            assertEquals(cronogramaEsperado, cronogramaEncontrado);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Ocurrió una SQLException al ejecutar el buscar cronograma por matricula existente. " +
                    e.getMessage(), e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("Ocurrió una IOException al ejecutar el buscar cronograma por matricula existente.  " +
                    e.getMessage(), e);

        } catch (Exception e) {

            fail("Ocurrió una Exception al ejecutar el buscar cronograma por matricula existente " +
                    e.getMessage(), e);
        }
    }

    @Test
    void testBuscarCronogramaPorMatriculaInexistente() {

        try {

            CronogramaActividadesDTO resultado = cronogramaDAO.buscarCronogramaPorMatricula("MATRICULA_INEXISTENTE");
            assertNull(resultado);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Ocurrió una SQLException al ejecutar el buscar cronograma por matricula inexistente. " +
                    e.getMessage(), e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("Ocurrió una IOException al ejecutar el buscar cronograma por matricula inexistente.  " +
                    e.getMessage(), e);

        } catch (Exception e) {

            fail("Ocurrió una Exception al ejecutar el buscar cronograma por matricula inexistente " +
                    e.getMessage(), e);
        }
    }
}
