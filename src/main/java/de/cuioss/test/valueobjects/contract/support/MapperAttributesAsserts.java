/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.contract.support;

import de.cuioss.test.valueobjects.api.VerifyMapperConfiguration;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.tools.logging.CuiLogger;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Helper class for asserting single-attribute mappings for
 * {@link VerifyMapperConfiguration}
 *
 * @author Oliver Wolff
 *
 */
@EqualsAndHashCode
@ToString
public class MapperAttributesAsserts {

    private static final CuiLogger log = new CuiLogger(MapperAttributesAsserts.class);

    private static final String PROPERTY_MAPPING_INCOMPLETE = """
        Caution: you have unmapped {}-properties: {} you can adapt this behavior by either:\

        - @VerifyMapperConfiguration(equals("name:firstName"))\

        - Make use of the provided property controls like @VerifyMapperConfiguration(of("name"))""";

    private final List<AssertTuple> sourceAsserts;

    /**
     * @param config
     * @param targetProperties
     * @param sourceProperties
     */
    public MapperAttributesAsserts(VerifyMapperConfiguration config, RuntimeProperties targetProperties,
        RuntimeProperties sourceProperties) {
        var targetPropertyMap = targetProperties.asMapView(false);
        var sourcePropertyMap = sourceProperties.asMapView(false);
        List<MappingTuple> mapping = new ArrayList<>(MappingAssertStrategy.EQUALS.readConfiguration(config));

        mapping.addAll(MappingAssertStrategy.NOT_NULL.readConfiguration(config));

        for (MappingTuple tuple : mapping) {
            assertTrue(targetPropertyMap.containsKey(tuple.getTarget()),
                "Invalid (unmapped) attribute-name for target: " + tuple);
            assertTrue(sourcePropertyMap.containsKey(tuple.getSource()),
                "Invalid (unmapped) attribute-name for source: " + tuple);
        }

        sourceAsserts = new ArrayList<>();
        for (MappingTuple tuple : mapping) {
            sourceAsserts.add(new AssertTuple(sourcePropertyMap.get(tuple.getSource()),
                targetPropertyMap.get(tuple.getTarget()), tuple));
        }
        logConfigurationStatus(targetProperties, sourceProperties);
    }

    private void logConfigurationStatus(RuntimeProperties targetProperties, RuntimeProperties sourceProperties) {
        if (sourceAsserts.isEmpty()) {
            log.warn(
                "No attribute specific mapping found. use @VerifyMapperConfiguration(equals(\"name:firstName\")) or @VerifyMapperConfiguration(notNull(\"name:lastName\")) in order to activate");
        }
        Set<String> sourceMappingNames = sourceAsserts.stream().map(a -> a.getMappingTuple().getSource())
            .collect(Collectors.toSet());
        Set<String> targetMappingNames = sourceAsserts.stream().map(a -> a.getMappingTuple().getTarget())
            .collect(Collectors.toSet());
        var sourceTypeProperties = RuntimeProperties.extractNames(sourceProperties.getAllProperties());
        var targetTypeProperties = RuntimeProperties.extractNames(targetProperties.getAllProperties());

        sourceTypeProperties.removeAll(sourceMappingNames);
        targetTypeProperties.removeAll(targetMappingNames);
        if (sourceTypeProperties.isEmpty()) {
            log.info("All source-properties are covered.");
        } else {
            log.warn(PROPERTY_MAPPING_INCOMPLETE, "source", sourceTypeProperties);
        }
        if (targetTypeProperties.isEmpty()) {
            log.info("All target-properties are covered.");
        } else {
            log.warn(PROPERTY_MAPPING_INCOMPLETE, "target", targetTypeProperties);
        }
    }

    /**
     * @param sourceAttributes to be tested
     * @param source
     * @param target
     */
    public void assertMappingForSourceAttributes(Collection<String> sourceAttributes, Object source, Object target) {
        if (sourceAsserts.isEmpty()) {
            return;
        }
        Map<String, List<AssertTuple>> asserts = new HashMap<>();
        for (String name : sourceAttributes) {
            var concreteAsserts = sourceAsserts.stream().filter(a -> a.isResponsibleForSource(name)).toList();
            if (concreteAsserts.isEmpty()) {
                log.info("Checked property '{}' is not configured to be asserted, ist this intentional?", name);
            } else {
                asserts.put(name, concreteAsserts);
            }
        }
        if (asserts.isEmpty()) {
            return;
        }
        // Defines the elements that should not be affected in the correct instance and
        // should
        // therefore not be changed
        Set<AssertTuple> activeAsserts = new HashSet<>();
        asserts.values().forEach(activeAsserts::addAll);
        List<AssertTuple> nullAsserts = new ArrayList<>(sourceAsserts);
        nullAsserts.removeAll(activeAsserts);

        // Now do the actual checks
        for (Entry<String, List<AssertTuple>> entry : asserts.entrySet()) {
            log.debug("Asserting attribute {}", entry.getKey());
            entry.getValue().forEach(a -> a.assertContract(source, target));
        }
        // All not affected elements that provide no default should be null / empty
        for (AssertTuple nullAssert : nullAsserts) {
            log.debug("Asserting attribute to be null / not set {}", nullAssert.getTargetSupport().getName());
            MappingAssertStrategy.NULL_OR_DEFAULT.assertMapping(nullAssert.getSourceSupport(), source,
                nullAssert.getTargetSupport(), target);
        }

    }

}
