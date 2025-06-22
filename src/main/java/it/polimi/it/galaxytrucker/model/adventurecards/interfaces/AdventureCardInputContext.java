package it.polimi.it.galaxytrucker.model.adventurecards.interfaces;

import java.util.HashMap;
import java.util.Map;

public class AdventureCardInputContext {
    private Map<String, Object> inputMap;

    public AdventureCardInputContext() {
        inputMap = new HashMap<>();
    }

    public void put(String key, Object value) {
        inputMap.put(key, value);
    }

    public <T> T get(String key, Class<T> clazz) {
        Object value = inputMap.get(key);

        if (!clazz.isInstance(value)) {
            throw new IllegalArgumentException("Expected type " + clazz + " for key '" + key + "'");
        }

        inputMap.remove(key, value);

        return clazz.cast(value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getUnsafe(String key) {
        return (T) inputMap.remove(key);
    }

    public boolean has(String key) {
        return inputMap.containsKey(key);
    }
}
