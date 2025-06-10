package logica;

import logica.DTOs.ActividadDTO;
import logica.DTOs.ReporteContieneDTO;

public class ContenedorActividadesReporte {

    private ActividadDTO actividad;
    private ReporteContieneDTO reporteContiene;

    public ContenedorActividadesReporte(ActividadDTO actividad, ReporteContieneDTO reporteContiene) {
        this.actividad = actividad;
        this.reporteContiene = reporteContiene;
    }

    public ActividadDTO getActividadDTO() {
        return actividad;
    }

    public void setActividad(ActividadDTO actividad) {
        this.actividad = actividad;
    }

    public ReporteContieneDTO getReporteContieneDTO() {
        return reporteContiene;
    }

    public void setReporteContieneDTO(ReporteContieneDTO reporteContiene) {
        this.reporteContiene = reporteContiene;
    }


}
