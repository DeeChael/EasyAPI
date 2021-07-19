package org.ezapi.storage;

import com.electronwill.nightconfig.core.file.FileConfig;
import org.ezapi.storage.sql.Closable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class TomlStorage extends FileStorage implements Storage, Closable {

    private final FileConfig fileConfig;

    private boolean closed = false;

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
    public StorageContext remove(String key) {
        StorageContext value = StorageContext.getByString(fileConfig.remove(key));
        fileConfig.save();
        return value;
    }

    @Override
    public void removeAll() {
        fileConfig.clear();
        fileConfig.save();
    }

    @Override
    public StorageContext get(String key) {
        return StorageContext.getByString(fileConfig.get(key));
    }

    @Override
    public StorageContext get(String key, StorageContext defaultValue) {
        StorageContext value = get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public void set(String key, StorageContext value) {
        fileConfig.set(key, value.toString());
        fileConfig.save();
    }

    @Override
    public List<String> keys() {
        return new ArrayList<>(fileConfig.valueMap().keySet());
    }

    @Override
    public List<StorageContext> values() {
        List<StorageContext> list = new ArrayList<>();
        keys().forEach(key -> list.add(get(key)));
        return list;
    }

    public void close() {
        if (!closed) {
            this.fileConfig.save();
            this.fileConfig.close();
            closed = true;
        }
    }

    @Override
    public boolean closed() {
        return closed;
    }

}
