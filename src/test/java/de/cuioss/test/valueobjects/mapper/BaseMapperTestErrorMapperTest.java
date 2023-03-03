package de.cuioss.test.valueobjects.mapper;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.MapperTest;
import de.cuioss.test.valueobjects.api.VerifyMapperConfiguration;
import de.cuioss.test.valueobjects.testbeans.mapper.SimpleErrorMapper;
import de.cuioss.test.valueobjects.testbeans.mapper.SimpleSourceBean;
import de.cuioss.test.valueobjects.testbeans.mapper.SimpleTargetBean;

@VerifyMapperConfiguration(equals = { "firstname:nameFirst", "lastname:nameLast", "attributeList:listOfAttributes" })
class BaseMapperTestErrorMapperTest extends MapperTest<SimpleErrorMapper, SimpleSourceBean, SimpleTargetBean> {

    @Override
    @Test
    public void verifyMapper() {
        assertThrows(AssertionError.class, super::verifyMapper);
    }
}
