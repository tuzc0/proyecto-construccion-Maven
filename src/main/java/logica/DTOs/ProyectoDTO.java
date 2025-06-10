package logica.DTOs;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import static logica.verificacion.VerificicacionGeneral.validar;

public class ProyectoDTO {

    private int idProyecto;
    private String nombre;
    private String objetivoGeneral;
    private String objetivosInmediatos;
    private String objetivosMediatos;
    private String metodologia;
    private String recursos;
    private String actividades;
    private String responsabilidades;
    private String duracion;
    private int estadoActivo;
    private int idRepresentante;
    private String descripcion;
    private int usuariosDirectos;
    private int usuariosIndirectos;
    private int estudiantesRequeridos;
    private int estudiantesAsignados;

    public ProyectoDTO() {
    }

    public ProyectoDTO(int idProyecto, String nombre, String objetivoGeneral,
                       String objetivosInmediatos, String objetivosMediatos, String metodologia,
                       String recursos, String actividades, String responsabilidades, String duracion,
                       int estadoActivo, int idRepresentante, String descripcion,
                       int usuariosDirectos, int usuariosIndirectos, int estudiantesRequeridos) {

        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.objetivoGeneral = objetivoGeneral;
        this.objetivosInmediatos = objetivosInmediatos;
        this.objetivosMediatos = objetivosMediatos;
        this.metodologia = metodologia;
        this.recursos = recursos;
        this.actividades = actividades;
        this.responsabilidades = responsabilidades;
        this.duracion = duracion;
        this.estadoActivo = estadoActivo;
        this.idRepresentante = idRepresentante;
        this.descripcion = descripcion;
        this.usuariosDirectos = usuariosDirectos;
        this.usuariosIndirectos = usuariosIndirectos;
        this.estudiantesRequeridos = estudiantesRequeridos;
    }

    public int getIdProyecto() {

        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {

        this.idProyecto = idProyecto;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public String getObjetivoGeneral() {

        return objetivoGeneral;
    }

    public void setObjetivoGeneral(String objetivoGeneral) {

        this.objetivoGeneral = objetivoGeneral;
    }

    public String getObjetivosInmediatos() {

        return objetivosInmediatos;
    }

    public void setObjetivosInmediatos(String objetivosInmediatos) {

        this.objetivosInmediatos = objetivosInmediatos;
    }

    public String getObjetivosMediatos() {

        return objetivosMediatos;
    }

    public void setObjetivosMediatos(String objetivosMediatos) {

        this.objetivosMediatos = objetivosMediatos;
    }

    public String getMetodologia() {

        return metodologia;
    }

    public void setMetodologia(String metodologia) {

        this.metodologia = metodologia;
    }

    public String getRecursos() {

        return recursos;
    }

    public void setRecursos(String recursos) {

        this.recursos = recursos;
    }

    public String getActividades() {

        return actividades;
    }

    public void setActividades(String actividades) {

        this.actividades = actividades;
    }

    public String getResponsabilidades() {

        return responsabilidades;
    }

    public void setResponsabilidades(String responsabilidades) {

        this.responsabilidades = responsabilidades;
    }

    public String getDuracion() {

        return duracion;
    }

    public void setDuracion(String duracion) {

        this.duracion = duracion;
    }

    public int getEstadoActivo() {

        return estadoActivo;
    }

    public void setEstadoActivo(int estadoActivo) {

        this.estadoActivo = estadoActivo;
    }

    public int getIdRepresentante() {

        return idRepresentante;}

    public void setIdRepresentante(int idRepresentante) {

        this.idRepresentante = idRepresentante;
    }

    public String getDescripcion() {

        return descripcion; }

    public void setDescripcion(String descripcion) {

        this.descripcion = descripcion;
    }

    public int getUsuariosDirectos() {

        return usuariosDirectos;
    }

    public void setUsuariosDirectos(int usuariosDirectos) {

        this.usuariosDirectos = usuariosDirectos;
    }

    public int getUsuariosIndirectos() {

        return usuariosIndirectos;
    }

    public void setUsuariosIndirectos(int usuariosIndirectos) {

        this.usuariosIndirectos = usuariosIndirectos;
    }

    public int getEstudiantesRequeridos() {

        return estudiantesRequeridos;
    }

    public void setestudiantesRequeridos(int estudiantesRequeridos) {

        this.estudiantesRequeridos = estudiantesRequeridos;
    }

    public int getEstudiantesAsignados() {
        return estudiantesAsignados;
    }

    public void setEstudiantesAsignados(int estudiantesAsignados) {
        this.estudiantesAsignados = estudiantesAsignados;
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