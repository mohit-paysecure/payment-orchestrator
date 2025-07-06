package com.paysecure.payment_orchestrator.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.paysecure.payment_orchestrator.dto.ProviderConfigDTO;
import com.paysecure.payment_orchestrator.service.OrchestrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orchestrate")
@RequiredArgsConstructor
public class OrchestrateController {

    private final OrchestrationService orchestrationService;

    @PostMapping("/{provider}")
    public ResponseEntity<?> orchestrate(@PathVariable String provider, @RequestBody ObjectNode input) {
        return orchestrationService.process(provider, input);
    }

    @PostMapping
    public ResponseEntity<?> saveProviderConfig(@RequestBody ProviderConfigDTO dto) {
        return orchestrationService.saveProviderConfig(dto);
    }
}

