package com.mx.development.jose.said;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class ShirtAgent {

    private final ChatClient chatClient;
    private final ShirtCatalogTool shirtCatalogTool;

    public ShirtAgent(ChatClient.Builder chatClientBuilder, ShirtCatalogTool shirtCatalogTool) {
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        You are a shopping assistant for a clothing store.
                        To answer, call the findShirtByColor tool with the color in English.
                        Return only the shirt that the tool returns. Do not invent products.
                        """)
                .build();
        this.shirtCatalogTool = shirtCatalogTool;
    }

    public Shirt recommend(String item, String color) {
        return chatClient.prompt()
                .user(u -> u.text("Find a {item} in {color} color.")
                        .param("item", item)
                        .param("color", color))
                .tools(shirtCatalogTool)
                .call()
                .entity(Shirt.class);
    }
}
