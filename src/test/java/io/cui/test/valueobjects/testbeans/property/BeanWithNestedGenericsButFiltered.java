package io.cui.test.valueobjects.testbeans.property;

import java.io.Serializable;
import java.util.List;

import io.cui.test.valueobjects.api.property.PropertyReflectionConfig;
import lombok.Getter;

@SuppressWarnings("javadoc")
@PropertyReflectionConfig(exclude = "nested")
public class BeanWithNestedGenericsButFiltered implements Serializable {

    private static final long serialVersionUID = 7213569922964396147L;

    @Getter
    private List<String> list;

    @Getter
    private List<List<String>> nested;
}
