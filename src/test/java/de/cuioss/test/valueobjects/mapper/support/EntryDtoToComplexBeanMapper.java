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
package de.cuioss.test.valueobjects.mapper.support;

import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntryDtoToComplexBeanMapper implements Function<EntryDto, ComplexBean> {

    @Override
    public ComplexBean apply(final EntryDto entryDto) {
        if (null == entryDto) {
            return null;
        }
        final var result = new ComplexBean();
        result.setString(entryDto.getIdentifier());
        if (null != entryDto.getKeyValueEntities()) {
            result.setStringSet(entryDto.getKeyValueEntities().stream().map(Entry::getKey).collect(Collectors.toSet()));
        }
        return result;
    }
}
