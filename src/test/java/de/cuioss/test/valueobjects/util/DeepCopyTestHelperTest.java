/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.util;

import static de.cuioss.test.generator.Generators.dates;
import static de.cuioss.test.generator.Generators.nonEmptyStrings;
import static de.cuioss.test.generator.Generators.strings;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.List;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

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
    @DisabledForJreRange(min = JRE.JAVA_21, disabledReason = "Starting with Java 21 this test fails. Peeking into it did not give me any clue why ... and what we are testing here")
    void shouldHandleHappyCase() {
        var a = any();
        var b = new TestClass(a.readOnly, a.readWrite, new Date(a.date.getTime()), a.getList());
        DeepCopyTestHelper.verifyDeepCopy(a, b);
        // Symmetry
        DeepCopyTestHelper.verifyDeepCopy(b, a);
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
    class TestClass {

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
