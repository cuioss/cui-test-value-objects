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
