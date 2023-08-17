package de.cuioss.test.valueobjects.testbeans.constructor;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@SuppressWarnings("javadoc")
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class SimpleConstructor {

    @Getter
    private final String attribute1;

    @Getter
    private final Integer attribute2;

    @Getter
    private final int attribute3;

    @Getter
    private final boolean attribute4;

    @Getter
    private final List<String> attribute5;

    public SimpleConstructor() {
        attribute1 = null;
        attribute2 = null;
        attribute3 = 0;
        attribute4 = false;
        attribute5 = null;
    }
}
