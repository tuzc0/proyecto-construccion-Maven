package GUI.utilidades;

public class VerificacionUsuario {

    public static boolean correoValido(String correo) {
        String patron = "^zS\\d{8}@estudiantes\\.uv\\.mx$";
        return correo.matches(patron);
    }

    public static boolean numeroPersonalValido(String numeroPersonal) {
        String patron = "^\\d{5}$";
        return numeroPersonal.matches(patron);
    }

    public static boolean matriculaValida(String matricula) {
        String patron = "^S\\d{8}$";
        return matricula.matches(patron);
    }

    public static boolean contrasenaValida(String contrasena) {
        String patron = "^\\[a-zA-Z0-9]{8}$";
        return contrasena.matches(patron);
    }

    public static boolean nombreValido(String nombre) {
        String patron = "^[a-zA-Z\\s]+$";
        return nombre.matches(patron);
    }

    public static boolean apellidosValidos(String apellidos) {
        String patron = "^[a-zA-Z\\s]+$";
        return apellidos.matches(patron);
    }
}
