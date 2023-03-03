package de.cuioss.test.valueobjects.testbeans.objectcontract;

import java.io.Serializable;

/**
 * Bad bean that neither implements {@link Serializable} nor override
 * , {@link Object#hashCode()} correctly
 *
 * @author Oliver Wolff
 */
public class BadObjectBeanWithInvalidHashCode {

    @Override
    public boolean equals(final Object obj) {
        if (null == obj) {
            return false;
        }
        return this == obj;
    }

    @Override
    public int hashCode() {
        // Bad Boy
        return 0;
    }
}
