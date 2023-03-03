package de.cuioss.test.valueobjects.util;

import static de.cuioss.test.valueobjects.util.StringCaseShuffler.shuffleCase;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.cuioss.test.generator.Generators;

class StringCaseShufflerTest {

    @Test
    void shouldShuffle() {
        assertNull(shuffleCase(null));
        var test = Generators.strings(24, 128).next();
        var shuffled = shuffleCase(test);
        assertNotEquals(test, shuffled);
        assertTrue(test.equalsIgnoreCase(shuffled));
    }
}
