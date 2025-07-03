package com.paysecure.payment_orchestrator.repo;

import com.paysecure.payment_orchestrator.entity.ProviderConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProviderConfigRepository extends JpaRepository<ProviderConfigEntity, UUID> {
    /**
     * Finds a ProviderConfigEntity by its provider name.
     *
     * @param providerName the name of the provider
     * @return the ProviderConfigEntity if found, otherwise null
     */
    ProviderConfigEntity findByName(String providerName);
}
