package de.cuioss.test.valueobjects.testbeans.serializable;

import java.io.Serializable;

import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import lombok.EqualsAndHashCode;

@SuppressWarnings("javadoc")
@ObjectTestConfig(serializableExclude = "string")
@EqualsAndHashCode
public class SerializationExclude implements Serializable {

    private static final long serialVersionUID = 5897828394896520725L;

}
