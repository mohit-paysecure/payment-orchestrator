package com.paysecure.payment_orchestrator.entity;

import com.paysecure.payment_orchestrator.constants.AuthType;
import com.paysecure.payment_orchestrator.utility.JpaJsonConverter;
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
    private Map<String, Object> requestTemplate;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> responseMapping;
}

