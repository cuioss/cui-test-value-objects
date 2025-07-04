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
package de.cuioss.test.valueobjects.contract.support;

import static de.cuioss.tools.string.MoreStrings.emptyToNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import de.cuioss.tools.string.Splitter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author Oliver Wolff
 *
 */
@EqualsAndHashCode
@ToString
public class MappingTuple {

    @Getter
    private final String source;

    @Getter
    private final String target;

    @Getter
    private final MappingAssertStrategy strategy;

    /**
     * @param mapping  in the form "source:target". Any other String will result in
     *                 an {@link AssertionError}
     * @param strategy must not be null. Identifies the MappingAssertStrategy for
     *                 this element
     */
    public MappingTuple(String mapping, @NonNull MappingAssertStrategy strategy) {
        assertNotNull(emptyToNull(mapping), "Mapping must not be null");
        var splitted = Splitter.on(':').splitToList(mapping);
        assertEquals(2, splitted.size(), "Expected a String in the form of 'source:target' but was: " + mapping);
        source = splitted.getFirst();
        target = splitted.get(1);
        this.strategy = strategy;
    }
}
