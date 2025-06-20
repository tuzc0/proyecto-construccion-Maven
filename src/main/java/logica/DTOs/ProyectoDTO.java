package logica.DTOs;

import java.util.Objects;

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

    public ProyectoDTO () {
    }

    public ProyectoDTO(String nombre, String objetivoGeneral, String objetivosInmediatos, String objetivosMediatos,
                       String metodologia, String recursos, String actividades, String responsabilidades,
                       String descripcion) {

        this.nombre = nombre;
        this.objetivoGeneral = objetivoGeneral;
        this.objetivosInmediatos = objetivosInmediatos;
        this.objetivosMediatos = objetivosMediatos;
        this.metodologia = metodologia;
        this.recursos = recursos;
        this.actividades = actividades;
        this.responsabilidades = responsabilidades;
        this.descripcion = descripcion;
    }

    public ProyectoDTO (int idProyecto, String nombre, String objetivoGeneral,
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

    public void setEstudiantesRequeridos(int estudiantesRequeridos) {

        this.estudiantesRequeridos = estudiantesRequeridos;
    }

    public int getEstudiantesAsignados() {

        return estudiantesAsignados;
    }

    public void setEstudiantesAsignados(int estudiantesAsignados) {

        this.estudiantesAsignados = estudiantesAsignados;
    }

    @Override
    public boolean equals(Object objetoAComparar) {

        if (this == objetoAComparar) {
            return true;
        }

        if (objetoAComparar == null || getClass() != objetoAComparar.getClass()) {
            return false;
        }

        ProyectoDTO proyectoComparado = (ProyectoDTO) objetoAComparar;

        return idProyecto == proyectoComparado.idProyecto &&
                estadoActivo == proyectoComparado.estadoActivo &&
                idRepresentante == proyectoComparado.idRepresentante &&
                usuariosDirectos == proyectoComparado.usuariosDirectos &&
                usuariosIndirectos == proyectoComparado.usuariosIndirectos &&
                estudiantesRequeridos == proyectoComparado.estudiantesRequeridos &&
                estudiantesAsignados == proyectoComparado.estudiantesAsignados &&
                Objects.equals(nombre, proyectoComparado.nombre) &&
                Objects.equals(objetivoGeneral, proyectoComparado.objetivoGeneral) &&
                Objects.equals(objetivosInmediatos, proyectoComparado.objetivosInmediatos) &&
                Objects.equals(objetivosMediatos, proyectoComparado.objetivosMediatos) &&
                Objects.equals(metodologia, proyectoComparado.metodologia) &&
                Objects.equals(recursos, proyectoComparado.recursos) &&
                Objects.equals(actividades, proyectoComparado.actividades) &&
                Objects.equals(responsabilidades, proyectoComparado.responsabilidades) &&
                Objects.equals(duracion, proyectoComparado.duracion) &&
                Objects.equals(descripcion, proyectoComparado.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idProyecto, nombre, objetivoGeneral, objetivosInmediatos, objetivosMediatos,
                metodologia, recursos, actividades, responsabilidades, duracion, estadoActivo,
                idRepresentante, descripcion, usuariosDirectos, usuariosIndirectos,
                estudiantesRequeridos, estudiantesAsignados);
    }
}