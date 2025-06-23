import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.UsuarioDTO;
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import logica.DAOs.AcademicoEvaluadorDAO;
import logica.DTOs.AcademicoEvaluadorDTO;
import utilidadesPruebas.UtilidadesConsola;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AcademicoEvaluadorDAOTest {

    private static final Logger LOGGER = LogManager.getLogger(AcademicoEvaluadorDAOTest.class);

    private AcademicoEvaluadorDAO academicoEvaluadorDAO;
    private UsuarioDAO usuarioDAO;
    private static Connection conexionBaseDeDatos;
    private IGestorAlertas gestorAlertas;
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
        }
    }

    @BeforeEach
    void prepararDatosDePrueba() {

        usuarioDAO = new UsuarioDAO();
        academicoEvaluadorDAO = new AcademicoEvaluadorDAO();
        IGestorAlertas utilidadesConsola = new UtilidadesConsola();
        manejadorExcepciones = new ManejadorExcepciones(utilidadesConsola, LOGGER);

        NUMEROS_DE_PERSONAL_INSERTADOS.clear();
        IDS_USUARIOS_INSERTADOS.clear();

        try {

            for (int numero : List.of(1001, 1002, 1003, 55555, 99999, 88888)) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM academicoevaluador WHERE numeroDePersonal = " + numero)
                        .executeUpdate();
            }

            conexionBaseDeDatos
                    .prepareStatement("DELETE FROM usuario WHERE idUsuario BETWEEN 1000 AND 2000")
                    .executeUpdate();

            UsuarioDTO usuarioAcademicoEvaluador1DTO = new UsuarioDTO(0, "Nombre", "Prueba", 1);
            UsuarioDTO usuarioAcademicoEvaluador2DTO = new UsuarioDTO(0, "NombrePrueba", "Apellido", 1);
            UsuarioDTO usuarioAcademicoEvaluador3DTO = new UsuarioDTO(0, "UsuarioDePrueba", "apellido", 1);

            int idUsuarioAcademicoEvaluador1 = usuarioDAO.insertarUsuario(usuarioAcademicoEvaluador1DTO);
            int idUsuarioAcademicoEvaluador2 = usuarioDAO.insertarUsuario(usuarioAcademicoEvaluador2DTO);
            int idUsuarioAcademicoEvaludor3 = usuarioDAO.insertarUsuario(usuarioAcademicoEvaluador3DTO);

            IDS_USUARIOS_INSERTADOS.addAll(List.of(idUsuarioAcademicoEvaluador1, idUsuarioAcademicoEvaluador2,
                    idUsuarioAcademicoEvaludor3));

            academicoEvaluadorDAO.insertarAcademicoEvaluador(new
                    AcademicoEvaluadorDTO(1001, idUsuarioAcademicoEvaluador1,
                    usuarioAcademicoEvaluador1DTO.getNombre(), usuarioAcademicoEvaluador1DTO.getApellido(),
                    usuarioAcademicoEvaluador1DTO.getEstado()));

            academicoEvaluadorDAO.insertarAcademicoEvaluador(new AcademicoEvaluadorDTO(1002,
                    idUsuarioAcademicoEvaluador2, usuarioAcademicoEvaluador2DTO.getNombre(),
                    usuarioAcademicoEvaluador2DTO.getApellido(), usuarioAcademicoEvaluador2DTO.getEstado()));

            academicoEvaluadorDAO.insertarAcademicoEvaluador(new AcademicoEvaluadorDTO(1003,
                    idUsuarioAcademicoEvaludor3, usuarioAcademicoEvaluador3DTO.getNombre(),
                    usuarioAcademicoEvaluador3DTO.getApellido(), usuarioAcademicoEvaluador3DTO.getEstado()));

            NUMEROS_DE_PERSONAL_INSERTADOS.addAll(List.of(1001, 1002, 1003));

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Error en @BeforeEach al preparar datos: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("Error en @BeforeEach al preparar datos: " + e);
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try {

            for (int numeroDePersonal : NUMEROS_DE_PERSONAL_INSERTADOS) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM academicoevaluador WHERE numeroDePersonal = " +
                                numeroDePersonal).executeUpdate();
            }

            for (int idUsuario : IDS_USUARIOS_INSERTADOS) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM usuario WHERE idUsuario = " + idUsuario)
                        .executeUpdate();
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

            AcademicoEvaluadorDTO nuevoAcademico = new AcademicoEvaluadorDTO(55555, idUsuarioDTO,
                    "Nombre", "Prueba", 1);
            boolean insercionExitosa = academicoEvaluadorDAO.insertarAcademicoEvaluador(nuevoAcademico);

            assertTrue(insercionExitosa, "El académico debería ser insertado correctamente.");
            NUMEROS_DE_PERSONAL_INSERTADOS.add(55555);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testInsertarAcademicoConDatosInvalidos() {

        AcademicoEvaluadorDTO academicoEvaluadorDTOInvalido = new AcademicoEvaluadorDTO(99999,
                -1, "Nombre", "Invalido", 1);

        assertThrows(SQLException.class, () ->
                        academicoEvaluadorDAO.insertarAcademicoEvaluador(academicoEvaluadorDTOInvalido),
                "Se esperaba una excepción debido a datos inválidos."
        );
    }

    @Test
    void testEliminarAcademicoPorNumeroDePersonalConDatosValidos() {

        int idParaEliminar = 1002;

        try {

            boolean academicoEvaluadorDTOEliminado =
                    academicoEvaluadorDAO.eliminarAcademicoEvaluadorPorNumeroDePersonal(idParaEliminar);
            assertTrue(academicoEvaluadorDTOEliminado,
                    "El académico debería ser eliminado correctamente.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testEliminarAcademicoPorNumeroDePersonalConDatosInvalidos() {

        int numeroDePersonalInvalido = -1;

        try {

            boolean AcademicoEvaluadorDTOEliminado =
                    academicoEvaluadorDAO.eliminarAcademicoEvaluadorPorNumeroDePersonal(numeroDePersonalInvalido);
            assertFalse(AcademicoEvaluadorDTOEliminado,
                    "No debería eliminarse ningún académico inexistente.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void eliminarAcademicoEvaluadorInexistente() {

        int numeroDePersonalInexistente = 77777;

        try {

            boolean resultadoAlEliminar =
                    academicoEvaluadorDAO.eliminarAcademicoEvaluadorPorNumeroDePersonal(numeroDePersonalInexistente);
            assertFalse(resultadoAlEliminar, "No debería poder eliminar un académico evaluador inexistente");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testModificarAcademicoConDatosValidos() {

        try {

            AcademicoEvaluadorDTO academicoExistente =
                    academicoEvaluadorDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(1003);
            int idUsuario = academicoExistente.getIdUsuario();

            AcademicoEvaluadorDTO academicoActualizado = new AcademicoEvaluadorDTO(
                    1003, idUsuario, "NuevoNombre", "NuevoApellido", 1);
            boolean modificado = academicoEvaluadorDAO.modificarAcademicoEvaluador(academicoActualizado);

            assertTrue(modificado, "El académico debería ser modificado correctamente.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testModificarAcademicoConDatosInvalidos() {

        AcademicoEvaluadorDTO AcademicoConDatosInvalido = new AcademicoEvaluadorDTO(1003, -100,
                "Nombre", "Apellido", 1);

        try {

            boolean resultadoDeLaModificacion =
                    academicoEvaluadorDAO.modificarAcademicoEvaluador(AcademicoConDatosInvalido);
            assertFalse(resultadoDeLaModificacion,
                    "No debería modificarse un académico con datos inválidos.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testModificarAcademicoInexistente() {

        AcademicoEvaluadorDTO academicoEvaluadorInexistente =
                new AcademicoEvaluadorDTO(77777, 9999,
                        "X", "Y", 1);

        try {

            boolean ResultadoDemodificacion =
                    academicoEvaluadorDAO.modificarAcademicoEvaluador(academicoEvaluadorInexistente);
            assertFalse(ResultadoDemodificacion,
                    "No se debería modificar un académico inexistente.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba excepción: " +e);
        }
    }

    @Test
    void testBuscarAcademicoPorNumeroDePersonalConDatosValidos() {

        AcademicoEvaluadorDTO AcademicoEvaluadorEsperado = new AcademicoEvaluadorDTO(1001,
                IDS_USUARIOS_INSERTADOS.get(0), "Nombre", "Prueba", 1);

        try {

            AcademicoEvaluadorDTO academicoEncontrado =
                    academicoEvaluadorDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(AcademicoEvaluadorEsperado.
                            getNumeroDePersonal());
            assertEquals(AcademicoEvaluadorEsperado, academicoEncontrado,
                    "El académico debería ser encontrado correctamente.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testBuscarAcademicoPorNumeroDePersonalConDatosInvalidos() {

        int numeroInexistente = -1;
        AcademicoEvaluadorDTO academicoEvaluadorEsperado = new AcademicoEvaluadorDTO(-1, -1, "N/A", "N/A", 0);

        try {

            AcademicoEvaluadorDTO academicoEncontrado =
                    academicoEvaluadorDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(numeroInexistente);
            assertEquals(academicoEvaluadorEsperado, academicoEncontrado,
                    "Debería retornar un DTO con valores por defecto");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void buscarAcademicoEvaluadorPorNumeroDePersonalInexistente() {

        int numeroDePersonalInexistente = 77777;
        AcademicoEvaluadorDTO academicoEvaluadorEsperado = new AcademicoEvaluadorDTO(-1, -1, "N/A", "N/A", 0);

        try {

            AcademicoEvaluadorDTO resultado =
                    academicoEvaluadorDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(numeroDePersonalInexistente);
            assertEquals(academicoEvaluadorEsperado, resultado,
                    "Debería retornar un DTO con valores por defecto para un académico inexistente");

        } catch (SQLException e) {

            fail("No se esperaba una excepción: " + e);
            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            fail("No se esperaba una excepción: " + e);
            manejadorExcepciones.manejarIOException(e);
        }
    }

    @Test
    void buscarAcademicoEvaluadorPorIDValido() {

        try {

            int idValido = IDS_USUARIOS_INSERTADOS.get(0);
            AcademicoEvaluadorDTO academicoEsperado = new AcademicoEvaluadorDTO(1001, idValido, "Nombre",
                    "Prueba", 1);

            AcademicoEvaluadorDTO academicoEncontrado = academicoEvaluadorDAO.buscarAcademicoEvaluadorPorID(idValido);
            assertEquals(academicoEsperado, academicoEncontrado,
                    "El académico encontrado debería ser igual al esperado según equals()");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void buscarAcademicoEvaluadorPorIDInvalido() {

        int idInvalido = -1;
        AcademicoEvaluadorDTO academicoEsperado = new AcademicoEvaluadorDTO(-1, -1, "N/A", "N/A", 0);

        try {

            AcademicoEvaluadorDTO resultado = academicoEvaluadorDAO.buscarAcademicoEvaluadorPorID(idInvalido);
            assertEquals(academicoEsperado, resultado,
                    "Debería retornar un DTO con valores por defecto para un ID inválido");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " +e);
        }
    }

    @Test
    void buscarAcademicoEvaluadorPorIDInexistente() {

        int idInexistente = 9999;
        AcademicoEvaluadorDTO academicoEsperado = new AcademicoEvaluadorDTO(-1, -1, "N/A", "N/A", 0);

        try {

            AcademicoEvaluadorDTO resultado = academicoEvaluadorDAO.buscarAcademicoEvaluadorPorID(idInexistente);
            assertEquals(academicoEsperado, resultado,
                    "Debería retornar un DTO con valores por defecto para un ID inexistente");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testListarAcademicosConDatos() {

        try {

            List<AcademicoEvaluadorDTO> listaEsperada = new ArrayList<>();
            listaEsperada.add(new AcademicoEvaluadorDTO(1001, IDS_USUARIOS_INSERTADOS.get(0),
                    "Nombre", "Prueba", 1));
            listaEsperada.add(new AcademicoEvaluadorDTO(1002, IDS_USUARIOS_INSERTADOS.get(1),
                    "NombrePrueba", "Apellido", 1));
            listaEsperada.add(new AcademicoEvaluadorDTO(1003, IDS_USUARIOS_INSERTADOS.get(2),
                    "UsuarioDePrueba", "apellido", 1));

            List<AcademicoEvaluadorDTO> listaObtenida = academicoEvaluadorDAO.listarAcademicos();

            assertTrue(listaObtenida.containsAll(listaEsperada),
                    "La lista de académicos obtenida debería contener los datos esperados.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testListarAcademicosSinDatos() {

        try {

            for (int numeroDePersonal : NUMEROS_DE_PERSONAL_INSERTADOS) {

                academicoEvaluadorDAO.eliminarAcademicoEvaluadorPorNumeroDePersonal(numeroDePersonal);
            }

            List<AcademicoEvaluadorDTO> listaAcademicosDTOs = academicoEvaluadorDAO.listarAcademicos();
            assertEquals(0, listaAcademicosDTOs.size(),
                    "La lista de académicos evaluadores debería estar vacía.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " +e);
        }
    }
}
