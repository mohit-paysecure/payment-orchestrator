package com.paysecure.payment_orchestrator.entity;

import com.paysecure.payment_orchestrator.constants.AuthType;
import com.paysecure.payment_orchestrator.utility.JpaJsonConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(columnDefinition = "jsonb")
    @Convert(converter = JpaJsonConverter.class)
    private Map<String, Object> requestTemplate;

    @Column(columnDefinition = "jsonb")
    @Convert(converter = JpaJsonConverter.class)
    private Map<String, Object> responseMapping;
}

