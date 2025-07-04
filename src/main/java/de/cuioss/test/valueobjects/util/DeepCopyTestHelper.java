/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.util;

import de.cuioss.test.valueobjects.objects.impl.ExceptionHelper;
import de.cuioss.tools.reflect.MoreReflection;
import lombok.experimental.UtilityClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static de.cuioss.tools.string.MoreStrings.isEmpty;
import static org.junit.jupiter.api.Assertions.*;

@UtilityClass
public class DeepCopyTestHelper {

    /**
     * To test the result of a deep copy function.
     * <p>
     * The main focus is to check if the copy is independent from the source and
     * does not have any reference to the source.
     * <p>
     * To check the equality of the objects the equals method should be implemented
     * correctly.
     *
     * @param source the source object
     * @param copy   the result of the copy function
     */
    public static void verifyDeepCopy(Object source, Object copy) {
        verifyDeepCopy(source, copy, Collections.emptyList());
    }

    /**
     * To test the result of a deep copy function.
     * <p>
     * The main focus is to check if the copy is independent from the source and
     * does not have any reference to the source. -> Deep Copy, instead of shallow
     * copy
     * <p>
     * To check the equality of the objects the equals method should be implemented
     * correctly.
     *
     * @param source           the source object
     * @param copy             the result of the copy function
     * @param ignoreProperties The top-level attribute names to be ignored
     */
    public static void verifyDeepCopy(Object source, Object copy, Collection<String> ignoreProperties) {
        testDeepCopy(source, copy, null, ignoreProperties);
    }

    @SuppressWarnings({"java:S2259", "java:S135"}) // owolff: False positive: assertions are not considered here
    private static void testDeepCopy(Object source, Object copy, String propertyString,
                                     Collection<String> ignoreProperties) {

        assertNotNull(ignoreProperties, "ignore-properties my be empty but never null");
        // first check: check equals
        assertEquals(source, copy);
        if (null == source) {
            return;
        }

        final var currentPropertyString = determinePropertyString(propertyString);

        for (final Method accessMethod : MoreReflection.retrieveAccessMethods(source.getClass(), ignoreProperties)) {
            var propertyName = MoreReflection.computePropertyNameFromMethodName(accessMethod.getName());
            try {
                var resultSource = accessMethod.invoke(source);
                var resultCopy = accessMethod.invoke(copy);

                // check for null
                // No sense in checking Strings, primitives and enums
                if (!checkNullContract(resultSource, resultCopy, currentPropertyString, propertyName)
                    || resultSource.getClass().isPrimitive() || resultSource.getClass().isEnum()
                    || String.class.equals(resultSource.getClass())) {
                    continue;
                }
                if (!checkForList(resultSource, resultCopy, currentPropertyString, propertyName)) {
                    if (MoreReflection.retrieveWriteMethod(source.getClass(), propertyName, resultSource.getClass())
                        .isEmpty()) {
                        continue;
                    }

                    assertNotSame(resultSource, resultCopy, "deep copy failed with: " + currentPropertyString
                        + propertyName + " (" + resultSource + ")");
                    testDeepCopy(resultSource, resultCopy, currentPropertyString + propertyName,
                        Collections.emptyList());
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                fail("invoke method " + accessMethod.getName() + "failed: "
                    + ExceptionHelper.extractCauseMessageFromThrowable(e));
            }

        }
    }

    private boolean checkNullContract(Object resultSource, Object resultCopy, String currentPropertyString,
                                      String propertyName) {
        // check for null
        if (null != resultSource) {
            if (null == resultCopy) {
                fail("property " + currentPropertyString + propertyName + " differs: " + resultSource
                    + " != null");
            }
        } else if (null == resultCopy) {
            return false;
        } else {
            fail("property " + currentPropertyString + propertyName + " differs: null != " + resultCopy);
        }
        return true;
    }

    private boolean checkForList(Object resultSource, Object resultCopy, String currentPropertyString,
                                 String propertyName) {
        if (!(resultSource instanceof List<?> resultSourceList)) {
            return false;
        }
        for (var i = 0; i < resultSourceList.size(); i++) {
            testDeepCopy(resultSourceList.get(i), ((List<?>) resultCopy).get(i),
                currentPropertyString + propertyName + "[" + i + "]", Collections.emptyList());
        }
        return true;

    }

    private static String determinePropertyString(String propertyString) {
        if (isEmpty(propertyString)) {
            return "";
        }
        return propertyString + ".";
    }
}
