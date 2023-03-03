package de.cuioss.test.valueobjects.junit5.testbeans;

import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("javadoc")
@RequiredArgsConstructor
public class MockTestContract<T> implements TestContract<T> {

    @Getter
    private final ParameterizedInstantiator<T> instantiator;

    @Override
    public void assertContract() {
    }

}
