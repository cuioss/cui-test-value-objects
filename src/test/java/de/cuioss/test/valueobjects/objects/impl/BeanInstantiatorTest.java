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
package de.cuioss.test.valueobjects.objects.impl;

import de.cuioss.test.valueobjects.objects.RuntimeProperties;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.PropertySupport;
import de.cuioss.test.valueobjects.testbeans.ComplexBean;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.cuioss.tools.collect.CollectionLiterals.immutableSortedSet;
import static org.junit.jupiter.api.Assertions.*;

class BeanInstantiatorTest {

    private final DefaultInstantiator<ComplexBean> instantiator = new DefaultInstantiator<>(ComplexBean.class);

    private final RuntimeProperties fullRuntimeInformation = new RuntimeProperties(
        immutableSortedSet(ComplexBean.completeValidMetadata()));

    private final BeanInstantiator<ComplexBean> populator = new BeanInstantiator<>(instantiator,
        fullRuntimeInformation);

    @Test
    void shouldFailOnNullInstantiatorParameter() {
        assertThrows(NullPointerException.class, () -> new BeanInstantiator<ComplexBean>(null, fullRuntimeInformation));
    }

    @Test
    void shouldFailOnNullRuntimeInformation() {
        assertThrows(NullPointerException.class, () -> new BeanInstantiator<>(instantiator, null));
    }

    @Test
    void shouldFailNullProperties() {
        assertThrows(AssertionError.class, () -> populator.newInstance(null, true));
    }

    @Test
    void shouldHandleEmptyProperties() {
        assertEquals(new ComplexBean(), populator.newInstance(Collections.emptyList(), true));
    }

    @Test
    void shouldNotCreateProperties() {
        final var props = basicProperties();
        final var target = instantiator.newInstance();
        for (final PropertySupport propertySupport : props) {
            propertySupport.generateTestValue();
        }
        final var populated = populator.newInstance(props, false);
        assertNotEquals(target, populated);
        for (final PropertySupport propertySupport : props) {
            propertySupport.assertValueSet(populated);
        }
    }

    @Test
    void shouldCreateProperties() {
        final var props = basicProperties();
        final var populated = populator.newInstance(props, true);
        assertNotEquals(new ComplexBean(), populated);
        for (final PropertySupport propertySupport : props) {
            assertNotNull(propertySupport.getGeneratedValue());
            propertySupport.assertValueSet(populated);
        }
    }

    @Test
    void shouldCreateAny() {
        final var populated = populator.newInstanceFull();
        assertNotEquals(new ComplexBean(), populated);
    }

    private static List<PropertySupport> basicProperties() {
        final var propertes = ComplexBean.completeValidMetadata();
        final List<PropertySupport> support = new ArrayList<>();
        for (final PropertyMetadata metadata : propertes) {
            support.add(new PropertySupport(metadata));
        }
        return support;
    }
}
