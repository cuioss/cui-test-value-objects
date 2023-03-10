package de.cuioss.test.valueobjects.testbeans.constructor;

import java.io.Serializable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("javadoc")
@RequiredArgsConstructor
public class BeanWithSingleArgumentConstructor implements Serializable {

    private static final long serialVersionUID = -7914292255779711820L;

    @Getter
    private final String name;
}
