package io.cui.test.valueobjects.testbeans.mapper;

import java.util.function.Function;

@SuppressWarnings("javadoc")
public class SimpleExceptionMapper implements Function<SimpleSourceBean, SimpleTargetBean> {

    @Override
    public SimpleTargetBean apply(SimpleSourceBean t) {
        throw new IllegalStateException("I'm not a good mapper");
    }

}
