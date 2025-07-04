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
package de.cuioss.test.valueobjects.generator.impl;

import de.cuioss.test.generator.TypedGenerator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Dummy variant of {@link TypedGenerator} that are used in some corner cases,
 * where you need a {@link TypedGenerator} for a contract, but do not need the
 * generated values.
 *
 * @author Oliver Wolff
 * @param <T> identifying the type of objects to be generated
 */
@RequiredArgsConstructor
public class DummyGenerator<T> implements TypedGenerator<T> {

    @Getter
    private final Class<T> type;

    @Override
    public T next() {
        return null;
    }

}
