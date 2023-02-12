/**
 * Provides annotations for adding / modifying existing
 * {@link de.icw.cui.test.generator.TypedGenerator}. This is useful if
 * the dynamically created generators won't fit. This is sometimes the case if an attribute
 * declaration uses an interfaces or abstract-class. Although the generator-system is capable of
 * providing generator capable of creating proxy objects this won't suffice for some needs,
 * because the proxies are not {@link java.io.Serializable} and not compliant to standard
 * {@link java.lang.Object#equals(Object)} and {@link java.lang.Object#hashCode()} contracts.
 * There are two ways to configure the generator
 * <ul>
 * <li>{@link io.cui.test.valueobjects.api.generator.PropertyGeneratorHint}: Actually hints the
 * generator system which concrete implementation type to use for a declaredType:<br />
 * {@code @PropertyGeneratorHint(declaredType = Serializable.class, implementationType = Integer.class)}<br/>
 * defines that each {@link java.io.Serializable} should be an {@link java.lang.Integer}<br />
 * <em>Caution</em>: The implementationType should always provide an accessible Constructor. Factory
 * or builder methods for object-creation will not be picked up at this level of configuration.
 * </li>
 * <li>{@link io.cui.test.valueobjects.api.generator.PropertyGenerator}: Defines explicitly an
 * implementation of {@link de.icw.cui.test.generator.TypedGenerator} to be used.</li>
 * </ul>
 */
package io.cui.test.valueobjects.api.generator;
