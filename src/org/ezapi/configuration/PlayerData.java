package org.ezapi.configuration;

public interface PlayerData {

    String getString(String path);

    default String getString(String path, String defaultValue) {
        Object value = get(path);
        return value != null ? (String) value : defaultValue;
    }

    int getInt(String path);

    default int getInt(String path, int defaultValue) {
        Object value = get(path);
        return value instanceof Number ? ((Number) value).intValue() : defaultValue;
    }

    boolean getBoolean(String path);

    default boolean getBoolean(String path, boolean defaultValue) {
        Object value = get(path);
        return value instanceof Boolean ? (Boolean) value : defaultValue;
    }

    double getDouble(String path);

    default double getDouble(String path, double defaultValue) {
        Object value = get(path);
        return value instanceof Number ? ((Number) value).doubleValue() : defaultValue;
    }

    long getLong(String path);

    default long getLong(String path, long defaultValue) {
        Object value = get(path);
        return value instanceof Number ? ((Number) value).longValue() : defaultValue;
    }

    Object get(String path);

    default Object get(String path, Object defaultValue) {
        Object value = get(path);
        return value != null ? value : defaultValue;
    }

    void set(String path, Object value);

    void addDefault(String path, Object defaultValue);

    boolean contains(String path);

    Object remove(String path);

    void reload();

}
