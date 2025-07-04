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
package de.cuioss.test.valueobjects.objects.impl;

import de.cuioss.test.valueobjects.objects.BuilderInstantiator;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.PropertySupport;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static de.cuioss.test.valueobjects.objects.impl.AbstractInlineInstantiator.PROPERTIES_MUST_NOT_BE_NULL;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Oliver Wolff
 * @param <T> identifying the type of object to be instantiated
 */
@RequiredArgsConstructor
public class BuilderParameterizedInstantiator<T> implements ParameterizedInstantiator<T> {

    @NonNull
    private final BuilderInstantiator<T> instantiator;

    @NonNull
    @Getter
    private final RuntimeProperties runtimeProperties;

    @Override
    public T newInstance(final List<PropertySupport> properties, final boolean generatePropertyValues) {
        assertNotNull(properties, PROPERTIES_MUST_NOT_BE_NULL);

        final var builder = instantiator.newBuilderInstance();

        if (generatePropertyValues) {
            properties.forEach(PropertySupport::generateTestValue);
        }
        for (final PropertySupport propertySupport : properties) {
            propertySupport.apply(builder);
        }
        return instantiator.build(builder);
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
    public T newInstanceFull() {
        return newInstance(runtimeProperties.getAllProperties());
    }

    @Override
    public String toString() {
        return getClass().getName() + "\nInstantiator: " + instantiator + runtimeProperties;
    }

}
