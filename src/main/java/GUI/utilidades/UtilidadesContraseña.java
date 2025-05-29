package GUI.utilidades;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UtilidadesContraseña {

    private boolean contraseñaVisible = false;

    @FXML
    public void alternarVisibilidadContrasena(PasswordField campoContraseña, TextField campoContraseñaVisible,
                                              PasswordField campoConfirmarContraseña, TextField campoConfirmarContraseñaVisible,
                                              ImageView iconoOjo) {
        contraseñaVisible = !contraseñaVisible;

        campoContraseña.setVisible(!contraseñaVisible);
        campoContraseña.setManaged(!contraseñaVisible);

        campoContraseñaVisible.setVisible(contraseñaVisible);
        campoContraseñaVisible.setManaged(contraseñaVisible);

        campoConfirmarContraseña.setVisible(!contraseñaVisible);
        campoConfirmarContraseña.setManaged(!contraseñaVisible);

        campoConfirmarContraseñaVisible.setVisible(contraseñaVisible);
        campoConfirmarContraseñaVisible.setManaged(contraseñaVisible);

        actualizarIcono(iconoOjo, contraseñaVisible);
    }

    @FXML
    public void visibilidadUnicaContraseña (PasswordField campoContraseña, TextField campoContraseñaVisible,
                                              ImageView iconoOjo) {
        if (contraseñaVisible) {

            campoContraseñaVisible.setVisible(false);
            campoContraseña.setText(campoContraseñaVisible.getText());
            campoContraseña.setVisible(true);
            iconoOjo.setImage(new Image(getClass().getResource("/ojo-cerrado.png").toExternalForm()));
        } else {

            campoContraseña.setVisible(false);
            campoContraseñaVisible.setText(campoContraseña.getText());
            campoContraseñaVisible.setVisible(true);
            iconoOjo.setImage(new Image(getClass().getResource("/ojo-abierto.png").toExternalForm()));
        }
        contraseñaVisible = !contraseñaVisible;
    }


    public void actualizarIcono(ImageView iconoOjo, boolean contraseñaVisible) {

        String rutaIcono = contraseñaVisible ? "/ojo-abierto.png" : "/ojo-cerrado.png";
        iconoOjo.setImage(new Image(getClass().getResourceAsStream(rutaIcono)));
    }

    public void inicializarIcono(ImageView iconoOjo) {

        actualizarIcono(iconoOjo, contraseñaVisible);
    }

    public static boolean esContraseñaIgual(PasswordField campoContraseña, PasswordField campoConfirmarContraseña) {

        return campoContraseña.getText().equals(campoConfirmarContraseña.getText());
    }

}