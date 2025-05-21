package logica.DTOs;

import java.util.Objects;

public class EstudianteDTO extends UsuarioDTO {

    private String matricula;

    public EstudianteDTO() {

    }

    public EstudianteDTO(int idUsuario, String nombre, String apellido, String matricula, int estadoActivo) {

        super(idUsuario, nombre, apellido, estadoActivo);
        this.matricula = matricula;
    }

    public String getMatricula() {

        return matricula;
    }

    public void setMatricula(String matriculaEstudiante) {

        this.matricula = matriculaEstudiante;
    }

    @Override
    public boolean equals(Object objetoAComparar) {

        if (this == objetoAComparar) {
            return true;
        }

        if (objetoAComparar == null || getClass() != objetoAComparar.getClass()) {
            return false;
        }

        if (!super.equals(objetoAComparar)){
            return false;
        }

        EstudianteDTO estudianteComparado = (EstudianteDTO) objetoAComparar;

        return Objects.equals(matricula, estudianteComparado.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), matricula);
    }
}