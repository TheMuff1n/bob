package de.marvinstier.bob.storage;

import java.io.IOException;
import java.io.InputStream;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoManager {
    private static MongoDatabase database;

    private static String getConnectionString() throws IOException {
        byte[] data;
        try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("mongo_connection.txt")) {
            data = in.readAllBytes();
        }
        return new String(data);
    }

    public static MongoDatabase getDatabase() throws IOException {
        if (database == null)
            database = MongoClients.create(getConnectionString()).getDatabase("hausmeister_bob");

        return database;
    }
}
