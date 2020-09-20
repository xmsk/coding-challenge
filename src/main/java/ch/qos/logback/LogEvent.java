package ch.qos.logback;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class LogEvent {
    public static long threshold = 4;

    private String id;
    private long duration;
    private String type;
    private String host;
    private boolean alert;

    /**
     * Default constructor for hibernate
     */
    public LogEvent() {}

    /**
     * Constructor for a "simple" LogEvent
     */
    public LogEvent(String id, long duration, boolean alert) {
        this(id, duration, null, null, alert);
    }

    /**
     * Standard constructor for LogEvent
     * @param id        id of the LogEvent
     * @param duration  duration of the LogEvent
     * @param type      type of the LogEvent (for Application Server LogEvents)
     * @param host      host of the LogEvent (for Application Server LogEvents)
     * @param alert     indicates if the LogEvent is flagged
     */
    public LogEvent(String id, long duration, String type, String host, boolean alert) {
        this.id = id;
        this.duration = duration;
        this.type = type;
        this.host = host;
        this.alert = alert;
    }

    /**
     * Constructs a LogEvent from two matching LogEntries
     * @param entry1    first LogEntry
     * @param entry2    second LogEntry
     */
    public LogEvent(LogEntry entry1, LogEntry entry2) throws IncompatibleLogentriesException {
        if (!entry1.getId().equals(entry2.getId())) {
            throw new IncompatibleLogentriesException("IDs of the two provided LogEntries don't match");
        }
        if (! (entry1.getState().equals("STARTED") && entry2.getState().equals("FINISHED") ||
            entry2.getState().equals("STARTED") && entry1.getState().equals("FINISHED"))) {
            throw new IncompatibleLogentriesException("LogEntries provided do not form a proper LogEvent");
        }

        this.id = entry1.getId();
        this.duration = Math.abs(entry1.getTimestamp() - entry2.getTimestamp());
        this.type = entry1.getType();
        this.host = entry1.getHost();
        this.alert = this.duration > threshold;
    }

    public String toString() {
        return "LogEvent - id (" + this.id + "), duration (" + this.duration + "), type (" + this.type + "), host (" +
             this.host + "), alert (" + this.alert + ")";
    }

    @Id
    public String getId() {
        return id;
    }

    public long getDuration() {
        return duration;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }
}
