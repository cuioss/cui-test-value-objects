package io.cui.test.valueobjects.junit5;

import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.cui.test.generator.TypedGenerator;
import io.cui.test.generator.junit.GeneratorControllerExtension;
import io.cui.test.valueobjects.BaseMapperTest;
import io.cui.test.valueobjects.api.VerifyMapperConfiguration;
import io.cui.test.valueobjects.junit5.extension.GeneratorController;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.objects.impl.BeanInstantiator;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.util.GeneratorRegistry;

/**
 * Allows to test a mapper implementing a {@link Function} to map a (pseudo-)DTO object based on
 * whatever technology (eHF, FHIR, ...) to a DTO object. The actual test-method is
 * {@link #shouldMap()}. The mapper-test-configuration is defined with
 * {@link VerifyMapperConfiguration}
 *
 * For simple uses-case, like well designed beans there is no special configuration needed
 * <ul>
 * <li>
 * In case the mapper needs to be configured in a special way (not using the default constructor)
 * you can overwrite {@link #getUnderTest()}
 * </li>
 * <li>The metadata / creation of the <em>source</em> objects can be adjusted in multiple ways:
 * <ul>
 * <li>{@link PropertyMetadata}: The annotations on class level affect the metadata for the
 * source-objects</li>
 * <li>{@link #resolveSourcePropertyMetadata()}: can be overwritten as an alternative for using
 * {@link PropertyMetadata} annotations
 * </li>
 * <li>{@link #getSourceInstantiator(RuntimeProperties)}: If not overwritten the default
 * implementation chooses the {@link BeanInstantiator} in order to generate source-objects.</li>
 * </ul>
 * </li>
 * <li>The metadata <em>target</em> objects is derived by reflection. <em>Caution</em>: For more
 * complex objects that can not be created by the generator framework you must provide either a
 * {@link TypedGenerator}, see {@link GeneratorRegistry} or overwrite
 * {@link #anyTargetObject()}</li>
 * </ul>
 *
 * @author Oliver Wolff
 * @param <M> Mapper: The type of the Mapper
 * @param <S> Source: The type of the source-Objects to be mapped from
 * @param <T> Target: The type of the source-Objects to be mapped to
 */
@ExtendWith({ GeneratorControllerExtension.class, GeneratorController.class })
public class AbstractMapperTest<M extends Function<S, T>, S, T> extends BaseMapperTest<M, S, T> {

    /**
     *
     */
    @Test
    public void shouldMap() {
        super.verifyMapper();
    }
}
