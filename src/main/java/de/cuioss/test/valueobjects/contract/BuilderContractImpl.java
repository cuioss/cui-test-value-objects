package de.cuioss.test.valueobjects.contract;

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.api.contracts.VerifyBuilder;
import de.cuioss.test.valueobjects.objects.BuilderInstantiator;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.BuilderConstructorBasedInstantiator;
import de.cuioss.test.valueobjects.objects.impl.BuilderFactoryBasedInstantiator;
import de.cuioss.test.valueobjects.objects.impl.BuilderParameterizedInstantiator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.PropertySupport;
import de.cuioss.test.valueobjects.util.AnnotationHelper;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.reflect.MoreReflection;

/**
 * Defines basic tests for builder. In essence it will try to create a builder
 * with a minimal set (required only) and one with all properties set. It sets
 * the properties, build the actual object and verifies whether the properties
 * are set correctly
 *
 * @author Oliver Wolff
 * @param <T> identifying the type of objects to be tested
 */
public class BuilderContractImpl<T> implements TestContract<T> {

    private static final CuiLogger log = new CuiLogger(BuilderContractImpl.class);

    private final BuilderInstantiator<T> builderInstantiator;

    private final RuntimeProperties runtimeProperties;

    /** The usually chosen name for the actual build method. */
    public static final String DEFAULT_BUILD_METHOD_NAME = "build";

    /** The usually chosen name for a factory builder method. */
    public static final String DEFAULT_BUILDER_FACTORY_METHOD_NAME = "builder";

    /**
     * @param instantiator      must not be null
     * @param runtimeProperties must not be null
     */
    public BuilderContractImpl(final BuilderInstantiator<T> instantiator, final RuntimeProperties runtimeProperties) {
        super();
        builderInstantiator = requireNonNull(instantiator, "builderInstantiator must not be null");
        this.runtimeProperties = requireNonNull(runtimeProperties, "runtimeProperties must not be null.");
    }

    @Override
    public void assertContract() {

        final var builder = new StringBuilder("Verifying ");
        builder.append(getClass().getName()).append("\nWith configuration: ").append(builderInstantiator.toString());
        log.info(builder.toString());
        setAndVerifyProperties(runtimeProperties.getRequiredProperties());
        setAndVerifyProperties(runtimeProperties.getAllProperties());
        shouldFailOnMissingRequiredAttributes();
    }

    private void setAndVerifyProperties(final List<PropertyMetadata> propertiesToBeChecked) {
        final var properties = propertiesToBeChecked.stream().map(PropertySupport::new).toList();

        properties.forEach(PropertySupport::generateTestValue);

        final var builder = builderInstantiator.newBuilderInstance();
        for (final PropertySupport support : properties) {
            support.apply(builder);
        }
        final var built = builderInstantiator.build(builder);

        for (final PropertySupport support : properties) {
            if (support.isReadable()) {
                support.assertValueSet(built);
            }
        }
    }

    private void shouldFailOnMissingRequiredAttributes() {
        if (runtimeProperties.getRequiredProperties().isEmpty()) {
            return;
        }
        for (final PropertyMetadata property : runtimeProperties.getRequiredProperties()) {
            final List<PropertyMetadata> requiredMinusOne = mutableList(runtimeProperties.getRequiredProperties());
            requiredMinusOne.remove(property);
            var failed = false;
            try {
                setAndVerifyProperties(requiredMinusOne);
                failed = true;
            } catch (final AssertionError e) {
                // Expected: Should have been thrown
            }
            if (failed) {
                throw new AssertionError("Property is marked as required but the builder accepts if it is missing: "
                        + property.toString());
            }
        }
    }

    @Override
    public ParameterizedInstantiator<T> getInstantiator() {
        return new BuilderParameterizedInstantiator<>(builderInstantiator, runtimeProperties);
    }

    /**
     * Factory method for creating an instance of {@link BuilderContractImpl}
     * depending on the given parameter
     *
     * @param beanType                identifying the type to be tested. Must not be
     *                                null
     * @param annotated               the annotated unit-test-class. It is expected
     *                                to be annotated with {@link VerifyBuilder},
     *                                otherwise the method will return
     *                                {@link Optional#empty()}
     * @param initialPropertyMetadata identifying the complete set of
     *                                {@link PropertyMetadata}, where the actual
     *                                {@link PropertyMetadata} for the bean tests
     *                                will be filtered by using the attributes
     *                                defined within {@link VerifyBuilder}. Must not
     *                                be null.
     * @return an instance Of {@link BeanPropertyContractImpl} in case all
     *         requirements for the parameters are correct, otherwise it will return
     *         {@link Optional#empty()}
     */
    public static final <T> Optional<BuilderContractImpl<T>> createBuilderTestContract(final Class<T> beanType,
            final Class<?> annotated, final List<PropertyMetadata> initialPropertyMetadata) {

        requireNonNull(beanType, "beantype must not be null");
        requireNonNull(annotated, "annotated must not be null");
        requireNonNull(initialPropertyMetadata, "initialPropertyMetadata must not be null");

        final Optional<VerifyBuilder> config = MoreReflection.extractAnnotation(annotated, VerifyBuilder.class);

        if (!config.isPresent()) {
            log.debug("No annotation of type BuilderTestContract available on class: " + annotated);
            return Optional.empty();
        }
        final var metadata = AnnotationHelper.handleMetadataForBuilderTest(annotated, initialPropertyMetadata);

        final var contract = config.get();
        BuilderInstantiator<T> instantiator;
        if (VerifyBuilder.class.equals(contract.builderClass())) {
            if (VerifyBuilder.class.equals(contract.builderFactoryProvidingClass())) {
                instantiator = new BuilderFactoryBasedInstantiator<>(beanType, contract.builderFactoryMethodName(),
                        contract.builderMethodName());
            } else {
                instantiator = new BuilderFactoryBasedInstantiator<>(contract.builderFactoryProvidingClass(),
                        contract.builderFactoryMethodName(), contract.builderMethodName());
            }
        } else {
            instantiator = new BuilderConstructorBasedInstantiator<>(contract.builderClass(),
                    contract.builderMethodName());
        }

        return Optional.of(new BuilderContractImpl<>(instantiator, new RuntimeProperties(metadata)));
    }
}
