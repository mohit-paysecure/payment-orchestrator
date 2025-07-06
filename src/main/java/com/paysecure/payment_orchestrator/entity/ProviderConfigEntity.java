package com.paysecure.payment_orchestrator.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.paysecure.payment_orchestrator.config.SignatureConfig;
import com.paysecure.payment_orchestrator.constants.AuthType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "provider_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String name;

    private String baseUrl;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    private String authKey;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode requestTemplate;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode responseMapping;

    @Column(name = "custom_headers", columnDefinition = "jsonb")
    @Type(JsonType.class)
    private JsonNode customHeaders;

    @Column(name = "signature_config", columnDefinition = "jsonb")
    @Type(JsonType.class)
    private SignatureConfig signatureConfig;

}

