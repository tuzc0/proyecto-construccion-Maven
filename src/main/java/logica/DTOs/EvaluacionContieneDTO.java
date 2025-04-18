package logica.DTOs;

public class EvaluacionContieneDTO {

    private int idEvaluacion;
    private float calificacion;
    private int idCriterio;

    public EvaluacionContieneDTO() {

    }

    public EvaluacionContieneDTO(int idEvaluacion, float calificacion, int idCriterio) {

        this.idEvaluacion = idEvaluacion;
        this.calificacion = calificacion;
        this.idCriterio = idCriterio;
    }

    public int getIdEvaluacion() {

        return idEvaluacion;
    }

    public void setIdEvaluacion(int idEvaluacion) {

        this.idEvaluacion = idEvaluacion;
    }

    public float getCalificacion() {

        return calificacion;
    }

    public void setCalificacion(float calificacion) {

        this.calificacion = calificacion;
    }

    public int getIdCriterio() {

        return idCriterio;
    }

    public void setIdCriterio(int idCriterio) {

        this.idCriterio = idCriterio;
    }
}
