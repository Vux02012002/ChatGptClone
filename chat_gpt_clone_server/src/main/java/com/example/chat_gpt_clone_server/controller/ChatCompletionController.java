package com.example.chat_gpt_clone_server.controller;

import com.example.chat_gpt_clone_server.dto.request.ChatRequest;
import com.example.chat_gpt_clone_server.dto.response.Response;
import com.example.chat_gpt_clone_server.service.ChatCompletionService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ChatCompletionController {
    private final ChatCompletionService chatCompletionService;

    @PostMapping(value = "/chat-completion", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getChatCompletion(@RequestBody ChatRequest messages) {
        return ResponseEntity.ok(chatCompletionService.getChatCompletion(messages));
    }
}