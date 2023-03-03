package de.cuioss.test.valueobjects.mapper.support;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

@SuppressWarnings("javadoc")
@Data
public class EntryDto implements Serializable {

    private static final long serialVersionUID = -6248699796893356889L;

    private final String identifier;

    private List<Map.Entry<String, ? extends Serializable>> keyValueEntities;

}
