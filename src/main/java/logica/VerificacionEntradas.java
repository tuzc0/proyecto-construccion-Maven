package logica;

import GUI.utilidades.Utilidades;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

import static logica.verificacion.VerificicacionGeneral.validar;

public class VerificacionEntradas {

    private static final Logger logger = LogManager.getLogger(VerificacionEntradas.class);
    private static final Utilidades utilidades = new Utilidades();


    private static final Pattern PATRON_SOLO_NUMEROS = Pattern.compile("^[-+]?\\d+(\\.\\d+)?$");
    private static final Pattern PATRON_TEXTO_SEGURO = Pattern.compile("\"^[\\\\p{L}\\\\s.,:;¡!¿?()]+$\"");
    private static final Pattern PATRON_NUMERO_ENTERO_POSITIVO = Pattern.compile("^\\d+$");
    private static final Pattern PATRON_TEXTO_CON_ESPACIOS = Pattern.compile("^[\\p{L}\\s\\d.,:;¡!¿?()]+$");

    public static boolean esNumeroValido(String entrada) {
        return validar(entrada, PATRON_SOLO_NUMEROS);
    }

    public static boolean esEnteroPositivo(String entrada) {
        return validar(entrada, PATRON_NUMERO_ENTERO_POSITIVO);
    }

    public static boolean esTextoSeguro(String texto) {
        return validar(texto, PATRON_TEXTO_SEGURO);
    }

    public static boolean validarTextoAlfanumerico(String texto) {
        return validar(texto, PATRON_TEXTO_CON_ESPACIOS);
    }

}
