package de.cuioss.test.valueobjects.objects.impl;

import static de.cuioss.test.valueobjects.objects.impl.AbstractInlineInstantiator.PROPERTIES_MUST_NOT_BE_NULL;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import de.cuioss.test.valueobjects.objects.ConfigurationCallBackHandler;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.TestObjectProvider;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.PropertySupport;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Variant of {@link ParameterizedInstantiator} that deals with test, where the actual beans to be
 * tested are Injected by cdi, see {@link TestObjectProvider} as well
 *
 * @param <T> identifying the type to be tested is usually but not necessarily at least
 *            {@link Serializable}.
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class InjectedBeanInstantiator<T> implements ParameterizedInstantiator<T> {

    private final TestObjectProvider<T> objectProvider;

    private final ConfigurationCallBackHandler<T> callbackHandler;

    @Getter
    @NonNull
    private final RuntimeProperties runtimeProperties;

    @Override
    public T newInstance(final List<PropertySupport> properties,
            final boolean generatePropertyValues) {
        assertNotNull(properties, PROPERTIES_MUST_NOT_BE_NULL);
        final var instance = objectProvider.getUnderTest();
        assertNotNull(instance, "Unable to obtain instance from " + objectProvider.toString());
        if (generatePropertyValues) {
            properties.forEach(PropertySupport::generateTestValue);
        }
        for (final PropertySupport propertySupport : properties) {
            propertySupport.apply(instance);
        }
        callbackHandler.configure(instance);
        return instance;
    }

    @Override
    public T newInstanceFull() {
        return newInstance(this.runtimeProperties.getAdditionalProperties());
    }

    @Override
    public T newInstance(final List<PropertyMetadata> properties) {
        assertNotNull(properties, PROPERTIES_MUST_NOT_BE_NULL);
        return newInstance(properties.stream().map(PropertySupport::new)
                .collect(Collectors.toList()), true);
    }

    @Override
    public T newInstanceMinimal() {
        return newInstance(this.runtimeProperties.getRequiredProperties());
    }

    @Override
    public String toString() {
        final var builder = new StringBuilder(getClass().getName());
        builder.append("\nProperty Configuration: ")
                .append(this.runtimeProperties.toString());
        return builder.toString();
    }

}
