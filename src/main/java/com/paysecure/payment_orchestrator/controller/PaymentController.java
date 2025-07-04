package com.paysecure.payment_orchestrator.controller;

import com.paysecure.payment_orchestrator.entity.PaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @GetMapping("/fields")
    public ResponseEntity<Map<String, Object>> getPaymentRequestFields() {
        Map<String, Object> fieldsStructure = new HashMap<>();

        try {
            // Get PaymentRequest class fields
            Class<?> paymentRequestClass = PaymentRequest.class;
            Map<String, Object> paymentRequestFields = extractFields(paymentRequestClass);

            // Get nested class fields
            Class<?> clientClass = PaymentRequest.Client.class;
            Map<String, Object> clientFields = extractFields(clientClass);

            Class<?> purchaseClass = PaymentRequest.Purchase.class;
            Map<String, Object> purchaseFields = extractFields(purchaseClass);

            Class<?> productClass = PaymentRequest.Product.class;
            Map<String, Object> productFields = extractFields(productClass);

            // Build the complete structure
            fieldsStructure.put("paymentRequest", paymentRequestFields);
            fieldsStructure.put("client", clientFields);
            fieldsStructure.put("purchase", purchaseFields);
            fieldsStructure.put("product", productFields);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to extract fields: " + e.getMessage()));
        }

        return ResponseEntity.ok(fieldsStructure);
    }

    @GetMapping("/fields/flat")
    public ResponseEntity<List<String>> getPaymentRequestFieldsFlat() {
        List<String> allFields = new ArrayList<>();

        try {
            // PaymentRequest main fields
            allFields.addAll(getFieldNames(PaymentRequest.class, ""));

            // Client fields with prefix
            allFields.addAll(getFieldNames(PaymentRequest.Client.class, "client."));

            // Purchase fields with prefix
            allFields.addAll(getFieldNames(PaymentRequest.Purchase.class, "purchase."));

            // Product fields with prefix
            allFields.addAll(getFieldNames(PaymentRequest.Product.class, "product."));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(allFields);
    }

    @GetMapping("/fields/structured")
    public ResponseEntity<Map<String, Object>> getPaymentRequestFieldsStructured() {
        Map<String, Object> structure = new HashMap<>();

        try {
            // Main PaymentRequest fields
            structure.put("brandId", "String");
            structure.put("paymentMethod", "String");
            structure.put("successRedirect", "String");
            structure.put("failureRedirect", "String");
            structure.put("successCallback", "String");
            structure.put("failureCallback", "String");
            structure.put("extraParam", "Map<String, String>");

            // Client nested object
            Map<String, String> clientFields = new HashMap<>();
            clientFields.put("email", "String");
            clientFields.put("streetAddress", "String");
            clientFields.put("city", "String");
            clientFields.put("fullName", "String");
            clientFields.put("zipCode", "String");
            clientFields.put("country", "String");
            clientFields.put("dateOfBirth", "LocalDate");
            clientFields.put("stateCode", "String");
            clientFields.put("phone", "String");
            clientFields.put("taxNumber", "String");
            clientFields.put("bankAccountNumber", "String");
            structure.put("client", clientFields);

            // Purchase nested object
            Map<String, Object> purchaseFields = new HashMap<>();
            purchaseFields.put("currency", "String");

            // Product nested object
            Map<String, String> productFields = new HashMap<>();
            productFields.put("name", "String");
            productFields.put("price", "BigDecimal");

            purchaseFields.put("products", Map.of("type", "List<Product>", "productFields", productFields));
            structure.put("purchase", purchaseFields);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to build structure: " + e.getMessage()));
        }

        return ResponseEntity.ok(structure);
    }

    @GetMapping("/fields/json-template")
    public ResponseEntity<Map<String, Object>> getPaymentRequestJsonTemplate() {
        Map<String, Object> template = new HashMap<>();

        // Main fields
        template.put("brandId", "");
        template.put("paymentMethod", "");
        template.put("successRedirect", "");
        template.put("failureRedirect", "");
        template.put("successCallback", "");
        template.put("failureCallback", "");
        template.put("extraParam", Map.of("key", "value"));

        // Client object template
        Map<String, Object> clientTemplate = new HashMap<>();
        clientTemplate.put("email", "");
        clientTemplate.put("streetAddress", "");
        clientTemplate.put("city", "");
        clientTemplate.put("fullName", "");
        clientTemplate.put("zipCode", "");
        clientTemplate.put("country", "");
        clientTemplate.put("dateOfBirth", "YYYY-MM-DD");
        clientTemplate.put("stateCode", "");
        clientTemplate.put("phone", "");
        clientTemplate.put("taxNumber", "");
        clientTemplate.put("bankAccountNumber", "");
        template.put("client", clientTemplate);

        // Purchase object template
        Map<String, Object> purchaseTemplate = new HashMap<>();
        purchaseTemplate.put("currency", "");

        // Product object template
        Map<String, Object> productTemplate = new HashMap<>();
        productTemplate.put("name", "");
        productTemplate.put("price", 0.0);

        purchaseTemplate.put("products", Arrays.asList(productTemplate));
        template.put("purchase", purchaseTemplate);

        return ResponseEntity.ok(template);
    }

    @GetMapping("/fields/validation-rules")
    public ResponseEntity<Map<String, Object>> getValidationRules() {
        Map<String, Object> validationRules = new HashMap<>();

        // Main fields validation
        Map<String, Object> mainFields = new HashMap<>();
        mainFields.put("brandId", Map.of("type", "String", "required", true, "format", "UUID"));
        mainFields.put("paymentMethod", Map.of("type", "String", "required", true, "allowedValues", Arrays.asList("PIX", "CARD", "BANK_TRANSFER")));
        mainFields.put("successRedirect", Map.of("type", "String", "required", true, "format", "URL"));
        mainFields.put("failureRedirect", Map.of("type", "String", "required", true, "format", "URL"));
        mainFields.put("successCallback", Map.of("type", "String", "required", false, "format", "URL"));
        mainFields.put("failureCallback", Map.of("type", "String", "required", false, "format", "URL"));
        mainFields.put("extraParam", Map.of("type", "Map<String, String>", "required", false));

        // Client validation rules
        Map<String, Object> clientRules = new HashMap<>();
        clientRules.put("email", Map.of("type", "String", "required", true, "format", "email"));
        clientRules.put("streetAddress", Map.of("type", "String", "required", true, "maxLength", 255));
        clientRules.put("city", Map.of("type", "String", "required", true, "maxLength", 100));
        clientRules.put("fullName", Map.of("type", "String", "required", true, "maxLength", 100));
        clientRules.put("zipCode", Map.of("type", "String", "required", true, "maxLength", 20));
        clientRules.put("country", Map.of("type", "String", "required", true, "format", "ISO-3166-1"));
        clientRules.put("dateOfBirth", Map.of("type", "LocalDate", "required", true, "format", "YYYY-MM-DD"));
        clientRules.put("stateCode", Map.of("type", "String", "required", false, "maxLength", 10));
        clientRules.put("phone", Map.of("type", "String", "required", true, "format", "phone"));
        clientRules.put("taxNumber", Map.of("type", "String", "required", false, "maxLength", 50));
        clientRules.put("bankAccountNumber", Map.of("type", "String", "required", false, "maxLength", 50));

        // Purchase validation rules
        Map<String, Object> purchaseRules = new HashMap<>();
        purchaseRules.put("currency", Map.of("type", "String", "required", true, "format", "ISO-4217"));
        purchaseRules.put("products", Map.of("type", "List<Product>", "required", true, "minSize", 1));

        // Product validation rules
        Map<String, Object> productRules = new HashMap<>();
        productRules.put("name", Map.of("type", "String", "required", true, "maxLength", 255));
        productRules.put("price", Map.of("type", "BigDecimal", "required", true, "min", 0.01));

        validationRules.put("paymentRequest", mainFields);
        validationRules.put("client", clientRules);
        validationRules.put("purchase", purchaseRules);
        validationRules.put("product", productRules);

        return ResponseEntity.ok(validationRules);
    }

    // Helper methods
    private Map<String, Object> extractFields(Class<?> clazz) {
        Map<String, Object> fields = new HashMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.getName().equals("serialVersionUID")) {
                Map<String, Object> fieldInfo = new HashMap<>();
                fieldInfo.put("type", field.getType().getSimpleName());
                fieldInfo.put("fullType", field.getType().getName());
                fieldInfo.put("genericType", field.getGenericType().getTypeName());
                fields.put(field.getName(), fieldInfo);
            }
        }

        return fields;
    }

    private List<String> getFieldNames(Class<?> clazz, String prefix) {
        List<String> fieldNames = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.getName().equals("serialVersionUID")) {
                fieldNames.add(prefix + field.getName());
            }
        }

        return fieldNames;
    }
}