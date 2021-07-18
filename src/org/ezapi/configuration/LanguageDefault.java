package org.ezapi.configuration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.plugin.Plugin;
import org.ezapi.util.ColorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public final class LanguageDefault {

    private final JsonObject jsonObject = new JsonObject();

    private final Plugin plugin;

    public LanguageDefault(Plugin plugin) {
        this.plugin = plugin;
    }

    public void addDefault(String path, String value) {
        if (!jsonObject.has(path)) {
            value = ColorUtils.transfer(value.replace("\n", "\\n"));
            jsonObject.addProperty(path, value);
        }
    }

    public void setDefault(Language language) {
        for (Entry<String,JsonElement> entry : this.jsonObject.entrySet()) {
            language.setDefault(entry.getKey(), entry.getValue().getAsString());
        }
    }

    public boolean hasDefault(String path) {
        return this.jsonObject.has(path);
    }

    public String getDefault(String path) {
        return this.jsonObject.get(path).getAsString();
    }

    public String removeDefault(String path) {
        return this.jsonObject.remove(path).getAsString();
    }

    public void clear() {
        for (Entry<String,JsonElement> entry : this.jsonObject.entrySet()) {
            this.jsonObject.remove(entry.getKey());
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }


    public String getRegistryName() {
        return plugin.getName();
    }

    public List<String> keys() {
        List<String> list = new ArrayList<>();
        for (Entry<String,JsonElement> entry : jsonObject.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

}
