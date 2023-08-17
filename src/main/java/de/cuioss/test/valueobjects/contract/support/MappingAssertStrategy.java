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
package de.cuioss.test.valueobjects.contract.support;

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.cuioss.test.valueobjects.api.VerifyMapperConfiguration;
import de.cuioss.test.valueobjects.property.PropertySupport;

/**
 * Defines different assert Strategies
 *
 * @author Oliver Wolff
 *
 */
enum MappingAssertStrategy {

    EQUALS {

        @Override
        public void assertMapping(PropertySupport sourceProperty, Object sourceObject, PropertySupport targetProperty,
                Object targetObject) {
            targetProperty.assertValueSet(targetObject, sourceProperty.readProperty(sourceObject));
        }

        @Override
        public List<MappingTuple> readConfiguration(VerifyMapperConfiguration config) {
            return mutableList(config.equals()).stream().map(mapping -> new MappingTuple(mapping, this)).toList();
        }

    },
    NOT_NULL {

        @Override
        public void assertMapping(PropertySupport sourceProperty, Object sourceObject, PropertySupport targetProperty,
                Object targetObject) {
            var readProperty = targetProperty.readProperty(targetObject);
            assertNotNull(readProperty, "The given object must not be null " + targetProperty.getPropertyMetadata());
            if (readProperty instanceof Collection<?> collection) {
                assertFalse(collection.isEmpty(),
                        "The given object is not null but an empty collection" + targetProperty.getPropertyMetadata());
            }
        }

        @Override
        public List<MappingTuple> readConfiguration(VerifyMapperConfiguration config) {
            return mutableList(config.notNullNorEmpty()).stream().map(mapping -> new MappingTuple(mapping, this))
                    .toList();
        }

    },
    /**
     * This Strategy is used to check whether the concrete value is null in case it
     * is not a property with a default-value. In case of a {@link Collection} it
     * emptiness is considered as default as well.
     */
    NULL_OR_DEFAULT {

        @Override
        public void assertMapping(PropertySupport sourceProperty, Object sourceObject, PropertySupport targetProperty,
                Object targetObject) {
            if (targetProperty.isDefaultValue()) {
                return;
            }
            var read = targetProperty.readProperty(targetObject);
            if (read instanceof Collection<?> collection) {
                assertTrue(collection.isEmpty(),
                        "The given object is not null nor an empty collection" + targetProperty.getPropertyMetadata());
            } else {
                assertNull(read, "The given object must be null " + targetProperty.getPropertyMetadata());
            }

        }

        @Override
        public List<MappingTuple> readConfiguration(VerifyMapperConfiguration config) {
            return Collections.emptyList();
        }
    };

    public abstract void assertMapping(PropertySupport sourceProperty, Object sourceObject,
            PropertySupport targetProperty, Object targetObject);

    public abstract List<MappingTuple> readConfiguration(VerifyMapperConfiguration config);

}
