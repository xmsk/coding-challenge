package ch.qos.logback;

import com.google.gson.Gson;

public class LogEntry {
    private final String id;
    private final String state;
    private final long timestamp;

    private final String type;
    private final String host;

    /**
     * constructs a "simple" LogEntry
     */
    public LogEntry(String id, String state, long timestamp) {
        this(id, state, timestamp, null, null);
    }

    /**
     * Constructs an Application Server LogEntry
     * @param id        id of the LogEvent
     * @param state     state of the LogEvent ("STARTED" or "FINISHED")
     * @param timestamp timestamp for the LogEvent
     * @param type      type of the LogEvent
     * @param host      host of the application server
     */
    public LogEntry(String id, String state, long timestamp, String type, String host) {
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
        this.type = type;
        this.host = host;
    }

    public String toString() {
        return "LogEntry - id (" + this.id + "), state (" + this.state + "), timestamp (" + this.timestamp +
            "), type (" + this.type + "), host (" + this.host + ")";
    }

    public String getId() {
        return this.id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getState() {
        return state;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    /**
     * Generates a LogEntry object from a json string
     * @param data  json String representing a LogEntry object
     * @return      a LogEntry object with the attributes set according to the Json object in the String
     */
    public static LogEntry fromJson(String data) throws IllegalLogEntryException {
        Gson g = new Gson();
        LogEntry logEntry = g.fromJson(data, LogEntry.class);

        if (logEntry.getId() == null || logEntry.getState() == null) {
            throw new IllegalLogEntryException("Mandatory field for LogEntry missing");
        }

        return logEntry;
    }
}
