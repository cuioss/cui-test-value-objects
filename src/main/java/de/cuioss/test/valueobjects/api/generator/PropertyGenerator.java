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
package de.cuioss.test.valueobjects.api.generator;

import de.cuioss.test.generator.TypedGenerator;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used for adding an additional {@link TypedGenerator} for the actual
 * test-class. This can be done defining one or more {@link TypedGenerator} as
 * class, see {@link #value()}.
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(PropertyGenerators.class)
public @interface PropertyGenerator {

    /**
     * @return one or an array of {@link TypedGenerator}. This is the standard usage
     *         for this annotation.
     */
    @SuppressWarnings("java:S1452") Class<? extends TypedGenerator<?>>[] value() default {};

}
