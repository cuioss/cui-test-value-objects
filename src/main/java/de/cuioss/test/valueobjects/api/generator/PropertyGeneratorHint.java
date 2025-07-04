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
package de.cuioss.test.valueobjects.api.generator;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.io.Serializable;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


import de.cuioss.test.generator.TypedGenerator;

/**
 * For some cases it is simpler to give a hint for a certain type instead of
 * declaring {@link PropertyGenerator}. e.g. Your class to be tested needs
 * instances of certain Interface that need to be {@link Serializable} but this
 * is not declared at interface level. Therefore this hint may suffice in case
 * you provide a concrete implementation that can be instantiated. The
 * test-framework will than always use a {@link TypedGenerator} for all the
 * tests.
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(PropertyGeneratorHints.class)
public @interface PropertyGeneratorHint {

    /**
     * @return The type of object that are declared at field and or method /
     *         constructor level
     */
    Class<?> declaredType();

    /**
     * @return the actual implementation type to be used for the
     *         {@link #declaredType()}. It should provide a publicly accessible
     *         constructor
     */
    Class<?> implementationType();

}
