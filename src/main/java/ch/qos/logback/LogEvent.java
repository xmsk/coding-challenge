package ch.qos.logback;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogEvent {
    private static final Logger LOGGER = Logger.getLogger(LogEvent.class.getName());

    // TODO: create ApplicationServerLogEvent sub class?
    private final String id;
    // TODO: change to enum?
    private final String state;
    private final long timestamp;

    private final String type;
    private final String host;

    /**
     * Constructs a "simple" LogEvent
     */
    public LogEvent(String id, String state, long timestamp) {
        this(id, state, timestamp, null, null);
    }

    /**
     * Constructs an "Application Server" LogEvent
     * @param id        id of the LogEvent
     * @param state     state of the LogEvent ("STARTED" or "FINISHED")
     * @param timestamp timestamp for the LogEvent
     * @param type      type of the LogEvent
     * @param host      host of the application server
     */
    public LogEvent(String id, String state, long timestamp, String type, String host) {
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
        this.type = type;
        this.host = host;
    }

    public String toString() {
        return "LogEvent - id (" + this.id + "), state (" + this.state + "), timestamp (" + this.timestamp +
            "), type (" + this.type + "), host (" + this.host + ")";
    }

    public String getId() {
        return id;
    }

    /**
     * calculates the duration of a LogEvent from two separate LogEvent objects, the order of the LogEvents
     * ("STARTED"/"FINISHED") doesn't matter
     * @param logEvent1 first LogEvent
     * @param logEvent2 second LogEvent
     * @return          the time in ms between the two LogEvents
     */
    public static long duration(LogEvent logEvent1, LogEvent logEvent2) {
        // TODO: do some checking about the two log events
        //  * check if both events have the same id?
        //  * one started and one finished?
        //  * some check for positivity of duration?
        final long duration = Math.abs(logEvent1.timestamp - logEvent2.timestamp);
        LOGGER.log(
            Level.FINER, "Duration between '{0}' and '{1}' is {2}",
            new Object[] {logEvent1, logEvent2, duration}
        );
        return duration;
    }
}
