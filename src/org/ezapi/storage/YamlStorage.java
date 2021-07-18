package org.ezapi.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class YamlStorage extends FileStorage implements Storage {

    private FileConfiguration fileConfiguration;

    public YamlStorage(File file) {
        super(file);
        this.fileConfiguration = YamlConfiguration.loadConfiguration(getFile());
    }

    @Override
    public boolean has(String key) {
        return fileConfiguration.contains(key.replace(".", "_"));
    }

    @Override
    public String remove(String key) {
        if (fileConfiguration.contains(key.replace(".", "_"))) {
            String value = fileConfiguration.getString(key.replace(".", "_"));
            fileConfiguration.set(key.replace(".", "_"), null);
            try {
                fileConfiguration.save(getFile());
                return value;
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    @Override
    public void removeAll() {
        fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.save(getFile());
        } catch (IOException ignored) {
        }
    }

    @Override
    public String get(String key) {
        if (fileConfiguration.contains(key.replace(".", "_"))) {
            return fileConfiguration.getString(key.replace(".", "_"));
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
        this.fileConfiguration.set(key.replace(".", "_"), value);
        try {
            fileConfiguration.save(getFile());
        } catch (IOException ignored) {
        }
    }

    @Override
    public List<String> keys() {
        return new ArrayList<>(fileConfiguration.getKeys(false));
    }

    @Override
    public List<String> values() {
        List<String> values = new ArrayList<>();
        for (String key : this.keys()) {
            if (fileConfiguration.isString(key)) {
                values.add(fileConfiguration.getString(key));
            }
        }
        return values;
    }

}
