package de.cuioss.test.valueobjects.property.util;

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import de.cuioss.test.valueobjects.objects.impl.ExceptionHelper;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.BuilderMetadata;
import de.cuioss.test.valueobjects.property.impl.BuilderMetadata.BuilderMetadataBuilder;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.property.PropertyUtil;
import de.cuioss.tools.reflect.MoreReflection;

/**
 * Defines different ways for reading / writing properties.
 *
 * @author Oliver Wolff
 */

public enum PropertyAccessStrategy {

    /**
     * Reads and writes property according to the JavaBean-Spec. It uses
     * {@link PropertyUtil} to do so. It acts as part of JUnit testing, therefore it
     * will translate many of the more technical Exceptions to corresponding
     * {@link AssertionError}
     *
     * @author Oliver Wolff
     */
    BEAN_PROPERTY {

        @Override
        public Object writeProperty(final Object target, final PropertyMetadata propertyMetadata,
                final Object propertyValue) {
            assertNotNull(target, TARGET_MUST_NOT_BE_NULL);
            assertNotNull(target, PROPERTY_METADATA_MUST_NOT_BE_NULL);
            try {
                PropertyUtil.writeProperty(target, propertyMetadata.getName(), propertyValue);
                return target;
            } catch (IllegalArgumentException | IllegalStateException e) {
                throw new AssertionError(String.format(UNABLE_TO_SET_PROPERTY, propertyMetadata.getName(),
                        ExceptionHelper.extractCauseMessageFromThrowable(e)), e);
            }

        }

        @Override
        public Object readProperty(final Object target, final PropertyMetadata propertyMetadata) {
            assertNotNull(target, TARGET_MUST_NOT_BE_NULL);
            assertNotNull(target, PROPERTY_METADATA_MUST_NOT_BE_NULL);
            try {
                return PropertyUtil.readProperty(target, propertyMetadata.getName());
            } catch (IllegalArgumentException | IllegalStateException e) {
                throw new AssertionError(String.format(UNABLE_TO_READ_PROPERTY, propertyMetadata.getName(),
                        ExceptionHelper.extractCauseMessageFromThrowable(e)), e);
            }
        }
    },
    /**
     * In some cases the builder supports multiple ways to fill {@link Collection}
     * based elements, e.g. there is a field with the structure
     *
     * <pre>
     * <code>
     *     private List&lt;String&gt; name;
     * </code>
     * </pre>
     *
     * There can be two methods to fill this elements in the builder:
     *
     * <pre>
     * <code>
     *  public Builder names(List&lt;String&gt; names);
     *  public Builder names(String name);
     * </code>
     * </pre>
     *
     * This strategy writes the property using <em>both</em> methods. Therefore it
     * uses {@link BuilderMetadata#getBuilderSingleAddMethodName()} in order to find
     * the single addMethod. The plural add method is supposed to be the name of the
     * property itself therefore derived by
     * {@link BuilderMetadata#getBuilderAddMethodName()}. In case there is different
     * methodName for adding, e.g.
     *
     * <pre>
     * <code>
     *  public Builder names(List&lt;String&gt; names);
     *  public Builder name(String name);
     * </code>
     * </pre>
     *
     * you can specify the method-name explicitly by using
     * {@link BuilderMetadataBuilder#builderSingleAddMethodName(String)}
     * <p>
     * The read method delegates to {@link PropertyAccessStrategy#BEAN_PROPERTY}
     * because it can not be read from an actual builder but from the later created
     * bean.
     * </p>
     *
     * @author Oliver Wolff
     */
    BUILDER_COLLECTION_AND_SINGLE_ELEMENT {

        @Override
        public Object writeProperty(final Object target, final PropertyMetadata propertyMetadata,
                final Object propertyValue) {
            if (!(propertyValue instanceof Iterable)) {
                throw new AssertionError(
                        "Invalid valueType given, must be at least Iterable, but was " + propertyValue);
            }
            final Iterable<?> iterable = (Iterable<?>) propertyValue;
            final List<?> elements = mutableList(iterable);
            BuilderMetadata builderMetadata;
            if (!(propertyMetadata instanceof BuilderMetadata)) {
                builderMetadata = BuilderMetadata.wrapFromMetadata(propertyMetadata);
            } else {
                builderMetadata = (BuilderMetadata) propertyMetadata;
            }
            try {
                if (!elements.isEmpty()) {
                    final Object singleElement = elements.iterator().next();

                    final var writeAddMethod = target.getClass().getMethod(
                            builderMetadata.getBuilderSingleAddMethodName(), propertyMetadata.getPropertyClass());
                    writeAddMethod.invoke(target, singleElement);

                    // Remove the element from the elements list
                    elements.remove(singleElement);
                }
                final var writeCollectionMethod = determineCollectionWriteMethod(target, propertyMetadata,
                        builderMetadata);

                // Now write the remaining elements
                return writeCollectionMethod.invoke(target,
                        propertyMetadata.getCollectionType().wrapToIterable(elements));
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new AssertionError(String.format(UNABLE_TO_SET_PROPERTY, propertyMetadata.getName(),
                        ExceptionHelper.extractCauseMessageFromThrowable(e)), e);
            }

        }

        private Method determineCollectionWriteMethod(final Object target, final PropertyMetadata propertyMetadata,
                BuilderMetadata builderMetadata) throws NoSuchMethodException {
            try {
                return target.getClass().getMethod(builderMetadata.getBuilderAddMethodName(),
                        propertyMetadata.getCollectionType().getIterableType());
            } catch (NoSuchMethodException e) {
                // Noop -> Assuming collection-parameter
            }
            return target.getClass().getMethod(builderMetadata.getBuilderAddMethodName(),
                    CollectionType.COLLECTION.getIterableType());
        }

        @Override
        public Object readProperty(final Object target, final PropertyMetadata propertyMetadata) {
            return PropertyAccessStrategy.BEAN_PROPERTY.readProperty(target, propertyMetadata);
        }
    },
    /**
     * Writes a property in a builder using
     * {@link BuilderMetadata#getBuilderAddMethodName()} to determine the correct
     * write method. The parameter type is exactly the same as defined at
     * {@link PropertyMetadata#getPropertyClass()}. The read method delegates to
     * {@link PropertyAccessStrategy#BEAN_PROPERTY} because it can not be read from
     * an actual builder but from the later created bean.
     *
     * @author Oliver Wolff
     */
    BUILDER_DIRECT {

        @Override
        public Object writeProperty(final Object target, final PropertyMetadata propertyMetadata,
                final Object propertyValue) {
            BuilderMetadata builderMetadata;
            if (!(propertyMetadata instanceof BuilderMetadata)) {
                builderMetadata = BuilderMetadata.wrapFromMetadata(propertyMetadata);
            } else {
                builderMetadata = (BuilderMetadata) propertyMetadata;
            }
            try {
                final var writeMethod = target.getClass().getMethod(builderMetadata.getBuilderAddMethodName(),
                        propertyMetadata.resolveActualClass());
                return writeMethod.invoke(target, propertyValue);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                var message = String.format(UNABLE_TO_SET_PROPERTY, propertyMetadata.getName(),
                        ExceptionHelper.extractCauseMessageFromThrowable(e));
                new CuiLogger(getClass()).error(message);
                throw new AssertionError(message, e);
            }
        }

        @Override
        public Object readProperty(final Object target, final PropertyMetadata propertyMetadata) {
            return PropertyAccessStrategy.BEAN_PROPERTY.readProperty(target, propertyMetadata);
        }
    },
    /**
     * This strategy is for cases where {@link PropertyUtil} is not capable of
     * writing an attribute. This is usually the case for elements that defined a
     * fluent-api (not void as return value). The read method delegates to
     * {@link PropertyAccessStrategy#BEAN_PROPERTY}
     */
    FLUENT_WRITER {

        @Override
        public Object writeProperty(Object target, PropertyMetadata propertyMetadata, Object propertyValue) {
            var writeMethod = MoreReflection.retrieveWriteMethod(target.getClass(), propertyMetadata.getName(),
                    propertyMetadata.resolveActualClass());
            if (!writeMethod.isPresent()) {
                throw new AssertionError(String.format(UNABLE_TO_SET_PROPERTY, propertyMetadata.getName(),
                        "No write-method could be found"));
            }
            try {
                return writeMethod.get().invoke(target, propertyValue);
            } catch (SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new AssertionError(String.format(UNABLE_TO_SET_PROPERTY, propertyMetadata.getName(),
                        ExceptionHelper.extractCauseMessageFromThrowable(e)), e);
            }
        }

        @Override
        public Object readProperty(final Object target, final PropertyMetadata propertyMetadata) {
            return PropertyAccessStrategy.BEAN_PROPERTY.readProperty(target, propertyMetadata);
        }

    };

    private static final String UNABLE_TO_READ_PROPERTY = "Unable to read property '%s' because of '%s'";
    private static final String TARGET_MUST_NOT_BE_NULL = "target must not be null";
    private static final String PROPERTY_METADATA_MUST_NOT_BE_NULL = "propertyMetadata must not be null";
    private static final String UNABLE_TO_SET_PROPERTY = "Unable to set property '%s' because of '%s'";

    /**
     * Writes the property into the given target;
     *
     * @param target           to be written to.
     * @param propertyMetadata identifying the concrete property, must not be null
     * @param propertyValue    to be set, may be null
     * @return the modified object
     * @throws AssertionError in case the property can not be written.
     */
    public abstract Object writeProperty(final Object target, final PropertyMetadata propertyMetadata,
            final Object propertyValue);

    /**
     * Reads the property from the given target;
     *
     * @param target           to be written to.
     * @param propertyMetadata identifying the concrete property, must not be null
     * @return the read property, may be null
     * @throws AssertionError in case the property can not be read.
     */
    public abstract Object readProperty(final Object target, final PropertyMetadata propertyMetadata);

}
