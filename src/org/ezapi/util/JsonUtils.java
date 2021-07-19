package org.ezapi.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.Reader;

public final class JsonUtils {

    public static boolean isJsonObject(String string) {
        try {
            JsonObject jsonObject = new JsonParser().parse(string).getAsJsonObject();
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public static boolean isJsonObject(Reader reader) {
        try {
            JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public static boolean isJsonObject(JsonReader jsonReader) {
        try {
            JsonObject jsonObject = new JsonParser().parse(jsonReader).getAsJsonObject();
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public static boolean isJsonArray(String string) {
        try {
            JsonArray jsonArray = new JsonParser().parse(string).getAsJsonArray();
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public static boolean isJsonArray(Reader reader) {
        try {
            JsonArray jsonArray = new JsonParser().parse(reader).getAsJsonArray();
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public static boolean isJsonArray(JsonReader jsonReader) {
        try {
            JsonArray jsonArray = new JsonParser().parse(jsonReader).getAsJsonArray();
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public static JsonObject getJsonObject(String string) {
        if (isJsonObject(string)) return new JsonParser().parse(string).getAsJsonObject();
        return new JsonObject();
    }

    public static JsonArray getJsonArray(String string) {
        if (isJsonArray(string)) return new JsonParser().parse(string).getAsJsonArray();
        return new JsonArray();
    }

}
