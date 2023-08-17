/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.contract;

import java.util.List;
import java.util.function.Function;

import de.cuioss.test.valueobjects.api.VerifyMapperConfiguration;
import de.cuioss.test.valueobjects.contract.support.MapperAttributesAsserts;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.property.PropertySupport;
import de.cuioss.tools.logging.CuiLogger;
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
        builder.append("\nWith target-configuration: ").append(targetMetadata.toString());
        log.info(builder.toString());
        var asserter = new MapperAttributesAsserts(config, targetMetadata, sourceInstantiator.getRuntimeProperties());
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

    private void verifyMapping(MapperAttributesAsserts asserter, List<PropertySupport> properties, String context) {
        S source = sourceInstantiator.newInstance(properties, false);
        var target = mapper.apply(source);
        var names = properties.stream().map(PropertySupport::getName).toList();
        log.debug("Verifying mapper in context of {} with attributes {}", context, names);
        asserter.assertMappingForSourceAttributes(names, source, target);
    }

}
