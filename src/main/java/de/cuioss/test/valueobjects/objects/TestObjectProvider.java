/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.objects;

import java.io.Serializable;

/**
 * Helper interface for integrating the test-framework with arbitrary tests. In
 * essence it provides the method {@link #getUnderTest()} that will return the
 * bean / value-object to be tested. Usually instantiated by Inject or other
 * ways, but out of control of the actual test-base-class
 *
 * @param <T> identifying the type to be tested is usually but not necessarily
 *            at least {@link Serializable}.
 * @author Oliver Wolff
 */
public interface TestObjectProvider<T> {

    /**
     * @return the bean to be tested. Usually instantiated by Inject or other
     *         concrete
     */
    T getUnderTest();
}
