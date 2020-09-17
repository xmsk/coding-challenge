package ch.qos.logback;

import org.junit.Test;

import static org.junit.Assert.*;

public class LogEntryTest {
    @Test
    public void generateValidSimpleLogEntryFromJson() throws Exception {
        String data = "{'id':'testId', 'state': 'STARTED', 'timestamp': 123456}";
        LogEntry logEntry = LogEntry.fromJson(data);
        assertEquals("testId", logEntry.getId());
        assertEquals("STARTED", logEntry.getState());
        assertEquals(123456, logEntry.getTimestamp());
        assertNull(logEntry.getType());
        assertNull(logEntry.getHost());
    }

    @Test
    public void generateValidApplicationServerLogEntryFromJson() throws Exception {
        String data = "{'id':'testId', 'state': 'STARTED', 'timestamp': 123456, 'type': 'APPLICATION_LOG'," +
            " 'host': 'testHost'}";
        LogEntry logEntry = LogEntry.fromJson(data);
        assertEquals("testId", logEntry.getId());
        assertEquals("STARTED", logEntry.getState());
        assertEquals(123456, logEntry.getTimestamp());
        assertEquals("APPLICATION_LOG", logEntry.getType());
        assertEquals("testHost", logEntry.getHost());
    }

    @Test(expected = IllegalLogEntryException.class)
    public void generateLogEntryWithoutId() throws Exception {
        String data = "{'state': 'STARTED', 'timestamp': 123456}";
        LogEntry.fromJson(data);
    }

    @Test(expected = IllegalLogEntryException.class)
    public void generateLogEntryWithoutState() throws Exception {
        String data = "{'id': 'testId', 'timestamp': 123456}";
        LogEntry.fromJson(data);
    }
}