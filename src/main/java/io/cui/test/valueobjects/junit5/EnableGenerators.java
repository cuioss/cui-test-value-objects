package io.cui.test.valueobjects.junit5;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

import io.cui.test.valueobjects.junit5.extension.GeneratorController;
import io.cui.test.valueobjects.util.GeneratorRegistry;

/**
 * Enables the test-generator handling, see {@link GeneratorRegistry} for details. The underlying
 * extension {@link GeneratorController} take care on all runtime aspects of the handling in a
 * junit-5 context.
 *
 * @author Oliver Wolff
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@ExtendWith(GeneratorController.class)
public @interface EnableGenerators {

}
