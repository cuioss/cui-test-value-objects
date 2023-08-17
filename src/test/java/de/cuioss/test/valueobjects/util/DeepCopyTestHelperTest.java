package de.cuioss.test.valueobjects.util;

import static de.cuioss.test.generator.Generators.dates;
import static de.cuioss.test.generator.Generators.nonEmptyStrings;
import static de.cuioss.test.generator.Generators.strings;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.generator.impl.CollectionGenerator;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

class DeepCopyTestHelperTest {

    private final CollectionGenerator<String> lists = Generators.asCollectionGenerator(nonEmptyStrings());

    @Test
    void shouldHandleHappyCase() {
        var a = any();
        var b = new TestClass(a.readOnly, a.readWrite, new Date(a.date.getTime()), a.getList());
        DeepCopyTestHelper.testDeepCopy(a, b);
        // Symmetry
        DeepCopyTestHelper.testDeepCopy(b, a);
    }

    @Test
    void shouldDetectShallowCopyOnDateAttribute() {
        var a = any();
        var b = new TestClass(a.readOnly, a.readWrite, a.date, a.getList());
        assertThrows(AssertionError.class, () -> DeepCopyTestHelper.testDeepCopy(a, b));
    }

    @Test
    void shouldDetectSameObject() {
        var a = any();
        assertThrows(AssertionError.class, () -> DeepCopyTestHelper.testDeepCopy(a, a));
    }

    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    class TestClass {

        @Getter
        private final String readOnly;

        @Getter
        @Setter
        private String readWrite;

        @Getter
        @Setter
        private Date // Needed Mutable in order to test shallow representation
        date;

        @Getter
        private final List<String> list;
    }

    public TestClass any() {
        var date = dates().next();
        var aString = strings().next();
        var bString = strings().next();
        var stringList = lists.list();
        return new TestClass(aString, bString, date, stringList);
    }
}
