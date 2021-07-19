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
    public StorageContext remove(String key) {
        if (fileConfiguration.contains(key.replace(".", "_"))) {
            StorageContext value = StorageContext.getByString(fileConfiguration.getString(key.replace(".", "_")));
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
    public StorageContext get(String key) {
        if (fileConfiguration.contains(key.replace(".", "_"))) {
            return StorageContext.getByString(fileConfiguration.getString(key.replace(".", "_")));
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
        this.fileConfiguration.set(key.replace(".", "_"), value.toString());
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
    public List<StorageContext> values() {
        List<StorageContext> values = new ArrayList<>();
        for (String key : this.keys()) {
            if (fileConfiguration.isString(key)) {
                values.add(StorageContext.getByString(fileConfiguration.getString(key)));
            }
        }
        return values;
    }

}
