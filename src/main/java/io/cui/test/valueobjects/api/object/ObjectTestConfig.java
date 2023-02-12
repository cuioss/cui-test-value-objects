package io.cui.test.valueobjects.api.object;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Simple annotation for extended configuration of {@link ObjectTestContract}. Usually not needed.
 * Used for corner cases.
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface ObjectTestConfig {

    /**
     * @return a number of properties, identified by their names that are not to be considered for
     *         equalsAndHashcode-tests: Blacklist
     */
    String[] equalsAndHashCodeExclude() default {};

    /**
     * @return a number of properties, identified by their names that are to be considered for
     *         equalsAndHashcode-tests: Whitelist: takes precedence over
     *         {@link #equalsAndHashCodeExclude()}
     */
    String[] equalsAndHashCodeOf() default {};

    /**
     * @return boolean indicating whether to only use the default checks ignoring any properties,
     *         defaults to {@code false}
     */
    boolean equalsAndHashCodeBasicOnly() default false;

    /**
     * @return a number of properties, identified by their names that are not to be considered for
     *         serializable-tests: Blacklist
     */
    String[] serializableExclude() default {};

    /**
     * @return a number of properties, identified by their names that are to be considered for
     *         equalsAndHashcode-tests: Whitelist: takes precedence over
     *         {@link #serializableExclude()}
     */
    String[] serializableOf() default {};

    /**
     * @return boolean indicating whether to only use the default checks ignoring any properties,
     *         defaults to {@code false}
     */
    boolean serializableBasicOnly() default false;

    /**
     * @return boolean indicating whether the serialized and de-serialized object should be compared
     *         using {@link Object#equals(Object)} with the original object
     *         defaults to {@code true}
     */
    boolean serializableCompareUsingEquals() default true;

    /**
     * @return boolean indicating whether during the {@link ObjectTestContracts#TO_STRING} minimal
     *         objects should be used regarding the properties defaults to {@code false}
     */
    boolean toStringUseMinimalInstance() default false;
}
