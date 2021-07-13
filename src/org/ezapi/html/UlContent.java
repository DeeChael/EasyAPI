package org.ezapi.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UlContent implements BodyContent {

    private final List<BodyContent> bodyContents = new ArrayList<>();
    private final Map<String,String> map = new HashMap<>();

    public UlContent addSetting(String key, String value) {
        map.put(key, value);
        return this;
    }

    public void removeSetting(String key) {
        map.remove(key);
    }

    public boolean containsSetting(String key) {
        return map.containsKey(key);
    }

    public UlContent add(BodyContent bodyContent) {
        if (!bodyContents.contains(bodyContent)) {
            bodyContents.add(bodyContent);
        }
        return this;
    }

    public String parseToString() {
        StringBuilder out = new StringBuilder();
        for (String key : map.keySet()) {
            out.append(key).append("=\"").append(map.get(key)).append("\" ");
        }
        StringBuilder in = new StringBuilder();
        for (BodyContent bodyContent : bodyContents) {
            in.append(bodyContent.parseToString()).append("\n");
        }
        return "<ul " + out.substring(0, out.length()) + ">\n" + in + "</ul>";
    }

}
