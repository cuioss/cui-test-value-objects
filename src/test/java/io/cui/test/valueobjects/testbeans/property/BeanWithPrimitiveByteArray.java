package io.cui.test.valueobjects.testbeans.property;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("javadoc")
public class BeanWithPrimitiveByteArray implements Serializable {

    private static final long serialVersionUID = 4407803707233096724L;

    @Getter
    @Setter
    private byte[] content;

}
