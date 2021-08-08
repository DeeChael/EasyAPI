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
import java.util.List;
import java.util.Map;

public final class Language {

    private boolean firstOpenAndFileNotExist = false;
    private final File file;
    private JsonObject jsonObject = new JsonObject();

    private final String languageCode;

    private AutoReloadFile reloader;

    /**
     * Language API
     *
     * @param languageDefault defaults
     * @param languageCode language code
     */
    public Language(LanguageDefault languageDefault, String languageCode) {
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
            try {
                if (file.createNewFile()) {
                    //FileWriter fileWriter = new FileWriter(file);
                    PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"Unicode")));
                    writer.write("{}");
                    writer.close();
                }
            } catch (IOException ignored) {
            }
        }
        try {
            jsonObject = new JsonParser().parse(new BufferedReader(new InputStreamReader(new FileInputStream(file),"Unicode"))).getAsJsonObject();
        } catch (FileNotFoundException | UnsupportedEncodingException ignored) {
        }
        for (String key : languageDefault.keys()) {
            if (!has(key)) {
                this.jsonObject.addProperty(key, languageDefault.getDefault(key));
            }
        }
        reloader = new AutoReloadFile(EasyAPI.getInstance(), file);
        reloader.onModified(this::reload);
        reloader.onDeleted(() -> LanguageManager.INSTANCE.unregister(this));
        save();
    }

    /**
     * Reload texts from language file
     */
    public void reload() {
        try {
            jsonObject = new JsonParser().parse(new BufferedReader(new InputStreamReader(new FileInputStream(file),"Unicode"))).getAsJsonObject();
        } catch (FileNotFoundException | UnsupportedEncodingException ignored) {
        }
    }

    /**
     * Get language code</br>
     * Find language code in org.ezapi.configuration.LanguageCode or "https://minecraft.fandom.com/wiki/Language"
     *
     * @return Language Code
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * Get text from path, not exists return "Null"
     *
     * @param path text path
     * @return text
     */
    public String get(String path) {
        return jsonObject.has(path) && jsonObject.get(path).isJsonPrimitive() ? ColorUtils.translate(StringUtils.r_reset(jsonObject.get(path).getAsString())) : "Null";
    }

    /**
     * Set default value, if exists will be ignored
     *
     * @param path text path
     * @param value default value
     */
    public void setDefault(String path, String value) {
        if (!jsonObject.has(path)) {
            value = ColorUtils.transfer(StringUtils.r(value));
            jsonObject.addProperty(path, value);
        }
    }

    /**
     * Check if path has text
     *
     * @param path text path
     * @return exists
     */
    public boolean has(String path) {
        return jsonObject.has(path);
    }

    /**
     * Save language file
     */
    public void save() {
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"Unicode")));
            writer.write(new Gson().toJson(jsonObject));
            writer.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * Get all exists paths
     * @return paths
     */
    public List<String> keys() {
        List<String> keys = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    /**
     * If language file not exists before and it's first time to be opened
     * @return Is first time opened
     */
    public boolean isFirstOpenAndFileNotExist() {
        return firstOpenAndFileNotExist;
    }

}
