package de.cuioss.test.valueobjects.contract;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import de.cuioss.test.valueobjects.api.object.ObjectTestContract;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.tools.logging.CuiLogger;
import lombok.RequiredArgsConstructor;

/**
 * Checks whether the object in hand implements {@link Object#toString()} and
 * calls it will fully populated object.
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class ToStringContractImpl implements ObjectTestContract {

    private static final CuiLogger log = new CuiLogger(ToStringContractImpl.class);

    @Override
    public void assertContract(final ParameterizedInstantiator<?> instantiator,
            final ObjectTestConfig objectTestConfig) {

        final var builder = new StringBuilder("Verifying ");
        builder.append(getClass().getName()).append("\nWith configuration: ").append(instantiator.toString());
        log.info(builder.toString());
        Object target;

        if (shouldUseMinimal(objectTestConfig)
                && !instantiator.getRuntimeProperties().getWritableProperties().isEmpty()) {
            target = instantiator.newInstanceMinimal();
        } else {
            target = instantiator.newInstance(instantiator.getRuntimeProperties().getWritableAsPropertySupport(true),
                    false);
        }
        ReflectionUtil.assertToStringMethodIsOverriden(target.getClass());
        assertNotNull(target.toString(), "toString must not return 'null'");
    }

    static boolean shouldUseMinimal(final ObjectTestConfig objectTestConfig) {
        return null != objectTestConfig && objectTestConfig.toStringUseMinimalInstance();
    }

}
