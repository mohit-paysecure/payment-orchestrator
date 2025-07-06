package com.paysecure.payment_orchestrator.service.template;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
@Service
public class TemplateEngineService {

    private final ObjectMapper mapper = new ObjectMapper();
    private final MustacheFactory mf = new DefaultMustacheFactory();

    public String render(JsonNode flatTemplateNode, JsonNode inputContextNode) {
        try {
            if (flatTemplateNode == null || flatTemplateNode.isEmpty()) return "";

            // Flatten context
            Map<String, Object> contextMap = convertJsonNodeToFlatMap("", inputContextNode);

            // Template map (e.g., { amount: "{{product.price}}" })
            Map<String, String> templateMap = mapper.convertValue(flatTemplateNode, Map.class);

            // Render each field individually
            ObjectNode renderedNode = mapper.createObjectNode();
            for (Map.Entry<String, String> entry : templateMap.entrySet()) {
                StringWriter sw = new StringWriter();
                Mustache mustache = mf.compile(new StringReader(entry.getValue()), "field-template");
                mustache.execute(sw, contextMap).flush();

                renderedNode.put(entry.getKey(), sw.toString());
            }

            return mapper.writeValueAsString(renderedNode);

        } catch (Exception e) {
            throw new RuntimeException("Template rendering failed", e);
        }
    }

    private Map<String, Object> convertJsonNodeToFlatMap(String prefix, JsonNode node) {
        Map<String, Object> flatMap = new LinkedHashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String fullKey = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            JsonNode value = entry.getValue();

            if (value.isObject()) {
                flatMap.putAll(convertJsonNodeToFlatMap(fullKey, value));
            } else if (value.isArray()) {
                // Convert arrays to stringified form
                flatMap.put(fullKey, value.toString());
            } else if (value.isNull()) {
                flatMap.put(fullKey, null);
            } else if (value.isBoolean()) {
                flatMap.put(fullKey, value.asBoolean());
            } else if (value.isNumber()) {
                flatMap.put(fullKey, value.numberValue());
            } else {
                flatMap.put(fullKey, value.asText());
            }
        }
        return flatMap;
    }
}
