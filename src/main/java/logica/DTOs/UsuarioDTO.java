package logica.DTOs;

import java.util.Objects;

public class UsuarioDTO {

    private int idUsuario;
    private String nombre;
    private String apellido;
    private int estado;

    public UsuarioDTO() {

    }

    public UsuarioDTO(int idUsuario, String nombre, String apellido, int estado) {

        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.estado = estado;
    }

    public int getIdUsuario() {

        return idUsuario;
    }

    public void setIdUsuario(int IDUsuario) {

        this.idUsuario = IDUsuario;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public String getApellido() {

        return apellido;
    }

    public void setApellido(String apellido) {

        this.apellido = apellido;
    }

    public int getEstado() {

        return estado;
    }

    public void setEstado(int estado) {

        this.estado = estado;
    }

    @Override
    public boolean equals(Object objetoAComparar) {

        if (this == objetoAComparar) {
            return true;
        }
        if (objetoAComparar == null || getClass() != objetoAComparar.getClass()) {
            return false;
        }

        UsuarioDTO usuarioComparado = (UsuarioDTO) objetoAComparar;

        return idUsuario == usuarioComparado.idUsuario &&
                estado == usuarioComparado.estado &&
                Objects.equals(nombre, usuarioComparado.nombre) &&
                Objects.equals(apellido, usuarioComparado.apellido);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idUsuario, nombre, apellido, estado);
    }
}
