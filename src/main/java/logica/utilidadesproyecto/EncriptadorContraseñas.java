package logica.utilidadesproyecto;

import accesoadatos.LectorPropiedadesContrasena;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncriptadorContraseñas {

    private static SecretKeySpec obtenerClave() throws Exception {

        String clave = LectorPropiedadesContrasena.obtenerClaveSecreta();
        if (clave == null || clave.length() != 16) {

            throw new IllegalArgumentException("La clave debe tener exactamente 16 caracteres");
        }

        return new SecretKeySpec(clave.getBytes(), "AES");
    }

    public static String encriptar(String texto) throws Exception {

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, obtenerClave());
        byte[] textoEncriptado = cipher.doFinal(texto.getBytes());
        return Base64.getEncoder().encodeToString(textoEncriptado);

    }

    public static String desencriptar(String textoEncriptado) throws Exception {

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, obtenerClave());
        byte[] textoDecodificado = Base64.getDecoder().decode(textoEncriptado);
        byte[] textoOriginal = cipher.doFinal(textoDecodificado);
        return new String(textoOriginal);

    }
}
