package logica.generacionPDFs;

import com.lowagie.text.DocumentException;
import logica.DTOs.EstudianteDTO;
import logica.DTOs.OrganizacionVinculadaDTO;
import logica.DTOs.ProyectoDTO;
import logica.DTOs.RepresentanteDTO;
import logica.generacionPDFs.plantillasdeoficios.PlantillaOficionDeAceptacionPDF;
import java.io.File;
import java.io.IOException;

public class GeneradoresPDF {

    public void generarOficionAsignacion(File archivoDestino,
                                         EstudianteDTO estudianteDTO,
                                         ProyectoDTO proyectoDTO,
                                         RepresentanteDTO representanteDTO,
                                         OrganizacionVinculadaDTO organizacionDTO)
                                         throws DocumentException, IOException {

        PlantillaOficionDeAceptacionPDF.generarOficionDeAceptacion(
                archivoDestino.getAbsolutePath(),
                estudianteDTO,
                proyectoDTO,
                representanteDTO,
                organizacionDTO
        );

    }
}
