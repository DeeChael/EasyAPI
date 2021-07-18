package org.ezapi.storage;

import com.electronwill.nightconfig.core.file.FileConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TomlStorage extends FileStorage implements Storage {

    private final FileConfig fileConfig;

    public TomlStorage(File file) {
        super(file);
        this.fileConfig = FileConfig.of(file);
        fileConfig.load();
    }

    @Override
    public boolean has(String key) {
        return fileConfig.contains(key);
    }

    @Override
    public String remove(String key) {
        String value = fileConfig.remove(key);
        fileConfig.save();
        return value;
    }

    @Override
    public void removeAll() {
        fileConfig.clear();
        fileConfig.save();
    }

    @Override
    public String get(String key) {
        return fileConfig.get(key);
    }

    @Override
    public String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public void set(String key, String value) {
        fileConfig.set(key, value);
        fileConfig.save();
    }

    @Override
    public List<String> keys() {
        return new ArrayList<>(fileConfig.valueMap().keySet());
    }

    @Override
    public List<String> values() {
        List<String> list = new ArrayList<>();
        keys().forEach(key -> list.add(get(key)));
        return list;
    }

    public void close() {
        this.fileConfig.save();
        this.fileConfig.close();
    }

}
