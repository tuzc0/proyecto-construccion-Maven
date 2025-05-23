package logica.DTOs;

import java.util.Objects;

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

    @Override
    public boolean equals(Object autoEvaluacionContieneAComparar) {

        if (this == autoEvaluacionContieneAComparar) {
            return true;
        }

        if (autoEvaluacionContieneAComparar == null || getClass() != autoEvaluacionContieneAComparar.getClass()) {
            return false;
        }

        AutoEvaluacionContieneDTO autoEvaluacionContieneDTO = (AutoEvaluacionContieneDTO) autoEvaluacionContieneAComparar;

        return idAutoevaluacion == autoEvaluacionContieneDTO.idAutoevaluacion &&
                Float.compare(autoEvaluacionContieneDTO.calificacion, calificacion) == 0 &&
                idCriterio == autoEvaluacionContieneDTO.idCriterio;
    }

    @Override
    public int hashCode() {

        return Objects.hash(idAutoevaluacion, calificacion, idCriterio);
    }
}
