package org.ezapi.configuration;

import org.ezapi.util.FileUtils;
import org.ezapi.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class EzProperties {

    private final File file;

    private final List<EzProperty> context = new ArrayList<>();

    /**
     * EzProperties, has able to add commit
     * @param file properties file
     */
    public EzProperties(File file) {
        this.file = file;
        if (!(file.exists() || file.isFile())) {
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
        String text = FileUtils.readText(file);
        if (text.contains("\n")) {
            for (String string : text.split("\\\\n")) {
                if (string.startsWith("#")) {
                    context.add(new PropertyAnnotation(string.substring(1)));
                } else {
                    if (string.contains("=")) {
                        String[] keyAndValue = StringUtils.divide(string, '=');
                        context.add(new PropertyObject(keyAndValue[0], keyAndValue[1]));
                    }
                }
            }
        } else {
            if (text.startsWith("#")) {
                context.add(new PropertyAnnotation(text.substring(1)));
            } else {
                if (text.contains("=")) {
                    String[] keyAndValue = StringUtils.divide(text, '=');
                    context.add(new PropertyObject(keyAndValue[0], keyAndValue[1]));
                }
            }
        }
    }

    /**
     * Get if properties contains key
     * @param key key
     * @return contains key
     */
    public boolean has(String key) {
        return keys().contains(key);
    }

    /**
     * Remove a key
     * @param key key
     * @return if not exists returns null or else value
     */
    public String remove(String key) {
        if (has(key)) {
            for (EzProperty ezProperty : context) {
                if (ezProperty instanceof PropertyObject) {
                    if (((PropertyObject) ezProperty).getKey().equals(key)) {
                        String value = ((PropertyObject) ezProperty).getValue();
                        context.remove(ezProperty);
                        save();
                        return value;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Remove all recorded data
     */
    public void removeAll() {
        context.clear();
        regenerate();
    }

    /**
     * Get properties file
     * @return properties file
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Regenerate file, won't remove all
     */
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

    /**
     * Get value by key
     * @param key key
     * @return if not exists returns null or else value
     */
    public String get(String key) {
        if (has(key)) {
            for (EzProperty ezProperty : context) {
                if (ezProperty instanceof PropertyObject) {
                    if (((PropertyObject) ezProperty).getKey().equals(key)) {
                        return ((PropertyObject) ezProperty).getValue();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Get value by key, if not exists returns default value
     * @param key key
     * @param defaultValue default value
     * @return if not exists returns default value or else value
     */
    public String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Set the value of the key
     * @param key key
     * @param value value
     */
    public void set(String key, String value) {
        if (!has(key)) {
            context.add(new PropertyObject(key, value));
        } else {
            for (EzProperty ezProperty : context) {
                if (ezProperty instanceof PropertyObject) {
                    if (((PropertyObject) ezProperty).getKey().equals(key)) {
                        ((PropertyObject) ezProperty).setValue(value);
                    }
                }
            }
        }
        save();
    }

    /**
     * Add commit
     * @param annotation commit
     */
    public void addAnnotation(String annotation) {
        context.add(new PropertyAnnotation(annotation));
    }

    /**
     * Get all keys
     * @return keys
     */
    public List<String> keys() {
        List<String> keys = new ArrayList<>();
        for (EzProperty ezProperty : context) {
            if (ezProperty instanceof PropertyObject) {
                keys.add(((PropertyObject) ezProperty).getKey());
            }
        }
        return keys;
    }

    /**
     * Get all values
     * @return values
     */
    public List<String> values() {
        List<String> values = new ArrayList<>();
        for (EzProperty ezProperty : context) {
            if (ezProperty instanceof PropertyObject) {
                values.add(((PropertyObject) ezProperty).getValue());
            }
        }
        return values;
    }

    /**
     * Write the properties content to the properties file
     */
    public void save() {
        StringBuilder text = new StringBuilder();
        for (EzProperty ezProperty : context) {
            text.append(ezProperty).append("\n");
        }
        FileUtils.writeText(file, text.toString());
    }

    private interface EzProperty {}

    private static final class PropertyAnnotation implements EzProperty {

        private final String annotation;

        private PropertyAnnotation(String annotation) {
            this.annotation = annotation;
        }

        public String getAnnotation() {
            return annotation;
        }

        @Override
        public String toString() {
            return "#" + annotation;
        }

    }

    private static final class PropertyObject implements EzProperty {

        private final String key;

        private String value;

        private PropertyObject(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

    }

}
