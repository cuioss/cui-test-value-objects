/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.generator;

import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl;
import de.cuioss.test.valueobjects.property.impl.PropertyMetadataImpl.PropertyMetadataBuilder;
import de.cuioss.test.valueobjects.property.util.CollectionType;

import java.io.Serializable;
import java.net.URL;
import java.time.*;
import java.time.temporal.Temporal;
import java.util.*;

import static de.cuioss.test.generator.Generators.*;
import static de.cuioss.tools.base.Preconditions.checkArgument;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static de.cuioss.tools.string.MoreStrings.nullToEmpty;

/**
 * Factory for creating instances {@link PropertyMetadata} for standard
 * java-types and primitives.
 * <p>
 * Usually it is used with the {@link #metadata(String)} method that will create
 * concrete instances. In case you want to to further configure the result you
 * can access the corresponding builder with {@link #metadataBuilder(String)}
 * </p>
 *
 * @author Oliver Wolff
 * @param <T> identifying the type to be generated
 */
public final class JavaTypesGenerator<T> {

    private static final List<TypedGenerator<?>> GENERATORS = new ArrayList<>();

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Boolean}.
     */
    public static final JavaTypesGenerator<Boolean> BOOLEANS = new JavaTypesGenerator<>(booleanObjects(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for boolean-primitives
     * with a default value of <code>false</code>.
     */
    public static final JavaTypesGenerator<Boolean> BOOLEANS_PRIMITIVE = new JavaTypesGenerator<>(booleans(),
        Boolean.FALSE);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Byte}.
     */
    public static final JavaTypesGenerator<Byte> BYTES = new JavaTypesGenerator<>(byteObjects(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for byte-primitives with a
     * default value of <code>0</code>.
     */
    public static final JavaTypesGenerator<Byte> BYTES_PRIMITIVE = new JavaTypesGenerator<>(bytes(), (byte) 0);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Character}.
     */
    public static final JavaTypesGenerator<Character> CHARACTERS = new JavaTypesGenerator<>(characterObjects(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for char-primitives with a
     * default value of <code>\u0000</code>.
     */
    public static final JavaTypesGenerator<Character> CHARACTERS_PRIMITIVE = new JavaTypesGenerator<>(characters(),
        '\u0000');

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Class}.
     */
    @SuppressWarnings("rawtypes")
    public static final JavaTypesGenerator<Class> CLASS = new JavaTypesGenerator<>(classTypes(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Double}.
     */
    public static final JavaTypesGenerator<Double> DOUBLES = new JavaTypesGenerator<>(doubleObjects(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for double-primitives with
     * a default value of <code>0.0d</code>.
     */
    public static final JavaTypesGenerator<Double> DOUBLES_PRIMITIVE = new JavaTypesGenerator<>(doubles(), 0.0d);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Float}.
     */
    public static final JavaTypesGenerator<Float> FLOATS = new JavaTypesGenerator<>(floatObjects(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for float-primitives with
     * a default value of <code>0.0f</code>.
     */
    public static final JavaTypesGenerator<Float> FLOATS_PRIMITIVE = new JavaTypesGenerator<>(floats(), 0.0f);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Integer}.
     */
    public static final JavaTypesGenerator<Integer> INTEGERS = new JavaTypesGenerator<>(integerObjects(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Integer} which
     * is bound between 1 &lt;= bound &lt;= 31, used for representing days in a month
     */
    public static final JavaTypesGenerator<Integer> INTEGER_DAYS = new JavaTypesGenerator<>(integers(1, 31), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Integer} which
     * is bound between 1 &lt;= bound &lt;= 12, used for representing months in a year
     */
    public static final JavaTypesGenerator<Integer> INTEGER_MONTHS = new JavaTypesGenerator<>(integers(1, 12), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Integer} which
     * is bound between 1900 &lt;= bound &lt;= 2100, used for representing years
     */
    public static final JavaTypesGenerator<Integer> INTEGER_YEARS = new JavaTypesGenerator<>(integers(1900, 2100),
        null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for int-primitives with a
     * default value of <code>0</code>.
     */
    public static final JavaTypesGenerator<Integer> INTEGERS_PRIMITIVE = new JavaTypesGenerator<>(integers(), 0);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Locale}.
     */
    public static final JavaTypesGenerator<Locale> LOCALES = new JavaTypesGenerator<>(locales(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Long}.
     */
    public static final JavaTypesGenerator<Long> LONGS = new JavaTypesGenerator<>(longObjects(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for long-primitives with a
     * default value of <code>0l</code>.
     */
    public static final JavaTypesGenerator<Long> LONGS_PRIMITIVE = new JavaTypesGenerator<>(longs(), 0L);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Number}.
     */
    public static final JavaTypesGenerator<Number> NUMBERS = new JavaTypesGenerator<>(numbers(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for
     * {@link RuntimeException}. The underlying generator will generate
     * corresponding exceptions.
     */
    public static final JavaTypesGenerator<RuntimeException> RUNTIME_EXCEPTIONS = new JavaTypesGenerator<>(
        runtimeExceptions(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Serializable}.
     */
    public static final JavaTypesGenerator<Serializable> SERIALIZABLES = new JavaTypesGenerator<>(serializables(),
        null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Short}.
     */
    public static final JavaTypesGenerator<Short> SHORTS = new JavaTypesGenerator<>(shortObjects(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for short-primitives with
     * a default value of <code>0</code>.
     */
    public static final JavaTypesGenerator<Short> SHORTS_PRIMITIVE = new JavaTypesGenerator<>(shorts(), (short) 0);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link String}. The
     * underlying generator will always create non-empty Strings
     */
    public static final JavaTypesGenerator<String> STRINGS = new JavaTypesGenerator<>(nonEmptyStrings(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link String}. The
     * underlying generator will always letter string in the size ranging from 1-12
     */
    public static final JavaTypesGenerator<String> STRINGS_LETTER = new JavaTypesGenerator<>(letterStrings(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Throwable}. The
     * underlying generator will generate corresponding exceptions.
     */
    public static final JavaTypesGenerator<Throwable> THROWABLES = new JavaTypesGenerator<>(throwables(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link TimeZone}.
     */
    public static final JavaTypesGenerator<TimeZone> TIME_ZONES = new JavaTypesGenerator<>(timeZones(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link LocalDate}.
     */
    public static final JavaTypesGenerator<LocalDate> LOCAL_DATES = new JavaTypesGenerator<>(localDates(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link LocalTime}.
     */
    public static final JavaTypesGenerator<LocalTime> LOCAL_TIMES = new JavaTypesGenerator<>(localTimes(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link LocalDateTime}.
     */
    public static final JavaTypesGenerator<LocalDateTime> LOCAL_DATE_TIMES = new JavaTypesGenerator<>(localDateTimes(),
        null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Date}.
     */
    public static final JavaTypesGenerator<Date> DATE = new JavaTypesGenerator<>(dates(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link Temporal}.
     */
    public static final JavaTypesGenerator<Temporal> TEMPORAL = new JavaTypesGenerator<>(temporals(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link URL}.
     */
    public static final JavaTypesGenerator<URL> URLS = new JavaTypesGenerator<>(urls(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link ZoneId}.
     */
    public static final JavaTypesGenerator<ZoneId> ZONE_IDS = new JavaTypesGenerator<>(zoneIds(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link ZoneOffset}.
     */
    public static final JavaTypesGenerator<ZoneOffset> ZONE_OFFSETS = new JavaTypesGenerator<>(zoneOffsets(), null);

    /**
     * Creates an instance of of {@link PropertyMetadata} for {@link ZonedDateTime}.
     */
    public static final JavaTypesGenerator<ZonedDateTime> ZONED_DATE_TIME = new JavaTypesGenerator<>(zonedDateTimes(),
        null);

    /** The concrete type of the object created by the generator. */
    private final Class<T> propertyType;

    /** The actual generator. */
    private final TypedGenerator<T> generator;

    /**
     * used for primitive types that have always a default, for all other types this
     * is <code>null</code>
     */
    private final Object defaultValue;

    private JavaTypesGenerator(final TypedGenerator<T> typedGenerator, final Object defaultValue) {
        propertyType = typedGenerator.getType();
        generator = typedGenerator;
        this.defaultValue = defaultValue;

        GENERATORS.add(typedGenerator);
    }

    /**
     * @return all {@link TypedGenerator} provided by this class
     */
    @SuppressWarnings("squid:S1452") // owolff all generators available, therefore the wildcard is
    // needed
    public static List<TypedGenerator<?>> allGenerators() {
        return immutableList(GENERATORS);
    }

    /**
     * Creates a configured instance of {@link PropertyMetadata}
     *
     * @param name of the property the {@link PropertyMetadata} is related to.
     * @return {@link PropertyMetadata} with the given name as
     *         {@link PropertyMetadata#getName()}, the corresponding
     *         {@link TypedGenerator} and
     *         {@link PropertyMetadata#getPropertyClass()}
     */
    public PropertyMetadata metadata(final String name) {
        return metadataBuilder(name).build();
    }

    /**
     * Creates a configured instance of {@link PropertyMetadata} with the required
     * attribute set to <code>true</code> -> Property is required
     *
     * @param name of the property the {@link PropertyMetadata} is related to.
     * @return {@link PropertyMetadata} with the given name as
     *         {@link PropertyMetadata#getName()}, the corresponding
     *         {@link TypedGenerator} and
     *         {@link PropertyMetadata#getPropertyClass()}
     */
    public PropertyMetadata metadataRequired(final String name) {
        return metadataBuilder(name).required(true).build();
    }

    /**
     * Creates a configured instance of {@link PropertyMetadata} with the required
     * attribute set to <code>true</code> -> Property is required and the variant
     * that the content is to be be used as collectionGenerator.
     *
     * @param name           of the property the {@link PropertyMetadata} is related
     *                       to.
     * @param collectionType to be set to the
     *                       {@link PropertyMetadataBuilder#collectionType(CollectionType)}
     * @return {@link PropertyMetadata} with the given name as
     *         {@link PropertyMetadata#getName()}, the corresponding
     *         {@link TypedGenerator} and
     *         {@link PropertyMetadata#getPropertyClass()}
     */
    public PropertyMetadata metadataRequired(final String name, final CollectionType collectionType) {
        return metadataBuilder(name).required(true).collectionType(collectionType).build();
    }

    /**
     * Creates a configured instance of {@link PropertyMetadata} with the variant
     * that the content is to be be used as collectionGenerator.
     *
     * @param name           of the property the {@link PropertyMetadata} is related
     *                       to.
     * @param collectionType to be set to the
     *                       {@link PropertyMetadataBuilder#collectionType(CollectionType)}
     * @return {@link PropertyMetadata} with the given name as
     *         {@link PropertyMetadata#getName()}, the corresponding
     *         {@link TypedGenerator} and
     *         {@link PropertyMetadata#getPropertyClass()}
     */
    public PropertyMetadata metadata(final String name, final CollectionType collectionType) {
        return metadataBuilder(name).collectionType(collectionType).build();
    }

    /**
     * Creates pre-configured instances of {@link PropertyMetadataBuilder}
     *
     * @param name of the property the {@link PropertyMetadata} is related to.
     * @return {@link PropertyMetadataBuilder} with the given name as
     *         {@link PropertyMetadata#getName()}, the corresponding
     *         {@link TypedGenerator} and
     *         {@link PropertyMetadata#getPropertyClass()}
     */
    public PropertyMetadataBuilder metadataBuilder(final String name) {
        checkArgument(!nullToEmpty(name).isEmpty(), "name must not be null nor empty");
        return PropertyMetadataImpl.builder().name(name).generator(generator).defaultValue(defaultValue != null)
            .propertyClass(propertyType);
    }

}
