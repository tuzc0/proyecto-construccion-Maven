package logica.DTOs;

public class AutoEvaluacionContieneDTO {

    private int idAutoevaluacion;
    private float calificacion;
    private int idCriterio;

    public AutoEvaluacionContieneDTO() {

    }

    public AutoEvaluacionContieneDTO(int idAutoevaluacion, float calificacion, int idCriterio) {

        this.idAutoevaluacion = idAutoevaluacion;
        this.calificacion = calificacion;
        this.idCriterio = idCriterio;
    }

    public int getIdAutoevaluacion() {

        return idAutoevaluacion;
    }

    public void setIdAutoevaluacion(int idAutoevaluacion) {

        this.idAutoevaluacion = idAutoevaluacion;
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
