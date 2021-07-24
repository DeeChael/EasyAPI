package org.ezapi.configuration;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class PlayerDataManager {

    private PlayerDataManager() {}

    private final static Map<String,PlayerData> cache = new HashMap<>();

    public static PlayerData getPlayerData(Player player) {
        if (cache.containsKey(player.getUniqueId().toString())) return cache.get(player.getUniqueId().toString());
        EzPlayerData playerData = new EzPlayerData(player);
        cache.put(player.getUniqueId().toString(), playerData);
        return playerData;
    }

    private static class EzPlayerData implements PlayerData {

        private final File file;

        private final FileConfiguration configuration;

        private EzPlayerData(Player player) {
            File base = new File("playerdatas/" + player.getUniqueId());
            this.file = new File(base, "data.yml");
            if (!base.exists() || !base.isDirectory()) {
                if (base.mkdirs()) {
                    if (!file.exists() || !file.isFile()) {
                        try {
                            if (!file.createNewFile()) {
                                throw new IllegalStateException("Cannot create player data file");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            configuration = YamlConfiguration.loadConfiguration(file);
            configuration.set("name", player.getName());
            try {
                configuration.save(this.file);
            } catch (IOException ignored) {
            }
        }

        @Override
        public String getString(String path) {
            return configuration.getString(path);
        }

        @Override
        public int getInt(String path) {
            return configuration.getInt(path);
        }

        @Override
        public boolean getBoolean(String path) {
            return configuration.getBoolean(path);
        }

        @Override
        public double getDouble(String path) {
            return configuration.getDouble(path);
        }

        @Override
        public long getLong(String path) {
            return configuration.getLong(path);
        }

        @Override
        public Object get(String path) {
            return configuration.get(path);
        }

        @Override
        public void set(String path, Object value) {
            configuration.set(path, value);
            try {
                configuration.save(this.file);
            } catch (IOException ignored) {
            }
        }

        @Override
        public void addDefault(String path, Object defaultValue) {
            if (!configuration.contains(path)) configuration.set(path, defaultValue);
        }

        @Override
        public boolean contains(String path) {
            return configuration.contains(path);
        }

        @Override
        public Object remove(String path) {
            Object value = configuration.get(path);
            configuration.set(path, null);
            return value;
        }

        @Override
        public void reload() {
            try {
                configuration.load(this.file);
            } catch (IOException | InvalidConfigurationException ignored) {
            }
        }

    }

}
