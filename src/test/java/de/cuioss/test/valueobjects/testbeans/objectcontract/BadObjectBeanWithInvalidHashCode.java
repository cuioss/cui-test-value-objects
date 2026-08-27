/*
 * Copyright © 2023-present CUI-OpenSource-Software (info@cuioss.de)
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
package de.cuioss.test.valueobjects.testbeans.objectcontract;

import java.io.Serializable;

/**
 * Bad bean that neither implements {@link Serializable} nor overrides
 * {@link Object#hashCode()} correctly: its {@code hashCode()} is not consistent across
 * invocations, violating the {@link Object#hashCode()} contract.
 *
 * @author Oliver Wolff
 */
public class BadObjectBeanWithInvalidHashCode {

    private int counter;

    @Override
    public boolean equals(final Object obj) {
        if (null == obj) {
            return false;
        }
        return this == obj;
    }

    @Override
    public int hashCode() {
        // Bad Boy: violates the consistency requirement of Object#hashCode by returning a
        // different value on every invocation. (A constant hashCode such as 0 would be perfectly
        // legal, so it is no longer treated as a contract violation.)
        return counter++;
    }
}
