package io.cui.test.valueobjects.property.util;

import static io.cui.test.valueobjects.property.util.CollectionAsserts.assertListsAreEqualIgnoringOrder;
import static io.cui.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.cui.test.generator.Generators;

class CollectionAssertsTest {

    @Test
    void shouldPassOnBothNullOrEmpty() {
        assertListsAreEqualIgnoringOrder("propertyName", null, null);
        assertListsAreEqualIgnoringOrder("propertyName", new ArrayList<>(), new ArrayList<>());
    }

    @Test
    @SuppressWarnings("java:S5778") // owolff: new ArrayList<>() is not a problem
    void shouldFailOnExpectedNull() {
        assertThrows(AssertionError.class,
                () -> assertListsAreEqualIgnoringOrder("propertyName", null, new ArrayList<>()));
    }

    @Test
    @SuppressWarnings("java:S5778") // owolff: new ArrayList<>() is not a problem
    void shouldFailOnActualNull() {
        assertThrows(AssertionError.class,
                () -> assertListsAreEqualIgnoringOrder("propertyName", new ArrayList<>(), null));
    }

    @Test
    void shouldFailOnNoExpectedCollection() {
        assertThrows(AssertionError.class, () -> assertListsAreEqualIgnoringOrder("propertyName", 1, null));
    }

    @Test
    void shouldFailOnNoActualCollection() {
        assertThrows(AssertionError.class, () -> assertListsAreEqualIgnoringOrder("propertyName", null, 1));
    }

    @Test
    void shouldFailOnNoCollection() {
        assertThrows(AssertionError.class, () -> assertListsAreEqualIgnoringOrder("propertyName", 2, 1));
    }

    @Test
    @SuppressWarnings("java:S5778") // owolff: List instantiation is not a problem
    void shouldFailOnDifferentSizes() {
        assertThrows(AssertionError.class, () -> assertListsAreEqualIgnoringOrder("propertyName",
                immutableList("a", "b", "b"), immutableList("a", "b")));
    }

    @Test
    void shouldPassSameList() {
        final var list = Generators.asCollectionGenerator(Generators.strings()).list();
        assertListsAreEqualIgnoringOrder("propertyName", list, list);
    }

    @Test
    void shouldPassOnListWithDifferetnOrder() {
        final var list = Generators.asCollectionGenerator(Generators.strings()).list();
        final List<String> list2 = new ArrayList<>(list);
        Collections.shuffle(list2);
        assertListsAreEqualIgnoringOrder("propertyName", list, list2);
        assertListsAreEqualIgnoringOrder("propertyName", list2, list);
    }

    @Test
    void shouldFailWithNonEqualContent() {
        final List<String> list1 = immutableList("a", "b", "b");
        final List<String> list2 = immutableList("a", "b", "c");
        assertThrows(AssertionError.class, () -> assertListsAreEqualIgnoringOrder("propertyName", list1, list2));
    }

    @Test
    void shouldFailWithNonEqualContentReverse() {
        final List<String> list1 = immutableList("a", "b", "b");
        final List<String> list2 = immutableList("a", "b", "c");
        assertThrows(AssertionError.class, () -> assertListsAreEqualIgnoringOrder("propertyName", list2, list1));
    }
}
