package logica.DTOs;

import java.util.Objects;

public class OrganizacionVinculadaDTO {

    private int IdOrganizacion;
    private String nombre;
    private String correo;
    private String numeroDeContacto;
    private String direccion;
    private int estadoActivo;

    public OrganizacionVinculadaDTO() {

    }

    public OrganizacionVinculadaDTO(int IdOrganizacion, String nombre, String direccion, String correo, String numeroDeContacto, int estadoActivo) {

        this.IdOrganizacion = IdOrganizacion;
        this.nombre = nombre;
        this.direccion = direccion;
        this.correo = correo;
        this.numeroDeContacto = numeroDeContacto;
        this.estadoActivo = estadoActivo;
    }

    public int getIdOrganizacion() {

        return IdOrganizacion;
    }

    public void setIdOrganizacion(int IdOrganizacion) {

        this.IdOrganizacion = IdOrganizacion;
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

    @Override
    public boolean equals(Object objetoAComparar) {

        if (this == objetoAComparar) {
            return true;
        }

        if (objetoAComparar == null || getClass() != objetoAComparar.getClass()) {
            return false;
        }

        OrganizacionVinculadaDTO organizacionComparada = (OrganizacionVinculadaDTO) objetoAComparar;

        return IdOrganizacion == organizacionComparada.IdOrganizacion &&
                estadoActivo == organizacionComparada.estadoActivo &&
                Objects.equals(nombre, organizacionComparada.nombre) &&
                Objects.equals(direccion, organizacionComparada.direccion) &&
                Objects.equals(correo, organizacionComparada.correo) &&
                Objects.equals(numeroDeContacto, organizacionComparada.numeroDeContacto);
    }

    @Override
    public int hashCode() {

        return Objects.hash(IdOrganizacion, nombre, direccion, correo, numeroDeContacto, estadoActivo);
    }
}
