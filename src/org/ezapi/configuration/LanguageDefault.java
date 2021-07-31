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

    /**
     * Language defaults
     * @param plugin your plugin instance
     */
    public LanguageDefault(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Add default
     * @param path path
     * @param value value
     */
    public void addDefault(String path, String value) {
        if (!jsonObject.has(path)) {
            value = ColorUtils.transfer(value.replace("\n", "\\n"));
            jsonObject.addProperty(path, value);
        }
    }

    /**
     * Set default to a language
     * @param language
     */
    public void setDefault(Language language) {
        for (Entry<String,JsonElement> entry : this.jsonObject.entrySet()) {
            language.setDefault(entry.getKey(), entry.getValue().getAsString());
        }
    }

    /**
     * Get if contains a default path
     * @param path path
     * @return contains default
     */
    public boolean hasDefault(String path) {
        return this.jsonObject.has(path);
    }

    /**
     * Get default value
     * @param path path
     * @return default value
     */
    public String getDefault(String path) {
        return this.jsonObject.get(path).getAsString();
    }

    /**
     * Remove default value
     * @param path path
     * @return default value
     */
    public String removeDefault(String path) {
        return this.jsonObject.remove(path).getAsString();
    }

    /**
     * Clear all default
     */
    public void clear() {
        for (Entry<String,JsonElement> entry : this.jsonObject.entrySet()) {
            this.jsonObject.remove(entry.getKey());
        }
    }

    /**
     * Get plugin instance
     * @return plugin instance
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Get registry name
     * @return plugin name
     */
    public String getRegistryName() {
        return plugin.getName();
    }

    /**
     * Get all exists defaults paths
     * @return all paths
     */
    public List<String> keys() {
        List<String> list = new ArrayList<>();
        for (Entry<String,JsonElement> entry : jsonObject.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

}
