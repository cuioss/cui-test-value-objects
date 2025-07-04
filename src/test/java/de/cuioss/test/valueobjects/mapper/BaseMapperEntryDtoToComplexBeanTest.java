/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.mapper;

import de.cuioss.test.valueobjects.MapperTest;
import de.cuioss.test.valueobjects.api.VerifyMapperConfiguration;
import de.cuioss.test.valueobjects.api.property.PropertyConfig;
import de.cuioss.test.valueobjects.api.property.PropertyReflectionConfig;
import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.mapper.support.ComplexBean;
import de.cuioss.test.valueobjects.mapper.support.EntryDto;
import de.cuioss.test.valueobjects.mapper.support.EntryDtoToComplexBeanMapper;
import de.cuioss.test.valueobjects.mapper.support.MapEntryGenerator;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.objects.impl.ConstructorBasedInstantiator;
import de.cuioss.test.valueobjects.property.util.CollectionType;
import de.cuioss.test.valueobjects.util.GeneratorAnnotationHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;

// owolff: List<Map.Entry<String, ? extends Serializable>> can not be determined by reflection
@PropertyConfig(name = "keyValueEntities", collectionType = CollectionType.LIST, propertyClass = Map.Entry.class, generator = MapEntryGenerator.class)
// owolff: because 'identifier' is final it should be considered as required
@PropertyReflectionConfig(exclude = "keyValueEntities", required = "identifier")
@VerifyMapperConfiguration
class BaseMapperEntryDtoToComplexBeanTest extends MapperTest<EntryDtoToComplexBeanMapper, EntryDto, ComplexBean> {

    /**
     * Clears the {@link TypedGeneratorRegistry}
     */
    @AfterEach
    void tearDownGeneratorRegistry() {
        TypedGeneratorRegistry.clear();
    }

    /**
     * Initializes all contracts, properties and generator
     */
    @BeforeEach
    void initializePropertiesAndGenerators() {
        GeneratorAnnotationHelper.handleGeneratorsForTestClass(this, registerAdditionalGenerators());
        super.intializeTypeInformation();
    }

    /**
     * owolff: this is needed because the default implementation of
     * #getSourceInstantiator(RuntimeProperties) assumes beans with a default
     * constructor
     */
    @Override
    public ParameterizedInstantiator<? extends EntryDto> getSourceInstantiator(RuntimeProperties runtimeProperties) {
        return new ConstructorBasedInstantiator<>(EntryDto.class,
            new RuntimeProperties(runtimeProperties.getRequiredProperties()));
    }
}
