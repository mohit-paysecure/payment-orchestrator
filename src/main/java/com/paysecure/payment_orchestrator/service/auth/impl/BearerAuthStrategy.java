package com.paysecure.payment_orchestrator.service.auth.impl;

import com.paysecure.payment_orchestrator.entity.ProviderConfigEntity;
import com.paysecure.payment_orchestrator.service.auth.AuthStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component("BEARER")
public class BearerAuthStrategy implements AuthStrategy {
    public HttpHeaders applyAuth(HttpHeaders headers, ProviderConfigEntity config) {
        headers.setBearerAuth(config.getAuthKey());
        return headers;
    }
}
