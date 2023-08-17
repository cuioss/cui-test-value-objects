/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.util;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * An {@link IdentityResourceBundle} is helpful for tests where you want to
 * ensure that a certain message key is used to create a message but do not want
 * to test the actual {@link ResourceBundle} mechanism itself, what is the case
 * for many tests. It will always return the given key itself.
 *
 * @author Oliver Wolff
 */
public class IdentityResourceBundle extends ResourceBundle {

    @Override
    protected Object handleGetObject(final String key) {
        return key;
    }

    @Override
    public Enumeration<String> getKeys() {
        return new Vector<String>().elements();
    }

    @Override
    public boolean containsKey(final String key) {
        return true;
    }
}
