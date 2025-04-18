package logica.DTOs;

public class EvidenciaAutoevaluacionDTO {

    private int idEvidencia;
    private String URL;
    private int idAutoevaluacion;

    public EvidenciaAutoevaluacionDTO() {

    }

    public EvidenciaAutoevaluacionDTO(int idEvidencia, String URL, int idAutoevaluacion) {

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
