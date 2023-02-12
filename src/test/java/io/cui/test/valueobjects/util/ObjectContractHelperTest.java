package io.cui.test.valueobjects.util;

import static io.cui.test.valueobjects.util.ObjectContractHelper.extractConfiguredVetoObjectContracts;
import static io.cui.test.valueobjects.util.ObjectContractHelper.handleOptedInContracts;
import static io.cui.test.valueobjects.util.ObjectContractHelper.handleVetoedContracts;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.testbeans.veto.ClassWithMixedVetoes;
import io.cui.test.valueobjects.testbeans.veto.ClassWithOneOptIn;
import io.cui.test.valueobjects.testbeans.veto.ClassWithOneVeto;
import io.cui.test.valueobjects.testbeans.veto.ClassWithTwoOptIns;
import io.cui.test.valueobjects.testbeans.veto.ClassWithTwoVetoes;
import io.cui.test.valueobjects.testbeans.veto.InheritedVeto;
import io.cui.test.valueobjects.testbeans.veto.InheritedVetoWithAdditionalVeto;


class ObjectContractHelperTest {

    @Test
    void shouldExtractVetoesFromInheritedClass() {
        assertEquals(2, extractConfiguredVetoObjectContracts(InheritedVeto.class).size());
        assertEquals(3, extractConfiguredVetoObjectContracts(InheritedVetoWithAdditionalVeto.class).size());
    }

    @Test
    void shouldHandleVetoedAnnotations() {
        assertEquals(3, handleVetoedContracts(getClass()).size());
        assertEquals(2, handleVetoedContracts(ClassWithOneVeto.class).size());
        assertEquals(1, handleVetoedContracts(ClassWithTwoVetoes.class).size());
        assertEquals(0, handleVetoedContracts(ClassWithMixedVetoes.class).size());
    }

    @Test
    void shouldHandleOptInContracts() {
        assertTrue(handleOptedInContracts(getClass()).isEmpty());
        assertEquals(1, handleOptedInContracts(ClassWithOneOptIn.class).size());
        assertEquals(2, handleOptedInContracts(ClassWithTwoOptIns.class).size());
    }
}
