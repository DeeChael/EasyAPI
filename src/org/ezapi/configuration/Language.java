package org.ezapi.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.ezapi.EasyAPI;
import org.ezapi.util.ColorUtils;
import org.ezapi.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Language {

    private boolean firstOpenAndFileNotExist = false;
    private final File file;
    private JsonObject jsonObject = new JsonObject();

    private final String languageCode;

    public Language(LanguageDefault languageDefault, String languageCode) throws IOException {
        this.languageCode = languageCode;
        File folder = new File("language/" + languageDefault.getRegistryName());
        if (!folder.exists()) {
            folder.mkdirs();
        } else {
            if (!folder.isDirectory()) {
                folder.mkdirs();
            }
        }
        file = new File("language/" + languageDefault.getRegistryName() + "/" + languageCode.replace("/", "").replace("\\", "") + ".json");
        if (!file.exists()) {
            firstOpenAndFileNotExist = true;
            if (file.createNewFile()) {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write("{}");
                fileWriter.close();
            }
        }
        try {
            jsonObject = new JsonParser().parse(new FileReader(file)).getAsJsonObject();
        } catch (FileNotFoundException e) {
        }
        for (String key : languageDefault.keys()) {
            if (!has(key)) {
                this.jsonObject.addProperty(key, languageDefault.getDefault(key));
            }
        }
        save();
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String get(String path) {
        return jsonObject.has(path) && jsonObject.get(path).isJsonPrimitive() ? ColorUtils.translate(StringUtils.r_reset(jsonObject.get(path).getAsString())) : "Null";
    }

    public void setDefault(String path, String value) {
        if (!jsonObject.has(path)) {
            value = ColorUtils.transfer(StringUtils.r(value));
            jsonObject.addProperty(path, value);
        }
    }

    public boolean has(String path) {
        return jsonObject.has(path);
    }

    public void save() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(new Gson().toJson(jsonObject));
            fileWriter.close();
        } catch (IOException ignored) {
        }
    }

    public List<String> keys() {
        List<String> keys = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    public boolean isFirstOpenAndFileNotExist() {
        return firstOpenAndFileNotExist;
    }

}
