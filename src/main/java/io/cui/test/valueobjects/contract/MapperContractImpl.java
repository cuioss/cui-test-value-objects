package io.cui.test.valueobjects.contract;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.cui.test.valueobjects.api.VerifyMapperConfiguration;
import io.cui.test.valueobjects.contract.support.MapperAttributesAsserts;
import io.cui.test.valueobjects.objects.ParameterizedInstantiator;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.property.PropertySupport;
import io.cui.tools.logging.CuiLogger;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Defines tests for Mapper
 *
 * @author Oliver Wolff
 * @param <S> Source: The type of the source-Objects to be mapped from
 * @param <T> Target: The type of the source-Objects to be mapped to
 */
@RequiredArgsConstructor
public class MapperContractImpl<S, T> {

    private static final CuiLogger log = new CuiLogger(MapperContractImpl.class);

    @NonNull
    private final VerifyMapperConfiguration config;

    @NonNull
    private final ParameterizedInstantiator<? extends S> sourceInstantiator;

    @NonNull
    private final RuntimeProperties targetMetadata;

    @NonNull
    private final Function<S, T> mapper;

    /**
     * Runs the asserts for the mapper tests
     */
    public void assertContract() {

        final var builder = new StringBuilder("Verifying ");
        builder.append(getClass().getName()).append("\nWith source-configuration: ")
                .append(sourceInstantiator.getRuntimeProperties().toString());
        builder.append("\nWith target-configuration: ")
                .append(targetMetadata.toString());
        log.info(builder.toString());
        var asserter =
            new MapperAttributesAsserts(config, targetMetadata,
                    sourceInstantiator.getRuntimeProperties());
        handleSimpleMapping(asserter);
    }

    private void handleSimpleMapping(MapperAttributesAsserts asserter) {
        log.info("Testing mimimal Mapping for mapper-class {}", mapper.getClass());
        verifyMapping(asserter, sourceInstantiator.getRuntimeProperties().getRequiredAsPropertySupport(true),
                "minimal-instance");
        log.info("Testing full Mapping for mapper-class {}", mapper.getClass());
        verifyMapping(asserter, sourceInstantiator.getRuntimeProperties().getWritableAsPropertySupport(true),
                "full-instance");
    }

    private void verifyMapping(MapperAttributesAsserts asserter, List<PropertySupport> properties,
            String context) {
        S source = sourceInstantiator.newInstance(properties, false);
        var target = mapper.apply(source);
        List<String> names = properties.stream().map(PropertySupport::getName).collect(Collectors.toList());
        log.debug("Verifying mapper in context of {} with attributes {}", context, names);
        asserter.assertMappingForSourceAttributes(names, source, target);
    }

}
