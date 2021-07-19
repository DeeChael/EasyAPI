package org.ezapi.configuration;

import org.ezapi.util.ColorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LanguageManager {

    private final Map<String, Map<String,String>> LANGUAGES = new HashMap<>();

    private final List<Language> REGISTERED_LANGUAGE = new ArrayList<>();

    private LanguageManager() {
    }

    public boolean hasText(String locale, String key) {
        return (LANGUAGES.containsKey(locale) && LANGUAGES.get(locale).containsKey(key));
    }

    public String getText(String locale, String key) {
        if (hasText(locale, key)) {
            return ColorUtils.transfer(LANGUAGES.get(locale).get(key));
        }
        return key;
    }

    public void register(Language language) {
        REGISTERED_LANGUAGE.add(language);
        if (!LANGUAGES.containsKey(language.getLanguageCode())) LANGUAGES.put(language.getLanguageCode(), new HashMap<>());
        for (String key : language.keys()) {
            if (!LANGUAGES.get(language.getLanguageCode()).containsKey(key)) {
                LANGUAGES.get(language.getLanguageCode()).put(key, language.get(key));
            }
        }
    }

    public void unregister(Language language) {
        REGISTERED_LANGUAGE.remove(language);
        reload();
    }

    public void reload() {
        LANGUAGES.clear();
        for (String languageCode : LanguageCode.values()) {
            LANGUAGES.put(languageCode, new HashMap<>());
        }
        for (Language language : REGISTERED_LANGUAGE) {
            language.reload();
            if (!LANGUAGES.containsKey(language.getLanguageCode())) LANGUAGES.put(language.getLanguageCode(), new HashMap<>());
            for (String key : language.keys()) {
                if (!LANGUAGES.get(language.getLanguageCode()).containsKey(key)) {
                    LANGUAGES.get(language.getLanguageCode()).put(key, language.get(key));
                }
            }
        }
    }

    public static final LanguageManager INSTANCE = new LanguageManager();

}
