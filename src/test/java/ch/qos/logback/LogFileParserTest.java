package ch.qos.logback;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LogFileParserTest {
    private Session dbSession;

    @Before
    public void setUp() throws Exception {
        SessionFactory dbSessionFactory = HibernateUtil.getNewSessionFactory();
        this.dbSession = dbSessionFactory.openSession();
    }

    @Test
    public void parseRegularFile() throws Exception {
        LogFileParser parser = new LogFileParser("src/test/resources/regularFile.txt");
        parser.parse();
        assertEquals(3, this.dbSession.createQuery("from LogEvent").list().size());
    }

    // TODO: add more parse tests
    //  - file not found?
    //  - invalid log entries - works fine?
}