package io.cui.test.valueobjects.junit5;

import io.cui.test.valueobjects.api.VerifyMapperConfiguration;
import io.cui.test.valueobjects.junit5.testbeans.mapper.SimpleMapper;
import io.cui.test.valueobjects.junit5.testbeans.mapper.SimpleSourceBean;
import io.cui.test.valueobjects.junit5.testbeans.mapper.SimpleTargetBean;

@VerifyMapperConfiguration(equals = { "firstname:nameFirst", "lastname:nameLast", "attributeList:listOfAttributes" })
class AbstractMapperTestTest extends AbstractMapperTest<SimpleMapper, SimpleSourceBean, SimpleTargetBean> {

}
