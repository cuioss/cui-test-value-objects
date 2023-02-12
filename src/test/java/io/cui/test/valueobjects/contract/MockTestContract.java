package io.cui.test.valueobjects.contract;

import io.cui.test.valueobjects.api.TestContract;
import io.cui.test.valueobjects.objects.ParameterizedInstantiator;
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
