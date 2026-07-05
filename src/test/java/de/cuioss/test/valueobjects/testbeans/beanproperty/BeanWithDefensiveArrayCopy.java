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
package de.cuioss.test.valueobjects.testbeans.beanproperty;

/**
 * A bean with a {@code String[]} property that returns / stores defensive
 * copies. As a consequence the value read back is equal by <em>value</em> but
 * never identical by <em>reference</em> to the value written. This is used to
 * verify that {@code PropertySupport#assertValueSet} compares array typed
 * properties by value ({@code Objects.deepEquals}) instead of reference.
 */
public class BeanWithDefensiveArrayCopy {

    private String[] array;

    public String[] getArray() {
        return null == array ? null : array.clone();
    }

    public void setArray(final String[] array) {
        this.array = null == array ? null : array.clone();
    }
}
