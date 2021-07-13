package org.ezapi.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.ezapi.EasyAPI;
import org.ezapi.utils.ColorUtils;
import org.ezapi.utils.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public final class Language {

    private boolean firstOpenAndFileNotExist = false;
    private final File file;
    private JsonObject jsonObject = new JsonObject();

    public Language(LanguageDefault languageDefault, String languageCode) throws IOException {
        if (EasyAPI.PLUGIN_LANGUAGES.containsKey(languageDefault.getPlugin())) {
            EasyAPI.PLUGIN_LANGUAGES.get(languageDefault.getPlugin()).put(languageCode.toLowerCase(), this);
        } else {
            Map<String, Language> map = new HashMap<>();
            map.put(languageCode.toLowerCase(), this);
            EasyAPI.PLUGIN_LANGUAGES.put(languageDefault.getPlugin(), map);
        }
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

    public boolean isFirstOpenAndFileNotExist() {
        return firstOpenAndFileNotExist;
    }

}
