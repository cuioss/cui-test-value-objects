package io.cui.test.valueobjects.api.contracts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

import io.cui.test.valueobjects.api.property.PropertyBuilderConfig;
import io.cui.test.valueobjects.api.property.PropertyConfig;
import io.cui.test.valueobjects.contract.BuilderContractImpl;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.impl.PropertyMetadataImpl;
import io.cui.test.valueobjects.property.impl.PropertyMetadataImpl.PropertyMetadataBuilder;
import io.cui.test.valueobjects.property.util.AssertionStrategy;
import io.cui.tools.property.PropertyMemberInfo;
import io.cui.tools.property.PropertyReadWrite;

/**
 * If used on ValueObjectTest this test checks / tests builder of value objects.
 * <p>
 * As default the test assumes a static factory method with the name "builder", e.g.
 * {@link PropertyMetadataImpl#builder()} being present on the
 * type to be tested. This can be controlled using {@link #builderFactoryMethodName()},
 * {@link #builderFactoryProvidingClass()} and {@link #builderMethodName()}. In case you have a
 * builder that can be instantiated directly by using a no-args public constructor directly you can
 * use {@link #builderClass()} instead.
 * </p>
 * <p>
 * The builder methods for the individual properties are assumed to use the property-name only as
 * method-name, e.g. {@link PropertyMetadataBuilder#propertyClass(Class)}. In case you prefer a
 * prefix you can either configure it using {@link #methodPrefix()} or can do some more fine grained
 * configuration with {@link PropertyBuilderConfig}
 * </p>
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface VerifyBuilder {

    /**
     * @return the name of the factory method for creating the builder instances. It is assumed that
     *         it is parameter-free static method. If not set it defaults to
     *         {@value BuilderContractImpl#DEFAULT_BUILDER_FACTORY_METHOD_NAME}
     */
    String builderFactoryMethodName() default BuilderContractImpl.DEFAULT_BUILDER_FACTORY_METHOD_NAME;

    /**
     * @return the name of the build method on the {@link #builderClass()}. It is assumed that
     *         it is parameter-free method on the builderType and returns instances
     *         of the type on unit class level of ValueObjectTest. Defaults to
     *         {@value BuilderContractImpl#DEFAULT_BUILD_METHOD_NAME}
     */
    String builderMethodName() default BuilderContractImpl.DEFAULT_BUILD_METHOD_NAME;

    /**
     * This attribute defines the actual type of the concrete builder. The default value
     * {@link VerifyBuilder} acts as {@code null} value. If it is set it will be used instead of any
     * factory. It is assumed to have a no args public constructor.
     *
     * @return the builder-class
     */
    Class<?> builderClass() default VerifyBuilder.class;

    /**
     * Optional attribute. The default assumes that the factory method for creating the concrete
     * builder is located at the object under test. This method is only necessary if it is located
     * on another type, e.g the actual unit-test-class. The default value
     * {@link VerifyBuilder} acts as null value
     *
     * @return the type where the builderFactoryMethod is defined, the name of this method is
     *         defined within {@link #builderFactoryMethodName()}
     */
    Class<?> builderFactoryProvidingClass() default VerifyBuilder.class;

    /**
     * @return a number of properties, identified by their names that are not to be considered for
     *         this test: black-list
     */
    String[] exclude() default {};

    /**
     * @return a number of properties, identified by their names that are to be considered for
     *         this test: white-list
     */
    String[] of() default {};

    /**
     * @return a number of properties, identified by their names that are to be treated as required
     *         properties, see {@link PropertyMetadata#isRequired()}
     */
    String[] required() default {};

    /**
     * @return a number of properties, identified by their names that are to be treated as transient
     *         properties, see {@link PropertyMemberInfo#TRANSIENT}
     */
    String[] transientProperties() default {};

    /**
     * @return a number of properties, identified by their names that are to be treated as having a
     *         default values, see {@link PropertyMetadata#isDefaultValue()}
     */
    String[] defaultValued() default {};

    /**
     * @return a number of properties, identified by their names that are to be treated as being
     *         read-only, see {@link PropertyReadWrite#READ_ONLY}, usually used in conjunction with
     *         {@link #defaultValued()}
     */
    String[] readOnly() default {};

    /**
     * @return a number of properties, identified by their names that are to be treated as being
     *         write-only, see {@link PropertyReadWrite#WRITE_ONLY}, usually used in cases where a
     *         property to be written will result in other properties but itself can not be accessed
     *         directly
     */
    String[] writeOnly() default {};

    /**
     * @return a number of properties, identified by their names representing at least a
     *         {@link Collection} that are to be asserted ignoring the concrete order, see
     *         {@link PropertyConfig#assertionStrategy()} and
     *         {@link AssertionStrategy#COLLECTION_IGNORE_ORDER}. The default implementation will
     *         always respect / assert the same order of elements.
     */
    String[] assertUnorderedCollection() default {};

    /**
     * In case methodPrefix is not set the corresponding build method to be accessed for setting the
     * value is the name of the attribute: propertyName(), in case it is a concrete value, e.g.
     * 'with' it will taken into account: withPropertName().
     *
     * @return the method prefix, defaults to empty string
     */
    String methodPrefix() default "";

}
