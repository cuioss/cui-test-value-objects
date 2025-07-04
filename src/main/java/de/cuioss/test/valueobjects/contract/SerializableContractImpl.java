/**
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
package de.cuioss.test.valueobjects.contract;

import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import de.cuioss.test.valueobjects.api.object.ObjectTestContract;
import de.cuioss.test.valueobjects.objects.ParameterizedInstantiator;
import de.cuioss.test.valueobjects.objects.impl.ExceptionHelper;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.tools.logging.CuiLogger;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests whether the object in hand implements {@link Serializable} and than
 * serializes / deserializes the object, and compares the newly created object
 * with the original by using {@link Object#equals(Object)}
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class SerializableContractImpl implements ObjectTestContract {

    private static final CuiLogger log = new CuiLogger(SerializableContractImpl.class);

    @Override
    public void assertContract(final ParameterizedInstantiator<?> instantiator,
        final ObjectTestConfig objectTestConfig) {
        requireNonNull(instantiator);

        log.info("Verifying " + getClass().getName() + "\nWith configuration: " + instantiator);

        var shouldUseEquals = checkForEqualsComparison(objectTestConfig);

        Object template = instantiator.newInstanceMinimal();

        assertInstanceOf(Serializable.class, template, template.getClass().getName() + " does not implement java.io.Serializable");

        final var serializationFailedMessage = template.getClass().getName() + " is not equal after serialization";
        var serializeAndDeserialize = serializeAndDeserialize(template);
        if (shouldUseEquals) {
            assertEquals(template, serializeAndDeserialize, serializationFailedMessage);
        }
        if (!checkTestBasicOnly(objectTestConfig)
            && !instantiator.getRuntimeProperties().getWritableProperties().isEmpty()) {
            var properties = filterProperties(instantiator.getRuntimeProperties().getWritableProperties(),
                objectTestConfig);
            template = instantiator.newInstance(properties);
            serializeAndDeserialize = serializeAndDeserialize(template);
            if (shouldUseEquals) {
                assertEquals(template, serializeAndDeserialize, serializationFailedMessage);
            }
        }
    }

    static List<PropertyMetadata> filterProperties(final List<PropertyMetadata> allProperties,
        final ObjectTestConfig objectTestConfig) {
        if (null == objectTestConfig) {
            return allProperties;
        }
        final SortedSet<String> consideredAttributes = new TreeSet<>();
        allProperties.forEach(p -> consideredAttributes.add(p.getName()));
        // Whitelist takes precedence
        if (objectTestConfig.serializableOf().length > 0) {
            consideredAttributes.clear();
            consideredAttributes.addAll(Arrays.asList(objectTestConfig.serializableOf()));
        } else {
            consideredAttributes.removeAll(Arrays.asList(objectTestConfig.serializableExclude()));
        }
        return allProperties.stream().filter(p -> consideredAttributes.contains(p.getName())).toList();
    }

    static boolean checkForEqualsComparison(final ObjectTestConfig objectTestConfig) {
        return null == objectTestConfig || objectTestConfig.serializableCompareUsingEquals();
    }

    static boolean checkTestBasicOnly(final ObjectTestConfig objectTestConfig) {
        return null != objectTestConfig && objectTestConfig.serializableBasicOnly();
    }

    /**
     * Shorthand combining the calls {@link #serializeObject(Object)}
     * {@link #deserializeObject(byte[])}
     *
     * @param object to be serialized, must not be null
     * @return the deserialized object.
     */
    public static final Object serializeAndDeserialize(final Object object) {
        assertNotNull(object, "Given Object must not be null");
        final var serialized = serializeObject(object);
        return deserializeObject(serialized);
    }

    /**
     * Serializes an object into a newly created byteArray
     *
     * @param object to be serialized
     * @return the resulting byte array
     */
    public static final byte[] serializeObject(final Object object) {
        assertNotNull(object, "Given Object must not be null");
        final var baos = new ByteArrayOutputStream(1024);
        try (var oas = new ObjectOutputStream(baos)) {
            oas.writeObject(object);
            oas.flush();
        } catch (final Exception e) {
            throw new AssertionError(
                "Unable to serialize, due to " + ExceptionHelper.extractCauseMessageFromThrowable(e));
        }
        return baos.toByteArray();
    }

    /**
     * Deserializes an object from a given byte-array
     *
     * @param bytes to be deserialized
     * @return the deserialized object
     */
    public static final Object deserializeObject(final byte[] bytes) {
        assertNotNull(bytes, "Given byte-array must not be null");
        final var bais = new ByteArrayInputStream(bytes);
        try (var ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        } catch (final Exception e) {
            throw new AssertionError(
                "Unable to deserialize, due to " + ExceptionHelper.extractCauseMessageFromThrowable(e));
        }
    }

}
