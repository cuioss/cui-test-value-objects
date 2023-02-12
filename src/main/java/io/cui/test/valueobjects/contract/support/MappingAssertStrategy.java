package io.cui.test.valueobjects.contract.support;

import static io.cui.tools.collect.CollectionLiterals.mutableList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.cui.test.valueobjects.api.VerifyMapperConfiguration;
import io.cui.test.valueobjects.property.PropertySupport;

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
            return mutableList(config.equals()).stream().map(mapping -> new MappingTuple(mapping, this))
                    .collect(Collectors.toList());
        }

    },
    NOT_NULL {

        @Override
        public void assertMapping(PropertySupport sourceProperty, Object sourceObject, PropertySupport targetProperty,
                Object targetObject) {
            var readProperty = targetProperty.readProperty(targetObject);
            assertNotNull(readProperty,
                    "The given object must not be null " + targetProperty.getPropertyMetadata());
            if (readProperty instanceof Collection) {
                assertFalse(((Collection<?>) readProperty).isEmpty(),
                        "The given object is not null but an empty collection" + targetProperty.getPropertyMetadata());
            }
        }

        @Override
        public List<MappingTuple> readConfiguration(VerifyMapperConfiguration config) {
            return mutableList(config.notNullNorEmpty()).stream().map(mapping -> new MappingTuple(mapping, this))
                    .collect(Collectors.toList());
        }

    },
    /**
     * This Strategy is used to check whether the concrete value is null in case it is not a
     * property with a default-value. In case of a {@link Collection} it emptiness is considered as
     * default as well.
     */
    NULL_OR_DEFAULT {

        @Override
        public void assertMapping(PropertySupport sourceProperty, Object sourceObject, PropertySupport targetProperty,
                Object targetObject) {
            if (targetProperty.isDefaultValue()) {
                return;
            }
            var read = targetProperty.readProperty(targetObject);
            if (read instanceof Collection) {
                assertTrue(((Collection<?>) read).isEmpty(),
                        "The given object is not null nor an empty collection" + targetProperty.getPropertyMetadata());
            } else {
                assertNull(read,
                        "The given object must be null " + targetProperty.getPropertyMetadata());
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
