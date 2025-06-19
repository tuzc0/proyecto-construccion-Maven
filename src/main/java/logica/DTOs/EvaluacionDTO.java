package logica.DTOs;

import java.util.Objects;

public class EvaluacionDTO {

    private int IDEvaluacion;
    private String comentarios;
    private float calificacionFinal;
    private int numeroPersonal;
    private String matriculaEstudiante;
    private int estadoActivo;

    public EvaluacionDTO() {

    }

    public EvaluacionDTO(int IDEvaluacion, String comentarios, float calificacionFinal, int numeroPersonal,
                         String matriculaEstudiante, int estadoActivo) {

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

    public float getCalificacionFinal() {

        return calificacionFinal;
    }

    public void setCalificacionFinal(float calificacionFinal) {

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

    @Override
    public boolean equals(Object objetoAComparar) {

        if (this == objetoAComparar) {
            return true;
        }

        if (objetoAComparar == null || getClass() != objetoAComparar.getClass()) {
            return false;
        }

        EvaluacionDTO evaluacionComparada = (EvaluacionDTO) objetoAComparar;

        return IDEvaluacion == evaluacionComparada.IDEvaluacion &&
                Float.compare(evaluacionComparada.calificacionFinal, calificacionFinal) == 0 &&
                numeroPersonal == evaluacionComparada.numeroPersonal &&
                estadoActivo == evaluacionComparada.estadoActivo &&
                Objects.equals(comentarios, evaluacionComparada.comentarios) &&
                Objects.equals(matriculaEstudiante, evaluacionComparada.matriculaEstudiante);
    }

    @Override
    public int hashCode() {

        return Objects.hash(IDEvaluacion, comentarios, calificacionFinal,
                numeroPersonal, matriculaEstudiante, estadoActivo);
    }
}
