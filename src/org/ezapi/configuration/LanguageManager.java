package org.ezapi.configuration;

import org.ezapi.util.ColorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LanguageManager {

    private final Map<String, Map<String,String>> LANGUAGES = new HashMap<>();

    private final List<Language> REGISTERED_LANGUAGE = new ArrayList<>();

    /**
     * You shouldn't create a new language manager
     */
    private LanguageManager() {
    }

    /**
     * Get if some plugin register the text path of the locale
     * @param locale locale name
     * @param key path
     * @return if exists
     */
    public boolean hasText(String locale, String key) {
        return (LANGUAGES.containsKey(locale) && LANGUAGES.get(locale).containsKey(key));
    }

    /**
     * Get text by path of the locale
     * @param locale locale name
     * @param key path
     * @return if exists return text or else key
     */
    public String getText(String locale, String key) {
        if (hasText(locale, key)) {
            return ColorUtils.transfer(LANGUAGES.get(locale).get(key));
        }
        return key;
    }

    /**
     * Register language
     * @param language language
     */
    public void register(Language language) {
        REGISTERED_LANGUAGE.add(language);
        if (!LANGUAGES.containsKey(language.getLanguageCode())) LANGUAGES.put(language.getLanguageCode(), new HashMap<>());
        for (String key : language.keys()) {
            if (!LANGUAGES.get(language.getLanguageCode()).containsKey(key)) {
                LANGUAGES.get(language.getLanguageCode()).put(key, language.get(key));
            }
        }
    }

    /**
     * Unregister language
     * @param language language
     */
    public void unregister(Language language) {
        REGISTERED_LANGUAGE.remove(language);
        reload();
    }

    /**
     * Reload all text from local files
     */
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

    /**
     * LanguageManager instance
     */
    public static final LanguageManager INSTANCE = new LanguageManager();

}
