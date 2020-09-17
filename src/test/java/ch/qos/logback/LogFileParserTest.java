package ch.qos.logback;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LogFileParserTest {
    // TODO: create tests for DB accessing methods?
    private LogFileParser logFileParser = new LogFileParser("dummy.txt");

    @Before
    public void setUp() throws Exception {
        // TODO: set p some cache records for testing?
    }

    @Test
    public void popExistingLogEntry() {
        // TODO: test method to pop entries from cache
    }
}