package GUI;

import java.sql.SQLException;

import GUI.utilidades.Utilidades;
import org.apache.logging.log4j.Logger;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;


public class ManejadorExepciones {

    public static void manejarSQLException(SQLException e, Logger logger, Utilidades utilidades) {

        String estadoSQL = e.getSQLState();

        switch (estadoSQL) {
            case "08S01":
                logger.error("Error de conexión con la base de datos: " + e);
                utilidades.mostrarAlerta(
                        "Error de conexión",
                        "No se pudo establecer una conexión con la base de datos.",
                        "La base de datos se encuentra desactivada o hay un problema de red."
                );
                break;

            case "42000":
                logger.error("La base de datos no existe o hay un error de sintaxis SQL: " + e);
                utilidades.mostrarAlerta(
                        "Error de conexión",
                        "No se pudo establecer conexión con la base de datos.",
                        "La base de datos actualmente no existe o hay un error en la consulta."
                );
                break;

            case "28000":
                logger.error("Credenciales inválidas para el acceso: " + e);
                utilidades.mostrarAlerta(
                        "Credenciales inválidas",
                        "Usuario o contraseña incorrectos.",
                        "Por favor, verifique los datos de acceso a la base de datos."
                );
                break;

            case "23000":
                logger.error("Violación de restricción de integridad: " + e);
                utilidades.mostrarAlerta(
                        "Dato duplicado o relación inválida",
                        "No se puede completar la operación.",
                        "Verifique que los datos no estén repetidos o que las relaciones sean válidas."
                );
                break;

            case "42S02":
                logger.error("Tabla no encontrada: " + e);
                utilidades.mostrarAlerta(
                        "Tabla inexistente",
                        "No se encontró una tabla necesaria para ejecutar la operación.",
                        "Verifique que todas las tablas estén correctamente creadas en la base de datos."
                );
                break;

            case "42S22":
                logger.error("Columna no encontrada: " + e);
                utilidades.mostrarAlerta(
                        "Columna inexistente",
                        "No se encontró una columna requerida en la operación.",
                        "Revise que los nombres de columnas usados coincidan con los definidos en la base de datos."
                );
                break;

            default:
                logger.error("Error de SQL no manejado: " + estadoSQL + " - " + e);
                utilidades.mostrarAlerta(
                        "Error del sistema",
                        "Se produjo un error inesperado al acceder a la base de datos.",
                        "Por favor, contacte al soporte técnico."
                );
                break;
        }
    }


    public static void manejarIOException(IOException e, Logger logger, Utilidades utilidades) {
        switch (e) {
            case FileNotFoundException fnfe -> {
                logger.error("Archivo no encontrado: " + fnfe);
                utilidades.mostrarAlerta(
                        "Archivo no encontrado",
                        "No se pudo encontrar el archivo especificado.",
                        "Verifique que el archivo exista y tenga permisos adecuados."
                );
            }
            case EOFException eofe -> {
                logger.error("Fin inesperado del archivo: " + eofe);
                utilidades.mostrarAlerta(
                        "Lectura incompleta",
                        "Se alcanzó el final del archivo antes de lo esperado.",
                        "El archivo puede estar incompleto o dañado."
                );
            }
            case ConnectException ce -> {
                logger.error("Error de conexión de red: " + ce);
                utilidades.mostrarAlerta(
                        "Fallo de conexión",
                        "No se pudo establecer una conexión con el servidor o recurso.",
                        "Revise su conexión de red o la disponibilidad del destino."
                );
            }
            default -> {
                logger.error("Error de E/S no manejado: " + e);
                utilidades.mostrarAlerta(
                        "Error de entrada/salida",
                        "Se ha producido un error inesperado de E/S.",
                        "Verifique los recursos utilizados o contacte a soporte técnico."
                );
            }
        }
    }
}
