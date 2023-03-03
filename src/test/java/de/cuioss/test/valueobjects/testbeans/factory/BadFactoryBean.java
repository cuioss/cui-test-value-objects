package de.cuioss.test.valueobjects.testbeans.factory;

@SuppressWarnings("javadoc")
public class BadFactoryBean {

    public static BadFactoryBean boom() {
        throw new IllegalArgumentException();
    }

    public static Object invalidType() {
        return new Object();
    }

    public static void voidMethod() {
    }
}
