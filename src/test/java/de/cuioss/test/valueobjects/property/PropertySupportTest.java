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
package de.cuioss.test.valueobjects.property;

import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyConstructor;
import de.cuioss.test.valueobjects.api.generator.PropertyGeneratorHint;
import de.cuioss.test.valueobjects.api.object.ObjectTestContracts;
import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;
import de.cuioss.test.valueobjects.api.property.PropertyConfig;
import de.cuioss.test.valueobjects.api.property.PropertyReflectionConfig;
import de.cuioss.test.valueobjects.objects.impl.DefaultInstantiator;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.test.valueobjects.testbeans.ComplexBean;
import org.junit.jupiter.api.Test;

import static de.cuioss.test.valueobjects.generator.JavaTypesGenerator.BOOLEANS;
import static de.cuioss.test.valueobjects.generator.JavaTypesGenerator.STRINGS;
import static de.cuioss.test.valueobjects.testbeans.ComplexBean.*;
import static de.cuioss.tools.property.PropertyReadWrite.WRITE_ONLY;
import static org.junit.jupiter.api.Assertions.*;

@PropertyConfig(name = "propertyMetadata", propertyClass = PropertyMetadata.class, required = true, propertyReadWrite = WRITE_ONLY)
@PropertyGeneratorHint(declaredType = PropertyMetadata.class, implementationType = PropertyMetadataImpl.class)
@PropertyReflectionConfig(skip = true)
@VerifyConstructor(of = "propertyMetadata")
@VetoObjectTestContract({ObjectTestContracts.SERIALIZABLE, ObjectTestContracts.EQUALS_AND_HASHCODE})
class PropertySupportTest extends ValueObjectTest<PropertySupport> {

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
        assertThrows(AssertionError.class, () -> propertySupport.assertValueSet(target));
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
        assertThrows(AssertionError.class, () -> propertySupport.assertValueSet(target));
    }

    @Test
    void shouldHandleDefaultValue() {
        final PropertyMetadata stringDefaultProperty = STRINGS.metadataBuilder(ATTRIBUTE_STRING_WITH_DEFAULT)
            .defaultValue(true).build();
        final var propertySupport = new PropertySupport(stringDefaultProperty);
        final var target = instantiator.newInstance();
        propertySupport.assertDefaultValue(target);
    }

    @Test
    void shouldFailHandleDefaultValue() {
        final var propertySupport = new PropertySupport(stringProperty);
        final var target = instantiator.newInstance();
        assertThrows(AssertionError.class, () -> propertySupport.assertDefaultValue(target));
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
