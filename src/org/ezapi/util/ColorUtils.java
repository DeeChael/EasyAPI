package org.ezapi.util;

import net.md_5.bungee.api.ChatColor;

import java.util.Random;

public final class ColorUtils {

    private final static char[] keys = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'k','l', 'm', 'n', 'o', 'r'};

    public static String randomColor() {
        return "§" + keys[new Random().nextInt(keys.length)];
    }

    public static String translate(String textToTranslate) {
        String text = textToTranslate;
        for (char c : ChatColor.ALL_CODES.toCharArray()) {
            text = text.replace("&" + c, "§" + c);
        }
        if (Ref.getVersion() >= 13) {
            if (text.contains("&#")) {
                for (String msg : text.split("&#")) {
                    if ((msg.length() >= 6)) {
                        text = text.replace("&#" + msg.substring(0, 6), ChatColor.of("#" + msg.substring(0, 6)).toString());
                    }
                }
            }
        }
        return text;
    }

    public static String transfer(String textToTransfer) {
        String text = textToTransfer;
        for (char c : ChatColor.ALL_CODES.toCharArray()) {
            text = text.replace("§" + c, "&" + c);
        }
        return text;
    }

}
