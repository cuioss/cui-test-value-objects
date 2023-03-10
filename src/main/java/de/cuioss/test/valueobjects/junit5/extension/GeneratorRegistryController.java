package de.cuioss.test.valueobjects.junit5.extension;

import java.util.Collections;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.util.GeneratorAnnotationHelper;
import de.cuioss.test.valueobjects.util.GeneratorRegistry;

/**
 * This extension handles the test-generator handling, see {@link GeneratorRegistry} for details
 *
 * @author Oliver Wolff
 *
 */
public class GeneratorRegistryController implements TestInstancePostProcessor, AfterAllCallback {

    private static final Logger log = LoggerFactory.getLogger(GeneratorRegistryController.class);

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
        log.debug(() -> "Clearing TypedGeneratorRegistry registry");
        TypedGeneratorRegistry.clear();
        if (testInstance instanceof GeneratorRegistry) {
            log.debug(() -> "Test-class '" + testInstance.getClass()
                    + "' is of type de.cuioss.test.valueobjects.util.GeneratorRegistry, initializing Generator framework");
            var registry = (GeneratorRegistry) testInstance;
            GeneratorAnnotationHelper.handleGeneratorsForTestClass(registry,
                    registry.registerAdditionalGenerators());
        } else {
            log.debug(() -> "Test-class '{" + testInstance.getClass()
                    + "}' is NOT of type de.cuioss.test.valueobjects.util.GeneratorRegistry, initializing Generator framework without local Generator");
            GeneratorAnnotationHelper.handleGeneratorsForTestClass(testInstance,
                    Collections.emptyList());
        }

    }

    @Override
    public void afterAll(ExtensionContext context) {
        log.debug(() -> "Tearing down TypedGeneratorRegistry registry");
        TypedGeneratorRegistry.clear();
    }

}
