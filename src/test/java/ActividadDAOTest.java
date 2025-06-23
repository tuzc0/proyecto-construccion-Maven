import accesoadatos.ConexionBaseDeDatos;
import logica.DTOs.ActividadDTO;
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import logica.DAOs.ActividadDAO;
import utilidadesPruebas.UtilidadesConsola;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActividadDAOTest {

    private static final Logger LOGGER = LogManager.getLogger(ActividadDAOTest.class);

    private ActividadDAO actividadDAO;

    private static Connection conexionBaseDeDatos;

    private IGestorAlertas gestorAlertas;

    private ManejadorExcepciones manejadorExcepciones;

    private final List<Integer> IDS_ACTIVIDADES_CREADAS = new ArrayList<>();


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

        actividadDAO = new ActividadDAO();
        IGestorAlertas utilidadesConsola = new UtilidadesConsola();
        manejadorExcepciones = new ManejadorExcepciones(utilidadesConsola, LOGGER);

        try {

            IDS_ACTIVIDADES_CREADAS.clear();

            for (int idActividad : List.of(1001, 1002, 1003, 1004)) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM actividad WHERE IDActividad = " + idActividad)
                        .executeUpdate();
            }

            String insercionActividad = "INSERT INTO actividad (IDActividad, nombre, duracion, hitos, fechaInicio, " +
                    "fechaFin, estadoActivo) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertarActividad = conexionBaseDeDatos.prepareStatement(insercionActividad);
            insertarActividad.setInt(1, 1001);
            insertarActividad.setString(2, "Actividad de Prueba 1");
            insertarActividad.setString(3, "1 mes");
            insertarActividad.setString(4, "Hito 1, Hito 2");
            insertarActividad.setDate(5, Date.valueOf("2023-01-01"));
            insertarActividad.setDate(6, Date.valueOf("2023-02-01"));
            insertarActividad.setInt(7, 1);
            insertarActividad.executeUpdate();
            IDS_ACTIVIDADES_CREADAS.add(1001);

            insertarActividad.setInt(1, 1002);
            insertarActividad.setString(2, "Actividad de Prueba 2");
            insertarActividad.setString(3, "2 semanas");
            insertarActividad.setString(4, "Hito único");
            insertarActividad.setDate(5, Date.valueOf("2023-03-01"));
            insertarActividad.setDate(6, Date.valueOf("2023-03-15"));
            insertarActividad.setInt(7, 1);
            insertarActividad.executeUpdate();
            IDS_ACTIVIDADES_CREADAS.add(1002);

            insertarActividad.setInt(1, 1003);
            insertarActividad.setString(2, "Actividad Inactiva");
            insertarActividad.setString(3, "3 días");
            insertarActividad.setString(4, "");
            insertarActividad.setDate(5, Date.valueOf("2023-04-01"));
            insertarActividad.setDate(6, Date.valueOf("2023-04-04"));
            insertarActividad.setInt(7, 0);
            insertarActividad.executeUpdate();
            IDS_ACTIVIDADES_CREADAS.add(1003);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Error al preparar los datos de prueba: " + e);
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try {

            for (int idActividad : IDS_ACTIVIDADES_CREADAS) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM actividad WHERE IDActividad = " + idActividad)
                        .executeUpdate();
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("Error al limpiar los datos de prueba: " + e);
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
    @DisplayName("Crear actividad con datos válidos")
    void testCrearActividadConDatosValidos() {

        try {

            ActividadDTO nuevaActividad = new ActividadDTO(0, "Nueva Actividad Válida", "3 semanas",
                    "Hito A, Hito B", new Date(System.currentTimeMillis()),
                    new Date(System.currentTimeMillis() + 2000000000), 1);

            int idGenerado = actividadDAO.crearNuevaActividad(nuevaActividad);

            assertTrue(idGenerado > 0, "Debería retornar un ID válido");
            IDS_ACTIVIDADES_CREADAS.add(idGenerado);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " +e);
        }
    }

    @Test
    @DisplayName("Crear actividad con datos vacíos")
    void testCrearActividadConDatosVacios() {

        ActividadDTO actividadVacia = new ActividadDTO(0, null, null, null,
                null, null, 1);

        assertThrows(SQLException.class, () -> {

            actividadDAO.crearNuevaActividad(actividadVacia);
        }, "Debería lanzar excepción con datos vacíos");
    }

    @Test
    @DisplayName("Buscar actividad existente")
    void testBuscarActividadExistente() {

        ActividadDTO actividadEsperada = new ActividadDTO(1003, "Actividad Inactiva", "3 días",
                "", Date.valueOf("2023-04-01"), Date.valueOf("2023-04-04"), 0);

        try {

            ActividadDTO actividadObtenida = actividadDAO.buscarActividadPorID(1003);
            assertEquals(actividadEsperada, actividadObtenida, "Debería retornar un DTO igual al esperado.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " +e);
        }
    }

    @Test
    @DisplayName("Buscar actividad inexistente")
    void testBuscarActividadInexistente() {

        ActividadDTO actividadEsperada = new ActividadDTO(-1, "N/A", "N/A",
                "N/A", null, null, 0);

        try {

            ActividadDTO actividadObtenida = actividadDAO.buscarActividadPorID(9999);
            assertEquals(actividadEsperada, actividadObtenida,
                    "Debería retornar un DTO igual al esperado.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " +e);
        }
    }

    @Test
    @DisplayName("Modificar actividad con datos válidos")
    void testModificarActividadConDatosValidos() {

        try {

            ActividadDTO actividadModificada = new ActividadDTO(1001, "Nombre Modificado",
                    "Duración Modificada", "Hitos Modificados",
                    new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000000000), 1);

            boolean resultado = actividadDAO.modificarActividad(actividadModificada);
            assertTrue(resultado, "Debería modificar correctamente");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " +e);
        }
    }

    @Test
    @DisplayName("Modificar actividad con datos vacíos")
    void testModificarActividadConDatosVacios() {

        ActividadDTO actividadVacia = new ActividadDTO(1001, null, null, null,
                null, null, 1);

        assertThrows(SQLException.class, () -> {

            actividadDAO.modificarActividad(actividadVacia);
        }, "Debería lanzar excepción con datos vacíos");
    }

    @Test
    @DisplayName("Eliminar actividad existente")
    void testEliminarActividadExistente() {

        try {

            boolean resultado = actividadDAO.eliminarActividadPorID(1001);
            assertTrue(resultado, "Debería eliminar correctamente");

            ActividadDTO actividadEliminada = actividadDAO.buscarActividadPorID(1001);
            assertEquals(0, actividadEliminada.getEstadoActivo());

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    @DisplayName("Eliminar actividad inexistente")
    void testEliminarActividadInexistente() {

        try {

            boolean resultado = actividadDAO.eliminarActividadPorID(9999);
            assertFalse(resultado, "No debería eliminar actividad inexistente");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            fail("No se esperaba una excepción: " + e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            fail("No se esperaba una excepción: " + e);
        }
    }
}