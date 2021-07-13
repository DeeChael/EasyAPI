package org.ezapi.html;

import java.util.HashMap;
import java.util.Map;

public class HContent implements BodyContent {

    private final Map<String,String> map = new HashMap<>();
    private final int level;
    private String text = "";

    public HContent(int level) {
        if (level > 6) level = 6;
        if (level < 1) level = 1;
        this.level = level;
    }

    public HContent(int level, BodyContent bodyContent) {
        if (level > 6) level = 6;
        if (level < 1) level = 1;
        this.level = level;
        this.text = bodyContent.parseToString();
    }

    public HContent set(BodyContent bodyContent) {
        this.text = bodyContent.parseToString();
        return this;
    }

    public HContent addSetting(String key, String value) {
        map.put(key, value);
        return this;
    }

    public void removeSetting(String key) {
        map.remove(key);
    }

    public boolean containsSetting(String key) {
        return map.containsKey(key);
    }

    public String parseToString() {
        StringBuilder out = new StringBuilder();
        for (String key : map.keySet()) {
            out.append(key).append("=\"").append(map.get(key)).append("\" ");
        }
        return "<h" + level + " " + out.substring(0, out.length()) + ">" + text + "</h" + level + ">";
    }

}
