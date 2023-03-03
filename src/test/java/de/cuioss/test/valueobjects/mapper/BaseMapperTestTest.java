package de.cuioss.test.valueobjects.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.MapperTest;
import de.cuioss.test.valueobjects.api.VerifyMapperConfiguration;
import de.cuioss.test.valueobjects.testbeans.mapper.SimpleMapper;
import de.cuioss.test.valueobjects.testbeans.mapper.SimpleSourceBean;
import de.cuioss.test.valueobjects.testbeans.mapper.SimpleTargetBean;

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
