package io.cui.test.valueobjects.testbeans.serializable;

import java.io.Serializable;

import io.cui.test.valueobjects.api.object.ObjectTestConfig;
import lombok.EqualsAndHashCode;

@SuppressWarnings("javadoc")
@ObjectTestConfig(serializableBasicOnly = true)
@EqualsAndHashCode
public class SerializationBasicOnlyTrueContract implements Serializable {

    private static final long serialVersionUID = 6451713471275564105L;

}
