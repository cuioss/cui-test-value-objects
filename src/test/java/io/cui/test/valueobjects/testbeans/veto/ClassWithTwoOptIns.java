package io.cui.test.valueobjects.testbeans.veto;

import io.cui.test.valueobjects.api.object.ObjectTestContracts;
import io.cui.test.valueobjects.api.object.VerifyObjectTestContract;

@SuppressWarnings("javadoc")
@VerifyObjectTestContract(veto = ObjectTestContracts.EQUALS_AND_HASHCODE)
@VerifyObjectTestContract(veto = ObjectTestContracts.EQUALS_AND_HASHCODE)
public class ClassWithTwoOptIns {

}
