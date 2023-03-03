package de.cuioss.test.valueobjects.junit5.contracts;

import de.cuioss.test.valueobjects.junit5.testbeans.ComplexBean;

class ShouldHandleObjectContractsTest implements ShouldHandleObjectContracts<ComplexBean> {

    @Override
    public ComplexBean getUnderTest() {
        return new ComplexBean();
    }

}
