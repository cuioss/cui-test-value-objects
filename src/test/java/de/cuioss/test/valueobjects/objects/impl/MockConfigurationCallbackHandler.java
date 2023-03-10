package de.cuioss.test.valueobjects.objects.impl;

import de.cuioss.test.valueobjects.objects.ConfigurationCallBackHandler;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("javadoc")
public class MockConfigurationCallbackHandler<T> implements ConfigurationCallBackHandler<T> {

    @Getter
    @Setter
    private boolean configureCalled = false;

    @Override
    public void configure(final T toBeConfigured) {
        configureCalled = true;
    }

    public void reset() {
        configureCalled = false;
    }
}
