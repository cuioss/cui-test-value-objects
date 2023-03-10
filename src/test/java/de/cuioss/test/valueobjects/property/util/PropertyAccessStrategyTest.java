package de.cuioss.test.valueobjects.property.util;

import static de.cuioss.test.generator.Generators.nonEmptyStrings;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.testbeans.property.BeanWithFluentSetter;
import de.cuioss.test.valueobjects.testbeans.property.BeanWithReadWriteProperties;

class PropertyAccessStrategyTest {

    @Test
    @SuppressWarnings("java:S5778") // owolff
    void shouldFailToReadWriteOnlyProperty() {
        assertThrows(AssertionError.class,
                () -> PropertyAccessStrategy.BEAN_PROPERTY.readProperty(new BeanWithReadWriteProperties(),
                        BeanWithReadWriteProperties.METATDATA_WRITE_ONLY));
    }

    @Test
    @SuppressWarnings("java:S5778") // owolff
    void shouldFailToWriteNonIterablePropertyToBuilderCollection() {
        assertThrows(AssertionError.class,
                () -> PropertyAccessStrategy.BUILDER_COLLECTION_AND_SINGLE_ELEMENT.writeProperty(
                        new BeanWithReadWriteProperties(),
                        BeanWithReadWriteProperties.METATDATA_WRITE_ONLY,
                        Generators.nonEmptyStrings().next()));
    }

    @Test
    void shouldWriteFluentSetter() {
        var bean = new BeanWithFluentSetter();
        var next = nonEmptyStrings().next();
        PropertyAccessStrategy.BEAN_PROPERTY.writeProperty(bean, BeanWithFluentSetter.METATDATA,
                next);
        assertEquals(next, bean.getField());
    }

}
