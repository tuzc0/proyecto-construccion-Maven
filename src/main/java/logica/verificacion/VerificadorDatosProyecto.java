package logica.verificacion;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import static logica.verificacion.VerificicacionGeneral.validar;

public class VerificadorDatosProyecto {

    private static final Pattern PATRON_USUARIOS_DIRECTOS_INDIRECTOS_ESTUDIANTES_REQUERIDOS =
            Pattern.compile("^[0-9]+$");

    private static boolean usuarioValido(String numeroUsuarios) {

        return validar(numeroUsuarios, PATRON_USUARIOS_DIRECTOS_INDIRECTOS_ESTUDIANTES_REQUERIDOS);
    }

    private boolean validarNombreProyecto(String nombre) {

        String[] palabras = nombre.trim().split("\\s+");
        return palabras.length >= 1 && nombre.length() <= 150;
    }

    private boolean validarTextoCamposProyecto(String texto) {

        String[] caracteres = texto.trim().split("\\s+");
        return caracteres.length >= 3 && texto.length() <= 255;
    }

    public List<String> camposVaciosProyecto(String campoNombre,
                                             String campoDescripcionGeneral,
                                             String campoObjetivosGenerales,
                                             String campoObjetivosInmediatos,
                                             String campoObjetivosMediatos,
                                             String campoMetodologia,
                                             String campoRecursos,
                                             String campoActividades,
                                             String campoResponsabilidades,
                                             String usuariosDirectos,
                                             String usuariosIndirectos,
                                             String estudiantesRequeridos)
    {

        List<String> errores = new ArrayList<>();

        if (campoNombre.isEmpty()) {
            errores.add("El campo de nombre no puede estar vacío.");
        }

        if (campoDescripcionGeneral.isEmpty()) {
            errores.add("El campo descripcion general no puede estar vacío.");
        }

        if (campoObjetivosGenerales.isEmpty()) {
            errores.add("El campo de objetivos generales no puede estar vacío.");
        }

        if (campoObjetivosInmediatos.isEmpty()) {
            errores.add("El campo de objetivos inmediatos no puede estar vacío.");
        }

        if (campoObjetivosMediatos.isEmpty()) {
            errores.add("El campo de objetivos mediatos no puede estar vacío.");
        }

        if (campoMetodologia.isEmpty()) {
            errores.add("El campo de metodologia no puede estar vacío.");
        }

        if (campoRecursos.isEmpty()) {
            errores.add("El campo de recursos no puede estar vacío.");
        }

        if (campoActividades.isEmpty()) {
            errores.add("El campo de actividades no puede estar vacío.");
        }

        if (campoResponsabilidades.isEmpty()) {
            errores.add("El campo de responsabilidades no puede estar vacío.");
        }

        if (usuariosDirectos.isEmpty()) {
            errores.add("El campo de usuarios directos no puede estar vacío.");
        }

        if (usuariosIndirectos.isEmpty()) {
            errores.add("El campo de usuarios indirectos no puede estar vacío.");
        }

        if (estudiantesRequeridos.isEmpty()) {
            errores.add("El campo de estudiantes requeridos no puede estar vacío.");
        }

        return errores;
    }

    public List<String> validarCamposProyecto( String campoNombre,
                                               String campoDescripcionGeneral,
                                               String campoObjetivosGenerales,
                                               String campoObjetivosInmediatos,
                                               String campoObjetivosMediatos,
                                               String campoMetodologia,
                                               String campoRecursos,
                                               String campoActividades,
                                               String campoResponsabilidades,
                                               String usuariosDirectos,
                                               String usuariosIndirectos,
                                               String estudiantesRequeridos) {

        List<String> errores = new ArrayList<>();

        if (!validarNombreProyecto(campoNombre)) {
            errores.add("El campo de nombre debe tener una o más palabras " +
                    "y no exceder 150 caracteres.");
        }

        if (!validarTextoCamposProyecto(campoDescripcionGeneral)) {
            errores.add("El campo de descripcion general debe tener 3 o más palabras " +
                    "y no exceder 255 caracteres.");
        }

        if (!validarTextoCamposProyecto(campoObjetivosGenerales)) {
            errores.add("El campo de objetivos generales debe tener 3 o más palabras " +
                    "y no exceder 255 caracteres.");
        }

        if (!validarTextoCamposProyecto(campoObjetivosInmediatos)) {
            errores.add("El campo de objetivos inmediatos debe tener 3 o más palabras " +
                    "y no exceder 255 caracteres.");
        }

        if (!validarTextoCamposProyecto(campoObjetivosMediatos)) {
            errores.add("El campo de objetivos mediatos debe tener 3 o más palabras " +
                    "y no exceder 255 caracteres.");
        }

        if (!validarTextoCamposProyecto(campoMetodologia)) {
            errores.add("El campo de metodologia debe tener de 3 o más palabras " +
                    "y no exceder 255 caracteres.");
        }

        if (!validarTextoCamposProyecto(campoRecursos)) {
            errores.add("El campo de recursos debe tener de 3 o más palabras " +
                    "y no exceder 255 caracteres.");
        }

        if (!validarTextoCamposProyecto(campoActividades)) {
            errores.add("El campo de actividades debe tener de 3 o más palabras " +
                    "y no exceder 255 caracteres.");
        }

        if (!validarTextoCamposProyecto(campoResponsabilidades)) {
            errores.add("El campo de responsabilidades debe tener de 3 o más palabras " +
                    "y no exceder 255 caracteres.");
        }

        if(!usuarioValido(usuariosDirectos)) {
            errores.add("El campo de usuarios directos debe ser un número entero positivo.");
        }

        if(!usuarioValido(usuariosIndirectos)) {
            errores.add("El campo de usuarios indirectos debe ser un número entero positivo.");
        }

        if(!usuarioValido(estudiantesRequeridos)) {
            errores.add("El campo de estudiantes requeridos debe ser un número entero positivo");
        }

        return errores;
    }
}
