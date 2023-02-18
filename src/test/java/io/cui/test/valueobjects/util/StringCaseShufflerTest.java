package io.cui.test.valueobjects.util;

import static io.cui.test.valueobjects.util.StringCaseShuffler.shuffleCase;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.cui.test.generator.Generators;

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
