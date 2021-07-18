package org.ezapi.storage;

import org.ezapi.util.FileUtils;
import org.ezapi.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class EzPropertiesStorage extends FileStorage implements Storage {

    private final List<EzProperty> context = new ArrayList<>();

    public EzPropertiesStorage(File file) {
        super(file);
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

    @Override
    public boolean has(String key) {
        return keys().contains(key);
    }

    @Override
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

    @Override
    public void removeAll() {
        context.clear();
        regenerate();
    }

    @Override
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

    @Override
    public String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    @Override
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

    public void addAnnotation(String annotation) {
        context.add(new PropertyAnnotation(annotation));
    }

    @Override
    public List<String> keys() {
        List<String> keys = new ArrayList<>();
        for (EzProperty ezProperty : context) {
            if (ezProperty instanceof PropertyObject) {
                keys.add(((PropertyObject) ezProperty).getKey());
            }
        }
        return keys;
    }

    @Override
    public List<String> values() {
        List<String> values = new ArrayList<>();
        for (EzProperty ezProperty : context) {
            if (ezProperty instanceof PropertyObject) {
                values.add(((PropertyObject) ezProperty).getValue());
            }
        }
        return values;
    }

    private void save() {
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
