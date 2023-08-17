/**
 * <h2>API</h2>
 * <ul>
 * <li>See {@link de.cuioss.test.valueobjects.api} for the documentation of the
 * api</li>
 * <li>See {@link de.cuioss.test.valueobjects.ValueObjectTest} as entry point
 * for the actual tests.</li>
 * </ul>
 * <h2>Examples</h2>
 * <h3>Minimum Bean Test</h3>
 *
 * <pre>
 * <code>
 * &#64;VerifyBeanProperty
 * class SomeBeanTest extends ValueObjectTest&lt;SomeBean&gt;{...}</code>
 * </pre>
 *
 * <h3>Two constructors but skip {@link java.lang.Object#toString()} test</h3>
 *
 * <pre>
 * <code>
 * &#64;VerifyConstructor(of = "name", required = "name")
 * &#64;VerifyConstructor(of = { "name", "number" }, required = { "name", "number" })
 * &#64;VetoObjectTestContract(ObjectTestContracts.TO_STRING)
 * class SomeBeanTest extends ValueObjectTest&lt;SomeBean&gt;{...}</code>
 * </pre>
 *
 * <h3>Factory method skip property-scanning, use own properties</h3>
 * <p>
 * In some cases the reflection-based scanning fails, usually if the objects in
 * hand do not provide getter methods. In this case you can skip the scanning
 * and add the properties manually. The required flag is defined at attribute
 * level and will therefore inherited by the test.
 * </p>
 *
 * <pre>
 * <code>
 * &#64;PropertyReflectionConfig(skip = true)
 * &#64;PropertyConfig(name = "name", propertyClass = String.class, required = true)
 * &#64;PropertyConfig(name = "number", propertyClass = Integer.class, required = true)
 * &#64;VerifyFactoryMethod(of = { "name", "number" })
 * class SomeBeanTest extends ValueObjectTest&lt;SomeBean&gt;{...}</code>
 * </pre>
 *
 * <h3>Builder using
 * {@link de.cuioss.test.valueobjects.api.generator.PropertyGeneratorHint}</h3>
 *
 * <pre>
 * <code>
 * &#64;PropertyGeneratorHint(declaredType = Serializable.class, implementationType = String.class)
 * &#64VerifyBuilder
 * class SomeBeanTest extends ValueObjectTest&lt;SomeBean&gt;{...}</code>
 * </pre>
 *
 * <h3>Builder using
 * {@link de.cuioss.test.valueobjects.api.generator.PropertyGenerator}</h3>
 * <p>
 * Synthetic example because
 * {@link de.cuioss.test.generator.impl.FloatObjectGenerator} is part of the
 * default generators that are already available
 * </p>
 *
 * <pre>
 * <code>
 * &#64;PropertyGenerator(FloatObjectGenerator.class)
 * &#64VerifyBuilder
 * class SomeBeanTest extends ValueObjectTest&lt;SomeBean&gt;{...}</code>
 * </pre>
 *
 * <h3>Hardcore internal example that covers many corner cases</h3>
 *
 * <pre>
 * <code>
 * &#64;VetoObjectTestContract(ObjectTestContracts.SERIALIZABLE)
 * &#64;VerifyBuilder
 * &#64;PropertyReflectionConfig(required = { "name", "generator", "propertyClass" },
 *         defaultValued = { "collectionType", "propertyMemberInfo", "propertyReadWrite", "propertyAccessStrategy" })
 * &#64;ObjectTestConfig(equalsAndHashCodeExclude = "generator")
 * class PropertyMetadataImplTest extends ValueObjectTest&lt;PropertyMetadataImpl&gt; {...}</code>
 * </pre>
 *
 */
package de.cuioss.test.valueobjects;
