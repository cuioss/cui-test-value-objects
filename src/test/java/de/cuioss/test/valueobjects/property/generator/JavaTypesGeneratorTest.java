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
package de.cuioss.test.valueobjects.property.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Serializable;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.generator.JavaTypesGenerator;

class JavaTypesGeneratorTest {

    @Test
    void shouldProvideStringGeneratorBuilder() {
        final var stringGenerator = JavaTypesGenerator.STRINGS.metadataBuilder("strings");
        assertNotNull(stringGenerator);
        assertEquals(String.class, stringGenerator.build().getPropertyClass());
    }

    @Test
    void shouldProvideStringGenerator() {
        final var stringGenerator = JavaTypesGenerator.STRINGS.metadata("strings");
        assertNotNull(stringGenerator);
        assertEquals(String.class, stringGenerator.getPropertyClass());
    }

    @Test
    void shouldFailOnEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> JavaTypesGenerator.STRINGS.metadataBuilder(""));
    }

    @Test
    void shouldProvideObjectGenerator() {
        assertEquals(Boolean.class, JavaTypesGenerator.BOOLEANS.metadata("booleans").getPropertyClass());
        assertEquals(Byte.class, JavaTypesGenerator.BYTES.metadata("bytes").getPropertyClass());
        assertEquals(Character.class, JavaTypesGenerator.CHARACTERS.metadata("character").getPropertyClass());
        assertEquals(Double.class, JavaTypesGenerator.DOUBLES.metadata("doubles").getPropertyClass());
        assertEquals(Float.class, JavaTypesGenerator.FLOATS.metadata("floats").getPropertyClass());
        assertEquals(Integer.class, JavaTypesGenerator.INTEGERS.metadata("integers").getPropertyClass());
        assertEquals(Locale.class, JavaTypesGenerator.LOCALES.metadata("locales").getPropertyClass());
        assertEquals(Long.class, JavaTypesGenerator.LONGS.metadata("longs").getPropertyClass());
        assertEquals(Serializable.class, JavaTypesGenerator.SERIALIZABLES.metadata("serializable").getPropertyClass());
        assertEquals(Short.class, JavaTypesGenerator.SHORTS.metadata("shorts").getPropertyClass());
        assertEquals(TimeZone.class, JavaTypesGenerator.TIME_ZONES.metadata("timezones").getPropertyClass());
    }

    @Test
    void shouldProvidePimitiveGenerator() {
        assertEquals(boolean.class, JavaTypesGenerator.BOOLEANS_PRIMITIVE.metadata("booleans").getPropertyClass());
        assertEquals(byte.class, JavaTypesGenerator.BYTES_PRIMITIVE.metadata("bytes").getPropertyClass());
        assertEquals(char.class, JavaTypesGenerator.CHARACTERS_PRIMITIVE.metadata("character").getPropertyClass());
        assertEquals(double.class, JavaTypesGenerator.DOUBLES_PRIMITIVE.metadata("doubles").getPropertyClass());
        assertEquals(float.class, JavaTypesGenerator.FLOATS_PRIMITIVE.metadata("floats").getPropertyClass());
        assertEquals(int.class, JavaTypesGenerator.INTEGERS_PRIMITIVE.metadata("integers").getPropertyClass());
        assertEquals(long.class, JavaTypesGenerator.LONGS_PRIMITIVE.metadata("longs").getPropertyClass());
        assertEquals(short.class, JavaTypesGenerator.SHORTS_PRIMITIVE.metadata("shorts").getPropertyClass());
    }
}
