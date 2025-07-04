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
package de.cuioss.test.valueobjects.testbeans.builder;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

import java.util.List;


import de.cuioss.test.valueobjects.generator.JavaTypesGenerator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;

@SuppressWarnings("java:S1172")
public class BadBuilderFailsOnAttributeRead {

    public String getAttributeName() {
        throw new IllegalStateException("Bad boy");
    }

    public static class BadBuilderFailsOnAttributeReadBuilder {

        public BadBuilderFailsOnAttributeReadBuilder attributeName(final String attribute) {
            return this;
        }

        public BadBuilderFailsOnAttributeRead build() {
            return new BadBuilderFailsOnAttributeRead();
        }
    }

    public static BadBuilderFailsOnAttributeReadBuilder builder() {
        return new BadBuilderFailsOnAttributeReadBuilder();
    }

    public static final List<PropertyMetadata> METADATA = immutableList(
            JavaTypesGenerator.STRINGS.metadata("attributeName"));

}
