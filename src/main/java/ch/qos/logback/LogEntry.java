package ch.qos.logback;

public class LogEntry {
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
    public LogEntry(String id, String state, long timestamp) {
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
        return id;
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
}
