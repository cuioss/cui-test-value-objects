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
 * A contract Veto is used for suppressing certain test-contracts. The default
 * implementations run usually all available contracts, e.g. it will run
 * {@link ObjectTestContracts#EQUALS_AND_HASHCODE},
 * {@link ObjectTestContracts#SERIALIZABLE} and
 * {@link ObjectTestContracts#TO_STRING}. If one of the contracts is not
 * suitable for your concrete test you can suppress this contract to be run by
 * using this annotation.
 *
 * @author Oliver Wolff
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Repeatable(VetoObjectTestContracts.class)
public @interface VetoObjectTestContract {

    /**
     * @return the concrete contract to be suppressed / ignored
     */
    ObjectTestContracts[] value();
}
