package de.cuioss.test.valueobjects.generator.dynamic.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.SortedSet;

import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.property.util.CollectionType;

class CollectionTypeGeneratorTest {

    @Test
    void shouldFailOnNullType() {
        assertThrows(NullPointerException.class, () -> new CollectionTypeGenerator<>(null, CollectionType.COLLECTION));
    }

    @Test
    void shouldFailOnNullCollectionType() {
        assertThrows(NullPointerException.class, () -> new CollectionTypeGenerator<>(Set.class, null));
    }

    @Test
    void shouldHandleCollection() {
        final var generator =
            new CollectionTypeGenerator<>(SortedSet.class, CollectionType.SORTED_SET);
        assertNotNull(generator.next());
        assertTrue(SortedSet.class.isAssignableFrom(generator.next().getClass()));
    }
}
