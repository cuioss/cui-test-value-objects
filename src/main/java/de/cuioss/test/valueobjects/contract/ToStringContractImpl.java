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
package de.cuioss.test.valueobjects.contract;

import static org.junit.jupiter.api.Assertions.assertNotNull;


import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import de.cuioss.test.valueobjects.api.object.ObjectTestContract;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.tools.logging.CuiLogger;
import lombok.RequiredArgsConstructor;

/**
 * Checks whether the object in hand implements {@link Object#toString()} and
 * calls it will fully populated object.
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class ToStringContractImpl implements ObjectTestContract {

    private static final CuiLogger log = new CuiLogger(ToStringContractImpl.class);

    @Override public void assertContract(final ParameterizedInstantiator<?> instantiator,
            final ObjectTestConfig objectTestConfig) {

        final var builder = new StringBuilder("Verifying ");
        builder.append(getClass().getName()).append("\nWith configuration: ").append(instantiator.toString());
        log.info(builder.toString());
        Object target;

        if (shouldUseMinimal(objectTestConfig)
                && !instantiator.getRuntimeProperties().getWritableProperties().isEmpty()) {
            target = instantiator.newInstanceMinimal();
        } else {
            target = instantiator.newInstance(instantiator.getRuntimeProperties().getWritableAsPropertySupport(true),
                    false);
        }
        ReflectionUtil.assertToStringMethodIsOverriden(target.getClass());
        assertNotNull(target.toString(), "toString must not return 'null'");
    }

    static boolean shouldUseMinimal(final ObjectTestConfig objectTestConfig) {
        return null != objectTestConfig && objectTestConfig.toStringUseMinimalInstance();
    }

}
