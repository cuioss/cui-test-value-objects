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
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.PropertySupport;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Delegate to {@link ParameterizedInstantiator} that calls
 * {@link ConfigurationCallBackHandler#configure(Object)} after creation of the
 * object.
 *
 * @author Oliver Wolff
 * @param <T> identifying the type of the objects to be instantiated
 */
@RequiredArgsConstructor
@ToString(of = "parameterizedInstantiator")
public class CallbackAwareInstantiator<T> implements ParameterizedInstantiator<T> {

    private final ParameterizedInstantiator<T> parameterizedInstantiator;
    private final ConfigurationCallBackHandler<T> callBackHandler;

    @Override
    public T newInstance(final List<PropertySupport> properties, final boolean generatePropertyValues) {
        final var instance = parameterizedInstantiator.newInstance(properties, generatePropertyValues);
        callBackHandler.configure(instance);
        return instance;
    }

    @Override
    public T newInstance(final List<PropertyMetadata> properties) {
        final var instance = parameterizedInstantiator.newInstance(properties);
        callBackHandler.configure(instance);
        return instance;
    }

    @Override
    public T newInstanceMinimal() {
        final var instance = parameterizedInstantiator.newInstanceMinimal();
        callBackHandler.configure(instance);
        return instance;
    }

    @Override
    public T newInstanceFull() {
        final var instance = parameterizedInstantiator.newInstanceFull();
        callBackHandler.configure(instance);
        return instance;
    }

    @Override
    public RuntimeProperties getRuntimeProperties() {
        return parameterizedInstantiator.getRuntimeProperties();
    }

}
