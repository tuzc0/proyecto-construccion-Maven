package logica.DTOs;

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
    private String diasYHorarios;
    private int idCronograma;
    private int estadoActivo;
    private int idRepresentante;
    private String descripcion;

    public ProyectoDTO() {
    }

    public ProyectoDTO(int idProyecto, String nombre, String objetivoGeneral,
                       String objetivosInmediatos, String objetivosMediatos, String metodologia,
                       String recursos, String actividades, String responsabilidades, String duracion,
                       String diasYHorarios, int idCronograma, int estadoActivo, int idRepresentante, String descripcion) {

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
        this.diasYHorarios = diasYHorarios;
        this.idCronograma = idCronograma;
        this.estadoActivo = estadoActivo;
        this.idRepresentante = idRepresentante;
        this.descripcion = descripcion;
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

    public String getDiasYHorarios() {
        return diasYHorarios;
    }

    public void setDiasYHorarios(String diasYHorarios) {
        this.diasYHorarios = diasYHorarios;
    }

    public int getIdCronograma() {
        return idCronograma;
    }

    public void setIdCronograma(int idCronograma) {
        this.idCronograma = idCronograma;
    }

    public int getEstadoActivo() {
        return estadoActivo;
    }

    public void setEstadoActivo(int estadoActivo) {
        this.estadoActivo = estadoActivo;
    }

    public int getIdRepresentante() {return idRepresentante;}

    public void setIdRepresentante(int idRepresentante) {
        this.idRepresentante = idRepresentante;
    }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}