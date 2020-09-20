package ch.qos.logback;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class LogFileParserTest {
    private Session dbSession;

    @Before
    public void setUp() {
        SessionFactory dbSessionFactory = HibernateUtil.getNewSessionFactory();
        this.dbSession = dbSessionFactory.openSession();
    }

    @Test
    public void parseRegularFile() throws Exception {
        LogFileParser parser = new LogFileParser("src/test/resources/regularFile.txt", dbSession);
        parser.parse();
        assertEquals(3, this.dbSession.createQuery("from LogEvent").list().size());
    }

    @Test
    public void singleFlaggedEntry() throws Exception {
        LogFileParser parser = new LogFileParser("src/test/resources/singleFlaggedEntry.txt", dbSession);
        parser.parse();
        LogEvent actual = (LogEvent) this.dbSession.createQuery("from LogEvent").list().get(0);
        assertEquals("testId", actual.getId());
        assertEquals(6, actual.getDuration());
        assertTrue(actual.isAlert());
    }

    @Test
    public void singleUnflaggedEntry() throws Exception {
        LogFileParser parser = new LogFileParser("src/test/resources/singleUnflaggedEntry.txt", dbSession);
        parser.parse();
        LogEvent logEvent = (LogEvent) this.dbSession.createQuery("from LogEvent").list().get(0);
        assertEquals("testId", logEvent.getId());
        assertEquals(3, logEvent.getDuration());
        assertFalse(logEvent.isAlert());
    }

    @Test
    public void unmatchedEntry() throws Exception {
        LogFileParser parser = new LogFileParser("src/test/resources/unmatchedEntry.txt", dbSession);
        parser.parse();
        // only wrote one event to the database and ignored the unmatched Event
        assertEquals(1, this.dbSession.createQuery("from LogEvent").list().size());
    }

    @Test
    public void garbageEntry() throws Exception {
        LogFileParser parser = new LogFileParser("src/test/resources/garbageEntry.txt", dbSession);
        parser.parse();
        // only wrote one event to the database and ignored the garbage Event
        assertEquals(1, this.dbSession.createQuery("from LogEvent").list().size());
    }

    @Test(expected = FileNotFoundException.class)
    public void fileNotFound() throws Exception {
        LogFileParser parser = new LogFileParser("src/test/resources/fileNotFound.dummy", dbSession);
        parser.parse();
    }
}