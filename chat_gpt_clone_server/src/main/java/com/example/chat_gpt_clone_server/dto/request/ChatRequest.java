package com.example.chat_gpt_clone_server.dto.request;

import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.Data;

import java.util.List;

@Data
public class ChatRequest {
    private List<ChatMessage> messages;
}
