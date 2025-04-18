package logica.DTOs;

public class EstudianteDTO extends UsuarioDTO {

    private String matricula;

    public EstudianteDTO() {

    }

    public EstudianteDTO(String matricula) {

        this.matricula = "-1";
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
}