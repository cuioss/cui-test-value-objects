package de.cuioss.test.valueobjects.testbeans.serializable;

import java.io.Serializable;

import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import lombok.EqualsAndHashCode;

@SuppressWarnings("javadoc")
@ObjectTestConfig(serializableBasicOnly = true)
@EqualsAndHashCode
public class SerializationBasicOnlyTrueContract implements Serializable {

    private static final long serialVersionUID = 6451713471275564105L;

}
