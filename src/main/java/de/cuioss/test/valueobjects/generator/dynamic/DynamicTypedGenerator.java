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
package de.cuioss.test.valueobjects.generator.dynamic;

import de.cuioss.test.generator.TypedGenerator;
import lombok.Getter;

/**
 * This generator acts always dynamically
 *
 * @author Oliver Wolff
 * @param <T> the type of objects to be generated
 */
public class DynamicTypedGenerator<T> implements TypedGenerator<T> {

    @Getter
    private final Class<T> type;

    private final TypedGenerator<T> generator;

    /**
     * @param type must not be null
     */
    public DynamicTypedGenerator(final Class<T> type) {
        this.type = type;

        generator = GeneratorResolver.resolveGenerator(type);
    }

    @Override
    public T next() {
        return generator.next();
    }

}
