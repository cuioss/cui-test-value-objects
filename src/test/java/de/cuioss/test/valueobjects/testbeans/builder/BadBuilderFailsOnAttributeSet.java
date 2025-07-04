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

import de.cuioss.test.valueobjects.generator.JavaTypesGenerator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;

import java.util.List;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

public class BadBuilderFailsOnAttributeSet {

    public static class BadBuilderFailsOnAttributeSetBuilder {

        public BadBuilderFailsOnAttributeSetBuilder attributeName(final String attribute) {
            throw new IllegalStateException("Bad boy");
        }

        public BadBuilderFailsOnAttributeSet build() {
            return new BadBuilderFailsOnAttributeSet();
        }
    }

    public static BadBuilderFailsOnAttributeSetBuilder builder() {
        return new BadBuilderFailsOnAttributeSetBuilder();
    }

    public static final List<PropertyMetadata> METADATA = immutableList(
        JavaTypesGenerator.STRINGS.metadata("attributeName"));

}
