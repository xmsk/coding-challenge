package ch.qos.logback;

import org.hibernate.Session;

import java.io.File;
import java.io.FileNotFoundException;
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
    // cache for temporary LogEntries that have not matched another event yet
    private final Vector<LogEntry> logEntries;
    private final Session dbSession;

    /**
     * Constructs a LogFileParser for a given log file and threshold
     * @param filename  name of the log file to parse
     * @param dbSession hibernate session for storing LogEvents in the database
     */
    public LogFileParser(String filename, Session dbSession) {
        this.filename = filename;
        this.logEntries = new Vector<>();
        this.dbSession = dbSession;
    }

    /**
     * Parses the log file this.filename, records all LogEntries in the local HSQLDB, and flags all LogEvents that last
     * for longer than this.threshold
     */
    public void parse() throws FileNotFoundException {
        File logFile = new File(this.filename);
        Scanner myReader = new Scanner(logFile);

        LOGGER.log(Level.FINE, "Start parsing LogEvents from file {0}", this.filename);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            LOGGER.log(Level.FINER, "Raw JSON log event read from log file: '{0}'", data);
            try {
                LogEntry logEntry = LogEntry.fromJson(data);
                LOGGER.log(Level.FINEST, "Parsed LogEvent: '{0}'", logEntry);
                this.handleLogEntry(logEntry);
            } catch (IllegalLogEntryException e) {
                LOGGER.log(Level.SEVERE, "LogEntry could not be created from Json data `{0}`, skipping", data);
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }

        myReader.close();

        if (this.logEntries.size() > 0) {
            LOGGER.log(
                Level.WARNING,
                "Unmatched LogEntries remaining after parsing: {}",
                this.logEntries.toString()
            );
        }
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
        LOGGER.log(Level.FINER, "`{0}` popped from the local cache", oldLogEntry);
        if (oldLogEntry == null) {
            LOGGER.log(Level.FINER, "No matching LogEvent found, adding `{0}` to local cache", logEntry);
            this.logEntries.add(logEntry);
        } else {
            try {
                LogEvent logEvent = new LogEvent(oldLogEntry, logEntry);
                this.recordLogEvent(logEvent);
            } catch (IncompatibleLogentriesException e) {
                LOGGER.log(
                    Level.SEVERE, "Cannot create LogEvent from provided LogEntries `{0}` and `{1}`, skipping",
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
        try {
            this.dbSession.getTransaction().begin();
            this.dbSession.save(logEvent);
            this.dbSession.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not record `{0}`", logEvent);
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }
}
