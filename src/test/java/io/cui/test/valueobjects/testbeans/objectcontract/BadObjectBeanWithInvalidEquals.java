package io.cui.test.valueobjects.testbeans.objectcontract;

import java.io.Serializable;

/**
 * Bad bean that neither implements {@link Serializable} nor override {@link Object#equals(Object)}
 * correctly
 *
 * @author Oliver Wolff
 */
public class BadObjectBeanWithInvalidEquals {

    @Override
    public boolean equals(final Object obj) {
        if (null == obj || this == obj) {
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
