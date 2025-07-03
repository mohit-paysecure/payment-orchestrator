package com.paysecure.payment_orchestrator.service.auth;

import com.paysecure.payment_orchestrator.entity.ProviderConfigEntity;
import org.springframework.http.HttpHeaders;


public interface AuthStrategy {
    HttpHeaders applyAuth(HttpHeaders headers, ProviderConfigEntity config);
}

