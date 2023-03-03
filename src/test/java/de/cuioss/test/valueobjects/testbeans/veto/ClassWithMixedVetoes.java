package de.cuioss.test.valueobjects.testbeans.veto;

import de.cuioss.test.valueobjects.api.object.ObjectTestContracts;
import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;

@SuppressWarnings("javadoc")
@VetoObjectTestContract({ ObjectTestContracts.SERIALIZABLE, ObjectTestContracts.EQUALS_AND_HASHCODE })
@VetoObjectTestContract(ObjectTestContracts.TO_STRING)
public class ClassWithMixedVetoes {

}
