package io.cui.test.valueobjects.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ResourceBundle;

import org.junit.jupiter.api.Test;

import io.cui.test.generator.Generators;

class IdentityResourceBundleTest {

    @Test
    void shouldReturnGivenKey() {
        final ResourceBundle bundle = new IdentityResourceBundle();
        final var key = Generators.strings().next();
        assertEquals(key, bundle.getString(key));
        assertEquals(key, bundle.getObject(key));
    }

    @Test
    void shouldReturnEmptyKeySet() {
        assertNotNull(new IdentityResourceBundle().getKeys());
    }
}
