package org.ezapi;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.ezapi.configuration.Language;
import org.ezapi.configuration.LanguageCode;
import org.ezapi.configuration.LanguageDefault;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class EasyAPI extends JavaPlugin {

    private static EasyAPI INSTANCE;

    public static final Map<Plugin, Map<String,Language>> PLUGIN_LANGUAGES = new HashMap<>();

    private static final Map<String,Language> DEFAULT_LANGUAGES = new HashMap<>();

    @Override
    public void onEnable() {
        INSTANCE = this;
        //Java 9 not support
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
        //new TestCommand();
        reload();
    }

    public void reload() {
        DEFAULT_LANGUAGES.clear();
        try {
            LanguageDefault english = new LanguageDefault(this);
            english.addDefault("command.unknown", "&cUnknown command or wrong usage");
            english.addDefault("command.be_a_player", "&cYou must be a player");
            Language en_US = new Language(english, LanguageCode.EN_US);
            DEFAULT_LANGUAGES.put("en_us", en_US);
            LanguageDefault chinese = new LanguageDefault(this);
            chinese.addDefault("command.unknown", "&c未知的指令或错误的用法");
            chinese.addDefault("command.be_a_player", "&c你必须是一个玩家");
            Language zh_CN = new Language(chinese, LanguageCode.ZH_CN);
            DEFAULT_LANGUAGES.put("zh_cn", zh_CN);
        } catch (IOException ignored) {
        }
    }

    public static EasyAPI getInstance() {
        return INSTANCE;
    }

}
