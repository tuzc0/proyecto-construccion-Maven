package logica.DTOs;

public class RepresentanteDTO {

    private int IDRepresentante;
    private String correo;
    private String telefono;
    private String nombre;
    private String apellidos;
    private int idOV;
    private int estadoActivo;

    public RepresentanteDTO() {

    }

    public RepresentanteDTO(int IDRepresentante, String correo, String telefono, String nombre, String apellidos, int idOV, int estadoActivo) {

        this.IDRepresentante = IDRepresentante;
        this.correo = correo;
        this.telefono = telefono;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.idOV = idOV;
        this.estadoActivo = estadoActivo;
    }

    public int getIDRepresentante() {

        return IDRepresentante;
    }

    public void setIDRepresentante(int IDRepresentante) {

        this.IDRepresentante = IDRepresentante;
    }

    public String getCorreo() {

        return correo;
    }

    public void setCorreo(String correo) {

        this.correo = correo;
    }

    public String getTelefono() {

        return telefono;
    }

    public void setTelefono(String telefono) {

        this.telefono = telefono;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public String getApellidos() {

        return apellidos;
    }

    public void setApellidos(String apellidos) {

        this.apellidos = apellidos;
    }

    public int getIdOV() {

        return idOV;
    }

    public void setIdOV(int idOV) {

        this.idOV = idOV;
    }

    public int getEstadoActivo() {

        return estadoActivo;
    }

    public void setEstadoActivo(int estadoActivo) {

        this.estadoActivo = estadoActivo;
    }
}