package io.cui.test.valueobjects.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

import io.cui.test.valueobjects.BaseMapperTest;
import io.cui.test.valueobjects.api.property.PropertyConfig;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.util.AssertionStrategy;
import io.cui.tools.property.PropertyReadWrite;

/**
 * To be used in conjunction with {@link BaseMapperTest}. Defines the mapper-specific definitions
 * that will be tested.
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface VerifyMapperConfiguration {

    /**
     * @return identifies the properties that can be asserted using {@link Object#equals(Object)}.
     *         The format is 'source.attributeName:target.attributeName'. The target.attributeName
     *         and source.attributeName must refer to an already defined {@link PropertyMetadata}.
     *         Sample: {@code @VerifyMapperConfiguration(equals="name:firstName")} results in
     *         {@code assertEquals(source.getName(),target.getFirstName()}
     */
    String[] equals() default {};

    /**
     * @return identifies the properties that change the target but result in a non null element. In
     *         case of a {@link Collection} it will additionally checked on emptiness
     *         The format is 'source.attributeName:target.attributeName'. The target.attributeName
     *         and source.attributeName must refer to an already existing {@link PropertyMetadata}.
     *         Sample: {@code @VerifyMapperConfiguration(notNullNorEmpty="name:lastName")} results
     *         in the attribute {@code target.getLastName()} being not null
     */
    String[] notNullNorEmpty() default {};

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

}