package io.cui.test.valueobjects.objects.impl;

import static io.cui.test.valueobjects.objects.impl.AbstractInlineInstantiator.PROPERTIES_MUST_NOT_BE_NULL;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.stream.Collectors;

import io.cui.test.valueobjects.objects.BuilderInstantiator;
import io.cui.test.valueobjects.objects.ParameterizedInstantiator;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.PropertySupport;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author Oliver Wolff
 * @param <T> identifying the type of object to be instantiated
 */
@RequiredArgsConstructor
public class BuilderParameterizedInstantiator<T> implements ParameterizedInstantiator<T> {

    @NonNull
    private final BuilderInstantiator<T> instantiator;

    @NonNull
    @Getter
    private final RuntimeProperties runtimeProperties;

    @Override
    public T newInstance(final List<PropertySupport> properties,
            final boolean generatePropertyValues) {
        assertNotNull(properties, PROPERTIES_MUST_NOT_BE_NULL);

        final var builder = this.instantiator.newBuilderInstance();

        if (generatePropertyValues) {
            properties.forEach(PropertySupport::generateTestValue);
        }
        for (final PropertySupport propertySupport : properties) {
            propertySupport.apply(builder);
        }
        return this.instantiator.build(builder);
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
        return newInstance(this.runtimeProperties.getAllProperties());
    }

    @Override
    public String toString() {
        final var builder = new StringBuilder(getClass().getName());
        builder.append("\nInstantiator: ").append(this.instantiator)
                .append(this.runtimeProperties.toString());
        return builder.toString();
    }

}
