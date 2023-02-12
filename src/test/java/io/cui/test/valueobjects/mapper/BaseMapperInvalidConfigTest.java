package io.cui.test.valueobjects.mapper;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.BaseMapperTest;
import io.cui.test.valueobjects.api.VerifyMapperConfiguration;
import io.cui.test.valueobjects.testbeans.mapper.SimpleErrorMapper;
import io.cui.test.valueobjects.testbeans.mapper.SimpleSourceBean;
import io.cui.test.valueobjects.testbeans.mapper.SimpleTargetBean;

@VerifyMapperConfiguration(equals = { "not:there", "lastname:nameLast", "attributeList:listOfAttributes" })
class BaseMapperInvalidConfigTest extends BaseMapperTest<SimpleErrorMapper, SimpleSourceBean, SimpleTargetBean> {

    @Test
    void shouldAssertMapper() {
        assertThrows(AssertionError.class, () -> super.verifyMapper());
    }
}
