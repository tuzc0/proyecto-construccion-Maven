package logica.DTOs;

import java.util.Objects;

public class CriterioEvaluacionDTO {

    private int IDCriterio;
    private String descripcion;
    private int numeroCriterio;
    private int estadoActivo;

    public CriterioEvaluacionDTO() {

    }

    public CriterioEvaluacionDTO(int IDCriterio, String descripcion, int numeroCriterio, int estadoActivo) {

        this.IDCriterio = IDCriterio;
        this.descripcion = descripcion;
        this.numeroCriterio = numeroCriterio;
        this.estadoActivo = estadoActivo;
    }

    public int getIDCriterio() {

        return IDCriterio;
    }

    public void setIDCriterio(int IDCriterio) {

        this.IDCriterio = IDCriterio;
    }

    public String getDescripcion() {

        return descripcion;
    }

    public void setDescripcion(String descripcion) {

        this.descripcion = descripcion;
    }

    public int getNumeroCriterio() {

        return numeroCriterio;
    }

    public void setNumeroCriterio(int numeroCriterio) {

        this.numeroCriterio = numeroCriterio;
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

        CriterioEvaluacionDTO criterioComparado = (CriterioEvaluacionDTO) objetoAComparar;

        return IDCriterio == criterioComparado.IDCriterio &&
                numeroCriterio == criterioComparado.numeroCriterio &&
                estadoActivo == criterioComparado.estadoActivo &&
                Objects.equals(descripcion, criterioComparado.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(IDCriterio, descripcion, numeroCriterio, estadoActivo);
    }
}
