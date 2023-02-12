package io.cui.test.valueobjects.testbeans.serializable;

import java.io.Serializable;

import io.cui.test.valueobjects.api.object.ObjectTestConfig;

@SuppressWarnings("javadoc")
@ObjectTestConfig(serializableOf = "string", serializableCompareUsingEquals = false)
public class SerializationOf implements Serializable {

    private static final long serialVersionUID = 5897828394896520725L;

}
