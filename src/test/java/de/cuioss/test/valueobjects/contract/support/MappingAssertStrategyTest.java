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
package de.cuioss.test.valueobjects.contract.support;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.PropertySupport;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Exercises the {@link MappingAssertStrategy#NOT_NULL} and
 * {@link MappingAssertStrategy#NULL_OR_DEFAULT} strategies for both the scalar
 * and the {@link java.util.Collection} branch. The passing cases are sufficient
 * to execute the eagerly built assertion messages.
 */
class MappingAssertStrategyTest {

    public static final class Bean {
        private final Object scalar;
        private final List<String> collection;

        Bean(final Object scalar, final List<String> collection) {
            this.scalar = scalar;
            this.collection = collection;
        }

        public Object getScalar() {
            return scalar;
        }

        public List<String> getCollection() {
            return collection;
        }
    }

    private static PropertySupport support(final String name) {
        final PropertyMetadata metadata = PropertyMetadataImpl.builder().name(name).propertyClass(Object.class)
            .generator(Generators.nonEmptyStrings()).build();
        return new PropertySupport(metadata);
    }

    @Test
    void notNullShouldPassForScalarAndCollectionBranch() {
        final var scalarSupport = support("scalar");
        final var collectionSupport = support("collection");
        final var bean = new Bean("value", List.of("element"));

        assertDoesNotThrow(() -> MappingAssertStrategy.NOT_NULL.assertMapping(null, null, scalarSupport, bean));
        assertDoesNotThrow(() -> MappingAssertStrategy.NOT_NULL.assertMapping(null, null, collectionSupport, bean));
    }

    @Test
    void nullOrDefaultShouldPassForScalarAndCollectionBranch() {
        final var scalarSupport = support("scalar");
        final var collectionSupport = support("collection");
        final var bean = new Bean(null, List.of());

        assertDoesNotThrow(() -> MappingAssertStrategy.NULL_OR_DEFAULT.assertMapping(null, null, scalarSupport, bean));
        assertDoesNotThrow(
            () -> MappingAssertStrategy.NULL_OR_DEFAULT.assertMapping(null, null, collectionSupport, bean));
    }
}
