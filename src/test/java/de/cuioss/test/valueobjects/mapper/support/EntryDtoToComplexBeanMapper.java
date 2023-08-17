package de.cuioss.test.valueobjects.mapper.support;

import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("javadoc")
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
