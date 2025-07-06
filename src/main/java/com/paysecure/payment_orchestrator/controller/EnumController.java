package com.paysecure.payment_orchestrator.controller;

import com.paysecure.payment_orchestrator.constants.AuthType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;

@RestController
@RequestMapping("/api/enums")
public class EnumController {

    @GetMapping("/auth-types")
    public ResponseEntity<?> getAuthTypes() {
        // Use reflection to get enum constants
        if (AuthType.class.isEnum()) {
            return ResponseEntity.ok(Arrays.stream(AuthType.class.getEnumConstants()).map(Enum::name).toList());
        }
        return ResponseEntity.badRequest().body("AuthType is not an enum.");
    }
}
