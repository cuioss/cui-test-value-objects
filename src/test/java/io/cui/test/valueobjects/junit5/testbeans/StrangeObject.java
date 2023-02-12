package io.cui.test.valueobjects.junit5.testbeans;

import java.io.Serializable;

import lombok.Data;

@SuppressWarnings("javadoc")
@Data
public class StrangeObject implements StrangeInterface, Serializable {

    private static final long serialVersionUID = 50932786360461418L;

    private String property;

    public UnknownObject getUnknownObject() {
        throw new IllegalStateException();
    }

    public StrangeObject() {

    }

    public StrangeObject(@SuppressWarnings("unused") StrangeInterface strangeInterface) {

    }

}
