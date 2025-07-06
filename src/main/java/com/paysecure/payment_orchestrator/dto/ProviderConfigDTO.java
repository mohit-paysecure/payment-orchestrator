package com.paysecure.payment_orchestrator.dto;

import com.paysecure.payment_orchestrator.constants.AuthType;
import lombok.Data;

import java.util.Map;

@Data
public class ProviderConfigDTO {
    private String name;
    private String baseUrl;
    private AuthType authType;
    private String authKey;
    private Map<String, Object> requestTemplate;
    private Map<String, Object> responseMapping;
}
