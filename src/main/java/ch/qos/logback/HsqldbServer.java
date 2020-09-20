package ch.qos.logback;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl;

public class HsqldbServer {
    private final Server dbServer;
    private final String dbFile;

    public HsqldbServer() {
        this("mydb");
    }

    /**
     * Constructs a HsqldbServer with a specific database file
     * @param dbFile    filename where the database should be stored
     */
    public HsqldbServer(String dbFile) {
        this.dbServer = new org.hsqldb.Server();
        this.dbFile = dbFile;
    }

    private void configureServer() throws java.io.IOException, ServerAcl.AclFormatException {
        HsqlProperties props = new HsqlProperties();
        props.setProperty("server.database.0", "file:" + this.dbFile);
        props.setProperty("server.dbname.0", "logging");
        props.setProperty("server.port", "9001");
        this.dbServer.setProperties(props);
    }

    public void startServer() throws java.io.IOException, ServerAcl.AclFormatException {
        if (this.dbServer.isNotRunning()) {
            this.configureServer();
            this.dbServer.start();
        }
    }

    public void stopServer() {
        if(! this.dbServer.isNotRunning()) {
            this.dbServer.stop();
        }
    }
}
