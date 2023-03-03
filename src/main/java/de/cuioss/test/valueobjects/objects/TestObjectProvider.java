package de.cuioss.test.valueobjects.objects;

import java.io.Serializable;

/**
 * Helper interface for integrating the test-framework with arbitrary tests. In essence it
 * provides the method {@link #getUnderTest()} that will return the bean / value-object to be
 * tested. Usually instantiated by Inject or other ways, but out of control of the actual
 * test-base-class
 *
 * @param <T> identifying the type to be tested is usually but not necessarily at least
 *            {@link Serializable}.
 * @author Oliver Wolff
 */
public interface TestObjectProvider<T> {

    /**
     * @return the bean to be tested. Usually instantiated by Inject or other concrete
     */
    T getUnderTest();
}
