package ch.qos.logback;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
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
            Connection con = HsqldbConnection.getConnection();
            LogFileParser logFileParser = new LogFileParser(filename, con);
            try {
                logFileParser.parse();
            } catch (FileNotFoundException e) {
                LOGGER.log(Level.SEVERE, "Log file {0} not found", filename);
                LOGGER.log(Level.SEVERE, e.toString(), e);
                System.exit(-1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could create a connection to the HSQLDB, is an HSQLDB server running?");
            LOGGER.log(Level.SEVERE, e.toString(), e);
            System.exit(-1);
        }
    }
}
