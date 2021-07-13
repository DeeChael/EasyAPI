package org.ezapi.html;

import java.util.HashMap;
import java.util.Map;

public class ScriptContent implements HeadContent, BodyContent {

    private String input = "";
    private final Map<String,String> map = new HashMap<>();

    public ScriptContent() {}

    public ScriptContent(String scriptInput) {
        this.input = scriptInput;
    }

    public ScriptContent addSetting(String key, String value) {
        map.put(key, value);
        return this;
    }

    public void removeSetting(String key) {
        map.remove(key);
    }

    public boolean containsSetting(String key) {
        return map.containsKey(key);
    }

    public ScriptContent set(String input) {
        this.input = input;
        return this;
    }

    public String parseToString() {
        StringBuilder out = new StringBuilder();
        for (String key : map.keySet()) {
            out.append(key).append("=\"").append(map.get(key)).append("\" ");
        }
        return "<script " + out.substring(0, out.length()) + ">\n" + input + "</script>";
    }

}
