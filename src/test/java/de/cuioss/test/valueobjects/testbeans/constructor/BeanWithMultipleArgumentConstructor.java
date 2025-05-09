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
package de.cuioss.test.valueobjects.testbeans.constructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Formattable;
import java.util.Set;

import de.cuioss.test.valueobjects.objects.BuilderInstantiator;
import de.cuioss.tools.property.PropertyMemberInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BeanWithMultipleArgumentConstructor implements Serializable {

    @Serial
    private static final long serialVersionUID = -7914292255779711820L;

    @Getter
    private final String name;

    @Getter
    private final PropertyMemberInfo propertyMemberInfo;

    @Getter
    private final Set<String> nameSet;

    @Getter
    private final BuilderInstantiator<String> builderInstantiator;

    @Getter
    private final Formattable formattable;

    @Getter
    private final AbstractList<String> abstractList;
}
