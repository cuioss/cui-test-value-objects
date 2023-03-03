package de.cuioss.test.valueobjects.testbeans.property;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;

@SuppressWarnings("javadoc")
public class BeanWithNestedGenerics implements Serializable {

    private static final long serialVersionUID = 7213569922964396147L;

    @Getter
    private List<String> list;

    @Getter
    private List<List<String>> nested;
}
