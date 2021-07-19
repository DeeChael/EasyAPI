package org.ezapi.storage;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.ezapi.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class StorageContext {

    private final JsonObject jsonObject;

    public StorageContext() {
        this.jsonObject = new JsonObject();
    }

    private StorageContext(JsonObject jsonObject) {
        if (jsonObject != null) {
            this.jsonObject = jsonObject;
        } else {
            this.jsonObject = new JsonObject();
        }
    }

    public void setContext(String key, StorageContext storageContext) {
        this.jsonObject.add(key, storageContext.jsonObject);
    }

    public void setString(String key, String value) {
        this.jsonObject.addProperty(key, value);
    }

    public void setInt(String key, int value) {
        this.jsonObject.addProperty(key, "int$" + value);
    }

    public void setBoolean(String key, boolean value) {
        this.jsonObject.addProperty(key, "boolean$" + value);
    }

    public void setDouble(String key, double value) {
        this.jsonObject.addProperty(key, "double$" + value);
    }

    public void setFloat(String key, float value) {
        this.jsonObject.addProperty(key, "float$" + value);
    }

    public void setLong(String key, long value) {
        this.jsonObject.addProperty(key, "long$" + value);
    }

    public void setShort(String key, short value) {
        this.jsonObject.addProperty(key, "short$" + value);
    }

    public void setByte(String key, byte value) {
        this.jsonObject.addProperty(key, "byte$" + value);
    }

    public void setCharacter(String key, char value) {
        this.jsonObject.addProperty(key, "char$" + value);
    }

    public void setStringList(String key, List<String> list) {
        JsonArray jsonArray = new JsonArray();
        for (String str : list) {
            jsonArray.add(str);
        }
        this.jsonObject.add(key, jsonArray);
    }

    public void setContextList(String key, List<StorageContext> list) {
        JsonArray jsonArray = new JsonArray();
        for (StorageContext sqlContext : list) {
            jsonArray.add(sqlContext.jsonObject);
        }
        this.jsonObject.add(key, jsonArray);
    }

    public boolean has(String key) {
        return jsonObject.has(key);
    }

    public String getString(String key) {
        if (this.has(key) && this.jsonObject.get(key).isJsonPrimitive()) {
            return this.jsonObject.get(key).getAsString();
        }
        return null;
    }

    public int getInt(String key) {
        String stringValue = getString(key);
        if (stringValue != null) {
            if (stringValue.startsWith("int$")) {
                String string = stringValue.replace("int$", "");
                if (!string.isEmpty()) {
                    return Integer.parseInt(string);
                }
            }
        }
        return 0;
    }

    public boolean getBoolean(String key) {
        String stringValue = getString(key);
        if (stringValue != null) {
            if (stringValue.startsWith("boolean$")) {
                String string = stringValue.replace("boolean$", "");
                if ((string.length() == 4 || string.length() == 5) && (string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false"))) {
                    return Boolean.parseBoolean(string);
                }
            }
        }
        return false;
    }

    public double getDouble(String key) {
        String stringValue = getString(key);
        if (stringValue != null) {
            if (stringValue.startsWith("double$")) {
                String string = stringValue.replace("double$", "");
                if (!string.isEmpty()) {
                    return Double.parseDouble(string);
                }
            }
        }
        return 0.0d;
    }

    public float getFloat(String key) {
        String stringValue = getString(key);
        if (stringValue != null) {
            if (stringValue.startsWith("float$")) {
                String string = stringValue.replace("float$", "");
                if (!string.isEmpty()) {
                    return Float.parseFloat(string);
                }
            }
        }
        return 0f;
    }

    public long getLong(String key) {
        String stringValue = getString(key);
        if (stringValue != null) {
            if (stringValue.startsWith("long$")) {
                String string = stringValue.replace("long$", "");
                if (!string.isEmpty()) {
                    return Long.parseLong(string);
                }
            }
        }
        return 0l;
    }

    public short getShort(String key) {
        String stringValue = getString(key);
        if (stringValue != null) {
            if (stringValue.startsWith("short$")) {
                String string = stringValue.replace("short$", "");
                if (!string.isEmpty()) {
                    return Short.parseShort(string);
                }
            }
        }
        return 0;
    }

    public byte getByte(String key) {
        String stringValue = getString(key);
        if (stringValue != null) {
            if (stringValue.startsWith("byte$")) {
                String string = stringValue.replace("byte$", "");
                if (!string.isEmpty()) {
                    return Byte.parseByte(string);
                }
            }
        }
        return 0;
    }

    public char getCharacter(String key) {
        String stringValue = getString(key);
        if (stringValue != null) {
            if (stringValue.startsWith("char$")) {
                String string = stringValue.replace("char$", "");
                if (string.length() == 1) {
                    return string.toCharArray()[0];
                }
            }
        }
        return ' ';
    }

    public StorageContext getContext(String key) {
        if (this.has(key) && this.jsonObject.get(key).isJsonObject()) {
            return new StorageContext(this.jsonObject.getAsJsonObject(key));
        }
        return new StorageContext();
    }

    public List<String> getStringList(String key) {
        List<String> list = new ArrayList<>();
        if (this.has(key) && this.jsonObject.get(key).isJsonArray()) {
            for (JsonElement jsonElement : this.jsonObject.getAsJsonArray(key)) {
                if (jsonElement.isJsonPrimitive()) {
                    list.add(jsonElement.getAsString());
                }
            }
        }
        return list;
    }

    public List<StorageContext> getContextList(String key) {
        List<StorageContext> list = new ArrayList<>();
        if (this.has(key) && this.jsonObject.get(key).isJsonArray()) {
            for (JsonElement jsonElement : this.jsonObject.getAsJsonArray(key)) {
                if (jsonElement.isJsonObject()) {
                    list.add(new StorageContext(jsonElement.getAsJsonObject()));
                }
            }
        }
        return list;
    }

    public boolean isString(String key) {
        return this.has(key) && this.jsonObject.get(key).isJsonPrimitive();
    }

    public boolean isInt(String key) {
        try {
            Integer.parseInt(getString(key).replace("int$", ""));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isBoolean(String key) {
        try {
            Boolean.parseBoolean(getString(key).replace("boolean$", ""));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDouble(String key) {
        try {
            Double.parseDouble(getString(key).replace("double$", ""));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isFloat(String key) {
        try {
            Float.parseFloat(getString(key).replace("float$", ""));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isLong(String key) {
        try {
            Long.parseLong(getString(key).replace("long$", ""));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isShort(String key) {
        try {
            Short.parseShort(getString(key).replace("short$", ""));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isByte(String key) {
        try {
            Byte.parseByte(getString(key).replace("byte$", ""));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isCharacter(String key) {
        return (getString(key).replace("byte$", "")).length() == 1;
    }

    public boolean isContext(String key) {
        return this.has(key) && this.jsonObject.get(key).isJsonObject();
    }

    public boolean isList(String key) {
        return this.has(key) && this.jsonObject.get(key).isJsonArray();
    }

    public boolean isStringList(String key) {
        return isList(key) && !this.getStringList(key).isEmpty();
    }

    public boolean isContextList(String key) {
        return isList(key) && !this.getContextList(key).isEmpty();
    }

    public void remove(String key) {
        jsonObject.remove(key);
    }

    public List<String> keys() {
        List<String> keys = new ArrayList<>();
        for (Map.Entry<String,JsonElement> entry : jsonObject.entrySet()) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    @Override
    public String toString() {
        return new Gson().toJson(jsonObject);
    }

    public static StorageContext getByString(String sqlContextString) {
        return new StorageContext(JsonUtils.getJsonObject(sqlContextString));
    }

}
