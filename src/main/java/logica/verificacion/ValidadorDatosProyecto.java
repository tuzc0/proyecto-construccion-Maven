package logica.verificacion;

import logica.DTOs.ProyectoDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static logica.verificacion.VerificicacionGeneral.validar;

public class ValidadorDatosProyecto {

    private static final Pattern PATRON_USUARIOS_DIRECTOS_INDIRECTOS_ESTUDIANTES_REQUERIDOS =
            Pattern.compile("^[0-9]+$");

    private static final Pattern PATRON_NOMBRE_PROYECTO = Pattern.compile("^[\\w\\s\\p{Punct}áéíóúÁÉÍÓÚñÑ]+$");

    private static boolean usuarioValido(String numeroUsuarios) {

        return validar(numeroUsuarios, PATRON_USUARIOS_DIRECTOS_INDIRECTOS_ESTUDIANTES_REQUERIDOS);
    }

    private boolean validarNombreProyecto(String nombre) {
        return validar(nombre, PATRON_NOMBRE_PROYECTO) && nombre.length() <= 150;
    }

    private boolean validarTextoCamposProyecto(String texto) {

        String[] palabras = texto.trim().split("\\s+");
        return palabras.length >= 3 && texto.length() <= 255;
    }

    public List<String> camposNumericosInvalidos(String usuariosDirectos, String usuariosIndirectos,
                                                 String estudiantesRequeridos) {

        List<String> errores = new ArrayList<>();

        if (!usuarioValido(usuariosDirectos)) {

            errores.add("El campo de usuarios directos debe ser un número entero positivo.");
        }

        if (!usuarioValido(usuariosIndirectos)) {

            errores.add("El campo de usuarios indirectos debe ser un número entero positivo.");
        }

        if (!usuarioValido(estudiantesRequeridos) || Integer.parseInt(estudiantesRequeridos) <= 0) {

            errores.add("El campo de estudiantes requeridos debe ser un número entero positivo mayor a cero.");
        }

        return errores;
    }

    public List<String> camposVaciosProyecto(ProyectoDTO proyectoDTO) {

        List<String> errores = new ArrayList<>();

        if (proyectoDTO.getNombre() == null || proyectoDTO.getNombre().isEmpty()) {
            errores.add("El campo de nombre no puede estar vacío.");
        }

        if (proyectoDTO.getDescripcion() == null || proyectoDTO.getDescripcion().isEmpty()) {
            errores.add("El campo descripción general no puede estar vacío.");
        }

        if (proyectoDTO.getObjetivoGeneral() == null || proyectoDTO.getObjetivoGeneral().isEmpty()) {
            errores.add("El campo de objetivos generales no puede estar vacío.");
        }

        if (proyectoDTO.getObjetivosInmediatos() == null || proyectoDTO.getObjetivosInmediatos().isEmpty()) {
            errores.add("El campo de objetivos inmediatos no puede estar vacío.");
        }

        if (proyectoDTO.getObjetivosMediatos() == null || proyectoDTO.getObjetivosMediatos().isEmpty()) {
            errores.add("El campo de objetivos mediatos no puede estar vacío.");
        }

        if (proyectoDTO.getMetodologia() == null || proyectoDTO.getMetodologia().isEmpty()) {
            errores.add("El campo de metodología no puede estar vacío.");
        }

        if (proyectoDTO.getRecursos() == null || proyectoDTO.getRecursos().isEmpty()) {
            errores.add("El campo de recursos no puede estar vacío.");
        }

        if (proyectoDTO.getActividades() == null || proyectoDTO.getActividades().isEmpty()) {
            errores.add("El campo de actividades no puede estar vacío.");
        }

        if (proyectoDTO.getResponsabilidades() == null || proyectoDTO.getResponsabilidades().isEmpty()) {
            errores.add("El campo de responsabilidades no puede estar vacío.");
        }

        return errores;
    }

    public List<String> validarCamposProyecto(ProyectoDTO proyectoDTO) {

        List<String> errores = new ArrayList<>();

        if (!validarNombreProyecto(proyectoDTO.getNombre())) {
            errores.add("El campo de nombre debe tener una o más palabras y no exceder los 150 caracteres.");
        }

        if (!validarTextoCamposProyecto(proyectoDTO.getDescripcion())) {
            errores.add("El campo de descripción general debe tener 3 o más palabras y no exceder los 255 caracteres.");
        }

        if (!validarTextoCamposProyecto(proyectoDTO.getObjetivoGeneral())) {
            errores.add("El campo de objetivos generales debe tener 3 o más palabras y no exceder los 255 caracteres.");
        }

        if (!validarTextoCamposProyecto(proyectoDTO.getObjetivosInmediatos())) {
            errores.add("El campo de objetivos inmediatos debe tener 3 o más palabras y no exceder los 255 caracteres.");
        }

        if (!validarTextoCamposProyecto(proyectoDTO.getObjetivosMediatos())) {
            errores.add("El campo de objetivos mediatos debe tener 3 o más palabras y no exceder los 255 caracteres.");
        }

        if (!validarTextoCamposProyecto(proyectoDTO.getMetodologia())) {
            errores.add("El campo de metodología debe tener 3 o más palabras y no exceder los 255 caracteres.");
        }

        if (!validarTextoCamposProyecto(proyectoDTO.getRecursos())) {
            errores.add("El campo de recursos debe tener 3 o más palabras y no exceder los 255 caracteres.");
        }

        if (!validarTextoCamposProyecto(proyectoDTO.getActividades())) {
            errores.add("El campo de actividades debe tener 3 o más palabras y no exceder los 255 caracteres.");
        }

        if (!validarTextoCamposProyecto(proyectoDTO.getResponsabilidades())) {
            errores.add("El campo de responsabilidades debe tener 3 o más palabras y no exceder los 255 caracteres.");
        }

        return errores;
    }
}
