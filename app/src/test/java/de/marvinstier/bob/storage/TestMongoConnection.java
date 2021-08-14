package de.marvinstier.bob.storage;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

public class TestMongoConnection {
    @Before
    public void cleanTestCollection() throws IOException {
        MongoManager.getDatabase().getCollection("tests").drop();
        MongoManager.getDatabase().createCollection("tests");
    }

    @Test
    public void testPutGetString() throws IOException {
        Document docPut = new Document("name", "yoyo").append("ages", new Document("min", 5));
        ObjectId id = MongoManager.getDatabase().getCollection("tests").insertOne(docPut).getInsertedId().asObjectId()
                .getValue();
        Document docGet = MongoManager.getDatabase().getCollection("tests").find(new Document("_id", id)).first();
        assertEquals(docPut, docGet);
    }
}
