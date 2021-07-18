package org.ezapi.storage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public final class PropertiesStorage extends FileStorage implements Storage {

    private Properties properties;

    public PropertiesStorage(File file) {
        super(file);
        properties = new Properties();
        try {
            properties.load(new FileReader(file));
        } catch (IOException ignored) {
        }
    }

    @Override
    public boolean has(String key) {
        return properties.containsKey(key);
    }

    @Override
    public String remove(String key) {
        return (String) properties.remove(key);
    }

    @Override
    public void removeAll() {
        properties = new Properties();
        try {
            properties.store(new FileWriter(this.file), "EasyAPI - removeAll");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String get(String key) {
        if (has(key)) {
            return properties.getProperty(key);
        }
        return null;
    }

    @Override
    public String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public void set(String key, String value) {
        properties.setProperty(key, value);
        try {
            properties.store(new FileWriter(this.file), "EasyAPI - set");
        } catch (IOException ignored) {
        }
    }

    @Override
    public List<String> keys() {
        List<String> keys = new ArrayList<>();
        for (Object object : properties.keySet()) {
            keys.add((String) object);
        }
        return keys;
    }

    @Override
    public List<String> values() {
        List<String> values = new ArrayList<>();
        for (String key : keys()) {
            values.add(get(key));
        }
        return values;
    }

}
