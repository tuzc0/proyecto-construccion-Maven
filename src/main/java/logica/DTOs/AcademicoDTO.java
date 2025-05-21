package logica.DTOs;

import java.util.Objects;

public class AcademicoDTO extends UsuarioDTO {

    private int numeroDePersonal;

    public AcademicoDTO() {

    }

    public AcademicoDTO(int numeroDePersonal, int idUsuario, String nombre, String apellido, int estadoActivo) {

        super(idUsuario, nombre, apellido, estadoActivo);
        this.numeroDePersonal = numeroDePersonal;
    }

    public int getNumeroDePersonal() {

        return numeroDePersonal;
    }

    public void setNumeroDePersonal(int numeroDePersonal) {

        this.numeroDePersonal = numeroDePersonal;
    }

    @Override
    public boolean equals(Object objetoAComparar) {

        if (this == objetoAComparar) {
            return true;
        }

        if (objetoAComparar == null || getClass() != objetoAComparar.getClass()) {
            return false;
        }

        AcademicoDTO academicoComparado = (AcademicoDTO) objetoAComparar;

        return numeroDePersonal == academicoComparado.numeroDePersonal &&
                getIdUsuario() == academicoComparado.getIdUsuario() &&
                getEstado() == academicoComparado.getEstado() &&
                Objects.equals(getNombre(), academicoComparado.getNombre()) &&
                Objects.equals(getApellido(), academicoComparado.getApellido());
    }

    @Override
    public int hashCode() {

        return Objects.hash(numeroDePersonal, getIdUsuario(), getNombre(),
                getApellido(), getEstado());
    }
}