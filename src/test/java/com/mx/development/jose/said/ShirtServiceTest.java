package com.mx.development.jose.said;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Test;

class ShirtServiceTest {

    @Test
    void rejectsUnsupportedItem() {
        ShirtAgent agent = mock(ShirtAgent.class);
        ShirtService service = new ShirtService(agent);

        assertThrows(IllegalArgumentException.class,
                () -> service.recommend(new ShirtRequest("sofa", "red")));
        verifyNoInteractions(agent);
    }

    @Test
    void rejectsMissingColor() {
        ShirtAgent agent = mock(ShirtAgent.class);
        ShirtService service = new ShirtService(agent);

        assertThrows(IllegalArgumentException.class,
                () -> service.recommend(new ShirtRequest("playera", " ")));
        verifyNoInteractions(agent);
    }
}
