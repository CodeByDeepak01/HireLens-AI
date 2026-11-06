
package com.hirelens.hirelens_backend.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiTestController {

    private final ChatClient chatClient;

    public AiTestController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/test")
    public Map<String, String> testAI() {

        String response = chatClient.prompt()
                .user("Explain Artificial Intelligence in simple English in 2 sentences.")
                .call()
                .content();

        return Map.of(
                "message", "AI connected successfully",
                "response", response
        );
    }
}
