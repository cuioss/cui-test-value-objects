package io.cui.test.valueobjects.mapper.support;

import java.util.Map.Entry;

import io.cui.test.generator.Generators;
import io.cui.test.generator.TypedGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("javadoc")
public class MapEntryGenerator implements TypedGenerator<Entry<?, ?>> {

    private final TypedGenerator<String> letters = Generators.nonEmptyStrings();

    @Override
    public Entry<String, String> next() {
        return new StringEntry(letters.next(), letters.next());
    }

    @AllArgsConstructor
    private static class StringEntry implements Entry<String, String> {

        @Getter
        private final String key;
        @Getter
        private String value;

        @Override
        public String setValue(String value) {
            var old = this.value;
            this.value = value;
            return old;
        }
    }

}
