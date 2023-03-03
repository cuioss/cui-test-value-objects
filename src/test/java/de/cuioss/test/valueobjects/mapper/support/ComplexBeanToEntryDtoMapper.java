package de.cuioss.test.valueobjects.mapper.support;

import java.util.function.Function;

@SuppressWarnings("javadoc")
public class ComplexBeanToEntryDtoMapper implements Function<ComplexBean, EntryDto> {

    @Override
    public EntryDto apply(final ComplexBean complexBean) {
        if (null == complexBean) {
            return null;
        }
        return new EntryDto(complexBean.getString());
    }
}
