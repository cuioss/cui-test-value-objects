package io.cui.test.valueobjects.testbeans.veto;

import io.cui.test.valueobjects.api.object.ObjectTestContracts;
import io.cui.test.valueobjects.api.object.VetoObjectTestContract;

@SuppressWarnings("javadoc")
@VetoObjectTestContract({ ObjectTestContracts.SERIALIZABLE, ObjectTestContracts.EQUALS_AND_HASHCODE })
@VetoObjectTestContract(ObjectTestContracts.TO_STRING)
public class ClassWithMixedVetoes {

}
