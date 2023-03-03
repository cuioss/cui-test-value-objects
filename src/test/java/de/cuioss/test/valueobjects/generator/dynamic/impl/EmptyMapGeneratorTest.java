package de.cuioss.test.valueobjects.generator.dynamic.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

class EmptyMapGeneratorTest {

    @Test
    void shouldProduceMaps() {
        final var generator = new EmptyMapGenerator();
        assertEquals(Map.class, generator.getType());
        assertNotNull(generator.next());
        assertTrue(Map.class.isAssignableFrom(generator.next().getClass()));
    }
}
