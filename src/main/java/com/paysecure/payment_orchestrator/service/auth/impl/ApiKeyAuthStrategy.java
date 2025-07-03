package com.paysecure.payment_orchestrator.service.auth.impl;

import com.paysecure.payment_orchestrator.entity.ProviderConfigEntity;
import com.paysecure.payment_orchestrator.service.auth.AuthStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;


@Component("API_KEY")
public class ApiKeyAuthStrategy implements AuthStrategy {
    public HttpHeaders applyAuth(HttpHeaders headers, ProviderConfigEntity config) {
        headers.set("x-api-key", config.getAuthKey());
        return headers;
    }
}

