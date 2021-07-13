package org.ezapi.chat;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.ezapi.EasyAPI;
import org.ezapi.configuration.Language;
import org.ezapi.configuration.LanguageCode;
import org.ezapi.utils.ColorUtils;

import java.util.Locale;

public class ChatMessage {

    private final String data;

    private final boolean flag;

    public ChatMessage(String data, boolean flag) {
        this.data = ColorUtils.translate(data);
        this.flag = flag;
    }

    public String getMessage(Player player, Plugin plugin) {
        if (flag) {
            String locale = player.getLocale();
            if (EasyAPI.PLUGIN_LANGUAGES.containsKey(plugin)) {
                if (EasyAPI.PLUGIN_LANGUAGES.get(plugin).containsKey(locale)) {
                    Language language = EasyAPI.PLUGIN_LANGUAGES.get(plugin).get(locale.toLowerCase(Locale.ROOT));
                    if (language.has(data)) {
                        return language.get(data);
                    }
                } else if (EasyAPI.PLUGIN_LANGUAGES.get(plugin).containsKey(LanguageCode.EN_US)) {
                    Language language = EasyAPI.PLUGIN_LANGUAGES.get(plugin).get(LanguageCode.EN_US);
                    if (language.has(data)) {
                        return language.get(data);
                    }
                }
            }
        }
        return data;
    }

}
