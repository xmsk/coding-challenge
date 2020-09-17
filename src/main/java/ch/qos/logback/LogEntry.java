package ch.qos.logback;

public class LogEntry {
    // TODO: create ApplicationServerLogEvent sub class?
    private String id;
    // TODO: change to enum?
    private String state;
    private long timestamp;

    private String type;
    private String host;

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
        return this.id;
    }

    // TODO: remove setters?
    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
