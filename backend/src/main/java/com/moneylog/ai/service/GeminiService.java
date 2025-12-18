package com.moneylog.ai.service;

import com.moneylog.ai.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Slf4j
public class GeminiService {
    
    private final WebClient webClient;
    private final String apiKey;
    private final String model;
    
    public GeminiService(
            @Value("${gemini.api.key}") String apiKey,
            @Value("${gemini.api.base-url}") String baseUrl,
            @Value("${gemini.api.model}") String model) {
        this.apiKey = apiKey;
        this.model = model;
        
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();
    }
    
    public String generateFinancialDiary(String date, List<AssetPositionDTO> positions) {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your-api-key-here")) {
            log.warn("Gemini API key not configured");
            return "API Key missing. Please configure your environment.";
        }
        
        double totalAssets = positions.stream()
                .mapToDouble(AssetPositionDTO::getAmount)
                .sum();
        
        double totalMonthlyGain = positions.stream()
                .mapToDouble(AssetPositionDTO::getMonthlyGain)
                .sum();
        
        StringBuilder summary = new StringBuilder();
        positions.forEach(p -> {
            String categoryLabel = getCategoryLabel(p.getCategory());
            summary.append(String.format("- %s (%s): Amount %.2f, Month Gain %.2f, Total Gain %.2f\n",
                    p.getName(), categoryLabel, p.getAmount(), p.getMonthlyGain(), p.getTotalGain()));
        });
        
        String prompt = String.format(
            "You are a helpful, encouraging personal finance assistant. " +
            "Write a short, \"Monthly Financial Diary\" entry (max 100 words) for this month based on this data.\n\n" +
            "Data:\n" +
            "Month: %s\n" +
            "Total Assets: %.2f\n" +
            "Total Monthly Gain: %.2f\n" +
            "Holdings:\n" +
            "%s\n\n" +
            "Style: Warm, concise, and professional. Mention the biggest winner of the month. " +
            "If the monthly result is positive, be celebratory. If negative, be reassuring and focus on long-term accumulation. " +
            "Output straight text, no markdown headers.",
            date, totalAssets, totalMonthlyGain, summary.toString()
        );
        
        try {
            GeminiRequest request = new GeminiRequest();
            GeminiRequest.Content content = new GeminiRequest.Content();
            GeminiRequest.Part part = new GeminiRequest.Part();
            part.setText(prompt);
            content.setParts(List.of(part));
            request.setContents(List.of(content));
            
            // Build the correct Gemini API URL: /v1beta/models/{model}:generateContent?key={apiKey}
            GeminiResponse response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/models/{model}:generateContent")
                            .queryParam("key", apiKey)
                            .build(model))
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(GeminiResponse.class)
                    .block();
            
            if (response != null && 
                response.getCandidates() != null && 
                !response.getCandidates().isEmpty() &&
                response.getCandidates().get(0).getContent() != null &&
                response.getCandidates().get(0).getContent().getParts() != null &&
                !response.getCandidates().get(0).getContent().getParts().isEmpty()) {
                
                return response.getCandidates().get(0).getContent().getParts().get(0).getText();
            }
            
            return "No analysis generated.";
        } catch (Exception e) {
            log.error("Gemini API Error", e);
            return "Unable to generate AI summary at this time.";
        }
    }
    
    private String getCategoryLabel(com.moneylog.ai.entity.AssetCategory category) {
        return switch (category) {
            case AH_Stock -> "A/H股基金";
            case US_Stock -> "美股基金";
            case Commodity -> "商品";
            case Bond -> "债券基金";
            case Wealth -> "理财";
            case Cash -> "活钱";
        };
    }
}

