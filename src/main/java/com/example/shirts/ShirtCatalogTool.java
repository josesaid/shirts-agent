package com.example.shirts;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class ShirtCatalogTool {

    // In a real application this would be a repository or an inventory API.
    private static final List<Shirt> CATALOG = List.of(
            new Shirt("red", "M", "Classic Crew", 299.0),
            new Shirt("red", "L", "Relaxed Fit", 349.0),
            new Shirt("blue", "M", "Classic Crew", 299.0),
            new Shirt("black", "L", "Essential Tee", 259.0)
    );

    @Tool(description = "Find a t-shirt (playera) available in the requested color. Returns the first matching shirt.")
    public Shirt findShirtByColor(
            @ToolParam(description = "Color in English, for example red, blue or black") String color) {

        return CATALOG.stream()
                .filter(shirt -> shirt.color().equalsIgnoreCase(color.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No shirt found for color: " + color));
    }
}
