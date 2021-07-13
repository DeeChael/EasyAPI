package org.ezapi.html;

import java.util.HashMap;
import java.util.Map;

public class MetaContent implements HeadContent {

    private final Map<String,String> map = new HashMap<>();

    public MetaContent addSetting(String key, String value) {
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
        StringBuilder in = new StringBuilder();
        for (String key : map.keySet()) {
            in.append(key).append("=\"").append(map.get(key)).append("\" ");
        }
        return "<meta " + in.substring(0, in.length()) + ">";
    }

}
