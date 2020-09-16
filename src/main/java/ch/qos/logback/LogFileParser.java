package ch.qos.logback;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used to parse a log file and flag all LogEvents that last longer than threshold
 */
public class LogFileParser {
    private static final Logger LOGGER = Logger.getLogger(LogFileParser.class.getName());

    private final String filename;
    private final long threshold;
    // cache for temporary LogEntries that have not matched another event yet
    private final Vector<LogEntry> logEntries;
    private final Connection dbConnection;

    public LogFileParser(String filename, Connection con) {
        this(filename, 4, con);
    }

    /**
     * Constructs a LogFileParser for a given log file and threshold
     * @param filename  name of the log file to parse
     * @param threshold threshold in ms for LogEvents to be flagged
     */
    public LogFileParser(String filename, long threshold, Connection con) {
        this.filename = filename;
        this.threshold = threshold;
        this.logEntries = new Vector<>();
        this.dbConnection = con;
    }

    /**
     * Parses the log file this.filename, records all LogEntries in the local HSQLDB, and flags all LogEvents that last
     * for longer than this.threshold
     */
    public void parse() throws FileNotFoundException {
        // TODO: add segmentation for memory efficiency
        Gson g = new Gson();

        File logFile = new File(this.filename);
        Scanner myReader = new Scanner(logFile);

        LOGGER.log(Level.FINE, "Start parsing LogEvents from file {0}", this.filename);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            LOGGER.log(Level.FINER, "Raw JSON log event read from log file: '{0}'", data);
            // TODO: catch some errors and log in case generation fails, etc.
            LogEntry logEntry = g.fromJson(data, LogEntry.class);
            LOGGER.log(Level.FINEST, "Parsed LogEvent: '{0}'", logEntry);
            this.handleLogEntry(logEntry);
        }

        myReader.close();
    }

    /**
     * Returns a LogEntry with a given id and removes it from the this.logEntries cache
     * @param id    id of the LogEvent to find, return, and remove
     * @return      the LogEvent with the given id or null if none is found
     */
    private LogEntry popLogEntryById(String id) {
        LOGGER.log(Level.FINER, "Looking for LogEvent with id '{0}'", id);
        for (LogEntry logEntry : this.logEntries) {
            LOGGER.log(Level.FINEST, "'{0}' found in local LogEvent cache", logEntry);
            if (logEntry.getId().equals(id)) {
                LOGGER.log(Level.FINER, "'{0}' matched the supplied id, removing from local cache", logEntry);
                this.logEntries.remove(logEntry);
                return logEntry;
            }
        }

        LOGGER.log(Level.FINER, "No LogEvent in the local cache matched the supplied id");
        return null;
    }

    /**
     * Handles a new LogEvent that has been read from the log file; if it has a pendant in this.logEvents, match them
     * and test for duration, otherwise add it to the local cache
     * @param logEntry  LogEvent to handle
     */
    private void handleLogEntry(LogEntry logEntry) {
        LogEntry oldLogEntry = this.popLogEntryById(logEntry.getId());
        LOGGER.log(Level.FINER, "'{0}' popped from the local cache", oldLogEntry);
        if (oldLogEntry == null) {
            LOGGER.log(Level.FINER, "No matching LogEvent found, adding '{0}' to local cache", logEntry);
            this.logEntries.add(logEntry);
            return;
        } else {
            try {
                // TODO: is there a better place for storing the threshold?
                LogEvent logEvent = new LogEvent(oldLogEntry, logEntry, this.threshold);
                this.recordLogEvent(logEvent);
            } catch (IncompatibleLogentriesException e) {
                LOGGER.log(
                    Level.SEVERE, "Cannot create LogEvent from provided LogEntries '{0}' and '{1}', skipping",
                    new Object[] {oldLogEntry, logEntry}
                );
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }
    }

    /**
     * Records a LogEvent in the database referenced by this.dbConnection and flags it if the duration exceeds
     * this.threshold
     * @param logEvent  the LogEvent to be recorded in the database
     */
    private void recordLogEvent(LogEvent logEvent) {
        // TODO: implement proper recording
        System.out.println(logEvent);
    }
}
