package io.cui.test.valueobjects.mapper.support;

import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("javadoc")
public class EntryDtoToComplexBeanMapper implements Function<EntryDto, ComplexBean> {

    @Override
    public ComplexBean apply(final EntryDto entryDto) {
        if (null == entryDto) {
            return null;
        }
        final ComplexBean result = new ComplexBean();
        result.setString(entryDto.getIdentifier());
        if (null != entryDto.getKeyValueEntities()) {
            result.setStringSet(entryDto.getKeyValueEntities().stream()
                    .map(entry -> entry.getKey())
                    .collect(Collectors.toSet()));
        }
        return result;
    }
}
