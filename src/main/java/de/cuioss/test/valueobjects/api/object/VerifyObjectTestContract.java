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
package de.cuioss.test.valueobjects.api.object;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * In contrast to {@link VetoObjectTestContract} that is designed for cases
 * where the tests run all {@link ObjectTestConfig}, e.g. ValueObjectTest this
 * annotation is for opt-in cases. As default all {@link ObjectTestContract}s
 * will be run if this annotation is present, but you can Veto the individual
 * contracts
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(VerifyObjectTestContracts.class)
public @interface VerifyObjectTestContract {

    /**
     * @return the concrete contract to be vetoed
     */
    ObjectTestContracts[] veto() default {};

}
