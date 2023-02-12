package io.cui.test.valueobjects.testbeans.mapper;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@SuppressWarnings("javadoc")
@Data
public class SimpleSourceBean {

    private String firstname;
    private String lastname;

    private List<String> attributeList = new ArrayList<>();
}
