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
import java.sql.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AutoevaluacionContieneDAOTest {

    private static final Logger LOGGER = LogManager.getLogger(AutoevaluacionContieneDAOTest.class);

    private AutoevaluacionContieneDAO autoevaluacionContieneDAO;
    private AutoevaluacionDAO autoevaluacionDAO;
    private EstudianteDAO estudianteDAO;
    private UsuarioDAO usuarioDAO;
    private CriterioAutoevaluacionDAO criterioAutoevaluacionDAO;
    private static Connection conexionBaseDeDatos;
    private IGestorAlertas gestorAlertas;
    private ManejadorExcepciones manejadorExcepciones;

    private final List<Integer> IDS_AUTOEVALUACIONES_CREADAS = new ArrayList<>();
    private final List<AutoEvaluacionContieneDTO> REGISTROS_AUTOEVALUACIONES_CONTIENE = new ArrayList<>();
    private final List<String> MATRICULAS_ESTUDIANTES_INSERTADAS = new ArrayList<>();
    private final List<Integer> IDS_USUARIOS_INSERTADOS = new ArrayList<>();

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

            fail("Error con la conexión con la base base de datos." + e.getMessage());
        }
    }

    @BeforeEach
    void prepararDatosDePrueba() {

        try {

            autoevaluacionContieneDAO = new AutoevaluacionContieneDAO();
            autoevaluacionDAO = new AutoevaluacionDAO();
            estudianteDAO = new EstudianteDAO();
            usuarioDAO = new UsuarioDAO();
            criterioAutoevaluacionDAO = new CriterioAutoevaluacionDAO();
            IGestorAlertas utilidadesConsola = new UtilidadesConsola();
            manejadorExcepciones = new ManejadorExcepciones(utilidadesConsola, LOGGER);

            conexionBaseDeDatos.prepareStatement("DELETE FROM autoevaluacioncontiene").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM autoevaluacion WHERE idAutoevaluacion BETWEEN 1000 AND 2000").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM estudiante WHERE matricula LIKE 'S230%'").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM usuario WHERE idUsuario BETWEEN 1000 AND 2000").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM criterioautoevaluacion WHERE IDCriterio BETWEEN 1000 AND 2000").executeUpdate();

            UsuarioDTO usuarioEstudianteDTO1 = new UsuarioDTO(0, "Nombre1", "Apellido1", 1);
            UsuarioDTO usuarioEstudianteDTO2 = new UsuarioDTO(0, "Nombre2", "Apellido2", 1);

            int idUsuarioEstudiante1 = usuarioDAO.insertarUsuario(usuarioEstudianteDTO1);
            int idUsuarioEstudiante2 = usuarioDAO.insertarUsuario(usuarioEstudianteDTO2);

            IDS_USUARIOS_INSERTADOS.addAll(List.of(idUsuarioEstudiante1, idUsuarioEstudiante2));

            estudianteDAO.insertarEstudiante(new EstudianteDTO(idUsuarioEstudiante1, "Nombre1", "Apellido1", "S23014102", 1,0));
            estudianteDAO.insertarEstudiante(new EstudianteDTO(idUsuarioEstudiante2, "Nombre2", "Apellido2", "S23014203", 1,0));

            MATRICULAS_ESTUDIANTES_INSERTADAS.addAll(List.of("S23014102", "S23014203"));

            autoevaluacionDAO.crearNuevaAutoevaluacion(new AutoevaluacionDTO(1001, Timestamp.valueOf("2023-01-01 00:00:00"), "Xalapa", 9.5f, "S23014102", 1));
            autoevaluacionDAO.crearNuevaAutoevaluacion(new AutoevaluacionDTO(1002, Timestamp.valueOf("2023-02-02 00:00:00"), "Veracruz", 8.0f, "S23014203", 1));

            IDS_AUTOEVALUACIONES_CREADAS.addAll(List.of(1001, 1002));

            criterioAutoevaluacionDAO.crearNuevoCriterioAutoevaluacion(new CriterioAutoevaluacionDTO(1001, "Criterio para insertar", 1, 1));
            criterioAutoevaluacionDAO.crearNuevoCriterioAutoevaluacion(new CriterioAutoevaluacionDTO(1002, "Criterio para modificar", 1, 1));

            AutoEvaluacionContieneDTO registroInicial = new AutoEvaluacionContieneDTO(1001, 4.0f, 1002);
            autoevaluacionContieneDAO.insertarAutoevaluacionContiene(registroInicial);
            REGISTROS_AUTOEVALUACIONES_CONTIENE.add(registroInicial);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Error en preparación de datos: " + e.getMessage());

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("Error en preparación de datos: " + e.getMessage());

        } catch (Exception e) {

            fail("Error con la conexión con la base base de datos." + e.getMessage());
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try {

            for (AutoEvaluacionContieneDTO registroAutoEvaluacionContieneDTO : REGISTROS_AUTOEVALUACIONES_CONTIENE) {

                PreparedStatement eliminarAutoevaluacionContiene = conexionBaseDeDatos.prepareStatement(
                        "DELETE FROM autoevaluacioncontiene WHERE idAutoevaluacion = ? AND IDCriterio = ?");

                eliminarAutoevaluacionContiene.setInt(1, registroAutoEvaluacionContieneDTO.getIdAutoevaluacion());
                eliminarAutoevaluacionContiene.setInt(2, registroAutoEvaluacionContieneDTO.getIdCriterio());
                eliminarAutoevaluacionContiene.executeUpdate();
            }

            for (int idAutoevaluacion : IDS_AUTOEVALUACIONES_CREADAS) {

                PreparedStatement eliminarAutoevaluacion = conexionBaseDeDatos.prepareStatement(
                        "DELETE FROM autoevaluacion WHERE idAutoevaluacion = ?");

                eliminarAutoevaluacion.setInt(1, idAutoevaluacion);
                eliminarAutoevaluacion.executeUpdate();
            }

            for (String matricula : MATRICULAS_ESTUDIANTES_INSERTADAS) {

                PreparedStatement eliminarEstudiante = conexionBaseDeDatos.prepareStatement(
                        "DELETE FROM estudiante WHERE matricula = ?");

                eliminarEstudiante.setString(1, matricula);
                eliminarEstudiante.executeUpdate();
            }

            for (int idUsuario : IDS_USUARIOS_INSERTADOS) {

                PreparedStatement eliminarUsuario = conexionBaseDeDatos.prepareStatement(
                        "DELETE FROM usuario WHERE idUsuario = ?");

                eliminarUsuario.setInt(1, idUsuario);
                eliminarUsuario.executeUpdate();
            }

            PreparedStatement eliminarCriterios = conexionBaseDeDatos.prepareStatement(
                    "DELETE FROM criterioautoevaluacion WHERE IDCriterio IN (1001, 1002)");
            eliminarCriterios.executeUpdate();

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Error al limpiar datos: " + e.getMessage());

        } catch (Exception e) {

            fail("Error con la conexión con la base base de datos." + e.getMessage());
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

            fail("Error con la conexión con la base base de datos." + e.getMessage());
        }
    }

    @Test
    void testInsertarAutoevaluacionContieneConDatosValidos() {

        try {

            AutoEvaluacionContieneDTO autoEvaluacionContieneDTO = new AutoEvaluacionContieneDTO(1001, 4.5f, 1001);
            boolean fueInsertado = autoevaluacionContieneDAO.insertarAutoevaluacionContiene(autoEvaluacionContieneDTO);
            REGISTROS_AUTOEVALUACIONES_CONTIENE.add(autoEvaluacionContieneDTO);
            assertTrue(fueInsertado);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Excepción inesperada: " + e.getMessage());

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("Excepción inesperada: " + e.getMessage());

        } catch (Exception e) {

            fail("Error con la conexión con la base base de datos." + e.getMessage());
        }
    }

    @Test
    void testInsertarAutoevaluacionContieneConDatosInvalidos() {

        AutoEvaluacionContieneDTO registroAutoEvaluacionContieneInvalido = new AutoEvaluacionContieneDTO(-1, -1.0f, -1);

        assertThrows(SQLException.class, () -> {
            autoevaluacionContieneDAO.insertarAutoevaluacionContiene(registroAutoEvaluacionContieneInvalido);
        });
    }


    @Test
    void testBuscarAutoevaluacionContieneConDatosValidos() {

        try {

            AutoEvaluacionContieneDTO resultadoBusqueda = autoevaluacionContieneDAO.buscarAutoevaluacionContienePorID(1001, 1002);
            assertNotNull(resultadoBusqueda);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Error al buscar registro existente: " + e.getMessage());

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("Error al buscar registro existente: " + e.getMessage());

        } catch (Exception e) {

            fail("Error con la conexión con la base base de datos." + e.getMessage());
        }
    }

    @Test
    void testBuscarAutoevaluacionContieneConDatosInvalidos() {

        try {

            AutoEvaluacionContieneDTO resultadoBusqueda = autoevaluacionContieneDAO.buscarAutoevaluacionContienePorID(-1, -1);
            assertEquals(-1, resultadoBusqueda.getIdAutoevaluacion());

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Error inesperado: " + e.getMessage());

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("Error inesperado: " + e.getMessage());

        } catch (Exception e) {

            fail("Error con la conexión con la base base de datos." + e.getMessage());
        }
    }

    @Test
    void testModificarAutoevaluacionContieneConDatosValidos() {

        try {

            AutoEvaluacionContieneDTO registroAutoEvaluacionContieneDTO = new AutoEvaluacionContieneDTO(1001, 5.0f, 1002);
            boolean fueModificado = autoevaluacionContieneDAO.modificarCalificacion(registroAutoEvaluacionContieneDTO);
            assertTrue(fueModificado);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Error inesperado: " + e.getMessage());

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("Error inesperado: " + e.getMessage());

        } catch (Exception e) {

            fail("Error con la conexión con la base base de datos." + e.getMessage());
        }
    }

    @Test
    void testModificarAutoevaluacionContieneConDatosInvalidos() {

        try {

            AutoEvaluacionContieneDTO registroAutoEvaluacionContieneDTOInvalido = new AutoEvaluacionContieneDTO(-1, -1.0f, -1);
            boolean fueModificado = autoevaluacionContieneDAO.modificarCalificacion(registroAutoEvaluacionContieneDTOInvalido);
            assertFalse(fueModificado);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Error inesperado: " + e.getMessage());

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("Error inesperado: " + e.getMessage());

        } catch (Exception e) {

            fail("Error inesperado: " + e.getMessage());
        }
    }

    @Test
    void testModificarAutoevaluacionContieneInexistente() {

        AutoEvaluacionContieneDTO autoevaluacionContieneInexistente = new AutoEvaluacionContieneDTO(
                99999, 5.0f, 99999);

        try {

            boolean resultadoModificacion = autoevaluacionContieneDAO.modificarCalificacion(autoevaluacionContieneInexistente);
            assertFalse(resultadoModificacion, "No debería modificarse un registro inexistente en autoevaluacioncontiene.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e.getMessage());

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " + e.getMessage());

        } catch (Exception e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }
}
