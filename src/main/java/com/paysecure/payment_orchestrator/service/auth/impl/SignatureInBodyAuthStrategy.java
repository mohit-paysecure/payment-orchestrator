package com.paysecure.payment_orchestrator.service.auth.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paysecure.payment_orchestrator.entity.ProviderConfigEntity;
import com.paysecure.payment_orchestrator.service.auth.AuthStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


@Component("SIGNATURE_IN_BODY")
public class SignatureInBodyAuthStrategy implements AuthStrategy {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public HttpHeaders applyAuth(HttpHeaders headers, ProviderConfigEntity config, String payload) {
        try {
            Map<String, Object> bodyMap = mapper.readValue(payload, new TypeReference<>() {});
            bodyMap.remove("signature");

            // Step 1: Trim + Sort
            Map<String, String> trimmed = new TreeMap<>();
            for (Map.Entry<String, Object> entry : bodyMap.entrySet()) {
                trimmed.put(entry.getKey(), entry.getValue().toString().trim());
            }

            // Step 2: Build Query String
            String queryString = trimmed.entrySet().stream()
                    .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" +
                            URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8).replace("%20", "+"))
                    .collect(Collectors.joining("&"));

            // Step 3: Sign
            String secret = config.getAuthKey();
            Mac sha512 = Mac.getInstance("HmacSHA512");
            sha512.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
            byte[] hash = sha512.doFinal(queryString.getBytes(StandardCharsets.UTF_8));
            String signature = Base64.getEncoder().encodeToString(hash);

            // Step 4: Put back into body
            bodyMap.put("signature", signature);

            // Update payload string (mutate config)
            config.setRequestTemplate(mapper.valueToTree(bodyMap));

            // ALSO add Token if needed (like in your curl)
            headers.set("Authorization", config.getAuthKey());

        } catch (Exception e) {
            throw new RuntimeException("Failed to build signature: " + e.getMessage());
        }

        return headers;
    }
}
