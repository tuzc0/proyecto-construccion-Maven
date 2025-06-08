package logica;

import logica.DTOs.AutoEvaluacionContieneDTO;
import logica.DTOs.CriterioAutoevaluacionDTO;

public class ContenedorCriteriosAutoevaluacion {

    private CriterioAutoevaluacionDTO criterioAutoevaluacion;
    private AutoEvaluacionContieneDTO autoEvaluacionContiene;

    public ContenedorCriteriosAutoevaluacion(CriterioAutoevaluacionDTO criterioAutoevaluacion, AutoEvaluacionContieneDTO autoEvaluacionContiene) {
        this.criterioAutoevaluacion = criterioAutoevaluacion;
        this.autoEvaluacionContiene = autoEvaluacionContiene;
    }

    public CriterioAutoevaluacionDTO getCriterioAutoevaluacion() {
        return criterioAutoevaluacion;
    }

    public void setCriterioAutoevaluacion(CriterioAutoevaluacionDTO criterioAutoevaluacion) {
        this.criterioAutoevaluacion = criterioAutoevaluacion;
    }

    public AutoEvaluacionContieneDTO getAutoEvaluacionContiene() {
        return autoEvaluacionContiene;
    }

    public void setAutoEvaluacionContiene(AutoEvaluacionContieneDTO autoEvaluacionContiene) {
        this.autoEvaluacionContiene = autoEvaluacionContiene;
    }


}
