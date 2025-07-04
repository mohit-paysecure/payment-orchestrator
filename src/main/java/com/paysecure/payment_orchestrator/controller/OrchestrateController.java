package com.paysecure.payment_orchestrator.controller;

import com.paysecure.payment_orchestrator.service.OrchestrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// controller/OrchestrateController.java
@RestController
@RequestMapping("/api/orchestrate")
@RequiredArgsConstructor
public class OrchestrateController {

    private final OrchestrationService orchestrationService;

    @PostMapping("/{provider}")
    public ResponseEntity<?> orchestrate(@PathVariable String provider, @RequestBody Map<String, Object> input) {
        return orchestrationService.process(provider, input);
    }
}

