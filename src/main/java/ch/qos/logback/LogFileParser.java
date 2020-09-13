package ch.qos.logback;

import com.google.gson.Gson;

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
    private final long threshold;
    // cache for temporary LogEvents that have not matched another event yet
    private final Vector<LogEvent> logEvents;

    public LogFileParser(String filename) {
        this(filename, 4);
    }

    /**
     * Constructs a LogFileParser for a given log file and threshold
     * @param filename  name of the log file to parse
     * @param threshold threshold in ms for LogEvents to be flagged
     */
    public LogFileParser(String filename, long threshold) {
        this.filename = filename;
        this.threshold = threshold;
        this.logEvents = new Vector<>();
    }

    /**
     * Parses the log file this.filename, records all LogEvents in the local HSQLDB, and flags all LogEvents that last
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
            LogEvent logEvent = g.fromJson(data, LogEvent.class);
            LOGGER.log(Level.FINEST, "Parsed LogEvent: '{0}'", logEvent);
            this.handleLogEvent(logEvent);
        }

        myReader.close();
    }

    /**
     * Returns a LogEvent with a given id and removes it from the logEvents cache
     * @param id    id of the LogEvent to find, return, and remove
     * @return      the LogEvent with the given id or null if none is found
     */
    private LogEvent popLogEventById(String id) {
        LOGGER.log(Level.FINER, "Looking for LogEvent with id '{0}'", id);
        for (LogEvent logEvent : this.logEvents) {
            LOGGER.log(Level.FINEST, "'{0}' found in local LogEvent cache", logEvent);
            if (logEvent.getId().equals(id)) {
                LOGGER.log(Level.FINER, "'{0}' matched the supplied id, removing from local cache", logEvent);
                this.logEvents.remove(logEvent);
                return logEvent;
            }
        }

        LOGGER.log(Level.FINER, "No LogEvent in the local cache matched the supplied id");
        return null;
    }

    /**
     * Handles a new LogEvent that has been read from the log file; if it has a pendant in this.logEvents, match them
     * and test for duration, otherwise add it to the local cache
     * @param logEvent  LogEvent to handle
     */
    private void handleLogEvent(LogEvent logEvent) {
        LogEvent oldLogEvent = this.popLogEventById(logEvent.getId());
        LOGGER.log(Level.FINER, "'{0}' popped from the local cache", oldLogEvent);
        if (oldLogEvent == null) {
            LOGGER.log(Level.FINER, "No matching LogEvent found, adding '{0}' to local cache", logEvent);
            this.logEvents.add(logEvent);
            return;
        }

        long duration = LogEvent.duration(oldLogEvent, logEvent);
        if (duration > this.threshold) {
            LOGGER.log(
                Level.FINE, "Duration for '{0}' and '{1}' exceeds threshold '{2}', flagging",
                new Object[] {logEvent, oldLogEvent, this.threshold}
            );
            // TODO: properly handle flagging
            this.flagLogEvent(logEvent);
        }

        // TODO: properly handle (i.e. record) LogEvents
    }

    /**
     * Flags a logEvent
     * @param logEvent  the LogEvent to flag
     */
    private void flagLogEvent(LogEvent logEvent) {
        // TODO: use better method for flagging LogEvents? -> local DB
        System.out.println("FLAG: " + logEvent.getId());
    }
}
