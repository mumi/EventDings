package org.av360.maverick.eventdispatcher.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloudEventValidator {
    private static final Logger log = LoggerFactory.getLogger(CloudEventValidator.class);
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
            log.error("Error parsing event", e);
            return false;
        }
    }
}