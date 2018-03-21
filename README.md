# Temperature Server application

A simple Spring Boot service for storing, retrieving and viewing temperature measurements. The idea is that for a certain `name` (e.g. location) a number of temperature measurements will be added over time.

## Requirements:

* Java 8
* maven (tested with 3.3.9 and 3.5.3)

Java 9 is currently **not** supported - the application will neither run nor build. All the necessary hoops that need
jumping through to get this working (see e.g. [here](https://blog.frankel.ch/migrating-to-java-9/1/)) with no real
benefit were the deciding factors. Once Spring Boot, Maven and Hibernate have fully committed to Java9, my opinion may change.

## Building

A simple `mvn clean install` will do.

## Running

From the root of the project, switch to the `target` subdirectory. Run

```
java -jar temperature-server-<version>.jar
```

The default port used is 8080 - to change it, add the VM option `-Dserver.port=<port number>` to the command line, e.g.

```
java -Dserver.port=8081 -jar temperature-server-<version>.jar
```

## Usage

Browsing to `http://localhost:8080/` (or whichever port you chose) will open the Vaadin-based UI showing the current
temperature readings available.

To add a new measurement point:

`HTTP POST /temperature/<name>?value=<value>`

Where `<name>` is a string label such as "Kitchen" and `<value>` a floating-point temperature such as `23.456`. By
default the unit will be Celsius. To change it (in other words: if you're American) to Fahrenheit, do

`HTTP POST /temperature/<name>?value=<value>&unit=FAHRENHEIT`

To retrieve the the most recent temperature measurement for every `name`, simply use

`HTTP GET /temperature/current`

which will return a JSON like this:

```
[
  {"id":2,"timestamp":"2018-03-11T13:33:26.774Z","name":"Kitchen","value":20.319,"unit":"CELSIUS"},
  {"id":3,"timestamp":"2018-03-11T14:02:16.775Z","name":"Living room","value":20.319,"unit":"CELSIUS"}
]
```

You can view all stored measurements using `HTTP GET /temperature` which will return the same JSON structure.

## Data Storage Options

By default, the server will use an in-memory database provider (H2) that will run out-of-the-box. Of course that means
any saved data will not survive a server restart.

There are options, however. Please refer to the [Spring Boot documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config) for ways to change any configuration properties to suit your specific setup.

Note that all the relational storage options below use `Flyway` to set up the database tables on application startup.
Refer to the specific section for other requirements.

### Postgresql

If you have a Postgres DB server running, you can use that. Start the server application with the `spring.profiles.active=postgres`
option to do so.

The default configuration is:

```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/temperature
    username: postgres
    password: postgres
```

You will need to create the `temperature` database before running the server, since Postgres does not support automatic creation upon connection.

### MariaDB

An existing MariaDB 10 can be used as the data store with the `spring.profiles.active=mariadb` option. By default a
database named `temperature` is used, which are also the default user and password. 
