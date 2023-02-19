package io.cui.test.valueobjects.mapper;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.MapperTest;
import io.cui.test.valueobjects.api.VerifyMapperConfiguration;
import io.cui.test.valueobjects.testbeans.mapper.SimpleErrorMapper;
import io.cui.test.valueobjects.testbeans.mapper.SimpleSourceBean;
import io.cui.test.valueobjects.testbeans.mapper.SimpleTargetBean;

@VerifyMapperConfiguration(equals = { "firstname:nameFirst", "lastname:nameLast", "attributeList:listOfAttributes" })
class BaseMapperTestErrorMapperTest extends MapperTest<SimpleErrorMapper, SimpleSourceBean, SimpleTargetBean> {

    @Override
    @Test
    public void verifyMapper() {
        assertThrows(AssertionError.class, () -> super.verifyMapper());
    }
}
