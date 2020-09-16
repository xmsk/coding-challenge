package ch.qos.logback;

public class LogEvent {
    private final String id;
    private final long duration;
    private final String type;
    private final String host;
    private final boolean alert;

    /**
     * Constructs a LogEvent from two matching LogEntries
     * @param entry1    first LogEntry
     * @param entry2    second LogEntry
     * @param threshold boolean indicating whether the event should be flagged
     */
    public LogEvent(LogEntry entry1, LogEntry entry2, long threshold) throws IncompatibleLogentriesException {
        if (!entry1.getId().equals(entry2.getId())) {
            throw new IncompatibleLogentriesException("IDs of the two provided LogEntries don't match");
        }
        if (! (entry1.getState().equals("STARTED") && entry2.getState().equals("STOPPED") ||
            entry2.getState().equals("STARTED") && entry1.getState().equals("STOPPED"))) {
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
}
