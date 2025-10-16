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
 * Simple interface used for dynamically creating builder objects.
 *
 * @author Oliver Wolff
 * @param <T> identifying the type of object to be instantiated by the builder
 */
public interface BuilderInstantiator<T> {

    /**
     * @return a newly created builder.
     * @throws AssertionError in case the object could not be created
     */
    Object newBuilderInstance();

    /**
     * @return the class of the object that will be created by the contained
     *         builder.
     */
    Class<T> getTargetClass();

    /**
     * @return the class of the builder.
     */
    Class<?> getBuilderClass();

    /**
     * Actually builds the target object
     *
     * @param builder
     * @return the Object created by the contained builder
     */
    T build(Object builder);

}
