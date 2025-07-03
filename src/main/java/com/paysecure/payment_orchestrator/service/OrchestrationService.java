package com.paysecure.payment_orchestrator.service;

import com.paysecure.payment_orchestrator.entity.ProviderConfigEntity;
import com.paysecure.payment_orchestrator.repo.ProviderConfigRepository;
import com.paysecure.payment_orchestrator.service.auth.AuthStrategy;
import com.paysecure.payment_orchestrator.service.template.TemplateEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class OrchestrationService {

    @Autowired
    private ProviderConfigRepository repo;
    @Autowired private Map<String, AuthStrategy> authStrategies;
    @Autowired private TemplateEngineService templateEngine;

    public ResponseEntity<?> process(String providerName, Map<String, Object> input) {
        ProviderConfigEntity config = repo.findByName(providerName);
        String payload = templateEngine.render(config.getRequestTemplate(), input);

        HttpHeaders headers = new HttpHeaders();
        AuthStrategy strategy = authStrategies.get(config.getAuthType().name());
        headers = strategy.applyAuth(headers, config);

        HttpEntity<String> entity = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(config.getBaseUrl(), HttpMethod.POST, entity, String.class);

        // Later: use JsonPath to map response to internal format using config.getResponseMapping()

        return response;
    }
}

