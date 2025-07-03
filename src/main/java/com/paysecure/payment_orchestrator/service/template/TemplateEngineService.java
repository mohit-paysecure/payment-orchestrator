package com.paysecure.payment_orchestrator.service.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

@Service
public class TemplateEngineService {
    private final MustacheFactory mf = new DefaultMustacheFactory();

    public String render(Map<String, Object> template, Map<String, Object> context) {
        String raw = null;
        try {
            if (template == null || template.isEmpty()) {
                return "";
            }
            raw = new ObjectMapper().writeValueAsString(template);
        } catch (Exception e) {
            throw new RuntimeException("Error rendering template", e);
        }

        Mustache m = mf.compile(new StringReader(raw), "template");
        StringWriter sw = new StringWriter();
        m.execute(sw, context);
        return sw.toString();
    }
}

