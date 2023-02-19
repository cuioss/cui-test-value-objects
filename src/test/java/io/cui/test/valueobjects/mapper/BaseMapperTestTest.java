package io.cui.test.valueobjects.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.MapperTest;
import io.cui.test.valueobjects.api.VerifyMapperConfiguration;
import io.cui.test.valueobjects.testbeans.mapper.SimpleMapper;
import io.cui.test.valueobjects.testbeans.mapper.SimpleSourceBean;
import io.cui.test.valueobjects.testbeans.mapper.SimpleTargetBean;

@VerifyMapperConfiguration(equals = { "firstname:nameFirst", "lastname:nameLast", "attributeList:listOfAttributes" })
class BaseMapperTestTest extends MapperTest<SimpleMapper, SimpleSourceBean, SimpleTargetBean> {

    @Test
    void shouldDetermineTypes() {
        super.intializeTypeInformation();
        assertEquals(SimpleMapper.class, getMapperClass());
        assertEquals(SimpleSourceBean.class, getSourceClass());
        assertEquals(SimpleTargetBean.class, getTargetClass());
    }

    @Test
    void shouldDetermineSourceMetadata() {
        assertNotNull(super.resolveSourcePropertyMetadata());
        assertEquals(3, super.resolveSourcePropertyMetadata().size());
    }

    @Test
    void shouldDetermineTargetMetadata() {
        assertNotNull(super.resolveTargetPropertyMetadata(null));
        assertEquals(3, super.resolveTargetPropertyMetadata(null).size());
    }

}
