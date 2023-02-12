package io.cui.test.valueobjects.objects.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.testbeans.ComplexBean;

class AbstractInlineInstantiatorTest extends AbstractInlineInstantiator<ComplexBean> {

    @Override
    protected ComplexBean any() {
        return new ComplexBean();
    }

    @Test
    void shouldBehave() {
        assertNotNull(getRuntimeProperties());
        assertNotNull(super.newInstance(null));
        assertNotNull(super.newInstanceMinimal());
        assertNotNull(super.newInstanceFull());
        assertNotNull(super.newInstance(null, true));
    }
}
