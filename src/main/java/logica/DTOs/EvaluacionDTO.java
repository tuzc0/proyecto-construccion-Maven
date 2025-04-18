package logica.DTOs;

public class EvaluacionDTO {

    private int IDEvaluacion;
    private String comentarios;
    private int calificacionFinal;
    private int numeroPersonal;
    private String matriculaEstudiante;
    private int estadoActivo;

    public EvaluacionDTO() {

    }

    public EvaluacionDTO(int IDEvaluacion, String comentarios, int calificacionFinal, int numeroPersonal, String matriculaEstudiante, int estadoActivo) {

        this.IDEvaluacion = IDEvaluacion;
        this.comentarios = comentarios;
        this.calificacionFinal = calificacionFinal;
        this.numeroPersonal = numeroPersonal;
        this.matriculaEstudiante = matriculaEstudiante;
        this.estadoActivo = estadoActivo;
    }

    public int getIDEvaluacion() {

        return IDEvaluacion;
    }

    public void setIDEvaluacion(int IDEvaluacion) {

        this.IDEvaluacion = IDEvaluacion;
    }

    public String getComentarios() {

        return comentarios;
    }

    public void setComentarios(String comentarios) {

        this.comentarios = comentarios;
    }

    public int getCalificacionFinal() {

        return calificacionFinal;
    }

    public void setCalificacionFinal(int calificacionFinal) {

        this.calificacionFinal = calificacionFinal;
    }

    public int getNumeroDePersonal() {

        return numeroPersonal;
    }

    public void setNumeroDePersonal(int numeroPersonal) {

        this.numeroPersonal = numeroPersonal;
    }

    public String getMatriculaEstudiante() {

        return matriculaEstudiante;
    }

    public void setMatriculaEstudiante(String matriculaEstudiante) {

        this.matriculaEstudiante = matriculaEstudiante;
    }

    public int getEstadoActivo() {

        return estadoActivo;
    }

    public void setEstadoActivo(int estadoActivo) {

        this.estadoActivo = estadoActivo;
    }
}
