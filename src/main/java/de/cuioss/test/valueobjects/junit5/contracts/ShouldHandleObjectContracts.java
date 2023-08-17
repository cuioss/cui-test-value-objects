package de.cuioss.test.valueobjects.junit5.contracts;

/**
 * Combination of all test-object contracts {@link ShouldBeNotNull},
 * {@link ShouldBeSerializable}, {@link ShouldImplementEqualsAndHashCode},
 * {@link ShouldImplementToString}
 *
 * @author Oliver Wolff
 * @param <T> identifying the type to be tested is usually but not necessarily
 *            at least Serializable.
 */
public interface ShouldHandleObjectContracts<T> extends ShouldBeNotNull<T>, ShouldBeSerializable<T>,
        ShouldImplementEqualsAndHashCode<T>, ShouldImplementToString<T> {

}
