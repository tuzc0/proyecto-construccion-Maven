package logica;

import logica.DTOs.CriterioEvaluacionDTO;
import logica.DTOs.EvaluacionContieneDTO;


public class ContenedorCriterios {

    private CriterioEvaluacionDTO criterioEvaluacion;
    private EvaluacionContieneDTO evaluacionContiene;

    public ContenedorCriterios(CriterioEvaluacionDTO criterioEvaluacion, EvaluacionContieneDTO evaluacionContiene) {
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


}
