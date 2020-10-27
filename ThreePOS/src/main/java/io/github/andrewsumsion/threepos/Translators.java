package io.github.andrewsumsion.threepos;

import java.util.HashMap;
import java.util.Map;

public class Translators {
    private static Translators INSTANCE;

    public static Translators getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Translators();
        }
        return INSTANCE;
    }

    private Map<String, Translator> translators = new HashMap<>();

    private Translators() {

    }

    public void registerTranslator(String pluginName, Translator translator) {
        translators.put(pluginName, translator);
    }

    public Translator getTranslator(String pluginName) {
        return translators.get(pluginName);
    }
}
