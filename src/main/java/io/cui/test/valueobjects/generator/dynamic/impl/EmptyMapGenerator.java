package io.cui.test.valueobjects.generator.dynamic.impl;

import static io.cui.tools.collect.CollectionLiterals.immutableMap;

import java.util.Map;

import io.cui.test.generator.TypedGenerator;

/**
 * Always generates an empty immutable map
 *
 * @author Oliver Wolff
 */
@SuppressWarnings("rawtypes")
public class EmptyMapGenerator implements TypedGenerator<Map> {

    @Override
    public Map<?, ?> next() {
        return immutableMap();
    }

    @Override
    public Class<Map> getType() {
        return Map.class;
    }

}
