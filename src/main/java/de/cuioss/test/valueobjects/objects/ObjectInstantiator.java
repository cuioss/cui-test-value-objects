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
package de.cuioss.test.valueobjects.objects;

import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;

/**
 * An {@link ObjectInstantiator} creates objects without further parameter, e.g.
 * the {@link DefaultInstantiator} uses the Default-Constructor
 *
 * @author Oliver Wolff
 * @param <T>
 */
public interface ObjectInstantiator<T> {

    /**
     * @return a newly created instance
     */
    T newInstance();

    /**
     * @return the type of object to be generated
     */
    Class<T> getTargetClass();

}
