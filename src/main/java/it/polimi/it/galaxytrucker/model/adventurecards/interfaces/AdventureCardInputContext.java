package it.polimi.it.galaxytrucker.model.adventurecards.interfaces;

import java.util.HashMap;
import java.util.Map;

import it.polimi.it.galaxytrucker.model.managers.AdventureCardInputFields;

public class AdventureCardInputContext {
    private Map<AdventureCardInputFields, Object> inputMap;

    public AdventureCardInputContext() {
        inputMap = new HashMap<>();
    }

    public void put(AdventureCardInputFields key, Object value) {
        inputMap.put(key, value);
    }

    public <T> T get(AdventureCardInputFields key, Class<T> clazz) {
        Object value = inputMap.get(key);

        if (!clazz.isInstance(value)) {
            throw new IllegalArgumentException("Expected type " + clazz + " for key '" + key + "'");
        }

        inputMap.remove(key, value);

        return clazz.cast(value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getUnsafe(AdventureCardInputFields key) {
        return (T) inputMap.remove(key);
    }

    public boolean has(AdventureCardInputFields key) {
        return inputMap.containsKey(key);
    }
}
