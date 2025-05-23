package logica.DTOs;

import java.sql.Timestamp;

public class CronogramaActividadesDTO {

    private int IDCronograma;
    private String matriculaEstudiante;
    private String agostoFebrero;
    private String septiembreMarzo;
    private String octubreAbril;
    private String noviembreMayo;


    public CronogramaActividadesDTO() {

    }

    public CronogramaActividadesDTO(int IDCronograma, String matriculaEstudiante, String agostoFebrero, String septiembreMarzo, String octubreAbril, String noviembreMayo) {

        this.IDCronograma = IDCronograma;
        this.matriculaEstudiante = matriculaEstudiante;
        this.agostoFebrero = agostoFebrero;
        this.septiembreMarzo = septiembreMarzo;
        this.octubreAbril = octubreAbril;
        this.noviembreMayo = noviembreMayo;
    }

    public int getIDCronograma() {

        return IDCronograma;
    }

    public void setIDCronograma(int IDCronograma) {

        this.IDCronograma = IDCronograma;
    }

    public String getMatriculaEstudiante() {

        return matriculaEstudiante;
    }

    public void setMatriculaEstudiante(String matriculaEstudiante) {

        this.matriculaEstudiante = matriculaEstudiante;
    }

    public String getAgostoFebrero() {

        return agostoFebrero;
    }

    public void setAgostoFebrero(String agostoFebrero) {

        this.agostoFebrero = agostoFebrero;
    }

    public String getSeptiembreMarzo() {

        return septiembreMarzo;
    }

    public void setSeptiembreMarzo(String septiembreMarzo) {

        this.septiembreMarzo = septiembreMarzo;
    }

    public String getOctubreAbril() {

        return octubreAbril;
    }

    public void setOctubreAbril(String octubreAbril) {

        this.octubreAbril = octubreAbril;
    }

    public String getNoviembreMayo() {

        return noviembreMayo;
    }

    public void setNoviembreMayo(String noviembreMayo) {

        this.noviembreMayo = noviembreMayo;
    }
}
