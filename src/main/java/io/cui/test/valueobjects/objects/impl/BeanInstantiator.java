package io.cui.test.valueobjects.objects.impl;

import static io.cui.test.valueobjects.objects.impl.AbstractInlineInstantiator.PROPERTIES_MUST_NOT_BE_NULL;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.stream.Collectors;

import io.cui.test.valueobjects.objects.ObjectInstantiator;
import io.cui.test.valueobjects.objects.ParameterizedInstantiator;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.PropertySupport;
import io.cui.tools.logging.CuiLogger;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @param <T> identifying the type of object to be instantiated
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class BeanInstantiator<T> implements ParameterizedInstantiator<T> {

    private static final CuiLogger log = new CuiLogger(BeanInstantiator.class);

    @NonNull
    private final ObjectInstantiator<T> instantiator;

    @NonNull
    @Getter
    private final RuntimeProperties runtimeProperties;

    @Override
    public T newInstance(final List<PropertySupport> properties,
            final boolean generatePropertyValues) {
        assertNotNull(properties, PROPERTIES_MUST_NOT_BE_NULL);
        final var instance = this.instantiator.newInstance();
        if (generatePropertyValues) {
            properties.forEach(PropertySupport::generateTestValue);
        }
        for (final PropertySupport propertySupport : properties) {
            if (!propertySupport.getPropertyMetadata().getPropertyReadWrite().isWriteable()) {
                log.warn(
                        "Trying to apply a property '{}' which is not writable. Please check usage and configuration of @VerifyBeanProperty, maybe add the property to 'exclude' list.",
                        propertySupport.getName());
            }
            propertySupport.apply(instance);
        }
        return instance;
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
    public T newInstanceFull() {
        return newInstance(this.runtimeProperties.getAdditionalProperties());
    }

    @Override
    public String toString() {
        final var builder = new StringBuilder(getClass().getName());
        builder.append("\nProperty Configuration: ")
                .append(this.runtimeProperties.toString());
        return builder.toString();
    }
}
