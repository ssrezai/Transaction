import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by DOTIN SCHOOL 3 on 2/7/2015.
 */
public class WriteLogEntriesToLogFile {
    public static void serverWriteLogFile(String loggerName, Level level, FileHandler fileHandler) throws Exception {

        Logger logger = Logger.getLogger(loggerName);
        logger.addHandler(fileHandler);
        if (level.equals(Level.SEVERE)) {
            logger.severe("SEVERE msg");
        } else if (level.equals(Level.CONFIG)) {
            logger.config("CONFIGURATION msg");
        } else if (level.equals(Level.INFO)) {
            logger.info("INFO msg");
        } else if (level.equals(Level.FINE)) {
            logger.fine("FINE msg");
        } else if (level.equals(Level.FINER)) {
            logger.finer("CONFIGURATION msg");
        }

    }

    public static void clientWriteLogFile(String loggerName, Level level, FileHandler fileHandler) throws Exception {
        Logger logger = Logger.getLogger(loggerName);
        logger.addHandler(fileHandler);
        if (level.equals(Level.SEVERE)) {
            logger.severe("SEVERE msg");
        } else if (level.equals(Level.CONFIG)) {
            logger.config("CONFIGURATION msg");
        } else if (level.equals(Level.INFO)) {
            logger.info("INFO msg");
        } else if (level.equals(Level.FINE)) {
            logger.fine("FINE msg");
        } else if (level.equals(Level.FINER)) {
            logger.finer("CONFIGURATION msg");
        }

    }

}
