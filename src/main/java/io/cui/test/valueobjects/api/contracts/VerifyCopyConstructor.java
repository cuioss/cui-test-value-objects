package io.cui.test.valueobjects.api.contracts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If used on ValueObjectTest this test verifies a copy-constructor.
 * Depending on its configuration it tests the individual properties to be copied correctly and of
 * course the {@link Object#equals(Object)} method, see {@link #useObjectEquals()}. It can only be
 * used if at least one other verification contract is there.
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface VerifyCopyConstructor {

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
     * @return the actual type needed for the copy-constructor. It is only needed if it differs from
     *         the type of the bean under test. {@link VerifyCopyConstructor} acts as {@code null}
     *         value
     */
    Class<?> argumentType() default VerifyCopyConstructor.class;

    /**
     * @return boolean indicating whether to {@link Object#equals(Object)} as well, defaults to
     *         {@code true}
     */
    boolean useObjectEquals() default true;

    /**
     * @return boolean indicating whether to implicitly check whether the copy is a deep instead of
     *         a shallow copy, defaults to {@code true}. See {@link #verifyDeepCopyIgnore()} as well
     */
    boolean verifyDeepCopy() default false;

    /**
     * @return a number of properties, identified by their names that are to be ignored by checking
     *         the deep copy, see {@link #verifyDeepCopy()}
     */
    String[] verifyDeepCopyIgnore() default {};
}