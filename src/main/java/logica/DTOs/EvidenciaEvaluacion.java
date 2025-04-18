package logica.DTOs;

public class EvidenciaEvaluacion {

    private int idEvidencia;
    private String URL;
    private int idEvaluacion;

    public EvidenciaEvaluacion() {

    }

    public EvidenciaEvaluacion(int idEvidencia, String URL, int idEvaluacion) {

        this.idEvidencia = idEvidencia;
        this.URL = URL;
        this.idEvaluacion = idEvaluacion;
    }

    public int getIdEvidencia() {

        return idEvidencia;
    }

    public void setIdEvidencia(int idEvidencia) {

        this.idEvidencia = idEvidencia;
    }

    public String getURL() {

        return URL;
    }

    public void setURL(String URL) {

        this.URL = URL;
    }

    public int getIdEvaluacion() {

        return idEvaluacion;
    }

    public void setIdEvaluacion(int idEvaluacion) {

        this.idEvaluacion = idEvaluacion;
    }
}
