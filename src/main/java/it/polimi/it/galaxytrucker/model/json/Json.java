package it.polimi.it.galaxytrucker.model.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public abstract class Json {
    public static ObjectMapper objectMapper = getDefaultObjectMapper();

    private static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    public static JsonNode parse(String src) throws IOException {
        return objectMapper.readTree(src);
    }

    public static JsonNode parse(File src) throws IOException {
        return objectMapper.readTree(src);
    }

    public static <A> A fromJson(JsonNode node, Class<A> clazz) throws IOException {
        return objectMapper.treeToValue(node, clazz);
    }

    public static <A> List<A> fromJsonList(JsonNode node, Class<A> clazz) throws IOException {
        return objectMapper.readValue(node.traverse(), objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }

    public static <A> Set<A> fromJsonSet(JsonNode node, Class<A> clazz) throws IOException {
        return objectMapper.readValue(node.traverse(),
                objectMapper.getTypeFactory().constructCollectionType(Set.class, clazz));
    }
}
