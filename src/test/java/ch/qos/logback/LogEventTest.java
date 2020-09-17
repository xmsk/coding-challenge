package ch.qos.logback;

import org.junit.Test;

import static org.junit.Assert.*;

public class LogEventTest {
    private final LogEntry testIdStart = new LogEntry("testId", "STARTED", 123456);
    private final LogEntry testIdFinish = new LogEntry("testId", "FINISHED", 123459);
    private final LogEntry fooIdFlaggedStart = new LogEntry("fooId", "STARTED", 123456);
    private final LogEntry fooIdFlaggedFinish = new LogEntry("fooId", "FINISHED", 123463);

    @Test
    public void createValidLogEvent() throws Exception {
        LogEvent logEvent = new LogEvent(this.testIdStart, testIdFinish);
        assertEquals("testId", logEvent.getId());
        assertEquals(3, logEvent.getDuration());
        assertFalse(logEvent.isAlert());
    }

    @Test
    public void createFlaggedLogEvent() throws Exception {
        LogEvent logEvent = new LogEvent(this.fooIdFlaggedStart, this.fooIdFlaggedFinish);
        assertTrue(logEvent.isAlert());
    }

    @Test(expected = IncompatibleLogentriesException.class)
    public void createLogEventFromLogEntriesWithDifferentIds() throws Exception {
        new LogEvent(this.testIdStart, this.fooIdFlaggedFinish);
    }

    @Test(expected = IncompatibleLogentriesException.class)
    public void createLogEventWithSameState() throws Exception {
        new LogEvent(this.testIdStart, this.testIdStart);
    }
}