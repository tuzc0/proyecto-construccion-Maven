package logica.utilidadescronograma;

import logica.DTOs.*;

public class AsociacionCronograma {

    private CronogramaActividadesDTO cronogramaActividadesDTO;
    private EstudianteDTO estudianteDTO;


    public AsociacionCronograma(CronogramaActividadesDTO cronogramaDTO, EstudianteDTO estudiante) {

        cronogramaActividadesDTO = cronogramaDTO;
        estudianteDTO = estudiante;
    }

    public CronogramaActividadesDTO getCronogramaActividadesDTO() {

        return cronogramaActividadesDTO;
    }

    public EstudianteDTO getEstudianteDTO() {

        return estudianteDTO;
    }

    public void setCronogramaActividadesDTO(CronogramaActividadesDTO cronogramaActividadesDTO) {

        this.cronogramaActividadesDTO = cronogramaActividadesDTO;
    }

    public void setEstudianteDTO(EstudianteDTO estudianteDTO) {

        this.estudianteDTO = estudianteDTO;
    }
}
