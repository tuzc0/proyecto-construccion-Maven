package logica;

import GUI.utilidades.Utilidades;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static logica.verificacion.VerificicacionGeneral.validar;

public class VerificacionUsuario {

    private static final Logger logger = LogManager.getLogger(VerificacionUsuario.class);
    private static final Utilidades utilidades = new Utilidades();

    private static final Pattern PATRON_CORREO_UV = Pattern.compile("^(zS\\d{8}@estudiantes\\.uv\\.mx|[a-z]+@uv\\.mx)$");
    private static final Pattern PATRON_CORREO = Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
    private static final Pattern PATRON_NUMERO_PERSONAL = Pattern.compile("^\\d{5}$");
    private static final Pattern PATRON_MATRICULA = Pattern.compile("^S\\d{8}$");
    private static final Pattern PATRON_CONTRASENA = Pattern.compile("^[a-zA-Z0-9]{6,64}$");
    private static final Pattern PATRON_NOMBRE = Pattern.compile("^[\\p{L}\\s]+$");

    public static boolean correoValidoUV(String correo) {

        return validar(correo, PATRON_CORREO_UV);
    }

    public static boolean correoValido(String correo) {

        return validar(correo, PATRON_CORREO);
    }

    public static boolean numeroPersonalValido(String numeroPersonal) {

        return validar(numeroPersonal, PATRON_NUMERO_PERSONAL);
    }

    public static boolean matriculaValida(String matricula) {

        return validar(matricula, PATRON_MATRICULA);
    }

    public static boolean contrasenaValida(String contrasena) {

        return validar(contrasena, PATRON_CONTRASENA);
    }

    public static boolean nombreValido(String nombre) {

        return validar(nombre, PATRON_NOMBRE);
    }

    public static boolean apellidosValidos(String apellidos) {

        return validar(apellidos, PATRON_NOMBRE);
    }

    public static List<String> camposVacios(String nombre, String apellidos, String numeroPersonal, String correo, String contrasena) {

        List<String> errores = new ArrayList<>();

        if (nombre.isEmpty()) {
            errores.add("El nombre no puede estar vacío.");
        }

        if (apellidos.isEmpty()) {
            errores.add("Los apellidos no pueden estar vacíos.");
        }

        if (numeroPersonal.isEmpty()) {
            errores.add("El número de personal no puede estar vacío.");
        }

        if (correo.isEmpty()) {
            errores.add("El correo electrónico no puede estar vacío.");
        }

        if (contrasena.isEmpty()) {
            errores.add("La contraseña no puede estar vacía.");
        }

        return errores;
    }

    public static List<String> validarCampos(String nombre, String apellidos, String numeroPersonalTexto, String correo, String contrasena) {

        List<String> errores = new ArrayList<>();

        if (!nombreValido(nombre)) {

            errores.add("El nombre no es válido.");
        }

        if (!apellidosValidos(apellidos)) {

            errores.add("Los apellidos no son válidos.");
        }

        if (!numeroPersonalValido(numeroPersonalTexto)) {

            errores.add("El número de personal no es válido.");
        }

        if (!correoValidoUV(correo)) {

            errores.add("El correo electrónico no es válido. \n");
        }

        if (!contrasenaValida(contrasena)) {

            errores.add("La contraseña no es válida. Verifique que contenga entre 6 y 64 caracteres alfanuméricos.");
        }

        return errores;
    }
}