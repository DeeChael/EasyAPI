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

    public static final ChatMessage NULL = new ChatMessage("", false);

    private final String data;

    private final boolean flag;

    private ClickEvent clickEvent = null;

    private HoverEvent hoverEvent = null;

    private List<ChatMessage> subs = new ArrayList<>();

    private ChatColor color = ChatColor.RESET;

    private Map<String,String> replaces = new HashMap<>();

    private PlaceholderProvider placeholderProvider = null;

    /**
     * Should be used with Language API</br>
     * Send to player by PlayerUtils.sendMessage(org.bukkit.entity.Player, org.ezapi.chat.ChatMessage);
     *
     * @param data data, if flag is true will find text in language texts, not exists or false will set text to data
     * @param flag find in language texts
     */
    public ChatMessage(String data, boolean flag) {
        this.data = data;
        this.flag = flag;
    }

    /**
     * Get message with player's locale
     *
     * @param player target
     * @return message
     */
    public TextComponent getMessage(Player player) {
        String locale = player.getLocale();
        TextComponent base = new TextComponent();
        TextComponent self = new TextComponent(getSelfText(player));
        if (clickEvent != null) self.setClickEvent(clickEvent);
        if (hoverEvent != null) self.setHoverEvent(hoverEvent);
        base.addExtra(self);
        for (ChatMessage chatMessage : subs) {
            base.addExtra(chatMessage.getMessage(player));
        }
        return base;
    }

    /**
     * Get message with locale
     *
     * @param locale target
     * @return message
     */
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

    /**
     * Add placeholder
     *
     * @param stringToBeReplaced string to be replaced
     * @param stringToReplace string to replace
     */
    public void setReplace(String stringToBeReplaced, String stringToReplace) {
        replaces.put(stringToBeReplaced, stringToReplace);
    }

    /**
     * Get message with locale "en_us"
     *
     * @return message
     */
    public TextComponent getMessage() {
        return getMessage("en_us");
    }

    /**
     * Set event on clicked</br>
     * Player only
     *
     * @param clickEvent event on click
     */
    public void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    /**
     * Set what should be shown when player move their cursor on the text
     *
     * @param hoverEvent what should be shown
     */
    public void setHoverEvent(HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

    /**
     * Set the color of the text
     *
     * @param color color
     */
    public void setColor(ChatColor color) {
        this.color = color;
    }

    /**
     * To set placeholder provider
     * @param placeholderProvider placeholder provider
     */
    public void setPlaceholderProvider(PlaceholderProvider placeholderProvider) {
        this.placeholderProvider = placeholderProvider;
    }

    /**
     * Add text</br>
     * Example:</br>
     * ChatMessage a = new ChatMessage("aaaa", false);</br>
     * ChatMessage b = new ChatMessage("bbbb", false);</br>
     * a.sub(b);</br>
     * System.out.println(a.getText());</br>
     * It will print "aaaabbbb";
     *
     * @param chatMessage message
     */
    public void sub(ChatMessage chatMessage) {
        if (chatMessage.subs.contains(this)) return;
        subs.add(chatMessage);
    }

    /**
     * Get data
     * @return data
     */
    public String getData() {
        return data;
    }

    /**
     * Get text only self without subs
     *
     * @param locale locale
     * @return self text
     */
    private String getSelfText(String locale) {
        if (flag) {
            if (LanguageManager.INSTANCE.hasText(locale, data)) {
                String finalText = ColorUtils.translate(LanguageManager.INSTANCE.getText(locale, data));
                for (String key : replaces.keySet()) {
                    finalText = finalText.replace(key, replaces.get(key));
                }
                if (placeholderProvider != null) {
                    finalText = placeholderProvider.setPlaceholder(finalText);
                }
                return finalText;
            } else if (LanguageManager.INSTANCE.hasText("en_us", data)) {
                String finalText = ColorUtils.translate(LanguageManager.INSTANCE.getText("en_us", data));
                for (String key : replaces.keySet()) {
                    finalText = finalText.replace(key, replaces.get(key));
                }
                if (placeholderProvider != null) {
                    finalText = placeholderProvider.setPlaceholder(finalText);
                }
                return finalText;
            }
        }
        String dataText = data;
        if (placeholderProvider != null) {
            dataText = placeholderProvider.setPlaceholder(dataText);
        }
        return ColorUtils.translate(dataText);
    }

    /**
     * Get text only self without subs
     * @param player placeholder requires
     * @return self text
     */
    private String getSelfText(Player player) {
        if (flag) {
            String locale = player.getLocale();
            if (LanguageManager.INSTANCE.hasText(locale, data)) {
                String finalText = ColorUtils.translate(LanguageManager.INSTANCE.getText(locale, data));
                for (String key : replaces.keySet()) {
                    finalText = finalText.replace(key, replaces.get(key));
                }
                if (placeholderProvider != null) {
                    finalText = placeholderProvider.setPlaceholder(finalText, player);
                }
                return finalText;
            } else if (LanguageManager.INSTANCE.hasText("en_us", data)) {
                String finalText = ColorUtils.translate(LanguageManager.INSTANCE.getText("en_us", data));
                for (String key : replaces.keySet()) {
                    finalText = finalText.replace(key, replaces.get(key));
                }
                if (placeholderProvider != null) {
                    finalText = placeholderProvider.setPlaceholder(finalText, player);
                }
                return finalText;
            }
        }
        String dataText = data;
        if (placeholderProvider != null) {
            dataText = placeholderProvider.setPlaceholder(dataText, player);
        }
        return ColorUtils.translate(dataText);
    }

    /**
     * Get text with player's locale
     *
     * @param player target
     * @return text
     */
    public String getText(Player player) {
        if (flag) {
            String locale = player.getLocale();
            if (LanguageManager.INSTANCE.hasText(locale, data)) {
                StringBuilder text = new StringBuilder(LanguageManager.INSTANCE.getText(locale, data));
                for (ChatMessage chatMessage : subs) {
                    text.append(chatMessage.getText(locale));
                }
                String finalText = ColorUtils.translate(text.toString());
                for (String key : replaces.keySet()) {
                    finalText = finalText.replace(key, replaces.get(key));
                }
                if (placeholderProvider != null) {
                    finalText = placeholderProvider.setPlaceholder(finalText, player);
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
                if (placeholderProvider != null) {
                    finalText = placeholderProvider.setPlaceholder(finalText, player);
                }
                return finalText;
            }
        }
        String dataText = data;
        if (placeholderProvider != null) {
            dataText = placeholderProvider.setPlaceholder(dataText, player);
        }
        return ColorUtils.translate(dataText);
    }

    /**
     * Get text with locale
     *
     * @param locale locale
     * @return text
     */
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
                if (placeholderProvider != null) {
                    finalText = placeholderProvider.setPlaceholder(finalText);
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
                if (placeholderProvider != null) {
                    finalText = placeholderProvider.setPlaceholder(finalText);
                }
                return finalText;
            }
        }
        String dataText = data;
        if (placeholderProvider != null) {
            dataText = placeholderProvider.setPlaceholder(dataText);
        }
        return ColorUtils.translate(dataText);
    }

    /**
     * Get text with locale "en_us"
     *
     * @return text
     */
    public String getText() {
        return getText("en_us");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatMessage)) return false;
        ChatMessage message = (ChatMessage) o;
        if (message.subs.size() != subs.size()) {
            return false;
        }
        for (int i = 0; i < subs.size(); i++) {
            if (!subs.get(i).equals(message.subs.get(i))) return false;
        }
        return flag == message.flag && Objects.equals(data, message.data);
    }

}
