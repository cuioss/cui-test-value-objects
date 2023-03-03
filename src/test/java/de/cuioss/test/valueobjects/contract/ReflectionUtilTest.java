package de.cuioss.test.valueobjects.contract;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.testbeans.ComplexBean;
import de.cuioss.test.valueobjects.testbeans.objectcontract.BadObjectBean;

class ReflectionUtilTest {

    @Test
    void shouldDetectEqualsIsOverridden() {
        ReflectionUtil.assertEqualsMethodIsOverriden(ComplexBean.class);
    }

    @Test
    void shouldDetectEqualsIsNotOverridden() {
        assertThrows(AssertionError.class, () -> ReflectionUtil.assertEqualsMethodIsOverriden(BadObjectBean.class));
    }

    @Test
    void shouldDetectHashCodeIsOverridden() {
        ReflectionUtil.assertHashCodeMethodIsOverriden(ComplexBean.class);
    }

    @Test
    void shouldDetectHashCodeIsNotOverridden() {
        assertThrows(AssertionError.class, () -> ReflectionUtil.assertHashCodeMethodIsOverriden(BadObjectBean.class));
    }

    @Test
    void shouldDetectToStringIsOverridden() {
        ReflectionUtil.assertToStringMethodIsOverriden(ComplexBean.class);
    }

    @Test
    void shouldDetectToStringIsNotOverridden() {
        assertThrows(AssertionError.class, () -> ReflectionUtil.assertToStringMethodIsOverriden(BadObjectBean.class));
    }
}
