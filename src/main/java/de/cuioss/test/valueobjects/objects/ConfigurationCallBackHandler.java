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
package de.cuioss.test.valueobjects.objects;

/**
 * Defines a callback-method in case of further configuration of concrete
 * test-objects. After initialization and prior to testing the method
 * {@link #configure(Object)} will be called allowing the concrete test-class to
 * do some specific configuration.
 *
 * @author Oliver Wolff
 * @param <T> identifying the type to be configured
 */
public interface ConfigurationCallBackHandler<T> {

    /**
     * Callback method for configuring a concrete created test-object. The default
     * implementation is NOOP
     *
     * @param toBeConfigured must never be null
     */
    default void configure(final T toBeConfigured) {
    }

}
