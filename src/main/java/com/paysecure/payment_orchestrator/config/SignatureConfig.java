package com.paysecure.payment_orchestrator.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignatureConfig {
    private String algorithm; // e.g., HMAC-SHA512
    private List<String> fields; // Order of fields to include
    private boolean trimValues;
    private boolean urlEncode;
    private String injectLocation; // body | header | query
    private String fieldName; // e.g., signature
    private String secretSource; // "authKey" or "custom"
    private String customSecret; // only if secretSource == "custom"
    private String encoding; // base64, hex, etc.
}

