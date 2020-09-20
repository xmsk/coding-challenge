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
java -cp <class_path> ch.qos.logback.LogProcessor
```
where `<class_path>` can be found using the `gradle` task `gradle printLib`.