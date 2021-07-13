package org.ezapi.html;

import java.util.HashMap;
import java.util.Map;

public class AContent implements BodyContent {

    private final Map<String,String> map = new HashMap<>();
    private String text = "";

    public AContent() {}

    public AContent(BodyContent bodyContent) {
        this.text = bodyContent.parseToString();
    }

    public AContent set(BodyContent bodyContent) {
        this.text = bodyContent.parseToString();
        return this;
    }

    public AContent addSetting(String key, String value) {
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
        return "<a " + out.substring(0, out.length()) + ">\n" + text + "</a>";
    }

}
