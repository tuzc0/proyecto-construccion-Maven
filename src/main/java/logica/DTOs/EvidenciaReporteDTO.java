package logica.DTOs;

public class EvidenciaReporteDTO {

    private int idEvidencia;
    private String URL;
    private int idReporte;

    public EvidenciaReporteDTO() {

    }

    public EvidenciaReporteDTO(int idEvidencia, String URL, int idReporte) {

        this.idEvidencia = idEvidencia;
        this.URL = URL;
        this.idReporte = idReporte;
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

    public int getIdReporte() {

        return idReporte;
    }

    public void setIdReporte(int idReporte) {

        this.idReporte = idReporte;
    }
}
