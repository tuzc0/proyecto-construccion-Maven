import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.UsuarioDTO;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import logica.DAOs.AcademicoDAO;
import logica.DTOs.AcademicoDTO;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import logica.ManejadorExcepciones;
import utilidadesPruebas.UtilidadesConsola;

public class AcademicoDAOTest {

    private static final Logger LOGGER = LogManager.getLogger(AcademicoDAOTest.class);

    private AcademicoDAO academicoDAO;
    private UsuarioDAO usuarioDAO;
    private static Connection conexionBaseDeDatos;
    private ManejadorExcepciones manejadorExcepciones;

    private final List<Integer> NUMEROS_DE_PERSONAL_INSERTADOS = new ArrayList<>();
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

            fail("Error durante el @BeforeAll: " + e.getMessage(), e);
        }
    }

    @BeforeEach
    void prepararDatosDePrueba() {

        IGestorAlertas utilidadesConsola = new UtilidadesConsola();
        manejadorExcepciones = new ManejadorExcepciones(utilidadesConsola, LOGGER);
        academicoDAO = new AcademicoDAO();
        usuarioDAO = new UsuarioDAO();

        try {

            NUMEROS_DE_PERSONAL_INSERTADOS.clear();
            IDS_USUARIOS_INSERTADOS.clear();

            try (PreparedStatement eliminarAcademicos = conexionBaseDeDatos
                    .prepareStatement("DELETE FROM academico");
                 PreparedStatement eliminarUsuarios = conexionBaseDeDatos
                         .prepareStatement("DELETE FROM usuario ")) {

                eliminarAcademicos.executeUpdate();
                eliminarUsuarios.executeUpdate();
            }

            UsuarioDTO usuarioAcademico1DTO =
                    new UsuarioDTO(0, "Nombre", "Prueba", 1);
            UsuarioDTO usuarioAcadmico2DTO =
                    new UsuarioDTO(0, "Usuario2", "Apellido2", 1);
            UsuarioDTO usuarioAcademico3DTO =
                    new UsuarioDTO(0, "Usuario3", "Apellido3", 1);

            int idUsuarioAcademico1 = usuarioDAO.insertarUsuario(usuarioAcademico1DTO);
            int idUsuarioAcademico2 = usuarioDAO.insertarUsuario(usuarioAcadmico2DTO);
            int idUsuarioAcademico3 = usuarioDAO.insertarUsuario(usuarioAcademico3DTO);

            IDS_USUARIOS_INSERTADOS.addAll(List.of(idUsuarioAcademico1, idUsuarioAcademico2,
                    idUsuarioAcademico3));

            academicoDAO.insertarAcademico(new AcademicoDTO(1001, idUsuarioAcademico1,
                    usuarioAcademico1DTO.getNombre(), usuarioAcademico1DTO.getApellido(),
                    usuarioAcademico1DTO.getEstado()));

            academicoDAO.insertarAcademico(new AcademicoDTO(1002, idUsuarioAcademico2,
                    usuarioAcadmico2DTO.getNombre(), usuarioAcadmico2DTO.getApellido(),
                    usuarioAcadmico2DTO.getEstado()));

            academicoDAO.insertarAcademico(new AcademicoDTO(1003, idUsuarioAcademico3,
                    usuarioAcademico3DTO.getNombre(), usuarioAcademico3DTO.getApellido(),
                    usuarioAcademico3DTO.getEstado()));

            NUMEROS_DE_PERSONAL_INSERTADOS.addAll(List.of(1001, 1002, 1003));

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Error al prepararDatosDePrueba", e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("Error al prepararDatosDePrueba", e);
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try {

            try (PreparedStatement eliminarAcademicos = conexionBaseDeDatos
                    .prepareStatement("DELETE FROM academico");
                 PreparedStatement eliminarUsuarios = conexionBaseDeDatos
                         .prepareStatement("DELETE FROM usuario")) {

                eliminarAcademicos.executeUpdate();
                eliminarUsuarios.executeUpdate();
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Error en @AfterEach al limpiar datos: " + e);
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
        }
    }

    @Test
    void testInsertarAcademicoConDatosValidos() {

        try {

            UsuarioDTO usuarioDTO = new UsuarioDTO(0, "AcademicoTest", "ApellidoTest", 1);
            int idUsuarioDTO = usuarioDAO.insertarUsuario(usuarioDTO);
            IDS_USUARIOS_INSERTADOS.add(idUsuarioDTO);

            AcademicoDTO academicoDTO = new AcademicoDTO(55555, idUsuarioDTO,
                    "Nombre", "Prueba", 1);
            boolean insercionExitosa = academicoDAO.insertarAcademico(academicoDTO);

            assertTrue(insercionExitosa, "El académico debería ser insertado correctamente.");
            NUMEROS_DE_PERSONAL_INSERTADOS.add(55555);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: ", e);
        }
    }

    @Test
    void testInsertarAcademicoConDatosInvalidos() {

        AcademicoDTO academicoInvalido = new AcademicoDTO(99999, -1,
                "Nombre", "Invalido", 1);

        assertThrows(SQLException.class, () -> academicoDAO.insertarAcademico(academicoInvalido),
                "Se esperaba una excepción debido a datos inválidos."
        );
    }

    @Test
    void testEliminarAcademicoPorNumeroDePersonalConNumeroDePersonalExistente() {

        int numeroDePersonalAEliminar = 1002;

        try {

            boolean resutadoDeEliminarAcademico = academicoDAO.eliminarAcademicoPorNumeroDePersonal(numeroDePersonalAEliminar);
            assertTrue(resutadoDeEliminarAcademico, "El académico debería ser eliminado correctamente.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepcion: " + e);
        }
    }

    @Test
    void testEliminarAcademicoPorNumeroDePersonalConNumeroDePersonalInexistente() {

        int numeroDePersonalInexistente = 88888;

        try {

            boolean resultadoDeEliminarAcademico = academicoDAO.eliminarAcademicoPorNumeroDePersonal(numeroDePersonalInexistente);
            assertFalse(resultadoDeEliminarAcademico, "No debería eliminarse ningún académico inexistente.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepcion: " + e);
        }
    }

    @Test
    void testEliminarAcademicoPorNumeroDePersonalInvalido() {

        int numeroDePersonalInexistente = -1;

        try {

            boolean resultado = academicoDAO.eliminarAcademicoPorNumeroDePersonal(numeroDePersonalInexistente);
            assertFalse(resultado, "No debería poder eliminar un académico inexistente");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepcion: " + e);
        }
    }

    @Test
    void testModificarAcademicoConDatosValidos() {

        try {

            AcademicoDTO academicoDTO = academicoDAO.buscarAcademicoPorNumeroDePersonal(1003);
            int idUsuario = academicoDTO.getIdUsuario();

            AcademicoDTO academicoActualizado = new AcademicoDTO(
                    1003, idUsuario, "NuevoNombre", "NuevoApellido", 1);
            boolean resultadoDeModificar = academicoDAO.modificarAcademico(academicoActualizado);

            assertTrue(resultadoDeModificar, "El académico debería ser modificado correctamente.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepcion: " + e);
        }
    }

    @Test
    void testModificarAcademicoConDatosInvalidos() {

        AcademicoDTO academicoDatosInvalido = new AcademicoDTO(1003, -100,
                "Nombre", "Apellido", 1);

        try {

            boolean resultadoAcademicoModificado = academicoDAO.modificarAcademico(academicoDatosInvalido);
            assertFalse(resultadoAcademicoModificado, "No debería modificarse un académico con datos inválidos.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepcion: " + e);
        }
    }

    @Test
    void testModificarAcademicoInexistente() {

        AcademicoDTO academicoInexistente = new AcademicoDTO(77777, 9999, "X",
                "Y", 1);

        try {

            boolean resultadoModificacion = academicoDAO.modificarAcademico(academicoInexistente);
            assertFalse(resultadoModificacion, "No se debería modificar un académico inexistente.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepcion: " + e);
        }
    }

    @Test
    void testBuscarAcademicoPorNumeroDePersonalConDatosValidos() {

        AcademicoDTO academicoEsperado = new AcademicoDTO(1001, IDS_USUARIOS_INSERTADOS.get(0),
                "Nombre", "Prueba", 1);

        try {

            AcademicoDTO academicoEncontrado = academicoDAO.buscarAcademicoPorNumeroDePersonal(academicoEsperado.getNumeroDePersonal());
            assertEquals(academicoEsperado, academicoEncontrado, "El académico debería ser encontrado correctamente.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepcion: " + e);
        }
    }

    @Test
    void testBuscarAcademicoPorNumeroDePersonalConDatosInvalidos() {

        int numeroDePersonalInexistente = -1;

        try {

            AcademicoDTO academicoEncontrado = academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroDePersonalInexistente);
            assertEquals(-1, academicoEncontrado.getNumeroDePersonal(), "No debería encontrarse un académico con ese número.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepcion: " + e);
        }
    }

    @Test
    void testBuscarAcademicoPorNumeroDePersonalInexistente() {

        int numeroDePersonalInexistente = 99999;

        try {

            AcademicoDTO resultado = academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroDePersonalInexistente);
            assertEquals(-1, resultado.getNumeroDePersonal(), "Debería retornar un DTO con valores por defecto");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepcion: " + e);
        }
    }

    @Test
    void testListarAcademicosConDatos() {

        try {

            List<AcademicoDTO> listaEsperada = new ArrayList<>();
            listaEsperada.add(new AcademicoDTO(1001, IDS_USUARIOS_INSERTADOS.get(0),
                    "Nombre", "Prueba", 1));
            listaEsperada.add(new AcademicoDTO(1002, IDS_USUARIOS_INSERTADOS.get(1),
                    "Usuario2", "Apellido2", 1));
            listaEsperada.add(new AcademicoDTO(1003, IDS_USUARIOS_INSERTADOS.get(2),
                    "Usuario3", "Apellido3", 1));

            List<AcademicoDTO> listaObtenida = academicoDAO.listarAcademicos();

            assertTrue(listaObtenida.containsAll(listaEsperada),
                    "La lista de académicos obtenida debería contener los datos esperados.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepcion: " + e);
        }
    }

    @Test
    void testListarAcademicosSinDatos() {

        try {

            for (int numeroDePersonal : NUMEROS_DE_PERSONAL_INSERTADOS) {

                academicoDAO.eliminarAcademicoPorNumeroDePersonal(numeroDePersonal);
            }

            List<AcademicoDTO> listaAcademicosDTOs = academicoDAO.listarAcademicos();
            assertEquals(0, listaAcademicosDTOs.size(),
                    "La lista de académicos evaluadores debería estar vacía.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepcion: " + e);
        }
    }

    @Test
    void testBuscarAcademicoPorIDValido() {

        UsuarioDTO usuarioDTO = new UsuarioDTO(0, "TestIDValido", "ApellidoTest", 1);

        try {

            int idUsuario = usuarioDAO.insertarUsuario(usuarioDTO);
            IDS_USUARIOS_INSERTADOS.add(idUsuario);

            AcademicoDTO academicoEsperado = new AcademicoDTO(12345, idUsuario,
                    usuarioDTO.getNombre(), usuarioDTO.getApellido(), usuarioDTO.getEstado());

            academicoDAO.insertarAcademico(academicoEsperado);
            NUMEROS_DE_PERSONAL_INSERTADOS.add(12345);

            AcademicoDTO academicoEncontrado = academicoDAO.buscarAcademicoPorID(idUsuario);
            assertEquals(academicoEsperado, academicoEncontrado,
                    "Debería encontrar el académico con el ID proporcionado");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepcion: " + e);
        }
    }

    @Test
    void testBuscarAcademicoPorIDInvalido() {

        UsuarioDTO usuarioDTO = new UsuarioDTO(-1, "TestIDInvalido", "ApellidoTest", 1);
        AcademicoDTO academicoEsperado = new AcademicoDTO(-1, -1, "N/A", "N/A", 0);

        try {

            int idUsuarioInvalido = usuarioDAO.insertarUsuario(usuarioDTO);
            IDS_USUARIOS_INSERTADOS.add(idUsuarioInvalido);

            AcademicoDTO resultadoObtenido = academicoDAO.buscarAcademicoPorID(idUsuarioInvalido);
            assertEquals(academicoEsperado, resultadoObtenido,
                    "Debería retornar un DTO con valores por defecto");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepcion: " + e);
        }
    }

    @Test
    void testBuscarAcademicoPorIDInexistente() {

        UsuarioDTO usuarioDTO = new UsuarioDTO(99999, "TestIDInvalido", "ApellidoTest", 1);
        AcademicoDTO academicoEsperado = new AcademicoDTO(-1, -1, "N/A", "N/A", 0);

        try {

            int idUsuarioInvalido = usuarioDAO.insertarUsuario(usuarioDTO);
            IDS_USUARIOS_INSERTADOS.add(idUsuarioInvalido);

            AcademicoDTO resultadoObtenido = academicoDAO.buscarAcademicoPorID(idUsuarioInvalido);
            assertEquals(academicoEsperado, resultadoObtenido,
                    "Debería retornar un DTO con valores por defecto");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepcion: " + e);
        }
    }
}
