package de.cuioss.test.valueobjects.generator.dynamic.impl;

import static de.cuioss.tools.collect.CollectionLiterals.immutableMap;

import java.util.Map;

import de.cuioss.test.generator.TypedGenerator;

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
