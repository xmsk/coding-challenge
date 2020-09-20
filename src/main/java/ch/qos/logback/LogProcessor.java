package ch.qos.logback;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogProcessor {
    private static final Logger LOGGER = Logger.getLogger(LogProcessor.class.getName());

    public static void main(String[] args) {
        if (args.length < 1) {
            LOGGER.log(Level.SEVERE, "No file name passed...exiting!");
            System.exit(-1);
        }
        final String filename = args[0];
        LOGGER.log(Level.INFO, "The first file name argument was: " + filename);

        try {
            SessionFactory dbSessionFactory = HibernateUtil.getNewSessionFactory();
            Session dbSession = dbSessionFactory.openSession();
            LogFileParser logFileParser = new LogFileParser(filename, dbSession);
            logFileParser.parse();
            dbSession.close();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Log file {0} not found", filename);
            LOGGER.log(Level.SEVERE, e.toString(), e);
            System.exit(-1);
        } catch (HibernateException e) {
            LOGGER.log(Level.SEVERE, "Cannot connect to database, is the HSQLDB running?");
            LOGGER.log(Level.SEVERE, e.toString(), e);
            System.exit(-1);
        }
    }
}
