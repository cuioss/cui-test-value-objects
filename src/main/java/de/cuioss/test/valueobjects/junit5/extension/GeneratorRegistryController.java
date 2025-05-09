/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.junit5.extension;

import java.util.Collections;


import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.util.GeneratorAnnotationHelper;
import de.cuioss.test.valueobjects.util.GeneratorRegistry;
import de.cuioss.tools.logging.CuiLogger;

/**
 * This extension handles the test-generator handling, see
 * {@link GeneratorRegistry} for details
 *
 * @author Oliver Wolff
 *
 */
public class GeneratorRegistryController implements TestInstancePostProcessor, AfterAllCallback {

    private static final CuiLogger LOGGER = new CuiLogger(GeneratorRegistryController.class);

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
        LOGGER.debug(() -> "Clearing TypedGeneratorRegistry registry");
        TypedGeneratorRegistry.clear();
        if (testInstance instanceof GeneratorRegistry registry) {
            LOGGER.debug(() -> "Test-class '" + testInstance.getClass()
                    + "' is of type de.cuioss.test.valueobjects.util.GeneratorRegistry, initializing Generator framework");
            GeneratorAnnotationHelper.handleGeneratorsForTestClass(registry, registry.registerAdditionalGenerators());
        } else {
            LOGGER.debug(() -> "Test-class '{" + testInstance.getClass()
                    + "}' is NOT of type de.cuioss.test.valueobjects.util.GeneratorRegistry, initializing Generator framework without local Generator");
            GeneratorAnnotationHelper.handleGeneratorsForTestClass(testInstance, Collections.emptyList());
        }

    }

    @Override
    public void afterAll(ExtensionContext context) {
        LOGGER.debug(() -> "Tearing down TypedGeneratorRegistry registry");
        TypedGeneratorRegistry.clear();
    }

}
