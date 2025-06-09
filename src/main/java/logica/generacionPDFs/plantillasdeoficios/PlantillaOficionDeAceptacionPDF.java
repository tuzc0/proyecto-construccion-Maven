package logica.generacionPDFs.plantillasdeoficios;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import logica.DTOs.EstudianteDTO;
import logica.DTOs.OrganizacionVinculadaDTO;
import logica.DTOs.ProyectoDTO;
import logica.DTOs.RepresentanteDTO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class PlantillaOficionDeAceptacionPDF {

    public static void generarOficionDeAceptacion(String rutaArchivo, EstudianteDTO estudianteSeleccionado,
                                                  ProyectoDTO proyectoSeleccionado, RepresentanteDTO representanteDTO,
                                                  OrganizacionVinculadaDTO organizacionVinculadaDTO) throws DocumentException, IOException {

        Document documento = new Document(PageSize.LETTER, 50, 50, 50, 50);

        try {

            PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
            documento.open();

            Font fuenteTitulo = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font fuenteNormal = new Font(Font.HELVETICA, 11);
            Font fuenteNegrita = new Font(Font.HELVETICA, 12, Font.BOLD);

            Paragraph encabezado = new Paragraph("Facultad de Estadística e Informática\n\n", fuenteTitulo);
            encabezado.setAlignment(Element.ALIGN_LEFT);
            documento.add(encabezado);

            documento.add(new Paragraph("Dirección\nAv. Xalapa esq. Ávila Camacho S/N\nCol. Obrero Campesina\nCP 91020\nXalapa de Enríquez\nVeracruz, México\n", fuenteNormal));
            documento.add(new Paragraph("Teléfonos\n(228) 8421700 Exts.14155, 14250, 14108, 14106\nFax (228) 8149990\n", fuenteNormal));
            documento.add(new Paragraph("Internet\nfei@uv.mx\nhttp://www.uv.mx/fei\n\n", fuenteNormal));

            documento.add(new Paragraph(obtenerFechaActual() + "\n\n", fuenteNormal));

            documento.add(new Paragraph(
                    representanteDTO.getNombre().toUpperCase() + "\n" +
                            organizacionVinculadaDTO.getNombre().toUpperCase() + "\n\n", fuenteNegrita));

            String cuerpo = String.format(
                    "En atención a su solicitud expresada a la Coordinación de Prácticas Profesionales de la Licenciatura en Ingeniería de Software, hacemos de su conocimiento que el C. %s %s, estudiante de la Licenciatura con matrícula %s, ha sido asignado al proyecto de %s, a su digno cargo hasta cubrir %s HORAS. Cabe mencionar que el estudiante cuenta con la formación y el perfil para las actividades a desempeñar.\n\n"
                            + "Anexo a este documento usted encontrará una copia del horario de las experiencias educativas que el estudiante asignado se encuentra cursando para que sea respetado y tomado en cuenta al momento de establecer el horario de realización de sus Prácticas Profesionales.\n\n"
                            + "Por otra parte, le solicito de la manera más atenta, haga llegar a la brevedad con el estudiante, el oficio de aceptación así como el plan de trabajo detallado del estudiante, además el horario que cubrirá. Deberá indicar además, la forma en que se registrará la evidencia de asistencia y número de horas cubiertas.\n\n"
                            + "Es importante mencionar que el estudiante deberá presentar mensualmente un reporte de avances de sus prácticas. Este reporte de avances puede entregarse hasta con una semana de atraso por lo que le solicito de la manera más atenta sean elaborados y avalados (incluyendo sello si aplica) de manera oportuna para su entrega al académico responsable de la experiencia de Prácticas de Ingeniería de Software.\n\n"
                            + "En relación con lo anterior, es importante que en el oficio de aceptación proporcione el nombre de la persona que supervisará y avalará en su dependencia la prestación de las prácticas profesionales así como número telefónico, extensión (cuando aplique) y correo electrónico. Lo anterior con el fin de contar con el canal de comunicación que permita dar seguimiento al desempeño del estudiante.\n\n"
                            + "Le informo que las Prácticas de Ingeniería de Software forman parte de la currícula de la Licenciatura en Ingeniería de Software, por lo cual es necesaria su evaluación y de ahí la necesidad de realizar el seguimiento correspondiente.\n\n"
                            + "Es por ello que, durante el semestre, el coordinador de Prácticas de Ingeniería de Software realizará al menos un seguimiento de las actividades del estudiante por lo que será necesario mostrar evidencias de la asistencia del estudiante, así como de sus actividades. Este seguimiento podrá ser vía correo electrónico, teléfono o incluso mediante una visita a sus oficinas, por lo que le solicito de la manera más atenta, proporcione las facilidades requeridas en su caso.\n\n"
                            + "Sin más por el momento, agradezco su atención al presente reiterándome a sus apreciables órdenes.",
                    estudianteSeleccionado.getNombre(),
                    estudianteSeleccionado.getApellido(),
                    estudianteSeleccionado.getMatricula(),
                    proyectoSeleccionado.getNombre(),
                    proyectoSeleccionado.getDuracion()
            );
            documento.add(new Paragraph(cuerpo, fuenteNormal));

            documento.add(new Paragraph("\n\nAtentamente\n\n________________________________", fuenteNormal));
            documento.add(new Paragraph("Dr. Ángel Juan Sánchez García\nCoordinador de Servicio Social y Prácticas Profesionales\nLicenciatura en Ingeniería de Software", fuenteNormal));

        } finally {

            documento.close();
        }
    }

    private static String obtenerFechaActual() {

        LocalDate hoy = LocalDate.now();
        String dia = String.valueOf(hoy.getDayOfMonth());
        String mes = hoy.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "MX"));
        String año = String.valueOf(hoy.getYear());

        mes = mes.substring(0, 1).toUpperCase() + mes.substring(1);

        return "Xalapa-Enríquez, Veracruz, a " + dia + " de " + mes + " de " + año;
    }
}
