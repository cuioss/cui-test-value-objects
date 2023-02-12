package io.cui.test.valueobjects.api.object;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This Veto is used to exclude specific object types from tests using class path scanning, like
 * {@PortalNavigationMenuItemPackageTest}.
 *
 * @author Matthias Walliczek
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface VetoType {

    /**
     * @return Array of classes that belong to classpath entries to make discoverable during
     *         testing.
     */
    Class<?>[] value();
}
