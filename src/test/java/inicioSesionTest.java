//import logica.DAOs.CuentaDAO;
//import logica.DAOs.UsuarioDAO;
//import logica.DTOs.CuentaDTO;
//import logica.DTOs.UsuarioDTO;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.fail;
//
//public class inicioSesionTest {
//
//    @BeforeEach
//    void prepeararDatosPrueba(){
//
//        UsuarioDAO usuarioDAO = new UsuarioDAO();
//        CuentaDAO cuentaDAO = new CuentaDAO();
//
//        UsuarioDTO usuarioDTO = new UsuarioDTO();
//        CuentaDTO cuentaDTO = new CuentaDTO();
//
//        usuarioDTO.setIdUsuario(1);
//        usuarioDTO.setNombre("Juan");
//        usuarioDTO.setApellido("Perez");
//
//        cuentaDTO.setIdUsuario(1);
//        cuentaDTO.setCorreoElectronico("hola@gmail.com");
//        cuentaDTO.setContrasena("12345678");
//
//        try{
//            usuarioDAO.insertarUsuario(usuarioDTO);
//            cuentaDAO.crearNuevaCuenta(cuentaDTO);
//        } catch (SQLException e) {
//
//            fail("Error SQL " + e);
//
//        } catch (IOException e) {
//
//            fail("Error de IO al insertar datos de prueba: " + e);
//
//        } catch (Exception e) {
//
//            fail("Error inesperado al insertar datos de prueba: " + e);
//
//        }
//
//    }
//
//    @Test
//    void testBuscarCuentaPorCorreoConCorreoExistente() {
//
//        try {
//
//            UsuarioDTO usuario = new UsuarioDTO(0, "Laura", "Gómez", 1);
//            int idUsuario = usuarioDAO.insertarUsuario(usuario);
//            IDS_USUARIOS_INSERTADOS.add(idUsuario);
//
//            CuentaDTO cuenta = new CuentaDTO("laura@correo.com", "12345", idUsuario);
//            cuentaDAO.crearNuevaCuenta(cuenta);
//
//            CuentaDTO cuentaEncontrada = cuentaDAO.buscarCuentaPorCorreo("laura@correo.com");
//            assertEquals(cuenta, cuentaEncontrada, "La cuenta encontrada debería coincidir con la esperada.");
//
//        } catch (SQLException | IOException e) {
//
//            fail("No se esperaba excepción: " + e);
//        }
//    }
//
//    @Test
//    public void testInicioSesion() {
//
//        try {
//
//            UsuarioDTO usuario = new UsuarioDTO(0, "Juan", "Perez", 1);
//            int idUsuario = usuarioDAO.insertarUsuario(usuario);
//
//        }
//
//    }
//
//
//}
