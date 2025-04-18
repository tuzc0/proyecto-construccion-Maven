import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("Esto es un mensaje informativo.");
        logger.debug("Mensaje de depuración.");
        logger.error("¡Algo salió mal!");
    }
}
