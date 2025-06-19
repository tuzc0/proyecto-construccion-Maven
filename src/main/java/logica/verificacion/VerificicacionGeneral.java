package logica.verificacion;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class VerificicacionGeneral {

    public void contadorCaracteresTextArea(TextArea textoArea, Label contadorCaracteres, int numeroMaximoDeCaracteres) {

        int numeroDeCaracteresMaximos = numeroMaximoDeCaracteres;

        textoArea.textProperty().addListener((textoObservado, textoAnterior, textoActual) -> {
            int cantidadCaracteresActuales = textoActual.length();
            contadorCaracteres.setText(cantidadCaracteresActuales + "/" + numeroDeCaracteresMaximos);
        });

        contadorCaracteres.setText("0/" + numeroDeCaracteresMaximos);
    }

    public void contadorCaracteresTextField(TextField campoDeTexto, Label contadorCaracteres, int numeroMaximoDeCaracteres) {

        campoDeTexto.textProperty().addListener((textoObservado, textoAnterior, textoActual) -> {
            if (textoActual.length() > numeroMaximoDeCaracteres) {
                campoDeTexto.setText(textoAnterior);
            } else {
                contadorCaracteres.setText(textoActual.length() + "/" + numeroMaximoDeCaracteres);
            }
        });

        contadorCaracteres.setText("0/" + numeroMaximoDeCaracteres);
    }

    public static boolean validar(String campo, Pattern patron) {

        return patron.matcher(campo).matches();
    }
}
