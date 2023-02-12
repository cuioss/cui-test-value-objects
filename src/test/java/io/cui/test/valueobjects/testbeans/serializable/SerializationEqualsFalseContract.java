package io.cui.test.valueobjects.testbeans.serializable;

import java.io.Serializable;

import io.cui.test.valueobjects.api.object.ObjectTestConfig;

@SuppressWarnings("javadoc")
@ObjectTestConfig(serializableCompareUsingEquals = false)
public class SerializationEqualsFalseContract implements Serializable {

    private static final long serialVersionUID = 8711716312273086051L;

}
