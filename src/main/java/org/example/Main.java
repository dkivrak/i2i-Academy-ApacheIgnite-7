package org.example;

import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.sql.ResultSet;
import org.apache.ignite.sql.SqlRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final Random random = new Random();

    public static void main(String[] args) {
        try (IgniteClient client = IgniteClient.builder()
                .addresses("127.0.0.1:10800")
                .build()) {

            System.out.println("Connected to Apache Ignite 3 successfully.");

            createTable(client);
            clearTable(client);
            insertDummySubscribers(client);
            simulateUsageUpdates(client);
            printFinalSubscribers(client);

            System.out.println("Program finished successfully.");
        }
    }

    private static void createTable(IgniteClient client) {
        client.sql().execute(null, """
                CREATE TABLE IF NOT EXISTS Subscriber (
                    customerId INT PRIMARY KEY,
                    dataUsage DOUBLE,
                    smsUsage INT,
                    callUsage INT
                )
                """);

        System.out.println("Subscriber table is ready.");
    }

    private static void clearTable(IgniteClient client) {
        client.sql().execute(null, "DELETE FROM Subscriber");
        System.out.println("Subscriber table cleared.");
    }

    private static void insertDummySubscribers(IgniteClient client) {
        for (int i = 1; i <= 5; i++) {
            client.sql().execute(null,
                    "INSERT INTO Subscriber (customerId, dataUsage, smsUsage, callUsage) VALUES (?, ?, ?, ?)",
                    i, 0.0, 0, 0);
        }

        System.out.println("5 dummy subscribers inserted.");
    }

    private static void simulateUsageUpdates(IgniteClient client) {
        List<Subscriber> subscribers = new ArrayList<>();

        try (ResultSet<SqlRow> rows = client.sql().execute(null,
                "SELECT customerId, dataUsage, smsUsage, callUsage FROM Subscriber")) {

            while (rows.hasNext()) {
                SqlRow row = rows.next();

                int customerId = row.intValue(0);
                double dataUsage = row.doubleValue(1);
                int smsUsage = row.intValue(2);
                int callUsage = row.intValue(3);

                subscribers.add(new Subscriber(customerId, dataUsage, smsUsage, callUsage));
            }
        }

        for (Subscriber subscriber : subscribers) {
            double addedDataUsage = 100 + random.nextInt(901);
            int addedSmsUsage = 10 + random.nextInt(91);
            int addedCallUsage = 20 + random.nextInt(181);

            double updatedDataUsage = subscriber.getDataUsage() + addedDataUsage;
            int updatedSmsUsage = subscriber.getSmsUsage() + addedSmsUsage;
            int updatedCallUsage = subscriber.getCallUsage() + addedCallUsage;

            client.sql().execute(null,
                    "UPDATE Subscriber SET dataUsage = ?, smsUsage = ?, callUsage = ? WHERE customerId = ?",
                    updatedDataUsage,
                    updatedSmsUsage,
                    updatedCallUsage,
                    subscriber.getCustomerId());
        }

        System.out.println("Usage simulation completed.");
    }

    private static void printFinalSubscribers(IgniteClient client) {
        System.out.println("\nFinal state of subscribers:");

        try (ResultSet<SqlRow> rows = client.sql().execute(null,
                "SELECT customerId, dataUsage, smsUsage, callUsage FROM Subscriber ORDER BY customerId")) {

            while (rows.hasNext()) {
                SqlRow row = rows.next();

                Subscriber subscriber = new Subscriber(
                        row.intValue(0),
                        row.doubleValue(1),
                        row.intValue(2),
                        row.intValue(3)
                );

                System.out.println(subscriber);
            }
        }
    }
}