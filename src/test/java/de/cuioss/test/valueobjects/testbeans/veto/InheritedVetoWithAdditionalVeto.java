package de.cuioss.test.valueobjects.testbeans.veto;

import de.cuioss.test.valueobjects.api.object.ObjectTestContracts;
import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;

@SuppressWarnings("javadoc")
@VetoObjectTestContract(ObjectTestContracts.EQUALS_AND_HASHCODE)
public class InheritedVetoWithAdditionalVeto extends ClassWithTwoVetoes {

}
