package org.ezapi.storage;

import java.io.File;
import java.io.IOException;

public abstract class FileStorage {

    protected final File file;

    private boolean isFirstTime = false;

    public FileStorage(File file) {
        this.file = file;
        if (!(file.exists() || file.isFile())) {
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            try {
                file.createNewFile();
                this.isFirstTime = true;
            } catch (IOException ignored) {
            }
        }
    }

    public File getFile() {
        return this.file;
    }

    public void regenerate() {
        if (this.file.exists()) {
            if (this.file.delete()) {
                try {
                    file.createNewFile();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public boolean isFirstTime() {
        return isFirstTime;
    }

}
