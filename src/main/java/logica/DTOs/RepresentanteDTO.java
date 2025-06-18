package logica.DTOs;

import java.util.Objects;

public class RepresentanteDTO {

    private int IDRepresentante;
    private String correo;
    private String telefono;
    private String nombre;
    private String apellidos;
    private int idOrganizacion;
    private int estadoActivo;

    public RepresentanteDTO() {

    }

    public RepresentanteDTO(int IDRepresentante, String correo, String telefono, String nombre, String apellidos, int idOrganizacion, int estadoActivo) {

        this.IDRepresentante = IDRepresentante;
        this.correo = correo;
        this.telefono = telefono;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.idOrganizacion = idOrganizacion;
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

    public int getIdOrganizacion() {

        return idOrganizacion;
    }

    public void setIdOrganizacion(int idOrganizacion) {

        this.idOrganizacion = idOrganizacion;
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

        RepresentanteDTO representanteComparado = (RepresentanteDTO) objetoAComparar;

        return IDRepresentante == representanteComparado.IDRepresentante &&
                idOrganizacion == representanteComparado.idOrganizacion &&
                estadoActivo == representanteComparado.estadoActivo &&
                Objects.equals(correo, representanteComparado.correo) &&
                Objects.equals(telefono, representanteComparado.telefono) &&
                Objects.equals(nombre, representanteComparado.nombre) &&
                Objects.equals(apellidos, representanteComparado.apellidos);
    }

    @Override
    public int hashCode() {

        return Objects.hash(IDRepresentante, correo, telefono, nombre, apellidos, idOrganizacion, estadoActivo);
    }
}