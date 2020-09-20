# Coding Assignment
## Building the project
Build the project using gradle in the top-level directory like so
```
gradle build
```

## Test project
Test the project using gradle like so
```
gradle test
```

## Running the project
The application automatically starts an HSQLDB server writing to a local file based database `mydb`.
Run the project from the top-level directory by passing the name of the log file (`<log_file_name>`) as an argument to the `gradle run` task like so
```
gradle run --args="<log_file_name>"
```

Alternatively the project can be run manually using the `java` command like so
```
java -cp <class_path> ch.qos.logback.LogProcessor <log_file_name>
```
where `<class_path>` can be found using the `gradle` task `gradle printLib`.

## Troubleshooting
### Port already in use
If the default port for the HSQLDB (9001) is already in use, it can be changed to a custom port by changing the port definition in three files
```
src/main/java/ch/qos/logback/HsqldbServer.java
---
props.setProperty("server.port", "9001");
```
```
ll src/main/resources/hibernate.cfg.xml
---
<property name="connection.url">jdbc:hsqldb:hsql://localhost:9001/logging</property>
```
```
ll src/test/resources/hibernate.cfg.xml
---
<property name="connection.url">jdbc:hsqldb:hsql://localhost:9001/logging</property>
```