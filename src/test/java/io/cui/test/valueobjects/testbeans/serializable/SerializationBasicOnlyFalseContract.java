package io.cui.test.valueobjects.testbeans.serializable;

import java.io.Serializable;

import io.cui.test.valueobjects.api.object.ObjectTestConfig;
import lombok.EqualsAndHashCode;

@SuppressWarnings("javadoc")
@ObjectTestConfig(serializableBasicOnly = false)
@EqualsAndHashCode
public class SerializationBasicOnlyFalseContract implements Serializable {

    private static final long serialVersionUID = 8711716312273086051L;

}
