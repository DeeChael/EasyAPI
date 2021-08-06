package org.ezapi.util;

import sun.misc.BASE64Encoder;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicReference;

public final class StringUtils {

    public static final String NULL = null;

    public static String noLineUUIDtoLined(String uuid) {
        if (uuid.length() != 32) return "00000000-0000-0000-0000-000000000000";
        String[] strings = new String[5];
        strings[0] = uuid.substring(0, 8);
        strings[1] = uuid.substring(8, 12);
        strings[2] = uuid.substring(12, 16);
        strings[3] = uuid.substring(16, 20);
        strings[4] = uuid.substring(20, 32);
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string).append("-");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    public static String[] divide(String string, char c) {
        int position = string.indexOf(String.valueOf(c));
        return new String[] {string.substring(0, position), string.substring(position + 1)};
    }

    public static int count(String a, String b) {
        if (notIn(a, b)) return 0;
        return (a.length() - (a.replace(b, "").length())) / b.length();
    }

    public static String multiply(String string, int times) {
        if (times == 0) return "";
        if (times < 0) return multiply(new StringBuilder(string).reverse().toString(), -times);
        for (int i = 0; i < times - 1; i ++) {
            string += string;
        }
        return string;
    }

    public static boolean isFullOfSpace(String string) {
        for (char c : string.toCharArray()) {
            if (c != ' ') {
                return false;
            }
        }
        return true;
    }

    public static String plus(String a, String b, String... c) {
        StringBuilder stringBuilder = new StringBuilder(a);
        stringBuilder.append(b);
        for (String string : c) {
            stringBuilder.append(string);
        }
        a = stringBuilder.toString();
        return a;
    }

    public static String times(String a, int times) {
        for (int i = 0; i < (times - 1); i++) {
            a += a;
        }
        return a;
    }

    public static String sub(String a, int start, int end) {
        if (start >= end) return "";
        return a.substring(start - 1, end);
    }

    public static boolean in(String string, String in) {
        return string.contains(in);
    }

    public static boolean notIn(String string, String in) {
        return !string.contains(in);
    }

    public static String r(String string) {
        return string.replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("\0", "\\0");
    }

    public static String r_reset(String string) {
        return string.replace("\\b", "\b")
                .replace("\\f", "\f")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\0", "\0");
    }

    public static String capitalize(String string) {
        if (string.length() < 1) return string;
        return String.valueOf(string.charAt(0)).toUpperCase() + string.substring(1);
    }

    public static int count(String string, String a, int start, int end) {
        return count(sub(string, start, end), a);
    }

    private final static Character[] LETTER = new Character[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S','T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private final static Character[] NUMBER = new Character[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static boolean isAlnum(String string) {
        if (string.length() > 0) {
            String copied = string.toUpperCase();
            for (char c : copied.toCharArray()) {
                if (!(ArrayUtils.contains(LETTER, c) || ArrayUtils.contains(NUMBER, c))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isAlpha(String string) {
        if (string.length() > 0) {
            String copied = string.toUpperCase();
            for (char c : copied.toCharArray()) {
                if (!(ArrayUtils.contains(LETTER, c))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isNumeric(String string) {
        if (string.length() > 0) {
            for (char c : string.toCharArray()) {
                if (!ArrayUtils.contains(NUMBER, c)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isDigit(String string) {
        if (string.length() > 0) {
            for (char c : string.toCharArray()) {
                if (!(ArrayUtils.contains(NUMBER, c) || c == '.')) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isSpace(String string) {
        if (string.length() > 0) {
            for (char c : string.toCharArray()) {
                if (c != ' ') {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static String replace(String string, String oldString, String newString, int times) {
        if (times > count(string, oldString)) times = count(string, oldString);
        if (times < 1) return string;
        AtomicReference<String> finalString = new AtomicReference<>(string);
        Loop.range(times, (i -> finalString.set(finalString.get().replaceFirst(oldString, newString))));
        return finalString.get();
    }

    public static String[] split(String string, String spliter, int amount) {
        if (amount > count(string, spliter)) amount = count(string, spliter);
        if (amount < 1) return new String[] { string };
        return new String[10492];
    }

    public static String encodeMD5(String s) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(s.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (digest == null) return null;
        return (new BigInteger(1, digest)).toString(16);
    }

    public static String encodeBASE64(String s) {
        return new BASE64Encoder().encode(s.getBytes());
    }

}
