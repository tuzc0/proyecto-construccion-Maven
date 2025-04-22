

import logica.DAOs.UsuarioDAO;
import logica.DTOs.UsuarioDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDAOTest {

    private UsuarioDAO usuarioDAO;

    @BeforeEach
    void setUp() {
        usuarioDAO = new UsuarioDAO();
    }

    @AfterEach
    void tearDown() {
        usuarioDAO = null;
    }

    @Test
    void testInsertarUsuario() {
        UsuarioDTO usuario = new UsuarioDTO(0, "John", "Doe", 1);

        try {
            int idUsuario = usuarioDAO.insertarUsuario(usuario);
            assertEquals(19, idUsuario, "El ID del usuario insertado debe coincidir con el esperado.");
        } catch (SQLException | IOException e) {
            fail("Ocurrió una excepción durante la prueba: " + e.getMessage());
        }
    }
}