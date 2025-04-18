package logica.DTOs;

public class EvidenciaCronogramaDTO {

    private int idEvidencia;
    private String URL;
    private int idCronograma;

    public EvidenciaCronogramaDTO() {

    }

    public EvidenciaCronogramaDTO(int idEvidencia, String URL, int idCronograma) {

        this.idEvidencia = idEvidencia;
        this.URL = URL;
        this.idCronograma = idCronograma;
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

    public int getIdCronograma() {

        return idCronograma;
    }

    public void setIdCronograma(int idCronograma) {

        this.idCronograma = idCronograma;
    }
}
