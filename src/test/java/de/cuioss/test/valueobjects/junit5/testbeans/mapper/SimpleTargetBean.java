package de.cuioss.test.valueobjects.junit5.testbeans.mapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@SuppressWarnings("javadoc")
@Data
public class SimpleTargetBean implements Serializable {

    private static final long serialVersionUID = -2365169061041263374L;
    private String nameFirst;
    private String nameLast;

    private List<String> listOfAttributes = new ArrayList<>();
}
