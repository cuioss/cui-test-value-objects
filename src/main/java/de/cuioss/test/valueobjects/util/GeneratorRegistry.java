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
package de.cuioss.test.valueobjects.util;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.api.generator.PropertyGenerator;
import de.cuioss.test.valueobjects.api.property.PropertyConfig;
import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.property.PropertyMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface providing the capability for handling the
 * {@link TypedGeneratorRegistry} programmatically. Implementors of this
 * interface, usually the base test-classes, are supposed to take care about the
 * runtime handling of the {@link TypedGeneratorRegistry} The runtime handling
 * consists of three steps
 * <ul>
 * <li>Initialize {@link TypedGeneratorRegistry} with the basic Java-types, by
 * calling {@link TypedGeneratorRegistry#registerBasicTypes()} usually called
 * before all tests.</li>
 * <li>Pick up all additional configured {@link TypedGenerator} and register
 * them to the {@link TypedGeneratorRegistry}, see Configuration</li>
 * <li>Tear down / clear {@link TypedGeneratorRegistry} by calling
 * {@link TypedGeneratorRegistry#clear()} using called in the context after all
 * tests.</li>
 * </ul>
 * <h2>Configuration</h2>
 * <ul>
 * <li>{@link PropertyMetadata}: The actual property values are generated using
 * {@link TypedGenerator}s, see {@link PropertyConfig#generator()} . They can be
 * tweaked in multiple ways, see
 * {@link de.cuioss.test.valueobjects.api.generator} for details</li>
 * <li>In case the actual test-class implements {@link TypedGenerator} itself it
 * will implicitly registered as {@link TypedGenerator} at
 * {@link TypedGeneratorRegistry}</li>
 * <li>In cases you can not use the {@link PropertyGenerator} annotation, e.g.
 * if you have instances of {@link TypedGenerator} but not types you can
 * override {@link #registerAdditionalGenerators()}.</li>
 * </ul>
 *
 * @author Oliver Wolff
 */
public interface GeneratorRegistry {

    /**
     * Callback method for registering additional generator, in cases you can not
     * use the {@link PropertyGenerator} annotation, e.g. if you have instances of
     * {@link TypedGenerator} but not types. The default implementation solely
     * return a mutable empty {@link List}, must therefore not be considered using
     * super
     *
     * @return A list of {@link TypedGenerator}s to be registered
     */
    @SuppressWarnings("squid:S1452") // owolff: No type information available at this level,
    // therefore the wildcard is needed
    default List<TypedGenerator<?>> registerAdditionalGenerators() {
        return new ArrayList<>();
    }

}
