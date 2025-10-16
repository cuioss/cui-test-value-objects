/*
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

import de.cuioss.test.valueobjects.api.contracts.VerifyBuilder;
import de.cuioss.test.valueobjects.testbeans.builder.BuilderMinimalTestClassSimple.BuilderMinimalTypeBuilder;

@VerifyBuilder(builderClass = BuilderMinimalTypeBuilder.class)
public class BuilderMinimalTestClassSimple {

    public static class BuilderMinimalTypeBuilder {

        public BuilderMinimalTestClassSimple build() {
            return new BuilderMinimalTestClassSimple();
        }
    }

    public static BuilderMinimalTypeBuilder builder() {
        return new BuilderMinimalTypeBuilder();
    }
}
