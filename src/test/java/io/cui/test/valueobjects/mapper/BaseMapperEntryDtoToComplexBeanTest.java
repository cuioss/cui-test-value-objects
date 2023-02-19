package io.cui.test.valueobjects.mapper;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import io.cui.test.valueobjects.MapperTest;
import io.cui.test.valueobjects.api.VerifyMapperConfiguration;
import io.cui.test.valueobjects.api.property.PropertyConfig;
import io.cui.test.valueobjects.api.property.PropertyReflectionConfig;
import io.cui.test.valueobjects.generator.TypedGeneratorRegistry;
import io.cui.test.valueobjects.mapper.support.ComplexBean;
import io.cui.test.valueobjects.mapper.support.EntryDto;
import io.cui.test.valueobjects.mapper.support.EntryDtoToComplexBeanMapper;
import io.cui.test.valueobjects.mapper.support.MapEntryGenerator;
import io.cui.test.valueobjects.objects.ParameterizedInstantiator;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.objects.impl.ConstructorBasedInstantiator;
import io.cui.test.valueobjects.property.util.CollectionType;
import io.cui.test.valueobjects.util.GeneratorAnnotationHelper;

// owolff: List<Map.Entry<String, ? extends Serializable>> can not be determined by reflection
@PropertyConfig(name = "keyValueEntities", collectionType = CollectionType.LIST, propertyClass = Map.Entry.class,
        generator = MapEntryGenerator.class)
// owolff: because 'identifier' is final it should be considered as required
@PropertyReflectionConfig(exclude = "keyValueEntities", required = "identifier")
@VerifyMapperConfiguration
class BaseMapperEntryDtoToComplexBeanTest
        extends MapperTest<EntryDtoToComplexBeanMapper, EntryDto, ComplexBean> {

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

    /**
     * owolff: this is needed because the default implementation of
     * #getSourceInstantiator(RuntimeProperties) assumes beans with a default constructor
     */
    @Override
    public ParameterizedInstantiator<? extends EntryDto> getSourceInstantiator(RuntimeProperties runtimeProperties) {
        return new ConstructorBasedInstantiator<>(EntryDto.class,
                new RuntimeProperties(runtimeProperties.getRequiredProperties()));
    }
}
