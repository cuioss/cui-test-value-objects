/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.valueobjects.testbeans.mapper;

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;

import java.util.function.Function;

public class SimpleErrorMapperWrongField implements Function<SimpleSourceBean, SimpleTargetBean> {

    @Override
    public SimpleTargetBean apply(SimpleSourceBean t) {
        var target = new SimpleTargetBean();
        target.setNameFirst(t.getLastname());
        target.setNameFirst(t.getFirstname());
        target.setListOfAttributes(mutableList(t.getFirstname()));
        return target;
    }

}
