package org.ezapi.storage.sql;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.util.Collections;

/**
 * Haven't finished
 */
public class MongoDB {

    private final MongoClient mongoClient;

    private final MongoDatabase mongoDatabase;

    private MongoDB(String host, int port, String username, String password, String authDatabase, String connectDatabase) {
        MongoCredential mongoCredential = MongoCredential.createCredential(username, authDatabase, password.toCharArray());
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .credential(mongoCredential)
                .applyToSslSettings(builder -> builder.enabled(false))
                .applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(new ServerAddress(host, port))))
                .build();
        mongoClient = MongoClients.create(mongoClientSettings);
        this.mongoDatabase = mongoClient.getDatabase(connectDatabase);
    }

    public void setString(String collection, String key, String value) {
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
        Document document = new Document();
        document.append(key, value);
        mongoCollection.insertMany(Collections.singletonList(document));
    }

    public String getString(String collection, String key) {
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
        for (Document document : mongoCollection.find()) {
            if (document.containsKey(key)) {
                return document.getString(key);
            }
        }
        return null;
    }

}
