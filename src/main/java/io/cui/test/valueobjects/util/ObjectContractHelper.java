package io.cui.test.valueobjects.util;

import static io.cui.tools.collect.CollectionLiterals.immutableSet;
import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.cui.test.valueobjects.api.object.ObjectTestContracts;
import io.cui.test.valueobjects.api.object.VerifyObjectTestContract;
import io.cui.test.valueobjects.api.object.VetoObjectTestContract;
import io.cui.test.valueobjects.api.object.VetoObjectTestContracts;
import io.cui.tools.collect.CollectionBuilder;
import io.cui.tools.reflect.MoreReflection;
import lombok.experimental.UtilityClass;

/**
 * Utility class for dealing with {@link ObjectTestContracts} and the {@link VetoObjectTestContract}
 * and {@link VetoObjectTestContracts} annotations.
 *
 * @author Oliver Wolff
 */
@UtilityClass
public final class ObjectContractHelper {

    /**
     * Checks the given type for the annotation {@link VetoObjectTestContract} and
     * {@link VetoObjectTestContracts} creates a set with all
     * {@link ObjectTestContracts#OBJECT_CONTRACTS} but the vetoed contracts.
     *
     * @param annotated the class that may or may not provide the annotations, must not be null
     * @return immutable set of found {@link ObjectTestContracts} elements.
     */
    public static Set<ObjectTestContracts> handleVetoedContracts(
            final Class<?> annotated) {
        final Set<ObjectTestContracts> objectTestContracts =
            new HashSet<>(ObjectTestContracts.OBJECT_CONTRACTS);

        extractConfiguredVetoObjectContracts(annotated)
                .forEach(veto -> objectTestContracts.removeAll(Arrays.asList(veto.value())));

        return immutableSet(objectTestContracts);
    }

    /**
     * Checks the given type for the annotation {@link VetoObjectTestContract} and
     * {@link VetoObjectTestContracts} and puts all found in the immutable list to be returned
     *
     * @param annotated the class that may or may not provide the annotations, must not be null
     * @return immutable {@link Set} of found {@link VetoObjectTestContract} elements.
     */
    public static Set<VetoObjectTestContract> extractConfiguredVetoObjectContracts(
            final Class<?> annotated) {
        requireNonNull(annotated);
        final var builder = new CollectionBuilder<VetoObjectTestContract>();

        MoreReflection.extractAllAnnotations(annotated, VetoObjectTestContracts.class)
                .forEach(contract -> builder.add(contract.value()));
        MoreReflection.extractAllAnnotations(annotated, VetoObjectTestContract.class)
                .forEach(builder::add);

        return builder.toImmutableSet();
    }

    /**
     * Checks the given type for the annotation {@link VerifyObjectTestContract} and creates a set
     * with all corresponding {@link ObjectTestContracts}.
     *
     * @param annotated the class that may or may not provide the annotations, must not be null
     * @return immutable set of found {@link ObjectTestContracts} elements.
     */
    public static Set<ObjectTestContracts> handleOptedInContracts(final Class<?> annotated) {
        final Set<ObjectTestContracts> builder = new HashSet<>();

        List<VerifyObjectTestContract> annotations =
            MoreReflection.extractAllAnnotations(annotated, VerifyObjectTestContract.class);
        if (!annotations.isEmpty()) {
            builder.addAll(Arrays.asList(ObjectTestContracts.values()));
        }
        annotations.forEach(a -> builder.removeAll(Arrays.asList(a.veto())));
        return immutableSet(builder);
    }

}
