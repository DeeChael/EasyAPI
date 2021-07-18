package org.ezapi;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.ezapi.command.EzCommandManager;
import org.ezapi.command.defaults.EzApiCommand;
import org.ezapi.configuration.Language;
import org.ezapi.configuration.LanguageCode;
import org.ezapi.configuration.LanguageDefault;
import org.ezapi.configuration.LanguageManager;
import org.ezapi.util.ReflectionUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class EasyAPI extends JavaPlugin {

    private static EasyAPI INSTANCE;

    private static final Map<String,Language> DEFAULT_LANGUAGES = new HashMap<>();

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
                    EzClassLoader.load(file);
                }
            }
        }
        */
        Bukkit.getPluginManager().registerEvents(EzCommandManager.INSTANCE, this);
        new EzApiCommand().register();
        reload();
    }

    @Override
    public void onDisable() {

    }

    public void reload() {
        for (Language language : DEFAULT_LANGUAGES.values()) {
            LanguageManager.INSTANCE.unregister(language);
        }
        try {
            LanguageDefault english = new LanguageDefault(this);
            english.addDefault("command.unknown", "&cUnknown command or wrong usage");
            english.addDefault("command.be_a_player", "&cYou must be a player");
            english.addDefault("ez-api.message.command.ez-api.reload.success", "&aConfiguration files has been reloaded");
            Language en_US = new Language(english, LanguageCode.EN_US);
            DEFAULT_LANGUAGES.put("en_us", en_US);
            LanguageDefault chinese = new LanguageDefault(this);
            chinese.addDefault("command.unknown", "&c未知的指令或错误的用法");
            chinese.addDefault("command.be_a_player", "&c你必须是一个玩家");
            chinese.addDefault("ez-api.message.command.ez-api.reload.success", "&a已重载配置文件");
            Language zh_CN = new Language(chinese, LanguageCode.ZH_CN);
            DEFAULT_LANGUAGES.put("zh_cn", zh_CN);
        } catch (IOException ignored) {
        }
        for (Language language : DEFAULT_LANGUAGES.values()) {
            LanguageManager.INSTANCE.register(language);
        }
        LanguageManager.INSTANCE.reload();
    }

    public static CommandMap getBukkitCommandMap() {
        return BUKKIT_COMMAND_MAP;
    }

    public static EasyAPI getInstance() {
        return INSTANCE;
    }

}
