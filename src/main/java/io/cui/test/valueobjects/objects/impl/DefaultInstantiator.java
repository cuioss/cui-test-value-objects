package io.cui.test.valueobjects.objects.impl;

import static io.cui.test.valueobjects.objects.impl.ExceptionHelper.extractCauseMessageFromThrowable;

import java.lang.reflect.InvocationTargetException;

import io.cui.test.valueobjects.objects.ObjectInstantiator;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Instantiator for any class that provide a public accessible default constructor
 *
 * @author Oliver Wolff
 * @param <T> identifying the type of object to be instantiated
 */
@RequiredArgsConstructor
@ToString
public class DefaultInstantiator<T> implements ObjectInstantiator<T> {

    @NonNull
    @Getter
    private final Class<T> targetClass;

    /**
     * @return a newly created instance
     */
    @Override
    public T newInstance() {
        try {
            return this.targetClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new AssertionError(
                    "Unable to instantiate class due to " + extractCauseMessageFromThrowable(e), e);
        }
    }

}
