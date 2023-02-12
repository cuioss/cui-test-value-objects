/**
 * Provides annotations for configuring / tweaking the management of
 * {@link io.cui.test.valueobjects.property.PropertyMetadata} objects that represent the
 * individual properties to be used for testing.
 * <h2>Property Handling</h2>
 * <p>
 * The handling of properties is the core-aspect of testing value-objects. The individual properties
 * are represented by instances of {@link io.cui.test.valueobjects.property.PropertyMetadata}
 * </p>
 * <p>
 * The resolving of {@link io.cui.test.valueobjects.property.PropertyMetadata} consists of
 * three steps:
 * <ul>
 * <li>Initially the object to be tested will be scanned using Javas Bean Introspection for the
 * properties. This is done using
 * {@link io.cui.test.valueobjects.util.ReflectionHelper#scanBeanTypeForProperties(Object)}.
 * The scanning can be adjusted / skipped by using
 * {@link io.cui.test.valueobjects.api.property.PropertyReflectionConfig}.</li>
 * <li>The annotations {@link io.cui.test.valueobjects.api.property.PropertyConfig} and
 * {@link io.cui.test.valueobjects.api.property.PropertyBuilderConfig} will be evaluated. The
 * first two steps will be aggregated to a superset of
 * {@link io.cui.test.valueobjects.property.PropertyMetadata} to be used for all other
 * tests. The result will be logged at info-level in order to simplify the adjustment.</li>
 * <li>The individual tests, e.g.
 * {@link io.cui.test.valueobjects.api.contracts.VerifyBeanProperty} can further tweak the
 * properties for that test, e.g.
 * {@link io.cui.test.valueobjects.api.contracts.VerifyBeanProperty#readOnly()}</li>
 * <li>The same goes for the {@link java.lang.Object} contract tests, that can be adjusted using
 * {@link io.cui.test.valueobjects.api.object.ObjectTestConfig}</li>
 * </ul>
 * </p>
 *
 * @author Oliver Wolff
 */
package io.cui.test.valueobjects.api.property;
