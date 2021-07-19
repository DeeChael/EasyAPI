package org.ezapi.storage.sql;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.ezapi.storage.StorageContext;

import java.sql.SQLException;
import java.util.*;

public final class MongoDB implements NoSql {

    private final MongoClient mongoClient;

    private MongoDatabase mongoDatabase;

    private final Map<String, List<StorageContext>> cache = new HashMap<>();

    private boolean closed;

    public MongoDB(String host, int port, String username, String password, String authDatabase, String connectDatabase) {
        MongoCredential mongoCredential = MongoCredential.createCredential(username, authDatabase, password.toCharArray());
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .credential(mongoCredential)
                .applyToSslSettings(builder -> builder.enabled(false))
                .applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(new ServerAddress(host, port))))
                .build();
        mongoClient = MongoClients.create(mongoClientSettings);
        this.mongoDatabase = mongoClient.getDatabase(connectDatabase);
        for (String key : mongoDatabase.listCollectionNames()) {
            List<StorageContext> list = new ArrayList<>();
            for (Document document : mongoDatabase.getCollection(key).find()) {
                StorageContext storageContext = StorageContext.getByString(document.toJson());
                storageContext.remove("_id");
                list.add(storageContext);
            }
            cache.put(key, list);
        }
    }

    @Override
    public void reload() {
        cache.clear();
        for (String key : mongoDatabase.listCollectionNames()) {
            List<StorageContext> list = new ArrayList<>();
            for (Document document : mongoDatabase.getCollection(key).find()) {
                StorageContext storageContext = StorageContext.getByString(document.toJson());
                storageContext.remove("_id");
                list.add(storageContext);
            }
            cache.put(key, list);
        }
    }

    @Override
    public boolean has(String key) {
        return cache.containsKey(key);
    }

    @Override
    public List<StorageContext> remove(String key) {
        if (has(key)) {
            List<StorageContext> list = get(key);
            mongoDatabase.getCollection(key).drop();
            cache.remove(key);
            return list;
        }
        return new ArrayList<>();
    }

    @Override
    public void removeAll() {
        String name = mongoDatabase.getName();
        mongoDatabase.drop();
        mongoDatabase = mongoClient.getDatabase(name);
        cache.clear();
    }

    @Override
    public List<StorageContext> get(String key) {
        if (has(key)) {
            return cache.get(key);
        }
        return new ArrayList<>();
    }

    @Override
    public void set(String key, List<StorageContext> value) {
        List<Document> list = new ArrayList<>();
        for (StorageContext storageContext : value) {
            list.add(Document.parse(storageContext.toString()));
        }
        mongoDatabase.getCollection(key).drop();
        mongoDatabase.getCollection(key).insertMany(list);
        cache.put(key, value);
    }

    @Override
    public void add(String key, StorageContext value) {
        add(key, Collections.singletonList(value));
    }

    @Override
    public void add(String key, StorageContext... value) {
        add(key, Arrays.asList(value));
    }

    @Override
    public void add(String key, List<StorageContext> value) {
        if (has(key)) {
            List<StorageContext> first = value;
            value = get(key);
            value.addAll(first);
        }
        set(key, value);
    }

    @Override
    public List<String> keys() {
        return new ArrayList<>(cache.keySet());
    }

    @Override
    public void close() {
        if (!closed) {
            mongoClient.close();
            closed = true;
        }
    }

    @Override
    public boolean closed() {
        return closed;
    }

}
