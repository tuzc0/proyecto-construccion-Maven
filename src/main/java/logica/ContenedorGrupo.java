package logica;

import logica.DTOs.AcademicoDTO;
import logica.DTOs.GrupoDTO;
import logica.DTOs.UsuarioDTO;

public class ContenedorGrupo {

    private GrupoDTO grupo;

    private AcademicoDTO academico;


    public ContenedorGrupo(GrupoDTO grupo, AcademicoDTO academico) {

        this.grupo = grupo;
        this.academico = academico;
    }

    public GrupoDTO getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoDTO grupo) {
        this.grupo = grupo;
    }

    public AcademicoDTO getAcademico() {
        return academico;
    }

    public void setAcademico(AcademicoDTO academico) {
        this.academico = academico;
    }



    public String getNombreGrupo() {
        return grupo.getNombre();
    }

    public String getNombreAcademico() {
        return academico.getNombre() + " " + academico.getApellido();
    }


}
