package com.paysecure.payment_orchestrator.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.paysecure.payment_orchestrator.constants.AuthType;
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

    public ResponseEntity<?> process(String providerName, Map<String, Object> input) {
        try {
            ProviderConfigEntity config = repo.findByName(providerName.toUpperCase());

            String payload = templateEngine.render(config.getRequestTemplate(), input);
            HttpHeaders headers = new HttpHeaders();

            if (!config.getAuthType().equals(AuthType.NONE)) {
                AuthStrategy strategy = authStrategies.get(config.getAuthType().name());
                headers = strategy.applyAuth(headers, config);
            }

            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    config.getBaseUrl(),
                    HttpMethod.POST,
                    request,
                    String.class
            );

            Map<String, Object> fullBody = new ObjectMapper().readValue(response.getBody(), new TypeReference<>() {
            });
            Map<String, Object> mapped = mapResponse(fullBody, config.getResponseMapping());

            return ResponseEntity.ok(mapped);
        } catch (Exception e) {
            // Log the error (not shown here for brevity)
            return ResponseEntity.status(500).body("An error occurred while processing the request: " + e.getMessage());
        }
    }

    private Map<String, Object> mapResponse(Map<String, Object> body, Map<String, Object> mappingConfig) {
        Map<String, Object> result = new HashMap<>();
        mappingConfig.forEach((key, jsonPath) -> {
            Object value = JsonPath.read(body, jsonPath.toString());
            result.put(key, value);
        });
        return result;
    }
}

