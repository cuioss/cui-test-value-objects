package de.cuioss.test.valueobjects.testbeans.veto;

import de.cuioss.test.valueobjects.api.object.ObjectTestContracts;
import de.cuioss.test.valueobjects.api.object.VerifyObjectTestContract;

@SuppressWarnings("javadoc")
@VerifyObjectTestContract(veto = { ObjectTestContracts.EQUALS_AND_HASHCODE, ObjectTestContracts.TO_STRING })
public class ClassWithOneOptIn {

}
