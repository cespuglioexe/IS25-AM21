package it.polimi.it.galaxytrucker.json;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {

    private static ObjectMapper objectMapper = getDefaultObjectMapper();

    private static ObjectMapper getDefaultObjectMapper () {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper;
    }
}
