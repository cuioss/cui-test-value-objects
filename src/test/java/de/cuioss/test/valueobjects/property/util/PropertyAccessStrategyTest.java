/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.property.util;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.testbeans.property.BeanWithFluentSetter;
import de.cuioss.test.valueobjects.testbeans.property.BeanWithReadWriteProperties;
import org.junit.jupiter.api.Test;

import static de.cuioss.test.generator.Generators.nonEmptyStrings;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropertyAccessStrategyTest {

    @Test
    @SuppressWarnings("java:S5778")
    // owolff
    void shouldFailToReadWriteOnlyProperty() {
        assertThrows(AssertionError.class, () -> PropertyAccessStrategy.BEAN_PROPERTY
            .readProperty(new BeanWithReadWriteProperties(), BeanWithReadWriteProperties.METATDATA_WRITE_ONLY));
    }

    @Test
    @SuppressWarnings("java:S5778")
    // owolff
    void shouldFailToWriteNonIterablePropertyToBuilderCollection() {
        assertThrows(AssertionError.class,
            () -> PropertyAccessStrategy.BUILDER_COLLECTION_AND_SINGLE_ELEMENT.writeProperty(
                new BeanWithReadWriteProperties(), BeanWithReadWriteProperties.METATDATA_WRITE_ONLY,
                Generators.nonEmptyStrings().next()));
    }

    @Test
    void shouldWriteFluentSetter() {
        var bean = new BeanWithFluentSetter();
        var next = nonEmptyStrings().next();
        PropertyAccessStrategy.BEAN_PROPERTY.writeProperty(bean, BeanWithFluentSetter.METATDATA, next);
        assertEquals(next, bean.getField());
    }

}
