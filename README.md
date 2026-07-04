# i2i Academy - Apache Ignite

This project is a Java-based Apache Ignite 3 implementation developed for the i2i Academy Apache Ignite assignment.

The application connects to a local Apache Ignite 3 node using the modern Thin Client API, creates a `Subscriber` table, inserts dummy subscriber records, simulates random usage updates, and prints the final state of the subscribers to the console.

## Technologies Used

- Java 17
- Maven
- Apache Ignite 3
- Apache Ignite Thin Client
- Docker
- Docker Compose

## Project Structure

```text
.
├── docker-compose.yml
├── pom.xml
├── README.md
└── src
    └── main
        └── java
            └── org
                └── example
                    ├── Main.java
                    └── Subscriber.java
```

## Subscriber Data Model

The project uses a Subscriber model with the following fields:

|Field     | Type |Description          |
|----------|------|---------------------|
|customerId|int   |Primary key          |
|dataUsage |double|Data usage amount    |
|smsUsage  |int   |SMS usage count      |
|callUsage |int   |Call usage in minutes|

## Running Apache Ignite 3

Start the Apache Ignite 3 container:

```bash
docker compose up -d
```


Check whether the container is running:

```bash
docker compose ps
```

The Ignite node exposes the following ports:

|Port |	Purpose               |
|-----|-----------------------|
|10300|	REST API              |
|10800|	Thin Client connection|


## Initializing the Ignite Cluster

Open the Apache Ignite CLI:

```bash
docker run --rm -it \
  --platform linux/amd64 \
  --network ignite3_default \
  -e LANG=C.UTF-8 \
  -e LC_ALL=C.UTF-8 \
  apacheignite/ignite:3.1.0 cli
```

Inside the CLI, run:

```bash
connect http://node1:10300
cluster init --name=ignite3
cluster status
exit
```


Expected cluster status:

```
[name: ignite3, nodes: 1, status: active]
```

## Running the Java Application

Build the project:

```bash
mvn clean package
```

Run the application:

```bash
mvn exec:java
```


## Application Flow

The Java application performs the following steps:


1.  Connects to the local Apache Ignite 3 node through 127.0.0.1:10800.
2.  Creates the Subscriber table if it does not already exist.
3.  Clears the table at the beginning of each execution.
4.  Inserts 5 dummy subscriber records.
5.  Simulates random usage updates for data, SMS, and call usage.
6.  Updates the records in Apache Ignite.
7.  Prints the final state of all subscribers to the console.

Example Output

```text
Connected to Apache Ignite 3 successfully.
Subscriber table is ready.
Subscriber table cleared.
5 dummy subscribers inserted.
Usage simulation completed.

Final state of subscribers:
Subscriber{customerId=1, dataUsage=450.0, smsUsage=68, callUsage=58}
Subscriber{customerId=2, dataUsage=500.0, smsUsage=21, callUsage=187}
Subscriber{customerId=3, dataUsage=870.0, smsUsage=15, callUsage=163}
Subscriber{customerId=4, dataUsage=788.0, smsUsage=49, callUsage=60}
Subscriber{customerId=5, dataUsage=154.0, smsUsage=82, callUsage=34}
Program finished successfully.
Notes
The project uses Apache Ignite 3 Thin Client API.
The application starts with a clean table on every execution.
Random values are generated for usage simulation, so the final output may be different on each run.
Only source code and configuration files are included in this repository.
```

## Notes
-  The project uses Apache Ignite 3 Thin Client API.
-  The application starts with a clean table on every execution.
-  Random values are generated for usage simulation, so the final output may be different on each run.
-   Only source code and configuration files are included in this repository.
