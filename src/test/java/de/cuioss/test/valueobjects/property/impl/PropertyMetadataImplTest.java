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
package de.cuioss.test.valueobjects.property.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;
import java.util.SortedSet;

import org.junit.jupiter.api.Test;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyBuilder;
import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import de.cuioss.test.valueobjects.api.object.ObjectTestContracts;
import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;
import de.cuioss.test.valueobjects.api.property.PropertyReflectionConfig;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.util.CollectionType;

@VetoObjectTestContract(ObjectTestContracts.SERIALIZABLE)
@VerifyBuilder
@PropertyReflectionConfig(required = { "name", "generator", "propertyClass" }, defaultValued = { "collectionType",
        "propertyMemberInfo", "propertyReadWrite", "propertyAccessStrategy", "assertionStrategy" })
@ObjectTestConfig(equalsAndHashCodeExclude = "generator")
class PropertyMetadataImplTest extends ValueObjectTest<PropertyMetadataImpl> {

    private final TypedGenerator<String> names = Generators.letterStrings();

    @Test
    void shouldFailOnEmptyBuilder() {
        var builder = PropertyMetadataImpl.builder();
        assertThrows(NullPointerException.class, () -> builder.build());
    }

    @Test
    void shouldFailOnMisssingGenerator() {
        var builder = PropertyMetadataImpl.builder().name(names.next()).propertyClass(String.class);
        assertThrows(NullPointerException.class, () -> builder.build());
    }

    @Test
    void shouldFailOnMisssingName() {
        var builder = PropertyMetadataImpl.builder().generator(names);
        assertThrows(NullPointerException.class, () -> builder.build());
    }

    @Test
    void shouldBuildMinimal() {
        final var propertyName = names.next();
        final PropertyMetadata meta = PropertyMetadataImpl.builder().generator(names).name(propertyName)
                .propertyClass(String.class).build();
        assertNotNull(meta);
        assertEquals(propertyName, meta.getName());
        assertEquals(names, meta.getGenerator());
        assertEquals(String.class, meta.getPropertyClass());
        assertFalse(meta.isDefaultValue());
        assertFalse(meta.isRequired());
    }

    @Test
    void shouldBuildWithPropertyValueGenerator() {
        final var propertyName = names.next();
        final PropertyMetadata support = PropertyMetadataImpl.builder().generator(Generators.locales())
                .name(propertyName).build();
        assertNotNull(support);
        assertEquals(Locale.class, support.getPropertyClass());
    }

    @Test
    void shouldCopyFrom() {
        final PropertyMetadata template = anyValueObject();
        final PropertyMetadata copy = PropertyMetadataImpl.builder(template).build();
        assertEquals(template, copy);
    }

    @Test
    void shouldHandleArrays() {
        final PropertyMetadata meta = PropertyMetadataImpl.builder().generator(Generators.bytes())
                .collectionType(CollectionType.ARRAY_MARKER).name(names.next()).propertyClass(String.class).build();
        assertTrue(meta.next().getClass().isArray());
        assertEquals(new String[0].getClass(), meta.resolveActualClass());
    }

    @Test
    void shouldHandlePrimitiveByteArrays() {
        final PropertyMetadata meta = PropertyMetadataImpl.builder().generator(Generators.bytes())
                .collectionType(CollectionType.ARRAY_MARKER).name(names.next()).propertyClass(byte.class).build();
        assertTrue(meta.next().getClass().isArray());
        final Class<? extends byte[]> byteArrayClass = new byte[0].getClass();
        assertEquals(byteArrayClass, meta.resolveActualClass());
        final var castArray = (byte[]) meta.next();
        assertNotNull(castArray);
    }

    @Test
    void shouldHandleCollections() {
        final PropertyMetadata meta = PropertyMetadataImpl.builder().generator(names)
                .collectionType(CollectionType.SORTED_SET).name(names.next()).propertyClass(String.class).build();
        assertTrue(SortedSet.class.isAssignableFrom(meta.next().getClass()));
    }
}
