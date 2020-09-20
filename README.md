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
### Providing the database
The Java program assumes that a HSQLDB is running on localhost, make sure this is the case.
If not database is running, download the HSQLDB package from [sourceforge](https://sourceforge.net/projects/hsqldb/files/hsqldb/hsqldb_2_5/hsqldb-2.5.1.zip/download).
From the working folder start the HSQLDB like so
```
java -cp <hsqldb_installdir>/lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:mydb --dbname.0 Logging
``` 

### Running the program
Run the project from the top-level directory by passing the name of the log file (`<log_file_name>`) as an argument to the `gradle run` task like so
```
gradle run --args="<log_file_name>"
```