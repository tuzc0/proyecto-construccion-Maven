package logica;

import logica.DTOs.CriterioEvaluacionDTO;
import logica.DTOs.EvaluacionContieneDTO;


public class ContenedorCriteriosEvaluacion {

    private CriterioEvaluacionDTO criterioEvaluacion;
    private EvaluacionContieneDTO evaluacionContiene;

    public ContenedorCriteriosEvaluacion(CriterioEvaluacionDTO criterioEvaluacion, EvaluacionContieneDTO evaluacionContiene) {

        this.criterioEvaluacion = criterioEvaluacion;
        this.evaluacionContiene = evaluacionContiene;
    }

    public CriterioEvaluacionDTO getCriterioEvaluacion() {
        return criterioEvaluacion;
    }

    public void setCriterioEvaluacion(CriterioEvaluacionDTO criterioEvaluacion) {
        this.criterioEvaluacion = criterioEvaluacion;
    }

    public EvaluacionContieneDTO getEvaluacionContiene() {
        return evaluacionContiene;
    }

    public void setEvaluacionContiene(EvaluacionContieneDTO evaluacionContiene) {
        this.evaluacionContiene = evaluacionContiene;
    }

    public String getNumeroCriterio() {
        return String.valueOf(criterioEvaluacion.getNumeroCriterio());
    }

    public String getDescripcion() {
        return criterioEvaluacion.getDescripcion();
    }

    public String getCalificacion() {
        return String.valueOf(evaluacionContiene.getCalificacion());
    }





}
