package io.cui.test.valueobjects.testbeans.mapper;

import java.util.function.Function;

@SuppressWarnings("javadoc")
public class SimpleMapper implements Function<SimpleSourceBean, SimpleTargetBean> {

    @Override
    public SimpleTargetBean apply(SimpleSourceBean t) {
        SimpleTargetBean target = new SimpleTargetBean();
        target.setNameLast(t.getLastname());
        target.setNameFirst(t.getFirstname());
        target.setListOfAttributes(t.getAttributeList());
        return target;
    }

}
