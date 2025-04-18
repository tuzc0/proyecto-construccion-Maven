package logica.DTOs;

public class CriterioAutoevaluacionDTO {

    private int IDCriterio;
    private String descripcion;
    private int numeroCriterio;

    public CriterioAutoevaluacionDTO() {

    }

    public CriterioAutoevaluacionDTO(int IDCriterio, String descripcion, int numeroCriterio) {

        this.IDCriterio = IDCriterio;
        this.descripcion = descripcion;
        this.numeroCriterio = numeroCriterio;
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
}
