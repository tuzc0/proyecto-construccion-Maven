package logica;

import java.sql.SQLException;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.Logger;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import java.net.UnknownHostException;



public class ManejadorExcepciones {

    private final Logger LOGGER;
    private final IGestorAlertas MENSAJE_DE_ALERTA;

    public ManejadorExcepciones(IGestorAlertas mensajeDeAlerta, Logger logger ) {

        this.MENSAJE_DE_ALERTA = mensajeDeAlerta;
        this.LOGGER = logger;
    }

    public void manejarSQLException(SQLException e) {

        String estadoSQL = e.getSQLState();

        switch (estadoSQL) {

            case "08S01" -> {

                LOGGER.error("Error de conexión con la base de datos: " + e);
                MENSAJE_DE_ALERTA.mostrarAlerta(
                        "Error de conexión",
                        "No se pudo establecer una conexión con la base de datos.",
                        "La base de datos se encuentra desactivada o hay un problema de red."
                );
            }

            case "42000" -> {

                LOGGER.error("Error de sintaxis SQL o base de datos no existe: " + e);
                MENSAJE_DE_ALERTA.mostrarAlerta(
                        "Error de conexión",
                        "No se pudo establecer conexión con la base de datos.",
                        "La base de datos no existe o hay un error de sintaxis en la consulta."
                );
            }

            case "28000" -> {

                LOGGER.error("Credenciales inválidas: " + e);
                MENSAJE_DE_ALERTA.mostrarAlerta(
                        "Credenciales inválidas",
                        "Usuario o contraseña incorrectos.",
                        "Verifique los datos de acceso a la base de datos."
                );
            }

            case "23000" -> {

                LOGGER.error("Violación de restricción de integridad: " + e);
                MENSAJE_DE_ALERTA.mostrarAlerta(
                        "Dato duplicado o relación inválida",
                        "No se puede completar la operación.",
                        "Verifique que los datos no estén repetidos o que las relaciones sean válidas."
                );
            }

            case "42S02" -> {

                LOGGER.error("Tabla no encontrada: " + e);
                MENSAJE_DE_ALERTA.mostrarAlerta(
                        "Tabla inexistente",
                        "No se encontró una tabla necesaria para ejecutar la operación.",
                        "Verifique que todas las tablas estén correctamente creadas."
                );
            }

            case "42S22" -> {

                LOGGER.error("Columna no encontrada: " + e);
                MENSAJE_DE_ALERTA.mostrarAlerta(
                        "Columna inexistente",
                        "No se encontró una columna requerida.",
                        "Revise los nombres de las columnas en su consulta."
                );
            }

            default -> {

                LOGGER.error("SQLState desconocido (" + estadoSQL + "): " + e);
                MENSAJE_DE_ALERTA.mostrarAlerta(
                        "Error desconocido",
                        "Se produjo un error inesperado al acceder a la base de datos.",
                        "Contacte a soporte técnico."
                );
            }
        }
    }

    public void manejarIOException(IOException e) {

        if (e instanceof FileNotFoundException) {

            LOGGER.error("Archivo no encontrado: " + e);
            MENSAJE_DE_ALERTA.mostrarAlerta(
                    "Archivo no encontrado",
                    "No se pudo encontrar el archivo especificado.",
                    "Verifique que el archivo exista."
            );

        } else if (e instanceof EOFException) {

            LOGGER.error("Fin inesperado del archivo: " + e);
            MENSAJE_DE_ALERTA.mostrarAlerta(
                    "Lectura incompleta",
                    "Se alcanzó el final del archivo antes de lo esperado.",
                    "El archivo puede estar incompleto o dañado."
            );

        } else if (e instanceof ConnectException) {

            LOGGER.error("Error de conexión de red: " + e);
            MENSAJE_DE_ALERTA.mostrarAlerta(
                    "Fallo de conexión",
                    "No se pudo conectar con el recurso.",
                    "Revise su conexión o intente más tarde."
            );

        } else {

            LOGGER.error("Error de E/S desconocido: " + e);
            MENSAJE_DE_ALERTA.mostrarAlerta(
                    "Error de entrada/salida",
                    "Se produjo un error inesperado.",
                    "Verifique los recursos utilizados o contacte soporte."
            );
        }
    }

    public void manejarExcepcionDrive(Exception e) {

        if (e instanceof GoogleJsonResponseException jsonEx) {

            int statusCode = jsonEx.getStatusCode();
            LOGGER.error("Error de la API de Google Drive (HTTP " + statusCode + "): " + jsonEx);

            switch (statusCode) {

                case 401 -> MENSAJE_DE_ALERTA.mostrarAlerta(

                        "No autorizado",
                        "Tu sesión ha expirado o no tienes permisos.",
                        "Inicia sesión nuevamente."
                );

                case 403 -> MENSAJE_DE_ALERTA.mostrarAlerta(

                        "Acceso denegado",
                        "No tienes permisos para realizar esta operación.",
                        "Verifica los permisos de tu cuenta."
                );

                case 404 -> MENSAJE_DE_ALERTA.mostrarAlerta(

                        "Archivo no encontrado",
                        "El archivo o recurso solicitado no existe.",
                        "Verifica que el ID sea correcto."
                );

                case 500, 503 -> MENSAJE_DE_ALERTA.mostrarAlerta(

                        "Error del servidor",
                        "Google Drive está teniendo problemas.",
                        "Intenta de nuevo más tarde."
                );

                default -> MENSAJE_DE_ALERTA.mostrarAlerta(

                        "Error desconocido",
                        "Se produjo un error con Google Drive.",
                        "Código de error: " + statusCode

                );
            }
        } else if (e instanceof UnknownHostException || e instanceof ConnectException) {

            LOGGER.error("Error de red: No hay conexión a internet o el servidor es inaccesible: " + e);
            MENSAJE_DE_ALERTA.mostrarAlerta(
                    "Sin conexión",
                    "No se puede conectar a Google Drive.",
                    "Verifica tu conexión a internet."
            );

        } else if (e instanceof IOException ioEx) {
            manejarIOException(ioEx);

        } else {

            LOGGER.error("Excepción no controlada durante operación con Google Drive: " + e);
            MENSAJE_DE_ALERTA.mostrarAlerta(
                    "Error inesperado",
                    "Ocurrió un error inesperado al subir el archivo.",
                    "Detalles: " + e.getMessage()
            );

        }
    }

}

