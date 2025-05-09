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
package de.cuioss.test.valueobjects.junit5;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


import org.junit.jupiter.api.extension.ExtendWith;

import de.cuioss.test.valueobjects.junit5.extension.GeneratorRegistryController;
import de.cuioss.test.valueobjects.util.GeneratorRegistry;

/**
 * Enables the test-generator handling, see {@link GeneratorRegistry} for
 * details. The underlying extension {@link GeneratorRegistryController} take
 * care on all runtime aspects of the handling in a junit-5 context.
 *
 * @author Oliver Wolff
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@ExtendWith(GeneratorRegistryController.class)
public @interface EnableGeneratorRegistry {

}
