package io.av360.eventdings.reception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CloudEventValidator {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static boolean isValidCloudEvent(String eventString) {
        try {

            JsonNode eventNode = OBJECT_MAPPER.readTree(eventString);

            JsonNode specversionNode = eventNode.get("specversion");
            if (specversionNode == null || !specversionNode.asText().equals("1.0")) {
                return false;
            }

            JsonNode typeNode = eventNode.get("type");
            if (typeNode == null || typeNode.asText().isEmpty()) {
                return false;
            }

            JsonNode sourceNode = eventNode.get("source");
            if (sourceNode == null || sourceNode.asText().isEmpty()) {
                return false;
            }

            JsonNode idNode = eventNode.get("id");
            return idNode != null && !idNode.asText().isEmpty();

        } catch (JsonProcessingException e) {
            return false;
        }
    }
}