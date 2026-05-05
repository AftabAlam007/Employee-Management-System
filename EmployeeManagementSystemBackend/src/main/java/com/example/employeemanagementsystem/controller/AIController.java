package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.dto.AIExecutionResponse;
import com.example.employeemanagementsystem.service.AIActionService;
import com.example.employeemanagementsystem.service.AIService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;
    private final AIActionService aiActionService;

    public AIController(AIService aiService, AIActionService aiActionService) {
        this.aiService = aiService;
        this.aiActionService = aiActionService;
    }

    @GetMapping("/execute")
    public ResponseEntity<?> execute(@RequestParam String prompt) {
        try {
            String aiJson = aiService.convertPromptToActionJson(prompt);
            Object result = aiActionService.executeAction(aiJson);
            return ResponseEntity.ok(new AIExecutionResponse(aiJson, result));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unable to execute AI command"));
        }
    }
}
