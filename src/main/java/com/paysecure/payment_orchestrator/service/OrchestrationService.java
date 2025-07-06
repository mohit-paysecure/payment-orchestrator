package com.paysecure.payment_orchestrator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.paysecure.payment_orchestrator.dto.ProviderConfigDTO;
import com.paysecure.payment_orchestrator.entity.ProviderConfigEntity;
import com.paysecure.payment_orchestrator.repo.ProviderConfigRepository;
import com.paysecure.payment_orchestrator.service.auth.AuthStrategy;
import com.paysecure.payment_orchestrator.service.template.TemplateEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrchestrationService {

    private final ProviderConfigRepository repo;
    private final Map<String, AuthStrategy> authStrategies;
    private final TemplateEngineService templateEngine;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResponseEntity<?> process(String providerName, JsonNode inputNode) {
        try {
            ProviderConfigEntity config = repo.findByName(providerName.toUpperCase());

            JsonNode templateNode = objectMapper.convertValue(config.getRequestTemplate(), JsonNode.class);
            String payload = templateEngine.render(templateNode, inputNode);

            HttpHeaders headers = new HttpHeaders();
            AuthStrategy strategy = authStrategies.get(config.getAuthType().name());
            headers = strategy.applyAuth(headers, config, payload);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(payload, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    config.getBaseUrl(),
                    HttpMethod.POST,
                    request,
                    String.class
            );

            // Parse response body as JsonNode
            JsonNode fullBody = objectMapper.readTree(response.getBody());
// Convert response mapping to JsonNode
            JsonNode responseMapping = objectMapper.convertValue(config.getResponseMapping(), JsonNode.class);
// Call mapResponse with JsonNode parameters
            Map<String, Object> mapped = mapResponse(fullBody, responseMapping);

            return ResponseEntity.ok(mapped);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while processing the request: " + e.getMessage());
        }
    }

    private Map<String, Object> mapResponse(JsonNode body, JsonNode mappingConfig) {
        Map<String, Object> result = new HashMap<>();
        String jsonString = body.toString();
        mappingConfig.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            String jsonPath = entry.getValue().asText();
            Object value = JsonPath.read(jsonString, jsonPath);
            result.put(key, value);
        });
        return result;
    }

    public ResponseEntity<?> saveProviderConfig(ProviderConfigDTO dto) {
        try {
            ProviderConfigEntity entity = new ProviderConfigEntity();
            entity.setName(dto.getName().toUpperCase());
            entity.setBaseUrl(dto.getBaseUrl());
            entity.setAuthType(dto.getAuthType());
            entity.setAuthKey(dto.getAuthKey());
            entity.setRequestTemplate(dto.getRequestTemplate());
            entity.setResponseMapping(dto.getResponseMapping());
            entity.setCustomHeaders(dto.getCustomHeaders());
            entity.setSignatureConfig(dto.getSignatureConfig());

            ProviderConfigEntity saved = repo.save(entity);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to save config: " + e.getMessage());
        }
    }
}
