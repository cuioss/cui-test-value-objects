/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.objects.impl;

import de.cuioss.test.valueobjects.objects.ConfigurationCallBackHandler;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.TestObjectProvider;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.PropertySupport;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

import static de.cuioss.test.valueobjects.objects.impl.AbstractInlineInstantiator.PROPERTIES_MUST_NOT_BE_NULL;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Variant of {@link ParameterizedInstantiator} that deals with test, where the
 * actual beans to be tested are Injected by cdi, see {@link TestObjectProvider}
 * as well
 *
 * @param <T> identifying the type to be tested is usually but not necessarily
 *            at least {@link Serializable}.
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class InjectedBeanInstantiator<T> implements ParameterizedInstantiator<T> {

    private final TestObjectProvider<T> objectProvider;

    private final ConfigurationCallBackHandler<T> callbackHandler;

    @Getter
    @NonNull
    private final RuntimeProperties runtimeProperties;

    @Override
    public T newInstance(final List<PropertySupport> properties, final boolean generatePropertyValues) {
        assertNotNull(properties, PROPERTIES_MUST_NOT_BE_NULL);
        final var instance = objectProvider.getUnderTest();
        assertNotNull(instance, "Unable to obtain instance from " + objectProvider);
        if (generatePropertyValues) {
            properties.forEach(PropertySupport::generateTestValue);
        }
        for (final PropertySupport propertySupport : properties) {
            propertySupport.apply(instance);
        }
        callbackHandler.configure(instance);
        return instance;
    }

    @Override
    public T newInstanceFull() {
        return newInstance(runtimeProperties.getAdditionalProperties());
    }

    @Override
    public T newInstance(final List<PropertyMetadata> properties) {
        assertNotNull(properties, PROPERTIES_MUST_NOT_BE_NULL);
        return newInstance(properties.stream().map(PropertySupport::new).toList(), true);
    }

    @Override
    public T newInstanceMinimal() {
        return newInstance(runtimeProperties.getRequiredProperties());
    }

    @Override
    public String toString() {
        return getClass().getName() + "\nProperty Configuration: " + runtimeProperties;
    }

}
