package logica.DTOs;

public class ProyectoDTO {

    private int IDProyecto;
    private String nombre;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private int IDRepresentante;
    private String matricula;
    private int estadoActivo;

    public ProyectoDTO() {

    }

    public ProyectoDTO(int IDProyecto, String nombre, String descripcion, String fechaInicio, String fechaFin, int IDRepresentante, String matricula, int estadoActivo) {

        this.IDProyecto = IDProyecto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.IDRepresentante = IDRepresentante;
        this.matricula = matricula;
        this.estadoActivo = estadoActivo;
    }

    public int getIDProyecto() {

        return IDProyecto;
    }

    public void setIDProyecto(int IDProyecto) {

        this.IDProyecto = IDProyecto;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public String getDescripcion() {

        return descripcion;
    }

    public void setDescripcion(String descripcion) {

        this.descripcion = descripcion;
    }

    public String getFechaInicio() {

        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {

        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {

        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {

        this.fechaFin = fechaFin;
    }

    public int getIDRepresentante() {

        return IDRepresentante;
    }

    public void setIDRepresentante(int IDRepresentante) {

        this.IDRepresentante = IDRepresentante;
    }

    public String getMatricula() {

        return matricula;
    }

    public void setMatricula(String matricula) {

        this.matricula = matricula;
    }

    public int getEstadoActivo() {

        return estadoActivo;
    }

    public void setEstadoActivo(int estadoActivo) {

        this.estadoActivo = estadoActivo;
    }
}
