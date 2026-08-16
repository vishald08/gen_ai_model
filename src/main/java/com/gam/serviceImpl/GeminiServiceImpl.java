package com.gam.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gam.service.GeminiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiServiceImpl implements GeminiService {

    @Value("${gemini.api.key:}")
    private String apiKey;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    // List of active models for automatic fallback during high demand
    private final String[] models = {
        "gemini-flash-latest",
        "gemini-3.5-flash-lite",
        "gemini-3.5-flash"
    };

    public GeminiServiceImpl() {
        this.restClient = RestClient.create();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String askGemini(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            return "Kripya koi sawal type karein.";
        }

        String cleanKey = (apiKey != null) ? apiKey.trim() : "";
        if (cleanKey.isEmpty() || cleanKey.contains("YOUR_GEMINI_API_KEY")) {
            return "Gemini API Key missing hai. Kripya application.properties me gemini.api.key set karein.";
        }

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", prompt.trim())
                ))
            )
        );

        String lastError = "";

        // Try primary model, if 503 or failure occurs, fallback to next model
        for (String modelName : models) {
            try {
                String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/" + modelName + ":generateContent";

                String rawResponse = restClient.post()
                        .uri(apiUrl)
                        .header("x-goog-api-key", cleanKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(requestBody)
                        .retrieve()
                        .body(String.class);

                JsonNode rootNode = objectMapper.readTree(rawResponse);
                JsonNode candidates = rootNode.path("candidates");

                if (candidates.isArray() && !candidates.isEmpty()) {
                    JsonNode parts = candidates.get(0).path("content").path("parts");
                    if (parts.isArray() && !parts.isEmpty()) {
                        StringBuilder resultText = new StringBuilder();
                        for (JsonNode part : parts) {
                            if (!part.has("thought") || !part.path("thought").asBoolean()) {
                                if (part.has("text")) {
                                    resultText.append(part.path("text").asText());
                                }
                            }
                        }
                        if (resultText.length() > 0) {
                            return resultText.toString();
                        }
                        return parts.get(0).path("text").asText();
                    }
                }
            } catch (Exception ex) {
                lastError = ex.getMessage();
                System.out.println("Model [" + modelName + "] is busy. Trying fallback model...");
                try {
                    Thread.sleep(800); // Short pause before fallback
                } catch (InterruptedException ignored) {}
            }
        }

        return "AI servers par abhi traffic bohot high hai. Kripya 10-15 seconds baad dubara send karein. (Details: " + lastError + ")";
    }
}