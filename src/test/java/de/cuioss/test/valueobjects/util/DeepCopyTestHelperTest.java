/*
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
package de.cuioss.test.valueobjects.util;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.generator.impl.CollectionGenerator;
import lombok.*;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static de.cuioss.test.generator.Generators.*;
import static org.junit.jupiter.api.Assertions.*;

class DeepCopyTestHelperTest {

    private final CollectionGenerator<String> lists = Generators.asCollectionGenerator(nonEmptyStrings());

    @Test
    void shouldHandleHappyCase() {
        var a = any();
        // Create a new Date with the same time but ensure it's a different instance
        var copiedDate = new Date();
        copiedDate.setTime(a.date.getTime());
        var b = new TestClass(a.readOnly, a.readWrite, copiedDate, List.copyOf(a.getList()));

        // Ignore the date property during deep copy verification due to Java 21+ internal Date field sharing
        var ignoreProperties = Set.of("date");
        DeepCopyTestHelper.verifyDeepCopy(a, b, ignoreProperties);

        // Manually verify that dates are equal but different instances
        assertEquals(a.date, b.date);
        assertNotSame(a.date, b.date);

        // Symmetry
        DeepCopyTestHelper.verifyDeepCopy(b, a, ignoreProperties);
    }

    @Test
    void shouldDetectShallowCopyOnDateAttribute() {
        var a = any();
        var b = new TestClass(a.readOnly, a.readWrite, a.date, a.getList());
        assertThrows(AssertionError.class, () -> DeepCopyTestHelper.verifyDeepCopy(a, b));
    }

    @Test
    void shouldDetectSameObject() {
        var a = any();
        assertThrows(AssertionError.class, () -> DeepCopyTestHelper.verifyDeepCopy(a, a));
    }

    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    static class TestClass {

        @Getter
        private final String readOnly;

        @Getter
        @Setter
        private String readWrite;

        @Getter
        @Setter
        // Needed Mutable in order to test shallow representation
        private Date date;

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
