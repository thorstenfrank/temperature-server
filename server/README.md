# temperature

An application to store and access temperature measurements.

## Temperature Server application

Requirements:

* Java 8
* maven (tested with 3.3.9 and 3.5.3)

Java 9 is currently **not** supported - the application will neither run nor build. All the necessary hoops that need
jumping through to get this working (see e.g. [here] (https://blog.frankel.ch/migrating-to-java-9/1/)) with no real
benefit were the deciding factors. Once Spring Boot, Maven and Hibernate have fully committed to Java9, my opinion may change.

### Building

Switch to the `server` directory and run `mvn clean install` in the root directory.

### Running

From the root of the project, switch to the `server/target` subdirectory. Run

```
java -jar temperature-server-<version>.jar
```

The default datastore is an in-memory H2 database.

The default port used i 8080 - to change it, add the option `-Dserver.port=<port number>` to the command line, e.g.

```
java -Dserver.port=8081 -jar temperature-server-<version>.jar
```
