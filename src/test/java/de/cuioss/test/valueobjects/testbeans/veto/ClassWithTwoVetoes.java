package de.cuioss.test.valueobjects.testbeans.veto;

import de.cuioss.test.valueobjects.api.object.ObjectTestContracts;
import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;

@SuppressWarnings("javadoc")
@VetoObjectTestContract({ ObjectTestContracts.SERIALIZABLE })
@VetoObjectTestContract(ObjectTestContracts.TO_STRING)
public class ClassWithTwoVetoes {

}
