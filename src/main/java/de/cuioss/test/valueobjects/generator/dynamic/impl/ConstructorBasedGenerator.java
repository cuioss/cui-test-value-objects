package de.cuioss.test.valueobjects.generator.dynamic.impl;

import static java.lang.String.format;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.generator.dynamic.GeneratorResolver;
import de.cuioss.test.valueobjects.objects.impl.ExceptionHelper;
import de.cuioss.tools.lang.SecuritySupport;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.string.Joiner;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author Oliver Wolff
 * @param <T> identifying the type to be generated
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class ConstructorBasedGenerator<T> implements TypedGenerator<T> {

    private static final CuiLogger log = new CuiLogger(ConstructorBasedGenerator.class);

    private static final String UNABLE_TO_CALL_CONSTRUCTOR_FOR_CLASS =
        "Unable to call constructor '%s' for class '%s' due to: '%s'";

    @NonNull
    private final Class<T> type;
    @NonNull
    private final List<TypedGenerator<?>> constructorGenerators;
    @NonNull
    private final Constructor<T> constructor;

    @Override
    public T next() {
        if (this.constructorGenerators.isEmpty()) {
            try {
                return this.constructor.newInstance();
            } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new IllegalStateException(
                        String.format(UNABLE_TO_CALL_CONSTRUCTOR_FOR_CLASS, this.constructor,
                                this.type, e.getMessage()),
                        e);
            }
        }
        final var parameter = new ArrayList<>();
        this.constructorGenerators.forEach(gen -> parameter.add(gen.next()));
        try {
            logExtendedInformationAboutUsedConstructor(parameter);
            return this.constructor.newInstance(parameter.toArray());
        } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new IllegalStateException(
                    format(UNABLE_TO_CALL_CONSTRUCTOR_FOR_CLASS, this.constructor,
                            this.type, ExceptionHelper.extractCauseMessageFromThrowable(e)),
                    e);
        }
    }

    private void logExtendedInformationAboutUsedConstructor(final ArrayList<Object> parameterValues) {
        final var constructorModifierValue = constructor.getModifiers();
        if (!Modifier.isPublic(constructorModifierValue)) {

            log.warn("!!! Attention : "
                    + "A non public constructor will be used to create an instance for {}. "
                    + "This is illegal and can cause unexpected behaviour. "
                    + "Solution: provide a fitting generator instead!",
                    constructor.getName());

            final List<String> parameterInfo = new ArrayList<>(constructor.getParameters().length);
            for (final Parameter parameter : constructor.getParameters()) {
                parameterInfo.add(parameter.getType().getSimpleName() + " " + parameter.getName());
            }

            final var modifier = Modifier.toString(constructorModifierValue);
            final var constructorInfo = modifier + " " + constructor.getName()
                    + "(" + Joiner.on(", ").skipNulls().join(parameterInfo) + ")";
            log.info("Used constructor : {}", constructorInfo);
            log.info("Used constructor parameter : {}", logUsedValuesForConstructor(parameterValues));
        }
    }

    private static String logUsedValuesForConstructor(final ArrayList<Object> parameter) {
        final List<String> parameterInfo = new ArrayList<>();
        for (final Object object : parameter) {
            parameterInfo.add("[" + object.getClass().getSimpleName() + " " + object.toString() + "]");
        }
        return Joiner.on(", ").skipNulls().join(parameterInfo);
    }

    @Override
    public Class<T> getType() {
        return this.type;
    }

    /**
     * Factory method for creating an instance of {@link ConstructorBasedGenerator}. It first tries
     * to find a public, than protected, package privates, private constructor. It always uses the
     * constructor with the fewest arguments
     *
     * @param type to be checked for constructors, must not be null, nor an interface, nor an
     *            annotation nor an abstract-class nor an enum
     * @return an {@link Optional} on the corresponding {@link TypedGenerator} if a constructor can
     *         be found
     */
    public static final <T> Optional<TypedGenerator<T>> getGeneratorForType(
            final Class<T> type) {
        if (!isReponsibleForType(type)) {
            return Optional.empty();
        }
        final List<Constructor<?>> constructors = Arrays.asList(type.getDeclaredConstructors());
        if (constructors.isEmpty()) {
            log.warn("Unable to determine constructor for class {} ", type);
            return Optional.empty();
        }
        // Order according to parameter-count
        constructors.sort(Comparator.comparingInt(Constructor::getParameterCount));

        // Find fitting public constructor
        List<Constructor<?>> filteredConstructors = constructors.stream()
                .filter(c -> Modifier.isPublic(c.getModifiers())).collect(Collectors.toList());
        if (!filteredConstructors.isEmpty()) {
            return findFittingConstructor(type, filteredConstructors);
        }

        // Find fitting protected constructor
        filteredConstructors = constructors.stream()
                .filter(c -> Modifier.isProtected(c.getModifiers())).collect(Collectors.toList());
        if (!filteredConstructors.isEmpty()) {
            return findFittingConstructor(type, filteredConstructors);
        }

        // Find fitting package private constructor
        filteredConstructors = constructors.stream()
                .filter(c -> 0 == c.getModifiers()).collect(Collectors.toList());
        if (!filteredConstructors.isEmpty()) {
            return findFittingConstructor(type, filteredConstructors);
        }

        // Find fitting private constructor
        filteredConstructors = constructors.stream()
                .filter(c -> Modifier.isPrivate(c.getModifiers())).collect(Collectors.toList());
        if (!filteredConstructors.isEmpty()) {
            return findFittingConstructor(type, filteredConstructors);
        }
        log.warn("Unable to determine constructor for class {} ", type);
        return Optional.empty();
    }

    private static boolean isReponsibleForType(final Class<?> type) {
        if (null == type || type.isAnnotation()) {
            return false;
        }
        return !type.isEnum() && !type.isInterface() && !Modifier.isAbstract(type.getModifiers());
    }

    private static <T> Optional<TypedGenerator<T>> findFittingConstructor(
            final Class<T> type, final List<Constructor<?>> constructorList) {
        log.debug("Searching constructor for class {}", type);
        if (1 == constructorList.size()) {
            log.debug("Only one constructor present, so choosing this one");
            return createForConstructor(type, constructorList.get(0));
        }
        for (final Constructor<?> con : constructorList) {
            // Ok, try to find a constructor where the parameter are already registered Generator
            final List<Class<?>> parameter = Arrays.asList(con.getParameterTypes());
            var allGeneratorAvailable = true;
            for (final Class<?> param : parameter) {
                if (!TypedGeneratorRegistry.containsGenerator(param)) {
                    allGeneratorAvailable = false;
                    log.debug("Missing generator for {}", param);
                    break;
                }
            }
            if (allGeneratorAvailable) {
                return createForConstructor(type, con);
            }
        }
        log.warn("No valid constructor found for class {}", type);
        for (final Constructor<?> con : constructorList) {
            if (1 == con.getParameterCount() && con.getParameterTypes()[0].equals(type)) {
                log.debug("Skipping copy constructor...");
                continue;
            }
            return createForConstructor(type, con);
        }
        throw new IllegalStateException("No matching constructor found for class " + type);
    }

    private static <T> Optional<TypedGenerator<T>> createForConstructor(
            final Class<T> type, final Constructor<?> con) {
        @SuppressWarnings("unchecked")
        final var constructor = (Constructor<T>) con;

        SecuritySupport.setAccessible(constructor, true);

        final List<TypedGenerator<?>> generators = new ArrayList<>();
        for (final Class<?> parameterType : constructor.getParameterTypes()) {
            if (parameterType.equals(type)) {
                // Special case her: eventually copy-constructor -> Play safe prevent infinite
                // loop, type in this case is not an interface
                log.warn(
                        "Unable to create a generator for copy-constuctor of same type for class {}, constructor parameter type = {}",
                        type, parameterType);
                return Optional.empty();

            }
            generators.add(GeneratorResolver.resolveGenerator(parameterType));
        }
        return Optional.of(
                new ConstructorBasedGenerator<>(type, generators, constructor));
    }
}
