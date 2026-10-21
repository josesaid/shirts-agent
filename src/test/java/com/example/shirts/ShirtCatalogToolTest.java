package com.example.shirts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ShirtCatalogToolTest {

    @Test
    void findsRedShirt() {
        ShirtCatalogTool tool = new ShirtCatalogTool();
        Shirt shirt = tool.findShirtByColor("red");
        assertEquals("red", shirt.color());
    }

    @Test
    void throwsWhenColorIsNotInCatalog() {
        ShirtCatalogTool tool = new ShirtCatalogTool();
        assertThrows(IllegalArgumentException.class, () -> tool.findShirtByColor("purple"));
    }
}
