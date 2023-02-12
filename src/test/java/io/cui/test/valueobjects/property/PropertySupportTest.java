package io.cui.test.valueobjects.property;

import static io.cui.test.valueobjects.generator.JavaTypesGenerator.BOOLEANS;
import static io.cui.test.valueobjects.generator.JavaTypesGenerator.STRINGS;
import static io.cui.test.valueobjects.testbeans.ComplexBean.ATTRIBUTE_BAD_STRING;
import static io.cui.test.valueobjects.testbeans.ComplexBean.ATTRIBUTE_BOOLEAN_OBJECT;
import static io.cui.test.valueobjects.testbeans.ComplexBean.ATTRIBUTE_BOOLEAN_PRIMITIVE;
import static io.cui.test.valueobjects.testbeans.ComplexBean.ATTRIBUTE_STRING;
import static io.cui.test.valueobjects.testbeans.ComplexBean.ATTRIBUTE_STRING_WITH_DEFAULT;
import static io.cui.tools.property.PropertyReadWrite.WRITE_ONLY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.AbstractValueObjectTest;
import io.cui.test.valueobjects.api.contracts.VerifyConstructor;
import io.cui.test.valueobjects.api.generator.PropertyGeneratorHint;
import io.cui.test.valueobjects.api.object.ObjectTestContracts;
import io.cui.test.valueobjects.api.object.VetoObjectTestContract;
import io.cui.test.valueobjects.api.property.PropertyConfig;
import io.cui.test.valueobjects.api.property.PropertyReflectionConfig;
import io.cui.test.valueobjects.objects.impl.DefaultInstantiator;
import io.cui.test.valueobjects.property.impl.PropertyMetadataImpl;
import io.cui.test.valueobjects.testbeans.ComplexBean;

@PropertyConfig(name = "propertyMetadata", propertyClass = PropertyMetadata.class, required = true,
        propertyReadWrite = WRITE_ONLY)
@PropertyGeneratorHint(declaredType = PropertyMetadata.class, implementationType = PropertyMetadataImpl.class)
@PropertyReflectionConfig(skip = true)
@VerifyConstructor(of = "propertyMetadata")
@VetoObjectTestContract({ ObjectTestContracts.SERIALIZABLE, ObjectTestContracts.EQUALS_AND_HASHCODE })
class PropertySupportTest extends AbstractValueObjectTest<PropertySupport> {

    private final DefaultInstantiator<ComplexBean> instantiator = new DefaultInstantiator<>(ComplexBean.class);

    private final PropertyMetadata stringProperty = STRINGS.metadata(ATTRIBUTE_STRING);

    @Test
    void shouldVerifySimpleString() {
        final var propertySupport = new PropertySupport(stringProperty);
        final var target = instantiator.newInstance();
        assertNull(propertySupport.getGeneratedValue());
        propertySupport.generateTestValue();
        assertNotNull(propertySupport.getGeneratedValue());
        propertySupport.apply(target);
        assertEquals(propertySupport.getGeneratedValue(), target.getString());
        propertySupport.assertValueSet(target);
    }

    @Test
    void shouldDetectInvalidValue() {
        final var property = STRINGS.metadata(ATTRIBUTE_BAD_STRING);
        final var propertySupport = new PropertySupport(property);
        final var target = instantiator.newInstance();
        propertySupport.generateTestValue();
        propertySupport.apply(target);
        assertThrows(AssertionError.class,
                () -> propertySupport.assertValueSet(target));
    }

    @Test
    void shouldHandleBooleanObjects() {
        final var booleanProperty = BOOLEANS.metadata(ATTRIBUTE_BOOLEAN_OBJECT);
        final var propertySupport = new PropertySupport(booleanProperty);
        final var target = instantiator.newInstance();
        propertySupport.generateTestValue();
        propertySupport.apply(target);
        assertEquals(propertySupport.getGeneratedValue(), target.getBooleanObject());
        propertySupport.assertValueSet(target);
    }

    @Test
    void shouldHandleBooleanPrimitives() {
        final var booleanProperty = BOOLEANS.metadata(ATTRIBUTE_BOOLEAN_PRIMITIVE);
        final var propertySupport = new PropertySupport(booleanProperty);
        final var target = instantiator.newInstance();
        propertySupport.generateTestValue();
        propertySupport.apply(target);
        assertEquals(propertySupport.getGeneratedValue(), target.isBooleanPrimitive());
        propertySupport.assertValueSet(target);
    }

    @Test
    void shouldDetectInvalidBooleanPrimitiveValue() {
        final var booleanProperty = BOOLEANS.metadata(ATTRIBUTE_BOOLEAN_PRIMITIVE);
        final var propertySupport = new PropertySupport(booleanProperty);
        final var target = instantiator.newInstance();
        propertySupport.generateTestValue();
        propertySupport.apply(target);
        // Revert Boolean
        target.setBooleanPrimitive(!target.isBooleanPrimitive());
        assertThrows(AssertionError.class,
                () -> propertySupport.assertValueSet(target));
    }

    @Test
    void shouldHandleDefaultValue() {
        final PropertyMetadata stringDefaultProperty =
            STRINGS.metadataBuilder(ATTRIBUTE_STRING_WITH_DEFAULT).defaultValue(true).build();
        final var propertySupport = new PropertySupport(stringDefaultProperty);
        final var target = instantiator.newInstance();
        propertySupport.assertDefaultValue(target);
    }

    @Test
    void shouldFailHandleDefaultValue() {
        final var propertySupport = new PropertySupport(stringProperty);
        final var target = instantiator.newInstance();
        assertThrows(AssertionError.class,
                () -> propertySupport.assertDefaultValue(target));
    }

    @Test
    void shouldCopyCorrecly() {
        final var support = new PropertySupport(stringProperty);
        assertNull(support.getGeneratedValue());
        var copy = support.createCopy(true);
        assertNull(copy.getGeneratedValue());
        support.generateTestValue();
        copy = support.createCopy(true);
        assertNotNull(copy.getGeneratedValue());
        assertEquals(support.getGeneratedValue(), copy.getGeneratedValue());
        copy = support.createCopy(false);
        assertNull(copy.getGeneratedValue());
    }
}
