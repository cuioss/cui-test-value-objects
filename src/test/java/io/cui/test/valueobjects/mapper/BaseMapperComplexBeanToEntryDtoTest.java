package io.cui.test.valueobjects.mapper;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.BaseMapperTest;
import io.cui.test.valueobjects.api.VerifyMapperConfiguration;
import io.cui.test.valueobjects.api.property.PropertyConfig;
import io.cui.test.valueobjects.api.property.PropertyReflectionConfig;
import io.cui.test.valueobjects.generator.TypedGeneratorRegistry;
import io.cui.test.valueobjects.mapper.support.ComplexBean;
import io.cui.test.valueobjects.mapper.support.ComplexBeanToEntryDtoMapper;
import io.cui.test.valueobjects.mapper.support.EntryDto;
import io.cui.test.valueobjects.mapper.support.MapEntryGenerator;
import io.cui.test.valueobjects.property.util.CollectionType;
import io.cui.test.valueobjects.util.GeneratorAnnotationHelper;

@VerifyMapperConfiguration
@PropertyReflectionConfig(exclude = "keyValueEntities")
@PropertyConfig(name = "keyValueEntities", collectionType = CollectionType.LIST, propertyClass = Map.Entry.class,
        generator = MapEntryGenerator.class)
class BaseMapperComplexBeanToEntryDtoTest extends BaseMapperTest<ComplexBeanToEntryDtoMapper, ComplexBean, EntryDto> {

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
        GeneratorAnnotationHelper.handleGeneratorsForTestClass(this,
                registerAdditionalGenerators());
        super.intializeTypeInformation();
    }

    @Disabled("see CUI-1248")
    @Test
    void shouldVerifyMapper() {
        super.verifyMapper();
    }

}
