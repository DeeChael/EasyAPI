package org.ezapi.storage;

import com.google.gson.*;
import org.ezapi.util.JsonUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class JsonStorage extends FileStorage implements Storage {

    private JsonObject jsonObject;

    public JsonStorage(File file) {
        super(file);
        if (isFirstTime()) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write("{}");
                fileWriter.close();
            } catch (IOException ignored) {
            }
        }
        try {
            FileReader fileReader = new FileReader(file);
            this.jsonObject = new JsonParser().parse(fileReader).getAsJsonObject();
            fileReader.close();
        } catch (IOException e) {
            this.jsonObject = new JsonObject();
        }
    }

    @Override
    public boolean has(String key) {
        return jsonObject.has(key);
    }

    @Override
    public StorageContext remove(String key) {
        if (jsonObject.has(key) && jsonObject.get(key).isJsonObject()) {
            StorageContext storageContext = StorageContext.getByString(new Gson().toJson(jsonObject.remove(key).getAsJsonObject()));
            try {
                FileWriter fileWriter = new FileWriter(getFile());
                fileWriter.write(new Gson().toJson(this.jsonObject));
                fileWriter.close();
                return storageContext;
            } catch (IOException ignored) {
                return storageContext;
            }
        }
        return null;
    }

    @Override
    public void removeAll() {
        this.jsonObject = new JsonObject();
        try {
            FileWriter fileWriter = new FileWriter(getFile());
            fileWriter.write(new Gson().toJson(this.jsonObject));
            fileWriter.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public StorageContext get(String key) {
        if (jsonObject.has(key) && jsonObject.get(key).isJsonObject()) {
            return StorageContext.getByString(new Gson().toJson(jsonObject.get(key).getAsJsonObject()));
        }
        return null;
    }

    @Override
    public StorageContext get(String key, StorageContext defaultValue) {
        StorageContext value = get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public void set(String key, StorageContext value) {
        this.jsonObject.add(key, JsonUtils.getJsonObject(value.toString()));
        try {
            FileWriter fileWriter = new FileWriter(getFile());
            fileWriter.write(new Gson().toJson(this.jsonObject));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    @Override
    public List<String> keys() {
        List<String> keys = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : this.jsonObject.entrySet()) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    @Override
    public List<StorageContext> values() {
        List<StorageContext> values = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : this.jsonObject.entrySet()) {
            StorageContext value = this.get(entry.getKey());
            if (value != null) {
                values.add(value);
            }
        }
        return values;
    }

    @Override
    public void regenerate() {
        super.regenerate();
        try {
            FileWriter fileWriter = new FileWriter(this.file);
            fileWriter.write("{}");
            fileWriter.close();
        } catch (IOException ignored) {
        }
    }

}