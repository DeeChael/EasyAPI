package org.ezapi.chat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.ezapi.EasyAPI;
import org.ezapi.configuration.Language;
import org.ezapi.configuration.LanguageCode;
import org.ezapi.configuration.LanguageManager;
import org.ezapi.util.ColorUtils;

import java.util.*;

public final class ChatMessage {

    private final String data;

    private final boolean flag;

    private ClickEvent clickEvent = null;

    private HoverEvent hoverEvent = null;

    private List<ChatMessage> subs = new ArrayList<>();

    private ChatColor color = ChatColor.RESET;

    private Map<String,String> replaces = new HashMap<>();

    public ChatMessage(String data, boolean flag) {
        this.data = data;
        this.flag = flag;
    }

    public TextComponent getMessage(Player player) {
        return getMessage(player.getLocale());
    }

    public TextComponent getMessage(String locale) {
        TextComponent base = new TextComponent();
        TextComponent self = new TextComponent(getSelfText(locale));
        if (clickEvent != null) self.setClickEvent(clickEvent);
        if (hoverEvent != null) self.setHoverEvent(hoverEvent);
        base.addExtra(self);
        for (ChatMessage chatMessage : subs) {
            base.addExtra(chatMessage.getMessage(locale));
        }
        return base;
    }

    public void setReplace(String stringToBeReplaced, String stringToReplace) {
        replaces.put(stringToBeReplaced, stringToReplace);
    }

    public TextComponent getMessage() {
        return getMessage("en_us");
    }

    public void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    public void setHoverEvent(HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public void sub(ChatMessage chatMessage) {
        subs.add(chatMessage);
    }

    public String getData() {
        return data;
    }

    private String getSelfText(String locale) {
        if (flag) {
            if (LanguageManager.INSTANCE.hasText(locale, data)) {
                String finalText = ColorUtils.translate(LanguageManager.INSTANCE.getText(locale, data));
                for (String key : replaces.keySet()) {
                    finalText = finalText.replace(key, replaces.get(key));
                }
                return finalText;
            } else if (LanguageManager.INSTANCE.hasText("en_us", data)) {
                String finalText = ColorUtils.translate(LanguageManager.INSTANCE.getText("en_us", data));
                for (String key : replaces.keySet()) {
                    finalText = finalText.replace(key, replaces.get(key));
                }
                return finalText;
            }
        }
        return ColorUtils.translate(data);
    }

    public String getText(Player player) {
        return getText(player.getLocale());
    }

    public String getText(String locale) {
        if (flag) {
            if (LanguageManager.INSTANCE.hasText(locale, data)) {
                StringBuilder text = new StringBuilder(LanguageManager.INSTANCE.getText(locale, data));
                for (ChatMessage chatMessage : subs) {
                    text.append(chatMessage.getText(locale));
                }
                String finalText = ColorUtils.translate(text.toString());
                for (String key : replaces.keySet()) {
                    finalText = finalText.replace(key, replaces.get(key));
                }
                return finalText;
            } else if (LanguageManager.INSTANCE.hasText("en_us", data)) {
                StringBuilder text = new StringBuilder(LanguageManager.INSTANCE.getText("en_us", data));
                for (ChatMessage chatMessage : subs) {
                    text.append(chatMessage.getText("en_us"));
                }
                String finalText = ColorUtils.translate(text.toString());
                for (String key : replaces.keySet()) {
                    finalText = finalText.replace(key, replaces.get(key));
                }
                return finalText;
            }
        }
        String finalText = ColorUtils.translate(data);
        for (String key : replaces.keySet()) {
            finalText = finalText.replace(key, replaces.get(key));
        }
        return finalText;
    }

    public String getText() {
        return getText("en_us");
    }

}
