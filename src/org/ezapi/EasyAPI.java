package org.ezapi;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.ezapi.command.EzCommandManager;
import org.ezapi.configuration.LanguageManager;
import org.ezapi.util.EzClassLoader;
import org.ezapi.util.ReflectionUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public final class EasyAPI extends JavaPlugin {

    private static EasyAPI INSTANCE;

    private static CommandMap BUKKIT_COMMAND_MAP;

    @Override
    public void onEnable() {
        if (ReflectionUtils.getVersion() < 9) {
            getLogger().severe("Unsupported Server Version " + ReflectionUtils.getServerVersion());
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        try {
            BUKKIT_COMMAND_MAP = (CommandMap) Bukkit.getServer().getClass().getMethod("getCommandMap").invoke(Bukkit.getServer());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        INSTANCE = this;
        //Java 9 not support
        //Trying to find way to solve this problem
        /*
        File libraries = new File(this.getDataFolder(), "libraries");
        if (!libraries.exists()) {
            if (!libraries.mkdirs()) {
                this.getLogger().warning("Failed to create libraries directory");
            }
        }
        File[] files = libraries.listFiles();
        if (files != null) {
            if (files.length > 0) {
                for (File file : files) {
                    if (file.getName().endsWith(".jar")) {
                        EzClassLoader.load(file);
                    }
                }
            }
        }
        try {
            Class.forName("com.zaxxer.hikari.HikariConfig");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        */
        Bukkit.getPluginManager().registerEvents(EzCommandManager.INSTANCE, this);
        //new EzApiCommand().register();
        //new HologramCommand().register();
        reload();
    }

    public void reload() {
        LanguageManager.INSTANCE.reload();
    }

    public static CommandMap getBukkitCommandMap() {
        return BUKKIT_COMMAND_MAP;
    }

    public static EasyAPI getInstance() {
        return INSTANCE;
    }

}
