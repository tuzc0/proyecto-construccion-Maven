package logica.DTOs;

public class EvidenciaAutoevaluacion {

    private int idEvidencia;
    private String URL;
    private int idAutoevaluacion;

    public EvidenciaAutoevaluacion() {

    }

    public EvidenciaAutoevaluacion(int idEvidencia, String URL, int idAutoevaluacion) {

        this.idEvidencia = idEvidencia;
        this.URL = URL;
        this.idAutoevaluacion = idAutoevaluacion;
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

    public int getIdAutoevaluacion() {

        return idAutoevaluacion;
    }

    public void setIdAutoevaluacion(int idAutoevaluacion) {

        this.idAutoevaluacion = idAutoevaluacion;
    }
}
