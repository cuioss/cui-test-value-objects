package de.cuioss.test.valueobjects.testbeans.objectcontract;

import java.io.Serializable;

/**
 * Bad bean that neither implements {@link Serializable} nor override {@link Object#equals(Object)}
 * , {@link Object#hashCode()} or {@link Object#toString()}
 *
 * @author Oliver Wolff
 */
public class BadObjectBean {

}
