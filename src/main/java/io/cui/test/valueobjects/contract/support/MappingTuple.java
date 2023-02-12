package io.cui.test.valueobjects.contract.support;

import static io.cui.tools.string.MoreStrings.emptyToNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.cui.tools.string.Splitter;
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
     * @param mapping in the form "source:target". Any other String will result in an
     *            {@link AssertionError}
     * @param strategy must not be null. Identifies the MappingAssertStrategy for this
     *            element
     */
    public MappingTuple(String mapping, @NonNull MappingAssertStrategy strategy) {
        assertNotNull(emptyToNull(mapping), "Mapping must not be null");
        var splitted = Splitter.on(':').splitToList(mapping);
        assertEquals(2, splitted.size(), "Expected a String in the form of 'source:target' but was: " + mapping);
        source = splitted.get(0);
        target = splitted.get(1);
        this.strategy = strategy;
    }
}
