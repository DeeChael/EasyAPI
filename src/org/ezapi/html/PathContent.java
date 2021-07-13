package org.ezapi.html;

import java.util.HashMap;
import java.util.Map;

public class PathContent implements BodyContent {

    private final Map<String,String> map = new HashMap<>();

    public PathContent addSetting(String key, String value) {
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
        return "<path " + in.substring(0, in.length()) + "/>";
    }

}
