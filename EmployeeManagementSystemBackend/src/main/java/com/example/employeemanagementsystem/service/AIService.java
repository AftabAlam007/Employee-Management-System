package com.example.employeemanagementsystem.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final ChatClient chatClient;

    public AIService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String convertPromptToActionJson(String userPrompt) {
        String systemPrompt = """
                You are an HR assistant for an Employee Management System.
                Convert the user input into STRICT JSON only.
                Allowed actions: GET_ALL, ADD, UPDATE, DELETE, MAX_SALARY, FIND_BY_NAME, GET_JOINING_DATE, GET_SALARY, MIN_SALARY, AVG_SALARY, COUNT_EMPLOYEES, GET_BY_MANAGER, GET_BY_PROJECT.
                Allowed JSON formats:
                {"action":"GET_ALL"}
                {"action":"ADD","name":"Rahul","salary":50000}
                {"action":"UPDATE","name":"Rahul","salary":70000}
                {"action":"DELETE","name":"Rahul"}
                {"action":"MAX_SALARY"}
                {"action":"FIND_BY_NAME","name":"Rahul"}
                {"action":"GET_JOINING_DATE","name":"Rahul"}
                {"action":"GET_SALARY","name":"Rahul"}
                {"action":"MIN_SALARY"}
                {"action":"AVG_SALARY"}
                {"action":"COUNT_EMPLOYEES"}
                {"action":"GET_BY_MANAGER","managerName":"Amit"}
                {"action":"GET_BY_PROJECT","projectName":"AI"}
                Rules:
                1) Output must be valid JSON object only, no markdown and no extra text.
                2) action must be uppercase.
                3) Include name only for ADD, UPDATE, DELETE, FIND_BY_NAME, GET_JOINING_DATE, GET_SALARY.
                4) Include salary only for ADD and UPDATE.
                5) Include managerName only for GET_BY_MANAGER.
                6) Include projectName only for GET_BY_PROJECT.
                7) If intent is unclear, output {"action":"UNKNOWN"}.
                """;

        Object responseObj = chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();
        if (!(responseObj instanceof String response) || response.isBlank()) {
            throw new IllegalArgumentException("AI model returned empty response");
        }
        return response;
    }
}
