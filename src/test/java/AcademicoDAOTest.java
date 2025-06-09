import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.UsuarioDTO;
import org.junit.jupiter.api.*;
import logica.DAOs.AcademicoDAO;
import logica.DTOs.AcademicoDTO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AcademicoDAOTest {

    private AcademicoDAO academicoDAO;
    private UsuarioDAO usuarioDAO;
    private Connection conexionBaseDeDatos;

    private final List<Integer> NUMEROS_DE_PERSONAL_INSERTADOS = new ArrayList<>();
    private final List<Integer> IDS_USUARIOS_INSERTADOS = new ArrayList<>();

    @BeforeEach
    void prepararDatosDePrueba() {

        academicoDAO = new AcademicoDAO();
        usuarioDAO = new UsuarioDAO();

        try {

            NUMEROS_DE_PERSONAL_INSERTADOS.clear();
            IDS_USUARIOS_INSERTADOS.clear();

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            for (int numeroDePersonal : List.of(1001, 1002, 1003, 55555, 99999, 88888)) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM academico WHERE numeroDePersonal = " + numeroDePersonal)
                        .executeUpdate();
            }

            conexionBaseDeDatos
                    .prepareStatement("DELETE FROM usuario WHERE idUsuario BETWEEN 1000 AND 2000")
                    .executeUpdate();

            UsuarioDTO usuarioAcademico1DTO = new UsuarioDTO(0, "Nombre", "Prueba", 1);
            UsuarioDTO usuarioAcadmico2DTO = new UsuarioDTO(0, "Usuario2", "Apellido2", 1);
            UsuarioDTO usuarioAcademico3DTO = new UsuarioDTO(0, "Usuario3", "Apellido3", 1);

            int idUsuarioAcademico1 = usuarioDAO.insertarUsuario(usuarioAcademico1DTO);
            int idUsuarioAcademico2 = usuarioDAO.insertarUsuario(usuarioAcadmico2DTO);
            int idUsuarioAcademico3 = usuarioDAO.insertarUsuario(usuarioAcademico3DTO);

            IDS_USUARIOS_INSERTADOS.addAll(List.of(idUsuarioAcademico1, idUsuarioAcademico2, idUsuarioAcademico3));

            academicoDAO.insertarAcademico(new AcademicoDTO(1001, idUsuarioAcademico1, usuarioAcademico1DTO.getNombre(),
                    usuarioAcademico1DTO.getApellido(), usuarioAcademico1DTO.getEstado()));

            academicoDAO.insertarAcademico(new AcademicoDTO(1002, idUsuarioAcademico2, usuarioAcadmico2DTO.getNombre(),
                    usuarioAcadmico2DTO.getApellido(), usuarioAcadmico2DTO.getEstado()));

            academicoDAO.insertarAcademico(new AcademicoDTO(1003, idUsuarioAcademico3, usuarioAcademico3DTO.getNombre(),
                    usuarioAcademico3DTO.getApellido(), usuarioAcademico3DTO.getEstado()));

            NUMEROS_DE_PERSONAL_INSERTADOS.addAll(List.of(1001, 1002, 1003));

        } catch (SQLException | IOException e) {

            fail("Error en @BeforeEach al preparar datos: " + e);
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try {

            for (int numeroDePersonal : NUMEROS_DE_PERSONAL_INSERTADOS) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM academico WHERE numeroDePersonal = " + numeroDePersonal)
                        .executeUpdate();
            }

            for (int idUsuario : IDS_USUARIOS_INSERTADOS) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM usuario WHERE idUsuario = " + idUsuario)
                        .executeUpdate();
            }

            conexionBaseDeDatos.close();

        } catch (SQLException e) {

            fail("Error en @AfterEach al limpiar datos: " + e);
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

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
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

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testEliminarAcademicoPorNumeroDePersonalConNumeroDePersonalInexistente() {

        int numeroDePersonalInexistente = 88888;

        try {

            boolean resultadoDeEliminarAcademico = academicoDAO.eliminarAcademicoPorNumeroDePersonal(numeroDePersonalInexistente);
            assertFalse(resultadoDeEliminarAcademico, "No debería eliminarse ningún académico inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testEliminarAcademicoPorNumeroDePersonalInvalido() {

        int numeroDePersonalInexistente = -1;

        try {

            boolean resultado = academicoDAO.eliminarAcademicoPorNumeroDePersonal(numeroDePersonalInexistente);
            assertFalse(resultado, "No debería poder eliminar un académico inexistente");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
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

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testModificarAcademicoConDatosInvalidos() {

        AcademicoDTO academicoDatosInvalido = new AcademicoDTO(1003, -100,
                "Nombre", "Apellido", 1);

        try {

            boolean resultadoAcademicoModificado = academicoDAO.modificarAcademico(academicoDatosInvalido);
            assertFalse(resultadoAcademicoModificado, "No debería modificarse un académico con datos inválidos.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testModificarAcademicoInexistente() {

        AcademicoDTO academicoInexistente = new AcademicoDTO(77777, 9999, "X", "Y", 1);

        try {

            boolean resultadoModificacion = academicoDAO.modificarAcademico(academicoInexistente);
            assertFalse(resultadoModificacion, "No se debería modificar un académico inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e);
        }
    }

    @Test
    void testBuscarAcademicoPorNumeroDePersonalConDatosValidos() {

        AcademicoDTO academicoEsperado = new AcademicoDTO(1001, IDS_USUARIOS_INSERTADOS.get(0),
                "Nombre", "Prueba", 1);

        try {

            AcademicoDTO academicoEncontrado = academicoDAO.buscarAcademicoPorNumeroDePersonal(academicoEsperado.getNumeroDePersonal());
            assertEquals(academicoEsperado, academicoEncontrado, "El académico debería ser encontrado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testBuscarAcademicoPorNumeroDePersonalConDatosInvalidos() {

        int numeroDePersonalInexistente = -1;

        try {

            AcademicoDTO academicoEncontrado = academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroDePersonalInexistente);
            assertEquals(-1, academicoEncontrado.getNumeroDePersonal(), "No debería encontrarse un académico con ese número.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testBuscarAcademicoPorNumeroDePersonalInexistente() {

        int numeroDePersonalInexistente = 99999;

        try {

            AcademicoDTO resultado = academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroDePersonalInexistente);
            assertEquals(-1, resultado.getNumeroDePersonal(), "Debería retornar un DTO con valores por defecto");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
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

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testListarAcademicosSinDatos() {

        try {

            for (int numeroDePersonal : NUMEROS_DE_PERSONAL_INSERTADOS) {

                academicoDAO.eliminarAcademicoPorNumeroDePersonal(numeroDePersonal);
            }

            List<AcademicoDTO> listaAcademicosDTOs = academicoDAO.listarAcademicos();
            assertEquals(0, listaAcademicosDTOs.size(), "La lista de académicos evaluadores debería estar vacía.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testBuscarAcademicoPorIDValido() {

        try {

            UsuarioDTO usuarioDTO = new UsuarioDTO(0, "TestIDValido", "ApellidoTest", 1);
            int idUsuario = usuarioDAO.insertarUsuario(usuarioDTO);
            IDS_USUARIOS_INSERTADOS.add(idUsuario);

            AcademicoDTO academicoEsperado = new AcademicoDTO(12345, idUsuario,
                    usuarioDTO.getNombre(), usuarioDTO.getApellido(), usuarioDTO.getEstado());

            academicoDAO.insertarAcademico(academicoEsperado);
            NUMEROS_DE_PERSONAL_INSERTADOS.add(12345);

            AcademicoDTO academicoEncontrado = academicoDAO.buscarAcademicoPorID(idUsuario);
            assertEquals(academicoEsperado, academicoEncontrado, "Debería encontrar el académico con el ID proporcionado");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testBuscarAcademicoPorIDInvalido() {

        int idUsuarioInvalido = -1;

        try {

            AcademicoDTO resultado = academicoDAO.buscarAcademicoPorID(idUsuarioInvalido);
            assertEquals(-1, resultado.getIdUsuario(), "Debería retornar un DTO con valores por defecto");

        } catch (SQLException | IOException e) {
            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testBuscarAcademicoPorIDInexistente() {

        int idUsuarioInexistente = 9999;

        try {

            AcademicoDTO resultado = academicoDAO.buscarAcademicoPorID(idUsuarioInexistente);
            assertEquals(-1, resultado.getIdUsuario(), "Debería retornar un DTO con valores por defecto");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }
}
