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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatMessage {

    private final String data;

    private final boolean flag;

    private ClickEvent clickEvent = null;

    private HoverEvent hoverEvent = null;

    private List<ChatMessage> subs = new ArrayList<>();

    private ChatColor color = ChatColor.RESET;

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
                return ColorUtils.translate(LanguageManager.INSTANCE.getText(locale, data));
            } else if (LanguageManager.INSTANCE.hasText("en_us", data)) {
                return ColorUtils.translate(LanguageManager.INSTANCE.getText("en_us", data));
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
                return ColorUtils.translate(text.toString());
            } else if (LanguageManager.INSTANCE.hasText("en_us", data)) {
                StringBuilder text = new StringBuilder(LanguageManager.INSTANCE.getText("en_us", data));
                for (ChatMessage chatMessage : subs) {
                    text.append(chatMessage.getText("en_us"));
                }
                return ColorUtils.translate(text.toString());
            }
        }
        return ColorUtils.translate(data);
    }

    public String getText() {
        return getText("en_us");
    }

}
