package de.cuioss.test.valueobjects.objects;

/**
 * Simple interface used for dynamically creating builder objects.
 *
 * @author Oliver Wolff
 * @param <T> identifying the type of object to be instantiated by the builder
 */
public interface BuilderInstantiator<T> {

    /**
     * @return a newly created builder.
     * @throws AssertionError in case the object could not be created
     */
    Object newBuilderInstance();

    /**
     * @return the class of the object that will be created by the contained
     *         builder.
     */
    Class<T> getTargetClass();

    /**
     * @return the class of the builder.
     */
    Class<?> getBuilderClass();

    /**
     * Actually builds the target object
     *
     * @param builder
     * @return the Object created by the contained builder
     */
    T build(Object builder);

}
