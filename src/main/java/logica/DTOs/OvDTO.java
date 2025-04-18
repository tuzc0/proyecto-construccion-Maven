package logica.DTOs;

public class OvDTO {

    private int IdOV;
    private String nombre;
    private String correo;
    private String numeroDeContacto;
    private String direccion;
    private int estadoActivo;

    public OvDTO() {

    }

    public OvDTO(int IdOV, String nombre, String direccion, String correo, String numeroDeContacto, int estadoActivo) {

        this.IdOV = IdOV;
        this.nombre = nombre;
        this.direccion = direccion;
        this.correo = correo;
        this.numeroDeContacto = numeroDeContacto;
        this.estadoActivo = estadoActivo;
    }

    public int getIdOV() {

        return IdOV;
    }

    public void setIdOV(int IdOV) {

        this.IdOV = IdOV;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public String getDireccion() {

        return direccion;
    }

    public void setDireccion(String direccion) {

        this.direccion = direccion;
    }

    public String getCorreo() {

        return correo;
    }

    public void setCorreo(String correo) {

        this.correo = correo;
    }

    public String getNumeroDeContacto() {

        return numeroDeContacto;
    }

    public void setNumeroDeContacto(String numeroDeContacto) {

        this.numeroDeContacto = numeroDeContacto;
    }

    public int getEstadoActivo() {

        return estadoActivo;
    }

    public void setEstadoActivo(int estadoActivo) {

        this.estadoActivo = estadoActivo;
    }
}
