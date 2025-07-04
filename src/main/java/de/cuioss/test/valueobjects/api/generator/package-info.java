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
/**
 * Provides annotations for adding / modifying existing
 * {@link de.cuioss.test.generator.TypedGenerator}. This is useful if the
 * dynamically created generators won't fit. This is sometimes the case if an
 * attribute declaration uses an interfaces or abstract-class. Although the
 * generator-system is capable of providing generator capable of creating proxy
 * objects this won't suffice for some needs, because the proxies are not
 * {@link java.io.Serializable} and not compliant to standard
 * {@link java.lang.Object#equals(Object)} and
 * {@link java.lang.Object#hashCode()} contracts. There are two ways to
 * configure the generator
 * <ul>
 * <li>{@link de.cuioss.test.valueobjects.api.generator.PropertyGeneratorHint}:
 * Actually hints the generator system which concrete implementation type to use
 * for a declaredType:
 * {@code @PropertyGeneratorHint(declaredType = Serializable.class, implementationType = Integer.class)}
 * defines that each {@link java.io.Serializable} should be an
 * {@link java.lang.Integer} <em>Caution</em>: The implementationType should
 * always provide an accessible Constructor. Factory or builder methods for
 * object-creation will not be picked up at this level of configuration.</li>
 * <li>{@link de.cuioss.test.valueobjects.api.generator.PropertyGenerator}:
 * Defines explicitly an implementation of
 * {@link de.cuioss.test.generator.TypedGenerator} to be used.</li>
 * </ul>
 */
package de.cuioss.test.valueobjects.api.generator;
