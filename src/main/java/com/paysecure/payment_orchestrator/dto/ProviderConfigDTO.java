package com.paysecure.payment_orchestrator.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.paysecure.payment_orchestrator.config.SignatureConfig;
import com.paysecure.payment_orchestrator.constants.AuthType;
import lombok.Data;

import java.util.Map;

@Data
public class ProviderConfigDTO {
    private String name;
    private String baseUrl;
    private AuthType authType;
    private String authKey;
    private JsonNode requestTemplate;
    private JsonNode responseMapping;
    private JsonNode customHeaders;
    private SignatureConfig signatureConfig;
}
